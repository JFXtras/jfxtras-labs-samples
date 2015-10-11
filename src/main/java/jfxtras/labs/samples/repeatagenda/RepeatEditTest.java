package jfxtras.labs.samples.repeatagenda;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Test;

import javafx.util.Callback;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.Agenda.Appointment;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.AppointmentFactory;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.Repeat;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.Repeat.EndCriteria;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.Repeat.IntervalUnit;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.RepeatableUtilities;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.RepeatableUtilities.RepeatChange;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.RepeatableUtilities.WindowCloseType;

public class RepeatEditTest extends RepeatTestAbstract {

    private final Callback<Collection<Appointment>, Void> writeAppointmentsCallback = (a) -> { return null; };
    private final Callback<Collection<Repeat>, Void> writeRepeatsCallback = (r) -> { return null; };
    
    @Test
    public void editAllDailyTime()
    {
        Repeat repeat = getRepeatDailyFixed();
        Set<Repeat> repeats = new HashSet<Repeat>(Arrays.asList(repeat));
        Set<Appointment> appointments = new TreeSet<Appointment>(new AppointmentComparator());
        LocalDate startDate = LocalDate.of(2015, 11, 1);
        LocalDate endDate = LocalDate.of(2015, 11, 7);
        repeat.makeAppointments(appointments, startDate, endDate);
        Iterator<Appointment> appointmentIterator = appointments.iterator();
        assertEquals(2, appointments.size()); // check if there are only two appointments
        
        Appointment madeAppointment1 = appointmentIterator.next();
        Appointment appointmentOld = AppointmentFactory.newAppointment(madeAppointment1);
        LocalDate date = madeAppointment1.getStartLocalDateTime().toLocalDate();
        madeAppointment1.setStartLocalDateTime(date.atTime(9, 45)); // change start time
        madeAppointment1.setEndLocalDateTime(date.atTime(11, 0)); // change end time

        Callback<RepeatChange[], RepeatChange> dialogStub = (a) -> RepeatChange.ALL;
        WindowCloseType windowCloseType = RepeatableUtilities.editAppointments(
                appointments
              , madeAppointment1
              , appointmentOld
              , repeats
              , dialogStub
              , writeAppointmentsCallback
              , writeRepeatsCallback);
        assertEquals(WindowCloseType.CLOSE_WITH_CHANGE, windowCloseType); // check to see if close type is corect

        // Check Repeat
        Appointment a = new MyAppointment()
                .withAppointmentGroup(appointmentGroups.get(15))
                .withSummary("Daily Appointment Fixed")
                .withStartLocalDateTime(LocalDate.of(2015, 11, 3).atTime(9, 45))
                .withEndLocalDateTime(LocalDate.of(2015, 11, 3).atTime(11, 0));
        Repeat expectedRepeat =  new MyRepeat()
                .withStartLocalDate(LocalDate.of(2015, 10, 7))
                .withStartLocalTime(LocalTime.of(9, 45))
                .withEndLocalTime(LocalTime.of(11, 0))
                .withIntervalUnit(IntervalUnit.DAILY)
                .withRepeatFrequency(3)
                .withEndCriteria(EndCriteria.AFTER)
                .withEndAfterEvents(11)
                .withAppointmentData(a);
        assertEquals(expectedRepeat, repeat); // check to see if repeat rule changed correctly
        assertEquals(2, appointments.size()); // check if there are only two appointments

        // Check Appointments
        Appointment expectedAppointment1 = AppointmentFactory.newAppointment()
            .withStartLocalDateTime(LocalDate.of(2015, 11, 3).atTime(9, 45))
            .withEndLocalDateTime(LocalDate.of(2015, 11, 3).atTime(11, 0))
            .withAppointmentGroup(appointmentGroups.get(9))
            .withSummary("Daily Appointment Fixed")
            .withRepeatMade(true)
            .withRepeat(repeat);
        assertEquals(expectedAppointment1, madeAppointment1); // Check to see if repeat-generated appointment changed correctly

        Appointment madeAppointment2 = appointmentIterator.next();
        Appointment expectedAppointment2 = AppointmentFactory.newAppointment()
                .withStartLocalDateTime(LocalDate.of(2015, 11, 6).atTime(9, 45))
                .withEndLocalDateTime(LocalDate.of(2015, 11, 6).atTime(11, 0))
                .withAppointmentGroup(appointmentGroups.get(9))
                .withSummary("Daily Appointment Fixed")
                .withRepeatMade(true)
                .withRepeat(repeat);
        assertEquals(expectedAppointment2, madeAppointment2); // Check to see if repeat-generated appointment changed correctly
    }

