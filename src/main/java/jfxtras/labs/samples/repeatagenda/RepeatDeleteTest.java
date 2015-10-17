package jfxtras.labs.samples.repeatagenda;

import static org.junit.Assert.assertEquals;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;

import jfxtras.labs.samples.repeatagenda.scene.control.agenda.Agenda.Appointment;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.AppointmentFactory;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.Repeat;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.Repeat.EndCriteria;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.Repeat.IntervalUnit;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.RepeatFactory;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.RepeatableAppointment;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.RepeatableUtilities;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.RepeatableUtilities.RepeatChange;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.RepeatableUtilities.WindowCloseType;

public class RepeatDeleteTest extends RepeatTestAbstract {

    /**
     * Tests deleting one appointment from a repeat rule
     * @throws ParserConfigurationException 
     */
    @Test
    public void deleteOneWeeklyTimeAndDate() throws ParserConfigurationException
    {
        Repeat repeat = getRepeatWeeklyFixed2();
        Set<Repeat> repeats = new HashSet<Repeat>(Arrays.asList(repeat));
        Set<Appointment> appointments = new TreeSet<Appointment>(getAppointmentComparator());
        LocalDate startDate = LocalDate.of(2015, 11, 1);
        LocalDate endDate = LocalDate.of(2015, 11, 7); // tests one week time range
        Collection<RepeatableAppointment> newAppointments = repeat.makeAppointments(startDate, endDate);
        appointments.addAll(newAppointments);
        Iterator<Appointment> appointmentIterator = appointments.iterator();
        assertEquals(3, appointments.size()); // check number of appointments

        // select appointment and apply changes (should be undone with cancel)
        Appointment selectedAppointment = appointmentIterator.next();
        
        WindowCloseType windowCloseType = RepeatableUtilities.deleteAppointments(
                appointments
              , selectedAppointment
              , repeats
              , a -> RepeatChange.ONE // delete one appointment
              , a -> true             // Are you sure - true
              , null
              , null);
        assertEquals(WindowCloseType.CLOSE_WITH_CHANGE, windowCloseType); // check to see if close type is correct
        assertEquals(2, appointments.size()); // check number of appointments

        // Check Repeat
        RepeatableAppointment a = AppointmentFactory.newAppointment()
                .withAppointmentGroup(appointmentGroups.get(3))
                .withSummary("Weekly Appointment Fixed2");
        Repeat expectedRepeat = RepeatFactory.newRepeat()
                .withStartLocalDate(LocalDate.of(2015, 10, 5))
                .withStartLocalTime(LocalTime.of(8, 45))
                .withEndLocalTime(LocalTime.of(10, 15))
                .withIntervalUnit(IntervalUnit.WEEKLY)
                .withDayOfWeek(DayOfWeek.MONDAY, true)
                .withDayOfWeek(DayOfWeek.WEDNESDAY, true)
                .withDayOfWeek(DayOfWeek.FRIDAY, true)
                .withRepeatFrequency(2)
                .withEndCriteria(EndCriteria.AFTER)
                .withEndAfterEvents(50)
                .withDeletedDates(new HashSet<LocalDate>(Arrays.asList(LocalDate.of(2015, 11, 2))))
                .withAppointmentData(a);
        assertEquals(expectedRepeat, repeat); // check to see if repeat rule changed correctly
        
        // Check appointments
        Iterator<Appointment> appointmentIteratorNew = appointments.iterator();

        Appointment editedAppointment1 = appointmentIteratorNew.next();
        Appointment expectedAppointment1 = AppointmentFactory.newAppointment()
                .withStartLocalDateTime(LocalDate.of(2015, 11, 4).atTime(8, 45))
                .withEndLocalDateTime(LocalDate.of(2015, 11, 4).atTime(10, 15))
                .withAppointmentGroup(appointmentGroups.get(3))
                .withSummary("Weekly Appointment Fixed2")
                .withRepeatMade(true)
                .withRepeat(repeat);
        assertEquals(expectedAppointment1, editedAppointment1); // Check to see if repeat-generated appointment changed correctly
                       
        Appointment editedAppointment2 = appointmentIteratorNew.next();
        Appointment expectedAppointment2 = AppointmentFactory.newAppointment()
                .withStartLocalDateTime(LocalDate.of(2015, 11, 6).atTime(8, 45))
                .withEndLocalDateTime(LocalDate.of(2015, 11, 6).atTime(10, 15))
                .withAppointmentGroup(appointmentGroups.get(3))
                .withSummary("Weekly Appointment Fixed2")
                .withRepeatMade(true)
                .withRepeat(repeat);
        assertEquals(expectedAppointment2, editedAppointment2); // Check to see if repeat-generated appointment changed correctly
    }

    /**
     * Tests deleting one appointment from a repeat rule
     * @throws ParserConfigurationException 
     */
    @Test
    public void deleteAllWeeklyTimeAndDate() throws ParserConfigurationException
    {
        Repeat repeat = getRepeatWeeklyFixed2();
        Set<Repeat> repeats = new HashSet<Repeat>(Arrays.asList(repeat));
        Set<Appointment> appointments = new TreeSet<Appointment>(getAppointmentComparator());
        LocalDate startDate = LocalDate.of(2015, 11, 1);
        LocalDate endDate = LocalDate.of(2015, 11, 7); // tests one week time range
        Collection<RepeatableAppointment> newAppointments = repeat.makeAppointments(startDate, endDate);
        appointments.addAll(newAppointments);
        Iterator<Appointment> appointmentIterator = appointments.iterator();
        assertEquals(3, appointments.size()); // check number of appointments

        // select appointment and apply changes (should be undone with cancel)
        Appointment selectedAppointment = appointmentIterator.next();
        
        WindowCloseType windowCloseType = RepeatableUtilities.deleteAppointments(
                appointments
              , selectedAppointment
              , repeats
              , a -> RepeatChange.ALL // delete one appointment
              , a -> true             // Are you sure - true
              , null
              , null);
        assertEquals(WindowCloseType.CLOSE_WITH_CHANGE, windowCloseType); // check to see if close type is correct
        assertEquals(0, appointments.size()); // check number of appointments
        assertEquals(0, repeats.size()); // check number of repeats

        System.out.println(repeat);
    }

}
