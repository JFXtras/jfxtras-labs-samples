package jfxtras.labs.samples.repeatagenda;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javafx.collections.ObservableList;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.AppointmentFactory;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.Repeat;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.Repeat.EndCriteria;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.Repeat.IntervalUnit;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.Repeat.MonthlyRepeat;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.RepeatFactory;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.RepeatableAgenda;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.RepeatableAgenda.RepeatableAppointment;
import jfxtras.scene.control.agenda.Agenda.Appointment;
import jfxtras.scene.control.agenda.Agenda.AppointmentGroup;

public abstract class RepeatTestAbstract {
    
    // Comparator for tree sort
    private final Comparator<Appointment> appointmentComparator = (a1, a2)
            -> a1.getStartLocalDateTime().compareTo(a2.getStartLocalDateTime());
    public final Comparator<Appointment> getAppointmentComparator() { return appointmentComparator; }
    
    public final static ObservableList<AppointmentGroup> DEFAULT_APPOINTMENT_GROUPS
    = javafx.collections.FXCollections.observableArrayList(
            IntStream
            .range(0, 23)
            .mapToObj(i -> new RepeatableAgenda.AppointmentGroupImpl()
//                   .withStyleClass("group" + i) // skipped due to static variable problem with junit
                   .withKey(i)
                   .withDescription("group" + (i < 10 ? "0" : "") + i))
            .collect(Collectors.toList()));
    ObservableList<AppointmentGroup> appointmentGroups = DEFAULT_APPOINTMENT_GROUPS;
    
    public Repeat getRepeatWeekly()
    {
        RepeatableAppointment a1 = AppointmentFactory.newAppointment()
                .withAppointmentGroup(appointmentGroups.get(5))
                .withSummary("Weekly Appointment Variable");
        return new RepeatImpl()
                .withStartLocalDate(LocalDate.now())
                .withStartLocalTime(LocalTime.now().plusHours(3))
                .withEndLocalTime(LocalTime.now().plusHours(5))
                .withEndCriteria(EndCriteria.NEVER)
                .withIntervalUnit(IntervalUnit.WEEKLY)
                .withDayOfWeek(LocalDate.now().getDayOfWeek(), true)
                .withDayOfWeek(LocalDate.now().plusDays(2).getDayOfWeek(), true)
                .withAppointmentData(a1);
    }

    public Repeat getRepeatMonthly()
    {
        RepeatableAppointment a2 = AppointmentFactory.newAppointment()
                .withAppointmentGroup(appointmentGroups.get(9))
                .withSummary("Monthly Appointment Variable");
        return new RepeatImpl()
                .withStartLocalDate(LocalDate.now().minusDays(1))
                .withStartLocalTime(LocalTime.now().minusHours(5))
                .withEndLocalTime(LocalTime.now().minusHours(3))
                .withEndCriteria(EndCriteria.ON)
                .withEndOnDate(LocalDate.now().minusDays(1).plusMonths(3))
                .withIntervalUnit(IntervalUnit.MONTHLY)
                .withMonthlyRepeat(MonthlyRepeat.DAY_OF_MONTH)
                .withAppointmentData(a2);
    }
    
    public Repeat getRepeatDaily()
    {
        RepeatableAppointment a3 = AppointmentFactory.newAppointment()
                .withAppointmentGroup(appointmentGroups.get(15))
                .withSummary("Daily Appointment Variable");
        return new RepeatImpl()
                .withStartLocalDate(LocalDate.now().minusDays(2))
                .withStartLocalTime(LocalTime.now().plusHours(4))
                .withEndLocalTime(LocalTime.now().plusHours(7))
                .withEndCriteria(EndCriteria.AFTER)
                .withIntervalUnit(IntervalUnit.DAILY)
                .withRepeatFrequency(2)
                .withEndAfterEvents(5)
                .withAppointmentData(a3);
    }

    public Repeat getRepeatDailyFixed()
    {
        RepeatableAppointment a = AppointmentFactory.newAppointment()
                .withAppointmentGroup(appointmentGroups.get(15))
                .withSummary("Daily Appointment Fixed");
        return RepeatFactory.newRepeat()
                .withStartLocalDate(LocalDate.of(2015, 10, 7))
                .withStartLocalTime(LocalTime.of(8, 45))
                .withEndLocalTime(LocalTime.of(10, 15))
                .withIntervalUnit(IntervalUnit.DAILY)
                .withRepeatFrequency(3)
                .withEndCriteria(EndCriteria.AFTER)
                .withEndAfterEvents(11)
                .withAppointmentData(a);
    }
    
