package jfxtras.labs.samples.repeatagenda;

import static org.junit.Assert.assertEquals;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Test;

import javafx.util.Callback;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.Agenda.Appointment;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.AppointmentFactory;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.Repeat;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.Repeat.EndCriteria;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.Repeat.IntervalUnit;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.RepeatFactory;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.RepeatableUtilities;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.RepeatableUtilities.RepeatChange;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.RepeatableUtilities.WindowCloseType;

public class RepeatEditTest extends RepeatTestAbstract {

    // Stub Callbacks for editAppointments
    private final Callback<RepeatChange[], RepeatChange> dialogAllStub = (a) -> RepeatChange.ALL; // simulates selecting ALL from the change dialog
    private final Callback<RepeatChange[], RepeatChange> dialogFutureStub = (a) -> RepeatChange.FUTURE; // simulates selecting FUTURE from the change dialog
    private final Callback<RepeatChange[], RepeatChange> dialogOneStub = (a) -> RepeatChange.ONE; // simulates selecting ONE from the change dialog
    private final Callback<RepeatChange[], RepeatChange> dialogCancelStub = (a) -> RepeatChange.CANCEL; // simulates selecting ONE from the change dialog
    private final Callback<Collection<Appointment>, Void> writeAppointmentsStub = (a) -> { return null; }; // stub that is a placeholder for write appointments IO methods
    private final Callback<Collection<Repeat>, Void> writeRepeatsStub = (r) -> { return null; }; // stubs that is a placeholder for write repeats IO methods
    
    private final Comparator<Appointment> appointmentComparator = (a1, a2)
            -> a1.getStartLocalDateTime().compareTo(a2.getStartLocalDateTime());
    
