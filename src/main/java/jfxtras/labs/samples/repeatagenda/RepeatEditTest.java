package jfxtras.labs.samples.repeatagenda;

import static org.junit.Assert.assertEquals;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Test;

import jfxtras.labs.samples.repeatagenda.scene.control.agenda.AppointmentFactory;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.Repeat;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.Repeat.EndCriteria;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.Repeat.IntervalUnit;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.RepeatFactory;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.RepeatableAgenda.RepeatableAppointment;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.RepeatableUtilities;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.RepeatableUtilities.RepeatChange;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.RepeatableUtilities.WindowCloseType;
import jfxtras.scene.control.agenda.Agenda.Appointment;

public class RepeatEditTest extends RepeatTestAbstract {
    
    /**
     * Tests a daily repeat event with start and end time edit ALL events
     */
    @Test
    public void editAllDailyTime()
    {
        Repeat repeat = getRepeatDailyFixed();
        Set<Repeat> repeats = new HashSet<Repeat>(Arrays.asList(repeat));
        Set<Appointment> appointments = new TreeSet<Appointment>(getAppointmentComparator());
        LocalDate startDate = LocalDate.of(2015, 11, 1);
        LocalDate endDate = LocalDate.of(2015, 11, 7); // tests one week time range
        Collection<RepeatableAppointment> newAppointments = repeat.makeAppointments(startDate, endDate);
        appointments.addAll(newAppointments);
        Iterator<Appointment> appointmentIterator = appointments.iterator();
        assertEquals(2, appointments.size()); // check if there are only two appointments
//        appointments.stream().forEach(a -> System.out.println(a.getStartLocalDateTime()));
//        System.exit(0);
        // select appointment and apply changes
        RepeatableAppointment selectedAppointment = (RepeatableAppointment) appointmentIterator.next();
        RepeatableAppointment appointmentOld = AppointmentFactory.newAppointment(selectedAppointment);
        LocalDate date = selectedAppointment.getStartLocalDateTime().toLocalDate();
        selectedAppointment.setStartLocalDateTime(date.atTime(9, 45)); // change start time
        selectedAppointment.setEndLocalDateTime(date.atTime(11, 0)); // change end time
        System.out.println("selectedAppointment " + selectedAppointment.getStartLocalDateTime());

        WindowCloseType windowCloseType = RepeatableUtilities.editAppointments(
                selectedAppointment
              , appointmentOld
              , appointments
              , repeats
              , a -> RepeatChange.ALL
              , null
              , null);
        assertEquals(WindowCloseType.CLOSE_WITH_CHANGE, windowCloseType); // check to see if close type is correct

        // Check Repeat
        RepeatableAppointment a = AppointmentFactory.newAppointment()
                .withAppointmentGroup(appointmentGroups.get(15))
                .withSummary("Daily Appointment Fixed");
        Repeat expectedRepeat =  RepeatFactory.newRepeat()
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
        Iterator<Appointment> appointmentIteratorNew = appointments.iterator();
        RepeatableAppointment editedAppointment1 = (RepeatableAppointment) appointmentIteratorNew.next();
//        System.out.println("editedAppointment1 " + editedAppointment1.getStartLocalDateTime());

        RepeatableAppointment expectedAppointment1 = AppointmentFactory.newAppointment()
            .withStartLocalDateTime(LocalDate.of(2015, 11, 3).atTime(9, 45))
            .withEndLocalDateTime(LocalDate.of(2015, 11, 3).atTime(11, 0))
            .withAppointmentGroup(appointmentGroups.get(15))
            .withSummary("Daily Appointment Fixed")
            .withRepeatMade(true)
            .withRepeat(repeat);
        assertEquals(expectedAppointment1, editedAppointment1); // Check to see if repeat-generated appointment changed correctly

        RepeatableAppointment editedAppointment2 = (RepeatableAppointment) appointmentIterator.next();
//        System.out.println("editedAppointment2 " + editedAppointment2.getStartLocalDateTime());
        RepeatableAppointment expectedAppointment2 = AppointmentFactory.newAppointment()
                .withStartLocalDateTime(LocalDate.of(2015, 11, 6).atTime(9, 45))
                .withEndLocalDateTime(LocalDate.of(2015, 11, 6).atTime(11, 0))
                .withAppointmentGroup(appointmentGroups.get(15))
                .withSummary("Daily Appointment Fixed")
                .withRepeatMade(true)
                .withRepeat(repeat);
        assertEquals(expectedAppointment2, editedAppointment2); // Check to see if repeat-generated appointment changed correctly
    }