    @Test
    public void editAllDailyTimeAndDate()
    {
        Repeat repeat = getRepeatDailyFixed();
        Set<Repeat> repeats = new HashSet<Repeat>(Arrays.asList(repeat));
        Set<Appointment> appointments = new TreeSet<Appointment>(new AppointmentComparator());
        LocalDate startDate = LocalDate.of(2015, 11, 1);
        LocalDate endDate = LocalDate.of(2015, 11, 7);
        repeat.makeAppointments(appointments, startDate, endDate);
        Iterator<Appointment> appointmentIterator = appointments.iterator();
        assertEquals(2, appointments.size()); // check if there are only two appointments

        appointments.stream().forEach(a -> System.out.println("dates1 " + a.getStartLocalDateTime()));
        
        Appointment madeAppointment1 = appointmentIterator.next();
        Appointment appointmentOld = AppointmentFactory.newAppointment(madeAppointment1);
        LocalDate date = madeAppointment1.getStartLocalDateTime().toLocalDate().plusDays(1); // shift all appointments 1 day forward
        madeAppointment1.setStartLocalDateTime(date.atTime(9, 45)); // change start time
        madeAppointment1.setEndLocalDateTime(date.atTime(11, 0)); // change end time
        
        Callback<RepeatChange[], RepeatChange> dialogStub = (a) -> RepeatChange.ALL;
        WindowCloseType windowCloseType = RepeatableUtilities.editAppointments(
                appointments
              , madeAppointment1
              , appointmentOld
              , repeats
              , dialogStub
              , writeAppointmentsCallback
              , writeRepeatsCallback);
        assertEquals(WindowCloseType.CLOSE_WITH_CHANGE, windowCloseType); // check to see if close type is correct
        assertEquals(3, appointments.size()); // check if there are only three appointments

        // Check Repeat
        Appointment a = new MyAppointment()
                .withAppointmentGroup(appointmentGroups.get(15))
                .withSummary("Daily Appointment Fixed")
                .withStartLocalDateTime(LocalDate.of(2015, 11, 4).atTime(9, 45))
                .withEndLocalDateTime(LocalDate.of(2015, 11, 4).atTime(11, 0));
        Repeat expectedRepeat =  new MyRepeat()
                .withStartLocalDate(LocalDate.of(2015, 10, 7))
                .withStartLocalTime(LocalTime.of(9, 45))
                .withEndLocalTime(LocalTime.of(11, 0))
                .withIntervalUnit(IntervalUnit.DAILY)
                .withRepeatFrequency(3)
                .withEndCriteria(EndCriteria.AFTER)
                .withEndAfterEvents(11)
                .withAppointmentData(a);
        assertEquals(expectedRepeat, repeat); // check to see if repeat rule changed correctly
        
        appointments.stream().forEach(b -> System.out.println("dates2 " + b.getStartLocalDateTime()));

        // Check appointments
        Iterator<Appointment> appointmentIteratorNew = appointments.iterator();

        madeAppointment1 = appointmentIteratorNew.next();
        Appointment expectedAppointment1 = AppointmentFactory.newAppointment()
            .withStartLocalDateTime(LocalDate.of(2015, 11, 1).atTime(9, 45))
            .withEndLocalDateTime(LocalDate.of(2015, 11, 1).atTime(11, 0))
            .withAppointmentGroup(appointmentGroups.get(9))
            .withSummary("Daily Appointment Fixed")
            .withRepeatMade(true)
            .withRepeat(repeat);
        assertEquals(expectedAppointment1, madeAppointment1); // Check to see if repeat-generated appointment changed correctly
                       
        System.out.println("appointments.size() " + appointments.size());
        Appointment madeAppointment2 = appointmentIteratorNew.next();
        Appointment expectedAppointment2 = AppointmentFactory.newAppointment()
                .withStartLocalDateTime(LocalDate.of(2015, 11, 4).atTime(9, 45))
                .withEndLocalDateTime(LocalDate.of(2015, 11, 4).atTime(11, 0))
                .withAppointmentGroup(appointmentGroups.get(9))
                .withSummary("Daily Appointment Fixed")
                .withRepeatMade(true)
                .withRepeat(repeat);
        assertEquals(expectedAppointment2, madeAppointment2); // Check to see if repeat-generated appointment changed correctly

        Appointment madeAppointment3 = appointmentIteratorNew.next();
        Appointment expectedAppointment3 = AppointmentFactory.newAppointment()
                .withStartLocalDateTime(LocalDate.of(2015, 11, 7).atTime(9, 45))
                .withEndLocalDateTime(LocalDate.of(2015, 11, 7).atTime(11, 0))
                .withAppointmentGroup(appointmentGroups.get(9))
                .withSummary("Daily Appointment Fixed")
                .withRepeatMade(true)
                .withRepeat(repeat);
        assertEquals(expectedAppointment3, madeAppointment3); // Check to see if repeat-generated appointment changed correctly

    }
    