    /**
     * Tests a daily repeat event with start and end time edit ALL events
     */
    @Test
    public void editAllDailyTime()
    {
        Repeat repeat = getRepeatDailyFixed();
        Set<Repeat> repeats = new HashSet<Repeat>(Arrays.asList(repeat));
        Set<Appointment> appointments = new TreeSet<Appointment>(appointmentComparator);
        LocalDate startDate = LocalDate.of(2015, 11, 1);
        LocalDate endDate = LocalDate.of(2015, 11, 7); // tests one week time range
        repeat.makeAppointments(appointments, startDate, endDate);
        Iterator<Appointment> appointmentIterator = appointments.iterator();
        assertEquals(2, appointments.size()); // check if there are only two appointments
//        appointments.stream().forEach(a -> System.out.println(a.getStartLocalDateTime()));
//        System.exit(0);
        // select appointment and apply changes
        Appointment selectedAppointment = appointmentIterator.next();
        Appointment appointmentOld = AppointmentFactory.newAppointment(selectedAppointment);
        LocalDate date = selectedAppointment.getStartLocalDateTime().toLocalDate();
        selectedAppointment.setStartLocalDateTime(date.atTime(9, 45)); // change start time
        selectedAppointment.setEndLocalDateTime(date.atTime(11, 0)); // change end time
        System.out.println("selectedAppointment " + selectedAppointment.getStartLocalDateTime());

        WindowCloseType windowCloseType = RepeatableUtilities.editAppointments(
                appointments
              , selectedAppointment
              , appointmentOld
              , repeats
              , dialogAllStub
              , writeAppointmentsStub
              , writeRepeatsStub);
        assertEquals(WindowCloseType.CLOSE_WITH_CHANGE, windowCloseType); // check to see if close type is correct

        // Check Repeat
        Appointment a = AppointmentFactory.newAppointment()
                .withAppointmentGroup(appointmentGroups.get(15))
                .withSummary("Daily Appointment Fixed");
//                .withStartLocalDateTime(LocalDate.of(2015, 11, 3).atTime(9, 45))
//                .withEndLocalDateTime(LocalDate.of(2015, 11, 3).atTime(11, 0));
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

        Appointment editedAppointment1 = appointmentIteratorNew.next();
        System.out.println("editedAppointment1 " + editedAppointment1.getStartLocalDateTime());

        Appointment expectedAppointment1 = AppointmentFactory.newAppointment()
            .withStartLocalDateTime(LocalDate.of(2015, 11, 3).atTime(9, 45))
            .withEndLocalDateTime(LocalDate.of(2015, 11, 3).atTime(11, 0))
            .withAppointmentGroup(appointmentGroups.get(15))
            .withSummary("Daily Appointment Fixed")
            .withRepeatMade(true)
            .withRepeat(repeat);
        assertEquals(expectedAppointment1, editedAppointment1); // Check to see if repeat-generated appointment changed correctly

        Appointment editedAppointment2 = appointmentIterator.next();
        System.out.println("editedAppointment2 " + editedAppointment2.getStartLocalDateTime());
        Appointment expectedAppointment2 = AppointmentFactory.newAppointment()
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
        Set<Appointment> appointments = new TreeSet<Appointment>(appointmentComparator);
        LocalDate startDate = LocalDate.of(2015, 11, 1);
        LocalDate endDate = LocalDate.of(2015, 11, 7); // tests one week time range
        repeat.makeAppointments(appointments, startDate, endDate);
        Iterator<Appointment> appointmentIterator = appointments.iterator();
        assertEquals(2, appointments.size()); // check if there are only two appointments

        // select appointment and apply changes
        Appointment selectedAppointment = appointmentIterator.next();
        Appointment appointmentOld = AppointmentFactory.newAppointment(selectedAppointment);
        LocalDate date = selectedAppointment.getStartLocalDateTime().toLocalDate().plusDays(1); // shift all appointments 1 day forward
        selectedAppointment.setStartLocalDateTime(date.atTime(9, 45)); // change start time
        selectedAppointment.setEndLocalDateTime(date.atTime(11, 0)); // change end time
        
        WindowCloseType windowCloseType = RepeatableUtilities.editAppointments(
                appointments
              , selectedAppointment
              , appointmentOld
              , repeats
              , dialogAllStub
              , writeAppointmentsStub
              , writeRepeatsStub);
        assertEquals(WindowCloseType.CLOSE_WITH_CHANGE, windowCloseType); // check to see if close type is correct
        assertEquals(3, appointments.size()); // check if there are only three appointments

        // Check Repeat
        Appointment a = AppointmentFactory.newAppointment()
                .withAppointmentGroup(appointmentGroups.get(15))
                .withSummary("Daily Appointment Fixed");
//                .withStartLocalDateTime(LocalDate.of(2015, 11, 4).atTime(9, 45))
//                .withEndLocalDateTime(LocalDate.of(2015, 11, 4).atTime(11, 0));
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

        Appointment editedAppointment1 = appointmentIteratorNew.next();
        Appointment expectedAppointment1 = AppointmentFactory.newAppointment()
            .withStartLocalDateTime(LocalDate.of(2015, 11, 1).atTime(9, 45))
            .withEndLocalDateTime(LocalDate.of(2015, 11, 1).atTime(11, 0))
            .withAppointmentGroup(appointmentGroups.get(15))
            .withSummary("Daily Appointment Fixed")
            .withRepeatMade(true)
            .withRepeat(repeat);
        assertEquals(expectedAppointment1, editedAppointment1); // Check to see if repeat-generated appointment changed correctly
                       
        Appointment editedAppointment2 = appointmentIteratorNew.next();
        Appointment expectedAppointment2 = AppointmentFactory.newAppointment()
                .withStartLocalDateTime(LocalDate.of(2015, 11, 4).atTime(9, 45))
                .withEndLocalDateTime(LocalDate.of(2015, 11, 4).atTime(11, 0))
                .withAppointmentGroup(appointmentGroups.get(15))
                .withSummary("Daily Appointment Fixed")
                .withRepeatMade(true)
                .withRepeat(repeat);
        assertEquals(expectedAppointment2, editedAppointment2); // Check to see if repeat-generated appointment changed correctly

        Appointment editedAppointment3 = appointmentIteratorNew.next();
        Appointment expectedAppointment3 = AppointmentFactory.newAppointment()
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
     */
    @Test
    public void editAllWeeklyTimeAndDate()
    {
        Repeat repeat = getRepeatWeeklyFixed();
        Set<Repeat> repeats = new HashSet<Repeat>(Arrays.asList(repeat));
        Set<Appointment> appointments = new TreeSet<Appointment>(appointmentComparator);
        LocalDate startDate = LocalDate.of(2015, 11, 1);
        LocalDate endDate = LocalDate.of(2015, 11, 14); // tests two week time range
        repeat.makeAppointments(appointments, startDate, endDate);
        Iterator<Appointment> appointmentIterator = appointments.iterator();
        assertEquals(4, appointments.size()); // check number of appointments

        // select appointment and apply changes
        Appointment selectedAppointment = appointmentIterator.next();
        Appointment appointmentOld = AppointmentFactory.newAppointment(selectedAppointment);
        LocalDate date = selectedAppointment.getStartLocalDateTime().toLocalDate().plusDays(1); // shift Wednesday appointments 1 day forward
        selectedAppointment.setStartLocalDateTime(date.atTime(15, 45)); // change start time
        selectedAppointment.setEndLocalDateTime(date.atTime(16, 30)); // change end time
        
        WindowCloseType windowCloseType = RepeatableUtilities.editAppointments(
                appointments
              , selectedAppointment
              , appointmentOld
              , repeats
              , dialogAllStub
              , writeAppointmentsStub
              , writeRepeatsStub);
        assertEquals(WindowCloseType.CLOSE_WITH_CHANGE, windowCloseType); // check to see if close type is correct
        assertEquals(4, appointments.size()); // check number of appointments

        // Check Repeat
        Appointment a = AppointmentFactory.newAppointment()
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

        Appointment editedAppointment1 = appointmentIteratorNew.next();
        Appointment expectedAppointment1 = AppointmentFactory.newAppointment()
            .withStartLocalDateTime(LocalDate.of(2015, 11, 5).atTime(15, 45))
            .withEndLocalDateTime(LocalDate.of(2015, 11, 5).atTime(16, 30))
            .withAppointmentGroup(appointmentGroups.get(3))
            .withSummary("Weekly Appointment Fixed")
            .withRepeatMade(true)
            .withRepeat(repeat);
        assertEquals(expectedAppointment1, editedAppointment1); // Check to see if repeat-generated appointment changed correctly
                       
        Appointment editedAppointment2 = appointmentIteratorNew.next();
        Appointment expectedAppointment2 = AppointmentFactory.newAppointment()
                .withStartLocalDateTime(LocalDate.of(2015, 11, 6).atTime(15, 45))
                .withEndLocalDateTime(LocalDate.of(2015, 11, 6).atTime(16, 30))
                .withAppointmentGroup(appointmentGroups.get(3))
                .withSummary("Weekly Appointment Fixed")
                .withRepeatMade(true)
                .withRepeat(repeat);
        assertEquals(expectedAppointment2, editedAppointment2); // Check to see if repeat-generated appointment changed correctly

        Appointment editedAppointment3 = appointmentIteratorNew.next();
        Appointment expectedAppointment3 = AppointmentFactory.newAppointment()
                .withStartLocalDateTime(LocalDate.of(2015, 11, 12).atTime(15, 45))
                .withEndLocalDateTime(LocalDate.of(2015, 11, 12).atTime(16, 30))
                .withAppointmentGroup(appointmentGroups.get(3))
                .withSummary("Weekly Appointment Fixed")
                .withRepeatMade(true)
                .withRepeat(repeat);
        assertEquals(expectedAppointment3, editedAppointment3); // Check to see if repeat-generated appointment changed correctly

        Appointment editedAppointment4 = appointmentIteratorNew.next();
        Appointment expectedAppointment4 = AppointmentFactory.newAppointment()
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
        Set<Appointment> appointments = new TreeSet<Appointment>(appointmentComparator);
        LocalDate startDate = LocalDate.of(2015, 11, 1);
        LocalDate endDate = LocalDate.of(2015, 11, 7); // tests one week time range
        repeat.makeAppointments(appointments, startDate, endDate);
        Iterator<Appointment> appointmentIterator = appointments.iterator();
        assertEquals(2, appointments.size()); // check number of appointments

        // select appointment and apply changes
        Appointment selectedAppointment = appointmentIterator.next();
        Appointment appointmentOld = AppointmentFactory.newAppointment(selectedAppointment);
        repeat.setDayOfWeek(DayOfWeek.SUNDAY, true);
        repeat.setDayOfWeek(DayOfWeek.MONDAY, true);
        repeat.setDayOfWeek(DayOfWeek.FRIDAY, false);
        
        WindowCloseType windowCloseType = RepeatableUtilities.editAppointments(
                appointments
              , selectedAppointment
              , appointmentOld
              , repeats
              , dialogAllStub
              , writeAppointmentsStub
              , writeRepeatsStub);
        assertEquals(WindowCloseType.CLOSE_WITH_CHANGE, windowCloseType); // check to see if close type is correct
        appointments.stream().forEach(a -> System.out.println(a.getStartLocalDateTime()));
        assertEquals(3, appointments.size()); // check number of appointments

        // Check Repeat
        Appointment a = AppointmentFactory.newAppointment()
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

        Appointment editedAppointment1 = appointmentIteratorNew.next();
        Appointment expectedAppointment1 = AppointmentFactory.newAppointment()
            .withStartLocalDateTime(LocalDate.of(2015, 11, 1).atTime(18, 0))
            .withEndLocalDateTime(LocalDate.of(2015, 11, 1).atTime(18, 45))
            .withAppointmentGroup(appointmentGroups.get(3))
            .withSummary("Weekly Appointment Fixed")
            .withRepeatMade(true)
            .withRepeat(repeat);
        assertEquals(expectedAppointment1, editedAppointment1); // Check to see if repeat-generated appointment changed correctly
                       
        Appointment editedAppointment2 = appointmentIteratorNew.next();
        Appointment expectedAppointment2 = AppointmentFactory.newAppointment()
                .withStartLocalDateTime(LocalDate.of(2015, 11, 2).atTime(18, 0))
                .withEndLocalDateTime(LocalDate.of(2015, 11, 2).atTime(18, 45))
                .withAppointmentGroup(appointmentGroups.get(3))
                .withSummary("Weekly Appointment Fixed")
                .withRepeatMade(true)
                .withRepeat(repeat);
        assertEquals(expectedAppointment2, editedAppointment2); // Check to see if repeat-generated appointment changed correctly

        Appointment editedAppointment3 = appointmentIteratorNew.next();
        Appointment expectedAppointment3 = AppointmentFactory.newAppointment()
                .withStartLocalDateTime(LocalDate.of(2015, 11, 4).atTime(18, 0))
                .withEndLocalDateTime(LocalDate.of(2015, 11, 4).atTime(18, 45))
                .withAppointmentGroup(appointmentGroups.get(3))
                .withSummary("Weekly Appointment Fixed")
                .withRepeatMade(true)
                .withRepeat(repeat);
        assertEquals(expectedAppointment3, editedAppointment3); // Check to see if repeat-generated appointment changed correctly
    }

    /**
     * Tests a weekly repeat by adding a new day of the week
     */
    @Test
    public void editCancelWeeklyTimeAndDate()
    {
        Repeat repeat = getRepeatWeeklyFixed();
        Set<Repeat> repeats = new HashSet<Repeat>(Arrays.asList(repeat));
        Set<Appointment> appointments = new TreeSet<Appointment>(appointmentComparator);
        LocalDate startDate = LocalDate.of(2015, 11, 1);
        LocalDate endDate = LocalDate.of(2015, 11, 7); // tests one week time range
        repeat.makeAppointments(appointments, startDate, endDate);
        Iterator<Appointment> appointmentIterator = appointments.iterator();
        assertEquals(2, appointments.size()); // check number of appointments

        // select appointment and apply changes (should be undone with cancel)
        Appointment selectedAppointment = appointmentIterator.next();
        Appointment appointmentOld = AppointmentFactory.newAppointment(selectedAppointment);
        repeat.setDayOfWeek(DayOfWeek.SUNDAY, true);
        repeat.setDayOfWeek(DayOfWeek.MONDAY, true);
        repeat.setDayOfWeek(DayOfWeek.FRIDAY, false);
        LocalDate date = selectedAppointment.getStartLocalDateTime().toLocalDate().plusDays(1); // shift Wednesday appointments 1 day forward
        selectedAppointment.setStartLocalDateTime(date.atTime(15, 45)); // change start time
        selectedAppointment.setEndLocalDateTime(date.atTime(16, 30)); // change end time
        
        WindowCloseType windowCloseType = RepeatableUtilities.editAppointments(
                appointments
              , selectedAppointment
              , appointmentOld
              , repeats
              , dialogCancelStub
              , writeAppointmentsStub
              , writeRepeatsStub);
        assertEquals(WindowCloseType.CLOSE_WITHOUT_CHANGE, windowCloseType); // check to see if close type is correct
      selectedAppointment.getRepeat().getDayOfWeekMap().entrySet().stream().forEach(a -> System.out.println(a.getKey() + " " + a.getValue().get()));
//      System.exit(0);
        assertEquals(2, appointments.size()); // check number of appointments

        // Check Repeat
        Appointment a = AppointmentFactory.newAppointment()
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

        Appointment editedAppointment1 = appointmentIteratorNew.next();
        Appointment expectedAppointment1 = AppointmentFactory.newAppointment()
            .withStartLocalDateTime(LocalDate.of(2015, 11, 4).atTime(18, 0))
            .withEndLocalDateTime(LocalDate.of(2015, 11, 4).atTime(18, 45))
            .withAppointmentGroup(appointmentGroups.get(3))
            .withSummary("Weekly Appointment Fixed")
            .withRepeatMade(true)
            .withRepeat(repeat);
        assertEquals(expectedAppointment1, editedAppointment1); // Check to see if repeat-generated appointment changed correctly
                       
        Appointment editedAppointment2 = appointmentIteratorNew.next();
        Appointment expectedAppointment2 = AppointmentFactory.newAppointment()
                .withStartLocalDateTime(LocalDate.of(2015, 11, 6).atTime(18, 0))
                .withEndLocalDateTime(LocalDate.of(2015, 11, 6).atTime(18, 45))
                .withAppointmentGroup(appointmentGroups.get(3))
                .withSummary("Weekly Appointment Fixed")
                .withRepeatMade(true)
                .withRepeat(repeat);
        assertEquals(expectedAppointment2, editedAppointment2); // Check to see if repeat-generated appointment changed correctly
    }
}