    /**
     * Tests a daily repeat event with day shift and start and end time edit ALL events
     */
    @Test
    public void editAllDailyTimeAndDate()
    {
        Repeat repeat = getRepeatDailyFixed();
        Set<Repeat> repeats = new HashSet<Repeat>(Arrays.asList(repeat));
        Set<Appointment> appointments = new TreeSet<Appointment>(getAppointmentComparator());
        LocalDate startDate = LocalDate.of(2015, 11, 1);
        LocalDate endDate = LocalDate.of(2015, 11, 7); // tests one week time range
        Collection<RepeatableAppointment> newAppointments = repeat.makeAppointments(startDate, endDate);
        appointments.addAll(newAppointments);
        Iterator<Appointment> appointmentIterator = appointments.iterator();
        assertEquals(2, appointments.size()); // check if there are only two appointments
        appointments.stream().forEach(a -> System.out.println(a.getStartLocalDateTime()));

        // select appointment and apply changes
        RepeatableAppointment selectedAppointment = (RepeatableAppointment) appointmentIterator.next();
        RepeatableAppointment appointmentOld = AppointmentFactory.newAppointment(selectedAppointment);
        LocalDate date = selectedAppointment.getStartLocalDateTime().toLocalDate().plusDays(1); // shift all appointments 1 day forward
        selectedAppointment.setStartLocalDateTime(date.atTime(9, 45)); // change start time
        selectedAppointment.setEndLocalDateTime(date.atTime(11, 0)); // change end time
        
        WindowCloseType windowCloseType = RepeatableUtilities.editAppointments(
                selectedAppointment
              , appointmentOld
              , appointments
              , repeats
              , a -> RepeatChange.ALL
              , null
              , null);
        assertEquals(WindowCloseType.CLOSE_WITH_CHANGE, windowCloseType); // check to see if close type is correct
        appointments.stream().forEach(a -> System.out.println(a.getStartLocalDateTime()));
        assertEquals(3, appointments.size()); // check if there are only three appointments

        // Check Repeat
        RepeatableAppointment a = AppointmentFactory.newAppointment()
                .withAppointmentGroup(appointmentGroups.get(15))
                .withSummary("Daily Appointment Fixed");
        Repeat expectedRepeat = RepeatFactory.newRepeat()
                .withStartLocalDate(LocalDate.of(2015, 10, 7))
                .withStartLocalTime(LocalTime.of(9, 45))
                .withEndLocalTime(LocalTime.of(11, 0))
                .withIntervalUnit(IntervalUnit.DAILY)
                .withRepeatFrequency(3)
                .withEndCriteria(EndCriteria.AFTER)
                .withEndAfterEvents(11)
                .withAppointmentData(a);
        assertEquals(expectedRepeat, repeat); // check to see if repeat rule changed correctly
        
        // Check appointments
        Iterator<Appointment> appointmentIteratorNew = appointments.iterator();

        RepeatableAppointment editedAppointment1 = (RepeatableAppointment) appointmentIteratorNew.next();
        RepeatableAppointment expectedAppointment1 = AppointmentFactory.newAppointment()
            .withStartLocalDateTime(LocalDate.of(2015, 11, 1).atTime(9, 45))
            .withEndLocalDateTime(LocalDate.of(2015, 11, 1).atTime(11, 0))
            .withAppointmentGroup(appointmentGroups.get(15))
            .withSummary("Daily Appointment Fixed")
            .withRepeatMade(true)
            .withRepeat(repeat);
        assertEquals(expectedAppointment1, editedAppointment1); // Check to see if repeat-generated appointment changed correctly
                       
        RepeatableAppointment editedAppointment2 = (RepeatableAppointment) appointmentIteratorNew.next();
        RepeatableAppointment expectedAppointment2 = AppointmentFactory.newAppointment()
                .withStartLocalDateTime(LocalDate.of(2015, 11, 4).atTime(9, 45))
                .withEndLocalDateTime(LocalDate.of(2015, 11, 4).atTime(11, 0))
                .withAppointmentGroup(appointmentGroups.get(15))
                .withSummary("Daily Appointment Fixed")
                .withRepeatMade(true)
                .withRepeat(repeat);
        assertEquals(expectedAppointment2, editedAppointment2); // Check to see if repeat-generated appointment changed correctly

        RepeatableAppointment editedAppointment3 = (RepeatableAppointment) appointmentIteratorNew.next();
        RepeatableAppointment expectedAppointment3 = AppointmentFactory.newAppointment()
                .withStartLocalDateTime(LocalDate.of(2015, 11, 7).atTime(9, 45))
                .withEndLocalDateTime(LocalDate.of(2015, 11, 7).atTime(11, 0))
                .withAppointmentGroup(appointmentGroups.get(15))
                .withSummary("Daily Appointment Fixed")
                .withRepeatMade(true)
                .withRepeat(repeat);
        assertEquals(expectedAppointment3, editedAppointment3); // Check to see if repeat-generated appointment changed correctly
    }
    
    /**
     * Tests a weekly repeat event with day shift and start and end time edit ALL events
     * Starts as Wednesday, Friday changes to Thursday, Friday
     */
    @Test
    public void editAllWeeklyTimeAndDate()
    {
        Repeat repeat = getRepeatWeeklyFixed();
        Set<Repeat> repeats = new HashSet<Repeat>(Arrays.asList(repeat));
        Set<Appointment> appointments = new TreeSet<Appointment>(getAppointmentComparator());
        LocalDate startDate = LocalDate.of(2015, 11, 1);
        LocalDate endDate = LocalDate.of(2015, 11, 14); // tests two week time range
        Collection<RepeatableAppointment> newAppointments = repeat.makeAppointments(startDate, endDate);
        appointments.addAll(newAppointments);
        Iterator<Appointment> appointmentIterator = appointments.iterator();
        assertEquals(4, appointments.size()); // check number of appointments

        // select appointment and apply changes
        RepeatableAppointment selectedAppointment = (RepeatableAppointment) appointmentIterator.next();
        RepeatableAppointment appointmentOld = AppointmentFactory.newAppointment(selectedAppointment);
        LocalDate date = selectedAppointment.getStartLocalDateTime().toLocalDate().plusDays(1); // shift Wednesday appointments 1 day forward
        selectedAppointment.setStartLocalDateTime(date.atTime(15, 45)); // change start time
        selectedAppointment.setEndLocalDateTime(date.atTime(16, 30)); // change end time
        
        WindowCloseType windowCloseType = RepeatableUtilities.editAppointments(
                selectedAppointment
              , appointmentOld
              , appointments
              , repeats
              , a -> RepeatChange.ALL
              , null
              , null);
        assertEquals(WindowCloseType.CLOSE_WITH_CHANGE, windowCloseType); // check to see if close type is correct
        assertEquals(4, appointments.size()); // check number of appointments

        // Check Repeat
        RepeatableAppointment a = AppointmentFactory.newAppointment()
                .withAppointmentGroup(appointmentGroups.get(3))
                .withSummary("Weekly Appointment Fixed");
        Repeat expectedRepeat = RepeatFactory.newRepeat()
                .withStartLocalDate(LocalDate.of(2015, 10, 8))
                .withStartLocalTime(LocalTime.of(15, 45))
                .withEndLocalTime(LocalTime.of(16, 30))
                .withEndCriteria(EndCriteria.NEVER)
                .withIntervalUnit(IntervalUnit.WEEKLY)
                .withDayOfWeek(DayOfWeek.THURSDAY, true)
                .withDayOfWeek(DayOfWeek.FRIDAY, true)
                .withAppointmentData(a);
        assertEquals(expectedRepeat, repeat); // check to see if repeat rule changed correctly

        // Check appointments
        Iterator<Appointment> appointmentIteratorNew = appointments.iterator();

        RepeatableAppointment editedAppointment1 = (RepeatableAppointment) appointmentIteratorNew.next();
        RepeatableAppointment expectedAppointment1 = AppointmentFactory.newAppointment()
            .withStartLocalDateTime(LocalDate.of(2015, 11, 5).atTime(15, 45))
            .withEndLocalDateTime(LocalDate.of(2015, 11, 5).atTime(16, 30))
            .withAppointmentGroup(appointmentGroups.get(3))
            .withSummary("Weekly Appointment Fixed")
            .withRepeatMade(true)
            .withRepeat(repeat);
        assertEquals(expectedAppointment1, editedAppointment1); // Check to see if repeat-generated appointment changed correctly
                       
        RepeatableAppointment editedAppointment2 = (RepeatableAppointment) appointmentIteratorNew.next();
        RepeatableAppointment expectedAppointment2 = AppointmentFactory.newAppointment()
                .withStartLocalDateTime(LocalDate.of(2015, 11, 6).atTime(15, 45))
                .withEndLocalDateTime(LocalDate.of(2015, 11, 6).atTime(16, 30))
                .withAppointmentGroup(appointmentGroups.get(3))
                .withSummary("Weekly Appointment Fixed")
                .withRepeatMade(true)
                .withRepeat(repeat);
        assertEquals(expectedAppointment2, editedAppointment2); // Check to see if repeat-generated appointment changed correctly

        RepeatableAppointment editedAppointment3 = (RepeatableAppointment) appointmentIteratorNew.next();
        RepeatableAppointment expectedAppointment3 = AppointmentFactory.newAppointment()
                .withStartLocalDateTime(LocalDate.of(2015, 11, 12).atTime(15, 45))
                .withEndLocalDateTime(LocalDate.of(2015, 11, 12).atTime(16, 30))
                .withAppointmentGroup(appointmentGroups.get(3))
                .withSummary("Weekly Appointment Fixed")
                .withRepeatMade(true)
                .withRepeat(repeat);
        assertEquals(expectedAppointment3, editedAppointment3); // Check to see if repeat-generated appointment changed correctly

        RepeatableAppointment editedAppointment4 = (RepeatableAppointment) appointmentIteratorNew.next();
        RepeatableAppointment expectedAppointment4 = AppointmentFactory.newAppointment()
                .withStartLocalDateTime(LocalDate.of(2015, 11, 13).atTime(15, 45))
                .withEndLocalDateTime(LocalDate.of(2015, 11, 13).atTime(16, 30))
                .withAppointmentGroup(appointmentGroups.get(3))
                .withSummary("Weekly Appointment Fixed")
                .withRepeatMade(true)
                .withRepeat(repeat);
        assertEquals(expectedAppointment4, editedAppointment4); // Check to see if repeat-generated appointment changed correctly
    }
    