    @Test
    public void editWeeklyTimeOnly()
    {
        Repeat repeat = getRepeatWeeklyFixed2();
        Set<Repeat> repeats = new HashSet<Repeat>(Arrays.asList(repeat));
        List<Appointment> appointments = new ArrayList<Appointment>();
        LocalDate startDate = LocalDate.of(2015, 11, 1);
        LocalDate endDate = LocalDate.of(2015, 11, 7);
        repeat.makeAppointments(appointments, startDate, endDate);
        Appointment madeAppointment = (appointments.size() == 1) ? appointments.get(0) : null;
        Appointment appointmentOld = AppointmentFactory.newAppointment(madeAppointment);
        LocalDate date = madeAppointment.getStartLocalDateTime().toLocalDate();
        madeAppointment.setStartLocalDateTime(date.atTime(9, 45));
        madeAppointment.setEndLocalDateTime(date.atTime(11, 0));

        appointments.stream().forEach(a -> System.out.println("dates1 " + a.getStartLocalDateTime()));

//        , LocalDate.of(2015, 11, 2)
//        , LocalDate.of(2015, 11, 4)
//        , LocalDate.of(2015, 11, 6)
        
        Callback<RepeatChange[], RepeatChange> dialogStub = (a) -> RepeatChange.ALL;
        WindowCloseType windowCloseType = RepeatableUtilities.editAppointments(
                appointments
              , madeAppointment
              , appointmentOld
              , repeats
              , dialogStub
              , writeAppointmentsCallback
              , writeRepeatsCallback);
        assertEquals(WindowCloseType.CLOSE_WITH_CHANGE, windowCloseType); // check to see if close type is correct
        
        appointments.stream().forEach(a -> System.out.println("dates2 " + a.getStartLocalDateTime()));
        
        Appointment expectedAppointment = AppointmentFactory.newAppointment()
            .withStartLocalDateTime(LocalDate.of(2015, 11, 3).atTime(9, 45))
            .withEndLocalDateTime(LocalDate.of(2015, 11, 3).atTime(11, 0))
            .withAppointmentGroup(appointmentGroups.get(9))
            .withSummary("Daily Appointment Fixed")
            .withRepeatMade(true)
            .withRepeat(repeat);
        assertEquals(expectedAppointment, madeAppointment); // Check to see if repeat-generated appointment changed correctly
        
        Appointment a = new MyAppointment()
                .withAppointmentGroup(appointmentGroups.get(15))
                .withSummary("Daily Appointment Fixed")
                .withStartLocalDateTime(LocalDate.of(2015, 11, 3).atTime(9, 45))
                .withEndLocalDateTime(LocalDate.of(2015, 11, 3).atTime(11, 0));
        Repeat expectedRepeat =  new MyRepeat()
                .withStartLocalDate(LocalDate.of(2015, 10, 7))
                .withStartLocalTime(LocalTime.of(9, 45))
                .withEndLocalTime(LocalTime.of(11, 0))
                .withIntervalUnit(IntervalUnit.DAILY)
                .withRepeatFrequency(3)
                .withEndCriteria(EndCriteria.AFTER)
                .withEndAfterEvents(10)
                .withAppointmentData(a);
        assertEquals(expectedRepeat, repeat); // check to see if repeat rule changed correctly
    }
    
    class AppointmentComparator implements Comparator<Appointment>{
        @Override
        public int compare(Appointment a1, Appointment a2) {
            return a1.getStartLocalDateTime().compareTo(a2.getStartLocalDateTime());
        }
    }

}