    public Repeat getRepeatWeeklyFixed()
    {
        RepeatableAppointment a1 = AppointmentFactory.newAppointment()
                .withAppointmentGroup(appointmentGroups.get(3))
                .withSummary("Weekly Appointment Fixed");
        return RepeatFactory.newRepeat()
                .withStartLocalDate(LocalDate.of(2015, 10, 7))
                .withStartLocalTime(LocalTime.of(18, 0))
                .withEndLocalTime(LocalTime.of(18, 45))
                .withEndCriteria(EndCriteria.NEVER)
                .withIntervalUnit(IntervalUnit.WEEKLY)
                .withDayOfWeek(DayOfWeek.WEDNESDAY, true)
                .withDayOfWeek(DayOfWeek.FRIDAY, true)
                .withAppointmentData(a1);
    }
    
    public Repeat getRepeatWeeklyFixed2()
    {
        RepeatableAppointment a1 = AppointmentFactory.newAppointment()
                .withAppointmentGroup(appointmentGroups.get(3))
                .withSummary("Weekly Appointment Fixed2");
        return RepeatFactory.newRepeat()
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
                .withAppointmentData(a1);
    }
    
    public Repeat getRepeatMonthlyFixed()
    {
        RepeatableAppointment a2 = AppointmentFactory.newAppointment()
                .withAppointmentGroup(appointmentGroups.get(9))
                .withSummary("Monthly Appointment Fixed");
        return RepeatFactory.newRepeat()
                .withStartLocalDate(LocalDate.of(2015, 10, 7))
                .withStartLocalTime(LocalTime.of(8, 45))
                .withEndLocalTime(LocalTime.of(10, 15))
                .withEndCriteria(EndCriteria.ON)
                .withEndOnDate(LocalDate.of(2016, 10, 7))
                .withIntervalUnit(IntervalUnit.MONTHLY)
                .withMonthlyRepeat(MonthlyRepeat.DAY_OF_MONTH)
                .withAppointmentData(a2);
    }

    public Repeat getRepeatMonthlyFixed2() // repeat every third Thursday
    {
        RepeatableAppointment a2 = AppointmentFactory.newAppointment()
                .withAppointmentGroup(appointmentGroups.get(9))
                .withSummary("Monthly Appointment Fixed2");
        return RepeatFactory.newRepeat()
                .withStartLocalDate(LocalDate.of(2015, 10, 15))
                .withStartLocalTime(LocalTime.of(8, 45))
                .withEndLocalTime(LocalTime.of(10, 15))
                .withEndCriteria(EndCriteria.ON)
                .withEndOnDate(LocalDate.of(2016, 10, 20))
                .withIntervalUnit(IntervalUnit.MONTHLY)
                .withMonthlyRepeat(MonthlyRepeat.DAY_OF_WEEK)
                .withAppointmentData(a2);
    }
    
    public Repeat getRepeatYearlyFixed()
    {
        RepeatableAppointment a2 = AppointmentFactory.newAppointment()
                .withAppointmentGroup(appointmentGroups.get(22))
                .withSummary("Yearly Appointment Fixed");
        return RepeatFactory.newRepeat()
                .withStartLocalDate(LocalDate.of(2015, 10, 7))
                .withStartLocalTime(LocalTime.of(8, 45))
                .withEndLocalTime(LocalTime.of(10, 15))
                .withEndCriteria(EndCriteria.NEVER)
                .withIntervalUnit(IntervalUnit.YEARLY)
                .withAppointmentData(a2);
    }
    
    public Repeat getRepeatDaily2()
    {
        RepeatableAppointment a = AppointmentFactory.newAppointment()
                .withAppointmentGroup(appointmentGroups.get(15))
                .withSummary("Daily Appointment Fixed2");
        return new RepeatImpl()
                .withStartLocalDate(LocalDate.of(2015, 10, 18))
                .withStartLocalTime(LocalTime.of(8, 0))
                .withEndLocalTime(LocalTime.of(9, 30))
                .withEndCriteria(EndCriteria.AFTER)
                .withIntervalUnit(IntervalUnit.DAILY)
                .withRepeatFrequency(2)
                .withEndAfterEvents(5)
                .withAppointmentData(a);
    }
}