    /**
     * Tests a weekly repeat by adding a new day of the week
     */
    @Test
    public void editAllWeeklyTimeAndDate2()
    {
        Repeat repeat = getRepeatWeeklyFixed();
        Set<Repeat> repeats = new HashSet<Repeat>(Arrays.asList(repeat));
        Set<Appointment> appointments = new TreeSet<Appointment>(getAppointmentComparator());
        LocalDate startDate = LocalDate.of(2015, 11, 1);
        LocalDate endDate = LocalDate.of(2015, 11, 7); // tests one week time range
        Collection<RepeatableAppointment> newAppointments = repeat.makeAppointments(startDate, endDate);
        appointments.addAll(newAppointments);
        Iterator<Appointment> appointmentIterator = appointments.iterator();
        assertEquals(2, appointments.size()); // check number of appointments

        // select appointment and apply changes
        RepeatableAppointment selectedAppointment = (RepeatableAppointment) appointmentIterator.next();
        RepeatableAppointment appointmentOld = AppointmentFactory.newAppointment(selectedAppointment);
        repeat.setDayOfWeek(DayOfWeek.SUNDAY, true);
        repeat.setDayOfWeek(DayOfWeek.MONDAY, true);
        repeat.setDayOfWeek(DayOfWeek.FRIDAY, false);
        
        WindowCloseType windowCloseType = RepeatableUtilities.editAppointments(
                selectedAppointment
              , appointmentOld
              , appointments
              , repeats
              , a -> RepeatChange.ALL
              , null
              , null);
        assertEquals(WindowCloseType.CLOSE_WITH_CHANGE, windowCloseType); // check to see if close type is correct
        appointments.stream().forEach(a -> System.out.println(a.getStartLocalDateTime()));
        assertEquals(3, appointments.size()); // check number of appointments

        // Check Repeat
        RepeatableAppointment a = AppointmentFactory.newAppointment()
                .withAppointmentGroup(appointmentGroups.get(3))
                .withSummary("Weekly Appointment Fixed");
        Repeat expectedRepeat = RepeatFactory.newRepeat()
                .withStartLocalDate(LocalDate.of(2015, 10, 8))
                .withStartLocalTime(LocalTime.of(18, 0))
                .withEndLocalTime(LocalTime.of(18, 45))
                .withEndCriteria(EndCriteria.NEVER)
                .withIntervalUnit(IntervalUnit.WEEKLY)
                .withDayOfWeek(DayOfWeek.SUNDAY, true)
                .withDayOfWeek(DayOfWeek.MONDAY, true)
                .withDayOfWeek(DayOfWeek.WEDNESDAY, true)
                .withAppointmentData(a);
        assertEquals(expectedRepeat, repeat); // check to see if repeat rule changed correctly
        
        // Check appointments
        Iterator<Appointment> appointmentIteratorNew = appointments.iterator();

        RepeatableAppointment editedAppointment1 = (RepeatableAppointment) appointmentIteratorNew.next();
        RepeatableAppointment expectedAppointment1 = AppointmentFactory.newAppointment()
            .withStartLocalDateTime(LocalDate.of(2015, 11, 1).atTime(18, 0))
            .withEndLocalDateTime(LocalDate.of(2015, 11, 1).atTime(18, 45))
            .withAppointmentGroup(appointmentGroups.get(3))
            .withSummary("Weekly Appointment Fixed")
            .withRepeatMade(true)
            .withRepeat(repeat);
        assertEquals(expectedAppointment1, editedAppointment1); // Check to see if repeat-generated appointment changed correctly
                       
        RepeatableAppointment editedAppointment2 = (RepeatableAppointment) appointmentIteratorNew.next();
        RepeatableAppointment expectedAppointment2 = AppointmentFactory.newAppointment()
                .withStartLocalDateTime(LocalDate.of(2015, 11, 2).atTime(18, 0))
                .withEndLocalDateTime(LocalDate.of(2015, 11, 2).atTime(18, 45))
                .withAppointmentGroup(appointmentGroups.get(3))
                .withSummary("Weekly Appointment Fixed")
                .withRepeatMade(true)
                .withRepeat(repeat);
        assertEquals(expectedAppointment2, editedAppointment2); // Check to see if repeat-generated appointment changed correctly

        RepeatableAppointment editedAppointment3 = (RepeatableAppointment) appointmentIteratorNew.next();
        RepeatableAppointment expectedAppointment3 = AppointmentFactory.newAppointment()
                .withStartLocalDateTime(LocalDate.of(2015, 11, 4).atTime(18, 0))
                .withEndLocalDateTime(LocalDate.of(2015, 11, 4).atTime(18, 45))
                .withAppointmentGroup(appointmentGroups.get(3))
                .withSummary("Weekly Appointment Fixed")
                .withRepeatMade(true)
                .withRepeat(repeat);
        assertEquals(expectedAppointment3, editedAppointment3); // Check to see if repeat-generated appointment changed correctly
    }

    /**
     * Tests canceling changes to both the repeat and appointment.  Confirms returning to pre-edit state.
     */
    @Test
    public void editCancelWeeklyTimeAndDate()
    {
        Repeat repeat = getRepeatWeeklyFixed();
        Set<Repeat> repeats = new HashSet<Repeat>(Arrays.asList(repeat));
        Set<Appointment> appointments = new TreeSet<Appointment>(getAppointmentComparator());
        LocalDate startDate = LocalDate.of(2015, 11, 1);
        LocalDate endDate = LocalDate.of(2015, 11, 7); // tests one week time range
        Collection<RepeatableAppointment> newAppointments = repeat.makeAppointments(startDate, endDate);
        appointments.addAll(newAppointments);
        Iterator<Appointment> appointmentIterator = appointments.iterator();
        assertEquals(2, appointments.size()); // check number of appointments

        // select appointment and apply changes (should be undone with cancel)
        RepeatableAppointment selectedAppointment = (RepeatableAppointment) appointmentIterator.next();
        RepeatableAppointment appointmentOld = AppointmentFactory.newAppointment(selectedAppointment);
        repeat.setDayOfWeek(DayOfWeek.SUNDAY, true);
        repeat.setDayOfWeek(DayOfWeek.MONDAY, true);
        repeat.setDayOfWeek(DayOfWeek.FRIDAY, false);
        LocalDate date = selectedAppointment.getStartLocalDateTime().toLocalDate().plusDays(1); // shift Wednesday appointments 1 day forward
        selectedAppointment.setStartLocalDateTime(date.atTime(15, 45)); // change start time
        selectedAppointment.setEndLocalDateTime(date.atTime(16, 30)); // change end time
        selectedAppointment.setSummary("Changed summary");
        
        WindowCloseType windowCloseType = RepeatableUtilities.editAppointments(
                selectedAppointment
              , appointmentOld
              , appointments
              , repeats
              , a -> RepeatChange.CANCEL
              , null
              , null);
        assertEquals(WindowCloseType.CLOSE_WITHOUT_CHANGE, windowCloseType); // check to see if close type is correct
        assertEquals(2, appointments.size()); // check number of appointments

        // Check Repeat
        RepeatableAppointment a = AppointmentFactory.newAppointment()
                .withAppointmentGroup(appointmentGroups.get(3))
                .withSummary("Weekly Appointment Fixed");
        Repeat expectedRepeat = RepeatFactory.newRepeat()
                .withStartLocalDate(LocalDate.of(2015, 10, 7))
                .withStartLocalTime(LocalTime.of(18, 0))
                .withEndLocalTime(LocalTime.of(18, 45))
                .withEndCriteria(EndCriteria.NEVER)
                .withIntervalUnit(IntervalUnit.WEEKLY)
                .withDayOfWeek(DayOfWeek.WEDNESDAY, true)
                .withDayOfWeek(DayOfWeek.FRIDAY, true)
                .withAppointmentData(a);
        assertEquals(expectedRepeat, repeat); // check to see if repeat rule changed correctly
        
        // Check appointments
        Iterator<Appointment> appointmentIteratorNew = appointments.iterator();

        RepeatableAppointment editedAppointment1 = (RepeatableAppointment) appointmentIteratorNew.next();
        RepeatableAppointment expectedAppointment1 = AppointmentFactory.newAppointment()
                .withStartLocalDateTime(LocalDate.of(2015, 11, 4).atTime(18, 0))
                .withEndLocalDateTime(LocalDate.of(2015, 11, 4).atTime(18, 45))
                .withAppointmentGroup(appointmentGroups.get(3))
                .withSummary("Weekly Appointment Fixed")
                .withRepeatMade(true)
                .withRepeat(repeat);
        assertEquals(expectedAppointment1, editedAppointment1); // Check to see if repeat-generated appointment changed correctly
                       
        RepeatableAppointment editedAppointment2 = (RepeatableAppointment) appointmentIteratorNew.next();
        RepeatableAppointment expectedAppointment2 = AppointmentFactory.newAppointment()
                .withStartLocalDateTime(LocalDate.of(2015, 11, 6).atTime(18, 0))
                .withEndLocalDateTime(LocalDate.of(2015, 11, 6).atTime(18, 45))
                .withAppointmentGroup(appointmentGroups.get(3))
                .withSummary("Weekly Appointment Fixed")
                .withRepeatMade(true)
                .withRepeat(repeat);
        assertEquals(expectedAppointment2, editedAppointment2); // Check to see if repeat-generated appointment changed correctly
    }

    /**
     * Tests canceling changes to both the repeat and appointment.  Confirms returning to pre-edit state.
     */
    @Test
    public void editOneWeeklyTimeAndDate()
    {
        Repeat repeat = getRepeatWeeklyFixed();
        Set<Repeat> repeats = new HashSet<Repeat>(Arrays.asList(repeat));
        Set<Appointment> appointments = new TreeSet<Appointment>(getAppointmentComparator());
        LocalDate startDate = LocalDate.of(2015, 11, 1);
        LocalDate endDate = LocalDate.of(2015, 11, 7); // tests one week time range
        Collection<RepeatableAppointment> newAppointments = repeat.makeAppointments(startDate, endDate);
        appointments.addAll(newAppointments);
        Iterator<Appointment> appointmentIterator = appointments.iterator();
        assertEquals(2, appointments.size()); // check number of appointments

        // select appointment and apply changes (should be undone with cancel)
        RepeatableAppointment selectedAppointment = (RepeatableAppointment) appointmentIterator.next();
        RepeatableAppointment appointmentOld = AppointmentFactory.newAppointment(selectedAppointment);
        LocalDate date = selectedAppointment.getStartLocalDateTime().toLocalDate().plusDays(1); // shift Wednesday appointments 1 day forward
        selectedAppointment.setStartLocalDateTime(date.atTime(15, 45)); // change start time
        selectedAppointment.setEndLocalDateTime(date.atTime(16, 30)); // change end time
        selectedAppointment.setSummary("Changed summary");
        
        WindowCloseType windowCloseType = RepeatableUtilities.editAppointments(
                selectedAppointment
              , appointmentOld
              , appointments
              , repeats
              , a -> RepeatChange.ONE
              , null
              , null);
        assertEquals(WindowCloseType.CLOSE_WITH_CHANGE, windowCloseType); // check to see if close type is correct
        assertEquals(2, appointments.size()); // check number of appointments

        // Check Repeat
        RepeatableAppointment a = AppointmentFactory.newAppointment()
                .withAppointmentGroup(appointmentGroups.get(3))
                .withSummary("Weekly Appointment Fixed");
        Repeat expectedRepeat = RepeatFactory.newRepeat()
                .withStartLocalDate(LocalDate.of(2015, 10, 7))
                .withStartLocalTime(LocalTime.of(18, 0))
                .withEndLocalTime(LocalTime.of(18, 45))
                .withEndCriteria(EndCriteria.NEVER)
                .withIntervalUnit(IntervalUnit.WEEKLY)
                .withDayOfWeek(DayOfWeek.WEDNESDAY, true)
                .withDayOfWeek(DayOfWeek.FRIDAY, true)
                .withDeletedDates(new HashSet<LocalDate>(Arrays.asList(LocalDate.of(2015, 11, 4))))
                .withAppointmentData(a);
        assertEquals(expectedRepeat, repeat); // check to see if repeat rule changed correctly
        
        // Check appointments
        Iterator<Appointment> appointmentIteratorNew = appointments.iterator();

        RepeatableAppointment editedAppointment1 = (RepeatableAppointment) appointmentIteratorNew.next();
        RepeatableAppointment expectedAppointment1 = AppointmentFactory.newAppointment()
                .withStartLocalDateTime(LocalDate.of(2015, 11, 5).atTime(15, 45))
                .withEndLocalDateTime(LocalDate.of(2015, 11, 5).atTime(16, 30))
                .withAppointmentGroup(appointmentGroups.get(3))
                .withSummary("Changed summary")
                .withRepeatMade(false)
                .withRepeat(repeat);
        assertEquals(expectedAppointment1, editedAppointment1); // Check to see if repeat-generated appointment changed correctly
                       
        RepeatableAppointment editedAppointment2 = (RepeatableAppointment) appointmentIteratorNew.next();
        RepeatableAppointment expectedAppointment2 = AppointmentFactory.newAppointment()
                .withStartLocalDateTime(LocalDate.of(2015, 11, 6).atTime(18, 0))
                .withEndLocalDateTime(LocalDate.of(2015, 11, 6).atTime(18, 45))
                .withAppointmentGroup(appointmentGroups.get(3))
                .withSummary("Weekly Appointment Fixed")
                .withRepeatMade(true)
                .withRepeat(repeat);
        assertEquals(expectedAppointment2, editedAppointment2); // Check to see if repeat-generated appointment changed correctly
    }

    /**
     * Tests editing only future appointments.  Splits one repeat rule into two rules.
     */
    @Test
    public void editFutureDailyTimeAndDate()
    {
        final Repeat repeat = getRepeatDailyFixed();
        final List<Repeat> repeats = new ArrayList<Repeat>(Arrays.asList(repeat));
        final Set<Appointment> appointments = new TreeSet<Appointment>(getAppointmentComparator());
        final LocalDate startDate = LocalDate.of(2015, 10, 25);
        final LocalDate endDate = LocalDate.of(2015, 10, 31); // tests one week time range
        Collection<RepeatableAppointment> newAppointments = repeat.makeAppointments(startDate, endDate);
        appointments.addAll(newAppointments);
        final Iterator<Appointment> appointmentIterator = appointments.iterator();
        assertEquals(3, appointments.size()); // check number of appointments

        // select appointment and apply changes
        appointmentIterator.next();
        final RepeatableAppointment selectedAppointment = (RepeatableAppointment) appointmentIterator.next(); // select second appointment
        final RepeatableAppointment appointmentOld = AppointmentFactory.newAppointment(selectedAppointment);
        LocalDate date = selectedAppointment.getStartLocalDateTime().toLocalDate().plusDays(1); // shift Wednesday appointments 1 day forward
        selectedAppointment.setStartLocalDateTime(date.atTime(15, 45)); // change start time
        selectedAppointment.setEndLocalDateTime(date.atTime(16, 30)); // change end time
        selectedAppointment.setSummary("Changed summary");
        selectedAppointment.setAppointmentGroup(appointmentGroups.get(10));

        WindowCloseType windowCloseType = RepeatableUtilities.editAppointments(
                selectedAppointment
              , appointmentOld
              , appointments
              , repeats
              , a -> RepeatChange.FUTURE
              , null
              , null);
        assertEquals(WindowCloseType.CLOSE_WITH_CHANGE, windowCloseType); // check to see if close type is correct
        assertEquals(2, appointments.size()); // check number of appointments

        // Check Repeat (with changes)
        final RepeatableAppointment a1 = AppointmentFactory.newAppointment()
                .withAppointmentGroup(appointmentGroups.get(10))
                .withSummary("Changed summary");
        final Repeat expectedRepeat = RepeatFactory.newRepeat()
                .withStartLocalDate(LocalDate.of(2015, 10, 29))
                .withStartLocalTime(LocalTime.of(15, 45))
                .withEndLocalTime(LocalTime.of(16, 30))
                .withIntervalUnit(IntervalUnit.DAILY)
                .withRepeatFrequency(3)
                .withEndCriteria(EndCriteria.AFTER)
                .withEndAfterEvents(4)
                .withAppointmentData(a1);
        assertEquals(expectedRepeat, repeat); // check to see if repeat rule changed correctly
        assertEquals(1, repeat.getAppointments().size());

        // Check Repeat (original settings, but ends earlier)
        final RepeatableAppointment a2 = AppointmentFactory.newAppointment()
                .withAppointmentGroup(appointmentGroups.get(15))
                .withSummary("Daily Appointment Fixed");
        final Repeat expectedRepeat2 = RepeatFactory.newRepeat()
                .withStartLocalDate(LocalDate.of(2015, 10, 7))
                .withStartLocalTime(LocalTime.of(8, 45))
                .withEndLocalTime(LocalTime.of(10, 15))
                .withIntervalUnit(IntervalUnit.DAILY)
                .withRepeatFrequency(3)
                .withEndCriteria(EndCriteria.ON)
                .withEndOnDate(LocalDate.of(2015, 10, 25))
                .withAppointmentData(a2);
        final Repeat repeat2 = repeats.get(1);
        assertEquals(1, repeat2.getAppointments().size());
        assertEquals(expectedRepeat2, repeat2); // check to see if repeat rule changed correctly
        
        // Check appointments
        Iterator<Appointment> appointmentIteratorNew = appointments.iterator();

        final RepeatableAppointment editedAppointment1 = (RepeatableAppointment) appointmentIteratorNew.next();
        final RepeatableAppointment expectedAppointment1 = AppointmentFactory.newAppointment()
                .withStartLocalDateTime(LocalDate.of(2015, 10, 25).atTime(8, 45))
                .withEndLocalDateTime(LocalDate.of(2015, 10, 25).atTime(10, 15))
                .withAppointmentGroup(appointmentGroups.get(15))
                .withSummary("Daily Appointment Fixed")
                .withRepeatMade(true)
                .withRepeat(repeat);
        assertEquals(expectedAppointment1, editedAppointment1); // Check to see if repeat-generated appointment changed correctly
                       
        final RepeatableAppointment editedAppointment2 = (RepeatableAppointment) appointmentIteratorNew.next();
        final RepeatableAppointment expectedAppointment2 = AppointmentFactory.newAppointment()
                .withStartLocalDateTime(LocalDate.of(2015, 10, 29).atTime(15, 45))
                .withEndLocalDateTime(LocalDate.of(2015, 10, 29).atTime(16, 30))
                .withAppointmentGroup(appointmentGroups.get(10))
                .withSummary("Changed summary")
                .withRepeatMade(true)
                .withRepeat(repeat);
        assertEquals(expectedAppointment2, editedAppointment2); // Check to see if repeat-generated appointment changed correctly
    }

    /**
     * Tests editing only future appointments.  Splits one repeat rule into two rules.
     */
    @Test
    public void editFutureWeeklyTimeAndDate()
    {
        
        Repeat repeat = getRepeatWeeklyFixed2();
        final List<Repeat> repeats = new ArrayList<Repeat>(Arrays.asList(repeat));
        Set<Appointment> appointments = new TreeSet<Appointment>(getAppointmentComparator());
        LocalDate startDate = LocalDate.of(2015, 11, 29);
        LocalDate endDate = LocalDate.of(2015, 12, 5); // tests one week time range
        Collection<RepeatableAppointment> newAppointments = repeat.makeAppointments(startDate, endDate);
        appointments.addAll(newAppointments);
        Iterator<Appointment> appointmentIterator = appointments.iterator();
        assertEquals(3, appointments.size()); // check number of appointments

        // select appointment and apply changes
        appointmentIterator.next(); // first appointment
        appointmentIterator.next(); // second appointment
        RepeatableAppointment selectedAppointment = (RepeatableAppointment) appointmentIterator.next(); // select third appointment (Friday)
        RepeatableAppointment appointmentOld = AppointmentFactory.newAppointment(selectedAppointment);
        LocalDate date = selectedAppointment.getStartLocalDateTime().toLocalDate().plusDays(1); // shift Wednesday appointments 1 day forward
        selectedAppointment.setStartLocalDateTime(date.atTime(3, 45)); // change start time
        selectedAppointment.setEndLocalDateTime(date.atTime(5, 10)); // change end time
        selectedAppointment.setSummary("Changed summary");
        selectedAppointment.setAppointmentGroup(appointmentGroups.get(12));

        WindowCloseType windowCloseType = RepeatableUtilities.editAppointments(
                selectedAppointment
              , appointmentOld
              , appointments
              , repeats
              , a -> RepeatChange.FUTURE
              , null
              , null);

        assertEquals(WindowCloseType.CLOSE_WITH_CHANGE, windowCloseType); // check to see if close type is correct
        assertEquals(3, appointments.size()); // check number of appointments

        // Check Repeat (with changes)
        RepeatableAppointment a1 = AppointmentFactory.newAppointment()
                .withAppointmentGroup(appointmentGroups.get(12))
                .withSummary("Changed summary");
        Repeat expectedRepeat = RepeatFactory.newRepeat()
                .withStartLocalDate(LocalDate.of(2015, 12, 5))
                .withStartLocalTime(LocalTime.of(3, 45))
                .withEndLocalTime(LocalTime.of(5, 10))
                .withIntervalUnit(IntervalUnit.WEEKLY)
                .withDayOfWeek(DayOfWeek.MONDAY, true)
                .withDayOfWeek(DayOfWeek.WEDNESDAY, true)
                .withDayOfWeek(DayOfWeek.SATURDAY, true)
                .withRepeatFrequency(2)
                .withEndCriteria(EndCriteria.AFTER)
                .withEndAfterEvents(36)
                .withAppointmentData(a1);
        assertEquals(expectedRepeat, repeat); // check to see if repeat rule changed correctly
        assertEquals(1, repeat.getAppointments().size());

        // Check Repeat (original settings, but ends earlier)
        RepeatableAppointment a2 = AppointmentFactory.newAppointment()
                .withAppointmentGroup(appointmentGroups.get(3))
                .withSummary("Weekly Appointment Fixed2");
        Repeat expectedRepeat2 = RepeatFactory.newRepeat()
                .withStartLocalDate(LocalDate.of(2015, 10, 5))
                .withStartLocalTime(LocalTime.of(8, 45))
                .withEndLocalTime(LocalTime.of(10, 15))
                .withIntervalUnit(IntervalUnit.WEEKLY)
                .withDayOfWeek(DayOfWeek.MONDAY, true)
                .withDayOfWeek(DayOfWeek.WEDNESDAY, true)
                .withDayOfWeek(DayOfWeek.FRIDAY, true)
                .withRepeatFrequency(2)
                .withEndCriteria(EndCriteria.ON)
                .withEndOnDate(LocalDate.of(2015, 12, 2))
                .withAppointmentData(a2);
        final Repeat repeat2 = repeats.get(1);
        assertEquals(2, repeat2.getAppointments().size());
        assertEquals(expectedRepeat2, repeat2); // check to see if repeat rule changed correctly
        
        // Check appointments
        Iterator<Appointment> appointmentIteratorNew = appointments.iterator();

        RepeatableAppointment editedAppointment1 = (RepeatableAppointment) appointmentIteratorNew.next();
        RepeatableAppointment expectedAppointment1 = AppointmentFactory.newAppointment()
                .withStartLocalDateTime(LocalDate.of(2015, 11, 30).atTime(8, 45))
                .withEndLocalDateTime(LocalDate.of(2015, 11, 30).atTime(10, 15))
                .withAppointmentGroup(appointmentGroups.get(3))
                .withSummary("Weekly Appointment Fixed2")
                .withRepeatMade(true)
                .withRepeat(repeat);
        assertEquals(expectedAppointment1, editedAppointment1); // Check to see if repeat-generated appointment changed correctly
                       
        RepeatableAppointment editedAppointment2 = (RepeatableAppointment) appointmentIteratorNew.next();
        RepeatableAppointment expectedAppointment2 = AppointmentFactory.newAppointment()
                .withStartLocalDateTime(LocalDate.of(2015, 12, 2).atTime(8, 45))
                .withEndLocalDateTime(LocalDate.of(2015, 12, 2).atTime(10, 15))
                .withAppointmentGroup(appointmentGroups.get(3))
                .withSummary("Weekly Appointment Fixed2")
                .withRepeatMade(true)
                .withRepeat(repeat);
        assertEquals(expectedAppointment2, editedAppointment2); // Check to see if repeat-generated appointment changed correctly

        RepeatableAppointment editedAppointment3 = (RepeatableAppointment) appointmentIteratorNew.next();
        RepeatableAppointment expectedAppointment3 = AppointmentFactory.newAppointment()
                .withStartLocalDateTime(LocalDate.of(2015, 12, 5).atTime(3, 45))
                .withEndLocalDateTime(LocalDate.of(2015, 12, 5).atTime(5, 10))
                .withAppointmentGroup(appointmentGroups.get(12))
                .withSummary("Changed summary")
                .withRepeatMade(true)
                .withRepeat(repeat);
        assertEquals(expectedAppointment3, editedAppointment3); // Check to see if repeat-generated appointment changed correctly
    }

    /**
     * Tests a daily repeat event with day shift and start and end time edit ONE event
     */
    @Test
    public void editOneDailyTimeAndDate()
    {
        Repeat repeat = getRepeatDailyFixed();
        Set<Repeat> repeats = new HashSet<Repeat>(Arrays.asList(repeat));
        Set<Appointment> appointments = new TreeSet<Appointment>(getAppointmentComparator());
        LocalDate startDate = LocalDate.of(2015, 10, 25);
        LocalDate endDate = LocalDate.of(2015, 10, 31); // tests one week time range
        Collection<RepeatableAppointment> newAppointments = repeat.makeAppointments(startDate, endDate);
        appointments.addAll(newAppointments);
        Iterator<Appointment> appointmentIterator = appointments.iterator();
        assertEquals(3, appointments.size()); // check if there are only two appointments

        // select appointment and apply changes
        appointmentIterator.next(); // skip first appointment
        RepeatableAppointment selectedAppointment = (RepeatableAppointment) appointmentIterator.next(); // edit second appointment
        RepeatableAppointment appointmentOld = AppointmentFactory.newAppointment(selectedAppointment);
        LocalDate date = selectedAppointment.getStartLocalDateTime().toLocalDate().plusDays(1); // shift all appointments 1 day forward
        selectedAppointment.setStartLocalDateTime(date.atTime(9, 45)); // change start time
        selectedAppointment.setEndLocalDateTime(date.atTime(11, 0)); // change end time
        selectedAppointment.setSummary("Edited Summary");
        selectedAppointment.setAppointmentGroup(appointmentGroups.get(7));

        WindowCloseType windowCloseType = RepeatableUtilities.editAppointments(
                selectedAppointment
              , appointmentOld
              , appointments
              , repeats
              , a -> RepeatChange.ONE
              , null
              , null);

        assertEquals(WindowCloseType.CLOSE_WITH_CHANGE, windowCloseType); // check to see if close type is correct
        assertEquals(3, appointments.size()); // check if there are only three appointments

        // Check Repeat
        RepeatableAppointment a = AppointmentFactory.newAppointment()
                .withAppointmentGroup(appointmentGroups.get(15))
                .withSummary("Daily Appointment Fixed");
        Repeat expectedRepeat = RepeatFactory.newRepeat()
                .withStartLocalDate(LocalDate.of(2015, 10, 7))
                .withStartLocalTime(LocalTime.of(9, 45))
                .withEndLocalTime(LocalTime.of(11, 0))
                .withIntervalUnit(IntervalUnit.DAILY)
                .withDeletedDates(new HashSet<LocalDate>(Arrays.asList(LocalDate.of(2015, 10, 28))))
                .withRepeatFrequency(3)
                .withEndCriteria(EndCriteria.AFTER)
                .withEndAfterEvents(11)
                .withAppointmentData(a);
        assertEquals(expectedRepeat, repeat); // check to see if repeat rule changed correctly
        
        // Check appointments
        Iterator<Appointment> appointmentIteratorNew = appointments.iterator();

        RepeatableAppointment editedAppointment1 = (RepeatableAppointment) appointmentIteratorNew.next();
        RepeatableAppointment expectedAppointment1 = AppointmentFactory.newAppointment()
            .withStartLocalDateTime(LocalDate.of(2015, 10, 25).atTime(8, 45))
            .withEndLocalDateTime(LocalDate.of(2015, 10, 25).atTime(10, 15))
            .withAppointmentGroup(appointmentGroups.get(15))
            .withSummary("Daily Appointment Fixed")
            .withRepeatMade(true)
            .withRepeat(repeat);
        assertEquals(expectedAppointment1, editedAppointment1); // Check to see if repeat-generated appointment changed correctly
                       
        RepeatableAppointment editedAppointment2 = (RepeatableAppointment) appointmentIteratorNew.next();
        RepeatableAppointment expectedAppointment2 = AppointmentFactory.newAppointment()
                .withStartLocalDateTime(LocalDate.of(2015, 10, 29).atTime(9, 45))
                .withEndLocalDateTime(LocalDate.of(2015, 10, 29).atTime(11, 0))
                .withAppointmentGroup(appointmentGroups.get(7))
                .withSummary("Edited Summary")
                .withRepeatMade(false)
                .withRepeat(null);
        assertEquals(expectedAppointment2, editedAppointment2); // Check to see if repeat-generated appointment changed correctly

        RepeatableAppointment editedAppointment3 = (RepeatableAppointment) appointmentIteratorNew.next();
        RepeatableAppointment expectedAppointment3 = AppointmentFactory.newAppointment()
                .withStartLocalDateTime(LocalDate.of(2015, 10, 31).atTime(8, 45))
                .withEndLocalDateTime(LocalDate.of(2015, 10, 31).atTime(10, 15))
                .withAppointmentGroup(appointmentGroups.get(15))
                .withSummary("Daily Appointment Fixed")
                .withRepeatMade(true)
                .withRepeat(repeat);
        assertEquals(expectedAppointment3, editedAppointment3); // Check to see if repeat-generated appointment changed correctly

        // Change week later and update
        startDate = LocalDate.of(2015, 11, 1);
        endDate = LocalDate.of(2015, 11, 7); // tests one week time range
        Collection<RepeatableAppointment> newAppointments2 = repeat.makeAppointments(startDate, endDate);
        assertEquals(2, newAppointments2.size()); // check if there are only three appointments

        // Change week earlier and update
        startDate = LocalDate.of(2015, 10, 25);
        endDate = LocalDate.of(2015, 10, 31); // tests one week time range
        repeat.getAppointments().clear();
        Collection<RepeatableAppointment> newAppointments3 = repeat.makeAppointments(startDate, endDate);
        assertEquals(2, newAppointments3.size()); // check if there are only three appointments
    
    }
    
    /**
     * Tests editing all daily appointments.  Changes from interval of 2 to 1.
     */
    @Test
    public void editAllDailyInterval()
    {
        final Repeat repeat = getRepeatDaily2();
        final List<Repeat> repeats = new ArrayList<Repeat>(Arrays.asList(repeat));
        final Set<Appointment> appointments = new TreeSet<Appointment>(getAppointmentComparator());
        final LocalDate startDate = LocalDate.of(2015, 10, 18);
        final LocalDate endDate = LocalDate.of(2015, 10, 24); // tests one week time range
        Collection<RepeatableAppointment> newAppointments = repeat.makeAppointments(startDate, endDate);
        appointments.addAll(newAppointments);
        appointments.stream().forEach(a -> System.out.println(a.getStartLocalDateTime()));
        final Iterator<Appointment> appointmentIterator = appointments.iterator();
        assertEquals(4, appointments.size()); // check number of appointments

        // select appointment and apply changes
        appointmentIterator.next();
        final RepeatableAppointment selectedAppointment = (RepeatableAppointment) appointmentIterator.next(); // select second appointment
        final RepeatableAppointment appointmentOld = AppointmentFactory.newAppointment(selectedAppointment);
        repeat.setRepeatFrequency(1);

        WindowCloseType windowCloseType = RepeatableUtilities.editAppointments(
                selectedAppointment
              , appointmentOld
              , appointments
              , repeats
              , a -> RepeatChange.ALL
              , null
              , null);
        assertEquals(WindowCloseType.CLOSE_WITH_CHANGE, windowCloseType); // check to see if close type is correct
        appointments.stream().forEach(a -> System.out.println(a.getStartLocalDateTime()));
        assertEquals(5, appointments.size()); // check number of appointments

        // Check Repeat (with changes)
        RepeatableAppointment a = AppointmentFactory.newAppointment()
                .withAppointmentGroup(appointmentGroups.get(15))
                .withSummary("Daily Appointment Fixed2");
        final Repeat expectedRepeat = RepeatFactory.newRepeat()
                .withStartLocalDate(LocalDate.of(2015, 10, 18))
                .withStartLocalTime(LocalTime.of(8, 0))
                .withEndLocalTime(LocalTime.of(9, 30))
                .withEndCriteria(EndCriteria.AFTER)
                .withIntervalUnit(IntervalUnit.DAILY)
                .withRepeatFrequency(1)
                .withEndAfterEvents(5)
                .withAppointmentData(a);
        assertEquals(expectedRepeat, repeat); // check to see if repeat rule changed correctly
        
        // Check appointments
        Iterator<Appointment> appointmentIteratorNew = appointments.iterator();

        final RepeatableAppointment editedAppointment1 = (RepeatableAppointment) appointmentIteratorNew.next();
        final RepeatableAppointment expectedAppointment1 = AppointmentFactory.newAppointment()
                .withStartLocalDateTime(LocalDate.of(2015, 10, 18).atTime(8, 00))
                .withEndLocalDateTime(LocalDate.of(2015, 10, 18).atTime(9, 30))
                .withAppointmentGroup(appointmentGroups.get(15))
                .withSummary("Daily Appointment Fixed2")
                .withRepeatMade(true)
                .withRepeat(repeat);
        assertEquals(expectedAppointment1, editedAppointment1); // Check to see if repeat-generated appointment changed correctly

        final RepeatableAppointment editedAppointment2 = (RepeatableAppointment) appointmentIteratorNew.next();
        final RepeatableAppointment expectedAppointment2 = AppointmentFactory.newAppointment()
                .withStartLocalDateTime(LocalDate.of(2015, 10, 19).atTime(8, 00))
                .withEndLocalDateTime(LocalDate.of(2015, 10, 19).atTime(9, 30))
                .withAppointmentGroup(appointmentGroups.get(15))
                .withSummary("Daily Appointment Fixed2")
                .withRepeatMade(true)
                .withRepeat(repeat);
        assertEquals(expectedAppointment2, editedAppointment2); // Check to see if repeat-generated appointment changed correctly

        final RepeatableAppointment editedAppointment3 = (RepeatableAppointment) appointmentIteratorNew.next();
        final RepeatableAppointment expectedAppointment3 = AppointmentFactory.newAppointment()
                .withStartLocalDateTime(LocalDate.of(2015, 10, 20).atTime(8, 00))
                .withEndLocalDateTime(LocalDate.of(2015, 10, 20).atTime(9, 30))
                .withAppointmentGroup(appointmentGroups.get(15))
                .withSummary("Daily Appointment Fixed2")
                .withRepeatMade(true)
                .withRepeat(repeat);
        assertEquals(expectedAppointment3, editedAppointment3); // Check to see if repeat-generated appointment changed correctly
        
        final RepeatableAppointment editedAppointment4 = (RepeatableAppointment) appointmentIteratorNew.next();
        final RepeatableAppointment expectedAppointment4 = AppointmentFactory.newAppointment()
                .withStartLocalDateTime(LocalDate.of(2015, 10, 21).atTime(8, 00))
                .withEndLocalDateTime(LocalDate.of(2015, 10, 21).atTime(9, 30))
                .withAppointmentGroup(appointmentGroups.get(15))
                .withSummary("Daily Appointment Fixed2")
                .withRepeatMade(true)
                .withRepeat(repeat);
        assertEquals(expectedAppointment4, editedAppointment4); // Check to see if repeat-generated appointment changed correctly

        final RepeatableAppointment editedAppointment5 = (RepeatableAppointment) appointmentIteratorNew.next();
        final RepeatableAppointment expectedAppointment5 = AppointmentFactory.newAppointment()
                .withStartLocalDateTime(LocalDate.of(2015, 10, 22).atTime(8, 00))
                .withEndLocalDateTime(LocalDate.of(2015, 10, 22).atTime(9, 30))
                .withAppointmentGroup(appointmentGroups.get(15))
                .withSummary("Daily Appointment Fixed2")
                .withRepeatMade(true)
                .withRepeat(repeat);
        assertEquals(expectedAppointment5, editedAppointment5); // Check to see if repeat-generated appointment changed correctly

    
    }
    
    /**
     * Tests editing individual appointment and applying monthly repeat rule.
     */
    @Test
    public void editIndividualToRepeatMonthly()
    {
        // Individual Appointment
        final RepeatableAppointment appointment = AppointmentFactory.newAppointment()
                .withStartLocalDateTime(LocalDate.of(2015, 10, 7).atTime(8, 45))
                .withEndLocalDateTime(LocalDate.of(2015, 10, 7).atTime(10, 15))
                .withAppointmentGroup(appointmentGroups.get(9))
                .withSummary("Monthly Appointment Fixed")
                .withRepeatMade(false);
                
        final RepeatableAppointment appointmentOld = AppointmentFactory.newAppointment(appointment);
        final Set<Appointment> appointments = new TreeSet<Appointment>(getAppointmentComparator());
        appointments.add(appointment);
        final List<Repeat> repeats = new ArrayList<Repeat>();
        
        // Modify Appointment - add repeat
        final Repeat repeat = getRepeatMonthlyFixed();
        appointment.setRepeat(repeat);

        WindowCloseType windowCloseType = RepeatableUtilities.editAppointments(
                appointment
              , appointmentOld
              , appointments
              , repeats
              , null
              , null
              , null);
        assertEquals(WindowCloseType.CLOSE_WITH_CHANGE, windowCloseType); // check to see if close type is correct
        assertEquals(1, appointments.size()); // check number of appointments

        final RepeatableAppointment expectedAppointment = AppointmentFactory.newAppointment()
                .withStartLocalDateTime(LocalDate.of(2015, 10, 7).atTime(8, 45))
                .withEndLocalDateTime(LocalDate.of(2015, 10, 7).atTime(10, 15))
                .withAppointmentGroup(appointmentGroups.get(9))
                .withSummary("Monthly Appointment Fixed")
                .withRepeatMade(true);
        assertEquals(expectedAppointment, appointment); // Check to see if repeat-generated appointment changed correctly        

        // Advance one month later and update, test to see if new repeatable appointment exists
        LocalDate startDate = LocalDate.of(2015, 11, 1);
        LocalDate endDate = LocalDate.of(2015, 11, 7); // tests one week time range
        Collection<RepeatableAppointment> newAppointments = repeat.makeAppointments(startDate, endDate);
        assertEquals(1, newAppointments.size()); // check if there are only three appointments
        RepeatableAppointment appointment2 = newAppointments.iterator().next();
        
        final RepeatableAppointment expectedAppointment2 = AppointmentFactory.newAppointment()
                .withStartLocalDateTime(LocalDate.of(2015, 11, 7).atTime(8, 45))
                .withEndLocalDateTime(LocalDate.of(2015, 11, 7).atTime(10, 15))
                .withAppointmentGroup(appointmentGroups.get(9))
                .withSummary("Monthly Appointment Fixed")
                .withRepeatMade(true);
        assertEquals(expectedAppointment2, appointment2); // Check to see if repeat-generated appointment changed correctly        
    }

   
}
