package jfxtras.labs.samples.repeatagenda.scene.control.agenda;

import java.security.InvalidParameterException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.util.StringConverter;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.Agenda.Appointment;

/**
 * Contains rules for repeatable appointments in the calendar
 *  
 * @author David Bal
 */
public class Repeat {
    
    public boolean isEmpty() { return intervalUnit.getValue() == null; }

    public static Period repeatPeriod = Period.ofWeeks(1);
    
    // Dates for which appointments are to be generated.  Should match the dates displayed on the calendar.
    private LocalDate startDate;
    private LocalDate endDate;
    
    final private ObjectProperty<IntervalUnit> intervalUnit = new SimpleObjectProperty<IntervalUnit>();
    public ObjectProperty<IntervalUnit> intervalUnitProperty() { return intervalUnit; }
    public IntervalUnit getIntervalUnit() { return intervalUnit.getValue(); }
    public void setIntervalUnit(IntervalUnit intervalUnit) { this.intervalUnit.set(intervalUnit); }
    public Repeat withIntervalUnit(IntervalUnit intervalUnit) { setIntervalUnit(intervalUnit); return this; }
    
    final private IntegerProperty repeatFrequency = new SimpleIntegerProperty(1);
    public Integer getRepeatFrequency() { return repeatFrequency.getValue(); }
    public IntegerProperty repeatFrequencyProperty() { return repeatFrequency; }
    public void setRepeatFrequency(Integer repeatFrequency) {
        if (repeatFrequency > 0)
        {
            this.repeatFrequency.set(repeatFrequency);
        } else {
            throw new InvalidParameterException("repeatFrequency can't be less than 1. (" + repeatFrequency + ")");
        }
    }
    public Repeat withRepeatFrequency(Integer repeatFrequency) { setRepeatFrequency(repeatFrequency); return this; }

    final private Map<DayOfWeek, BooleanProperty> dayOfWeekMap = Arrays // Initialized map of all days of the week, each BooleanProperty is false
            .stream(DayOfWeek.values())
            .collect(Collectors.toMap(k -> k, v -> new SimpleBooleanProperty(false)));
    protected Map<DayOfWeek, BooleanProperty> getDayOfWeekMap() { return dayOfWeekMap; }
    public void setDayOfWeek(DayOfWeek d, boolean value) { getDayOfWeekMap().get(d).set(value); }
    public boolean getDayOfWeek(DayOfWeek d) { return getDayOfWeekMap().get(d).get(); }
    public BooleanProperty getDayOfWeekProperty(DayOfWeek d) { return getDayOfWeekMap().get(d); }
    public Repeat withDayOfWeek(DayOfWeek d, boolean value) { setDayOfWeek(d, value); return this; }
    private boolean dayOfWeekMapEqual(Map<DayOfWeek, BooleanProperty> dayOfWeekMap2) {
        Iterator<Entry<DayOfWeek, BooleanProperty>> i1 = getDayOfWeekMap().entrySet().stream().iterator();
        Iterator<Entry<DayOfWeek, BooleanProperty>> i2 = dayOfWeekMap2.entrySet().stream().iterator();
        boolean dayOfWeekEqual = true;
        while (i1.hasNext())
        {
            boolean b1 = i1.next().getValue().get();
            boolean b2 = i2.next().getValue().get();
            if (b1 != b2) {
                dayOfWeekEqual = false;
                break;
            }
        }
        return dayOfWeekEqual;
    }
     
    final private BooleanProperty repeatDayOfMonth = new SimpleBooleanProperty(true); // default option
    private Boolean isRepeatDayOfMonth() { return repeatDayOfMonth.getValue(); }
    public BooleanProperty repeatDayOfMonthProperty() { return repeatDayOfMonth; }
    private void setRepeatDayOfMonth(Boolean repeatDayOfMonth) { this.repeatDayOfMonth.set(repeatDayOfMonth); }

    final private BooleanProperty repeatDayOfWeek = new SimpleBooleanProperty(false);
    private Boolean isRepeatDayOfWeek() { return repeatDayOfWeek.getValue(); }
    public BooleanProperty repeatDayOfWeekProperty() { return repeatDayOfWeek; }
    private void setRepeatDayOfWeek(Boolean repeatDayOfWeek) { this.repeatDayOfWeek.set(repeatDayOfWeek); }
    private int ordinal; // used when repeatDayOfWeek is true, this is the number of weeks into the month the date is set (i.e 3rd Wednesday -> ordinal=3).
    
    public MonthlyRepeat getMonthlyRepeat()
    { // returns MonthlyRepeat enum from boolean properties
        if (isRepeatDayOfMonth()) return MonthlyRepeat.DAY_OF_MONTH;
        if (isRepeatDayOfWeek()) return MonthlyRepeat.DAY_OF_WEEK;
        return null; // should not get here
    }
    public void setMonthlyRepeat(MonthlyRepeat monthlyRepeat)
    { // sets boolean properties from MonthlyRepeat
        switch (monthlyRepeat)
        {
        case DAY_OF_MONTH:
            setRepeatDayOfMonth(true);
            setRepeatDayOfWeek(false);
            break;
        case DAY_OF_WEEK:
            setRepeatDayOfMonth(false);
            setRepeatDayOfWeek(true);
            DayOfWeek dayOfWeek = getStartLocalDate().getDayOfWeek();
            LocalDate myDay = getStartLocalDate()
                    .with(TemporalAdjusters.firstDayOfMonth())
                    .with(TemporalAdjusters.next(dayOfWeek));
            ordinal = 0;
            if (dayOfWeek == myDay.getDayOfWeek()) ordinal++; // add one if first day of month is correct day of week
            while (! myDay.isAfter(getStartLocalDate()))
            { // set ordinal number for day-of-week repeat
                ordinal++;
                myDay = myDay.with(TemporalAdjusters.next(dayOfWeek));
            }
        }
    }
    public Repeat withMonthlyRepeat(MonthlyRepeat monthlyRepeat) { setMonthlyRepeat(monthlyRepeat); return this; }
    
    final private ObjectProperty<LocalDate> startLocalDate = new SimpleObjectProperty<LocalDate>();
    public ObjectProperty<LocalDate> startLocalDateProperty() { return startLocalDate; }
    public LocalDate getStartLocalDate() { return startLocalDate.getValue(); }
    public void setStartLocalDate(LocalDate startDate) { this.startLocalDate.set(startDate); }
    public Repeat withStartLocalDate(LocalDate startDate) { setStartLocalDate(startDate); return this; }

    final private ObjectProperty<LocalTime> startLocalTime = new SimpleObjectProperty<LocalTime>(this, "startLocalTime");
    public ObjectProperty<LocalTime> startLocalTimeProperty() { return startLocalTime; }
    public LocalTime getStartLocalTime() { return startLocalTime.getValue(); }
    public void setStartLocalTime(LocalTime value) { startLocalTime.setValue(value); }
    public Repeat withStartLocalTime(LocalTime value) { setStartLocalTime(value); return this; }
    
    final private ObjectProperty<LocalTime> endLocalTime = new SimpleObjectProperty<LocalTime>(this, "endLocalTimeProperty");
    public ObjectProperty<LocalTime> endLocalTimeProperty() { return endLocalTime; }
    public LocalTime getEndLocalTime() { return endLocalTime.getValue(); }
    public void setEndLocalTime(LocalTime value) { endLocalTime.setValue(value); }
    public Repeat withEndLocalTime(LocalTime value) { setEndLocalTime(value); return this; } 
    
    final private ObjectProperty<EndCriteria> endCriteria = new SimpleObjectProperty<EndCriteria>();
    public ObjectProperty<EndCriteria> endCriteriaProperty() { return endCriteria; }
    public EndCriteria getEndCriteria() { return endCriteria.getValue(); }
    public void setEndCriteria(EndCriteria endCriteria) { this.endCriteria.set(endCriteria); }
//    public Repeat withEndNever() { setEndCriteria(EndCriteria.NEVER); return this; }
    public Repeat withEndCriteria(EndCriteria endCriteria){ setEndCriteria(endCriteria); return this; }
    
    final private IntegerProperty endAfterEvents = new SimpleIntegerProperty();
    public Integer getEndAfterEvents() { return endAfterEvents.getValue(); }
    public IntegerProperty endAfterEventsProperty() { return endAfterEvents; }
    public void setEndAfterEvents(Integer endAfterEvents) { this.endAfterEvents.set(endAfterEvents); makeEndOnDateFromEndAfterEvents(); }
    public Repeat withEndAfterEvents(Integer endAfterEvents)
    {
        if (getEndCriteria() != EndCriteria.AFTER
                || getIntervalUnit() == null
                || getRepeatFrequency() == null)
        {
            throw new InvalidParameterException
            ("endAfterEvents must be set after EndCriteria is set to AFTER and intervalUnit and repeatFrequency are set");
        }
        setEndAfterEvents(endAfterEvents);
        return this;
    }
    /**
     * find end date from start date and number of events,  Value put into endOnDate.
     */
    public void makeEndOnDateFromEndAfterEvents()
    {
        int eventCounter = 0;
        LocalDate myDate = getStartLocalDate().minusDays(1);
        while (eventCounter < getEndAfterEvents())
        {
            myDate = myDate.with(new NextAppointment());
            eventCounter++;
        }
        setEndOnDate(myDate);
    }
    /**
     * Find number of events from end date.  Value put into endAfterEvents
     */
    public void makeEndAfterEventsFromEndOnDate()
    {
        if (getEndCriteria() != EndCriteria.AFTER) throw new InvalidParameterException
            ("Can't Calculate endAfterEvents with " + getEndCriteria() + " criteria");
        int eventCounter = 0;
        LocalDate myDate = getStartLocalDate().minusDays(1);
        while (myDate.isBefore(this.getEndOnDate()))
        {
            myDate = myDate.with(new NextAppointment());
            eventCounter++;
        }
        setEndAfterEvents(eventCounter);
    }
    
    final private ObjectProperty<LocalDate> endOnDate = new SimpleObjectProperty<LocalDate>();
    public ObjectProperty<LocalDate> endOnDateProperty() { return endOnDate; }
    public LocalDate getEndOnDate() { return endOnDate.getValue(); }
    public void setEndOnDate(LocalDate endOnDate) { this.endOnDate.set(endOnDate); }
    public Repeat withEndOnDate(LocalDate endOnDate)
    {
        if (getEndCriteria() != EndCriteria.ON) throw new InvalidParameterException("EndCriteria must be set to ON before endOnDate is set");
        setEndOnDate(endOnDate);
        return this;
    }
    
    private Set<LocalDate> deletedDates = new HashSet<LocalDate>();
    public Set<LocalDate> getDeletedDates() { return deletedDates; }
    public void setDeletedDates(Set<LocalDate> dates) { deletedDates = dates; }
    public Repeat withDeletedDates(Set<LocalDate> dates) { setDeletedDates(dates); return this; }
    
    /** Appointment-specific data */
//    private Repeatable appointmentData = AppointmentFactory.newAppointmentRepeatable();
//    public Repeatable getAppointmentData() { return appointmentData; }
//    public Repeat withAppointmentData(Repeatable appointment) { appointment.copyInto(appointment); return this; }

    private Appointment appointmentData = AppointmentFactory.newAppointment();
    public Appointment getAppointmentData() { return appointmentData; }
    public void setAppointmentData(Appointment appointment) { appointmentData = appointment; }
    public Repeat withAppointmentData(Appointment appointment) { setAppointmentData(appointment); return this; }
//    public void setAppointmentData(Appointment appointment) { appointment.copyNonDateFieldsInto(appointmentData); }

    /** Appointments generated from this repeat rule.  Objects are a subset of appointments in main appointments list
     * used in the Agenda calendar.  Names myAppointments to differentiate it from main name appointments */
    final private Set<Appointment> myAppointments = new HashSet<Appointment>();
//    private Set<Appointment> myAppointments;
    public Set<Appointment> getAppointments() { return myAppointments; }
    public Repeat withAppointments(Collection<Appointment> s) { getAppointments().addAll(s); return this; }
//    public Repeat withAppointments(Collection<Appointment> s) {myAppointments = new HashSet<Appointment>(s); return this; }
//    public boolean isNew() { return getAppointments().size() <= 1; }
    public boolean isNew() { 
        System.out.println("getAppointmentData().getStartLocalDateTime() == null " + (getAppointmentData().getStartLocalDateTime() == null));
        return getAppointmentData().getStartLocalDateTime() == null; }

//    @Override
//    public boolean equals(Object obj) {
//        if (obj == this) return true;
//        if((obj == null) || (obj.getClass() != getClass())) {
//            return false;
//        }
//        Appointment testObj = (Appointment) obj;
//
//        boolean descriptionEquals = (getDescription() == null)
//                ? (testObj.getDescription() == null) : getDescription().equals(testObj.getDescription());
//        boolean locationEquals = (getLocation() == null)
//                ? (testObj.getLocation() == null) : getLocation().equals(testObj.getLocation());
//        boolean summaryEquals = (getSummary() == null)
//                ? (testObj.getSummary() == null) : getSummary().equals(testObj.getSummary());
//        boolean repeatEquals = (getRepeat() == null)
//                ? (testObj.getRepeat() == null) : getRepeat().equals(testObj.getRepeat());
//        return descriptionEquals && locationEquals && summaryEquals && repeatEquals;
//    }

    // equals needs to be overridden by any class extending Repeat
    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if((obj == null) || (obj.getClass() != getClass())) {
            return false;
        }
        Repeat testObj = (Repeat) obj;

//        System.out.println(getEndAfterEvents() + " " + testObj.getEndAfterEvents());
//        System.out.println( getEndAfterEvents().equals(testObj.getEndAfterEvents())
//            + " " + (getEndCriteria() == testObj.getEndCriteria())
//            + " " + isRepeatDayOfMonth().equals(testObj.isRepeatDayOfMonth())
//            + " " + isRepeatDayOfWeek().equals(testObj.isRepeatDayOfWeek())
//            + " " + getRepeatFrequency().equals(testObj.getRepeatFrequency())
//            + " " + dayOfWeekMapEqual(testObj.getDayOfWeekMap()));
        
        return getEndAfterEvents().equals(testObj.getEndAfterEvents())
            && getEndCriteria() == testObj.getEndCriteria()
            && getIntervalUnit() == testObj.getIntervalUnit()
            && isRepeatDayOfMonth().equals(testObj.isRepeatDayOfMonth()) 
            && isRepeatDayOfWeek().equals(testObj.isRepeatDayOfWeek())
            && getRepeatFrequency().equals(testObj.getRepeatFrequency())
            && dayOfWeekMapEqual(testObj.getDayOfWeekMap());
//            && myEquals(getStartLocalDate(), testObj.getStartLocalDate())
//            && myEquals(getStartLocalTime(), (testObj.getStartLocalTime()));
    }
    
    /**
     * Determines if repeat rules make sense (true) or can't define a series (false)
     * Need to generate string of repeat rule
     * TODO - add more features
     * 
     * @return
     */
    public boolean isValid()
    {
        if (getIntervalUnit() == IntervalUnit.WEEKLY) {
            if (! isWeeklyValid()) return false;
        } else if (getIntervalUnit() == IntervalUnit.MONTHLY) {
            if (! isMonthlyValid()) return false;
        }
        return true;
    }
    

    /**
     * Default constructor
     */
    public Repeat() { }
    
    /**
     * Copy constructor that makes a new object with the parts from an Appointment copied
     * 
     * @param appointment
     * @return
     * @throws CloneNotSupportedException
     */
    public Repeat(Appointment appointment) {
        this.withStartLocalDate(appointment.getStartLocalDateTime().toLocalDate())
            .withStartLocalTime(appointment.getStartLocalDateTime().toLocalTime())
            .withEndLocalTime(appointment.getEndLocalDateTime().toLocalTime())
            .withAppointmentData(appointment)
            .withDayOfWeek(appointment.getStartLocalDateTime().toLocalDate().getDayOfWeek(), true);
    }
    
    /**
     * Copy constructor that makes a new object with the parts from an Appointment copied
     * 
     * @param appointment
     * @return
     * @throws CloneNotSupportedException
     */
    public Repeat(Repeat oldRepeat) {
        if (oldRepeat != null) {
            oldRepeat.copyInto(this);           
        }
    }
    
    /**
     * Default settings for a new Repeat rule, set after repeatable checkBox is checked.
     * 
     */
    public Repeat setDefaults() {
        setIntervalUnit(IntervalUnit.WEEKLY);
        setRepeatFrequency(1);
        setEndCriteria(EndCriteria.NEVER);
        return this;
    }

    /**
     * Creates a string describing the repeat rule.  The string gets displayed in the summary field
     * of the Repeat control
     * 
     * @return
     */
    public String makeSummary() {
        return null;
    }
    
    /**
     * Types of time intervals allowed for repeat rules
     * 
     * @author David Bal
     *
     */
    public enum IntervalUnit
    {
        DAILY(Period.ofDays(1)) // value is adjustment subtracted from start date to allow NextAppointment to find start date
      , WEEKLY(Period.ofDays(1)) // keep as Period.ofDays(1)
      , MONTHLY(Period.ofMonths(1))
      , YEARLY(Period.ofYears(1));
        
        private final Period value;
        
        private IntervalUnit(Period value) {
            this.value = value;
        }
        
        public Period getValue() {
            return value;
        }
        
        public String toStringSingular() {
            switch (this) {
            case DAILY:
                return Settings.REPEAT_INTERVALS_SINGULAR.get(DAILY);
            case WEEKLY:
                return Settings.REPEAT_INTERVALS_SINGULAR.get(WEEKLY);
            case MONTHLY:
                return Settings.REPEAT_INTERVALS_SINGULAR.get(MONTHLY);
            case YEARLY:
                return Settings.REPEAT_INTERVALS_SINGULAR.get(YEARLY);
            default:
                return null;                
            }
        }
        
        public String toStringPlural() {
            switch (this) {
            case DAILY:
                return Settings.REPEAT_INTERVALS_PLURAL.get(DAILY);
            case WEEKLY:
                return Settings.REPEAT_INTERVALS_PLURAL.get(WEEKLY);
            case MONTHLY:
                return Settings.REPEAT_INTERVALS_PLURAL.get(MONTHLY);
            case YEARLY:
                return Settings.REPEAT_INTERVALS_PLURAL.get(YEARLY);
            default:
                return null;                
            }
        }
        
        public static StringConverter<Repeat.IntervalUnit> stringConverter
            = new StringConverter<Repeat.IntervalUnit>() {
            @Override public String toString(Repeat.IntervalUnit object) {
                switch (object) {
                case DAILY:
                    return Settings.REPEAT_INTERVALS.get(IntervalUnit.DAILY);
                case WEEKLY:
                    return Settings.REPEAT_INTERVALS.get(IntervalUnit.WEEKLY);
                case MONTHLY:
                    return Settings.REPEAT_INTERVALS.get(IntervalUnit.MONTHLY);
                case YEARLY:
                    return Settings.REPEAT_INTERVALS.get(IntervalUnit.YEARLY);
                default:
                    return null;                
                }
            }
            @Override public Repeat.IntervalUnit fromString(String string) {
                throw new RuntimeException("not required for non editable ComboBox");
            }
        };
    }
    
    /**
     * Checks if the WEEKLY IntervalUnit is valid (has at least one day selected)
     * 
     * @return
     */
    public boolean isWeeklyValid()
    {
        boolean weekly = (getIntervalUnit() == IntervalUnit.WEEKLY);
        boolean anyDay = getDayOfWeekMap().entrySet()
                                               .stream()
                                               .anyMatch(a -> a.getValue().get() == true);
        return weekly && anyDay;
    }
    /**
     * Checks if the MONTHLY IntervalUnit is valid (has one of the options for selecting next
     * month's day selected)
     * 
     * @return
     */
    public boolean isMonthlyValid()
    {
        boolean monthly = (getIntervalUnit() == IntervalUnit.MONTHLY);
        boolean dayOptionSelected = (isRepeatDayOfMonth() || isRepeatDayOfWeek());
        return monthly && dayOptionSelected;
    }
    
    public enum EndCriteria {
        NEVER
      , AFTER
      , ON;
    }
    
    public enum MonthlyRepeat {
        DAY_OF_MONTH, DAY_OF_WEEK;
    }



    /**
     * Adds appointments as members of this repeat rule to myAppointments collection
     * 
     * @param appointments
     */
    public void collectAppointments(Collection<Appointment> appointments) {
        Set<Appointment> s = appointments.stream()
                                         .filter(a -> a.hasRepeat())
                                         .filter(a -> a.getRepeat().equals(this))
                                         .collect(Collectors.toSet());
        getAppointments().addAll(s);
    }
    
    /**
     * Make initial Appointments for Repeat object for one week before and one week after.  Calls to this method needs
     * to be replaced by ones to the one with startDate and endDate.
     * 
     * @param appointments
     * @return
     */
    @Deprecated
    public Repeat makeAppointments(Collection<Appointment> appointments)
    {
        if (startDate == null) startDate = LocalDate.now().minusWeeks(1);
        if (endDate == null) endDate = LocalDate.now().plusWeeks(1);
        return makeAppointments(appointments, startDate, endDate);
    }

    /**
     * Make appointments that should exist between startDate and endDate based on Repeat rules.
     * Adds those appointments to the input parameter appointments Collection.
     * Doesn't make Appointment for dates that are already represented as individual appointments
     * as specified in usedDates.
     * 
     * @param appointments
     * @param startDate
     * @param endDate
     * @return
     */
    public Repeat makeAppointments(Collection<Appointment> appointments, LocalDate startDate, LocalDate endDate)
    {
        final LocalDate myEndDate;
        if (getEndOnDate() == null) {
            myEndDate = endDate;
        } else {
            myEndDate = (endDate.isBefore(getEndOnDate())) ? endDate : getEndOnDate();
        }
        this.endDate = endDate;

        LocalDate myStartDate = nextValidDateSlow(startDate.minusDays(1));
//        System.out.println("myStartDate " + myStartDate);
        this.startDate = startDate;
        if (! myStartDate.isAfter(myEndDate))
        { // create set of appointment dates already used, to be skipped in making more
            final Set<LocalDate> usedDates = getAppointments()
                    .stream()
                    .map(a -> a.getStartLocalDateTime().toLocalDate())
                    .collect(Collectors.toSet());
            
            final Iterator<Appointment> i = Stream                              // appointment iterator
                    .iterate(myStartDate, (a) -> a.with(new NextAppointment())) // infinite stream of valid dates
                    .filter(a -> ! usedDates.contains(a))                       // filter out dates already used
                    .filter(a -> ! getDeletedDates().contains(a))               // filter out deleted dates
                    .map(a -> {                                                 // make new appointment
                        LocalDateTime myStartDateTime = a.atTime(getStartLocalTime());
                        LocalDateTime myEndDateTime = a.atTime(getEndLocalTime());
                        Appointment appt = AppointmentFactory
                                .newAppointment()
                                .withStartLocalDateTime(myStartDateTime)
                                .withEndLocalDateTime(myEndDateTime)
                                .withRepeat(this)
                                .withRepeatMade(true)
                                .withAppointmentGroup(getAppointmentData().getAppointmentGroup())
                                .withDescription(getAppointmentData().getDescription())
                                .withSummary(getAppointmentData().getSummary());
                        return appt;
                    })
                    .iterator();                                                // make iterator
            
            while (i.hasNext())
            { // Process new appointments
                final Appointment a = i.next();
                if (a.getStartLocalDateTime().toLocalDate().isAfter(myEndDate)) break; // exit loop when at end
//                System.out.println("add " + a.getStartLocalDateTime());
                appointments.add(a);                                                   // add appointments to main collection
                getAppointments().add(a);                                              // add appointments to this repeat's collection
            }
        }
        
        return this;
    }
    
    /**
     * Removes appointments that were made by this repeat rule and are now outside the startDate and endDate
     * values (startDate and endDate are private and set by calls to makeAppointments).  Removes appointments
     * from both the input parameter appointments and this repeat object's appointments collection as well.
     * 
     * @param appointments
     * @return
     */
    public Repeat removeOutsideRangeAppointments(Collection<Appointment> appointments)
    {
        Iterator<Appointment> i = getAppointments().stream()
            .filter(a -> {
                boolean tooEarly = a.getStartLocalDateTime().toLocalDate().isBefore(startDate);
                boolean tooLate = a.getStartLocalDateTime().toLocalDate().isAfter(endDate);
                return tooEarly || tooLate;
            })
            .collect(Collectors.toList())
            .iterator();
        
        while (i.hasNext())
        {
            final Appointment a = i.next();
            RepeatableUtilities.removeOne(appointments, a);
            RepeatableUtilities.removeOne(getAppointments(), a);
        }
        return this;
    }

    
//    /**
//     * Copies appointment data from this objects appointmentData field into the appointmentData
//     * argument unless the data in appointmentData is unique
//     * 
//     * @param appointmentData
//     * @param appointmentOld
//     * @return
//     */
//    public Appointment copyAppointmentInto(Appointment appointmentData, Appointment appointmentOld) {
//    if (appointmentData.getAppointmentGroup().equals(appointmentOld.getAppointmentGroup())) {
//        appointmentData.setAppointmentGroup(getAppointmentData().getAppointmentGroup());            
//    }
//    if (appointmentData.getDescription().equals(appointmentOld.getDescription())) {
//        appointmentData.setDescription(getAppointmentData().getDescription());            
//    }
////  if (appointmentData.getLocationKey().equals(appointmentOld.getLocationKey())) {
////      appointmentData.setLocationKey(getLocationKey());
////  }
////  if (appointmentData.getStaffKeys().equals(appointmentOld.getStaffKeys())) {
////      appointmentData.getStaffKeys().addAll(getStaffKeys());
////  }
////  if (appointmentData.getStyleKey().equals(appointmentOld.getStyleKey())) {
////      appointmentData.setStyleKey(getStyleKey());
////  }
//    if (appointmentData.getSummary().equals(appointmentOld.getSummary())) {
//        appointmentData.setSummary(getAppointmentData().getSummary());
//    }
//    return appointmentData;
//}
    
    /**
     * Modifies old dates and times by a start and end TemporalAdjuster in an attempt to convert invalid
     * dates/times to valid ones.
     * Updates repeat-rule appointments with new repeat rule from startDate on.
     * Deletes repeat-rule generated appointments that don't meet the current repeat rule.
     * Changes the attached Repeat for non-repeat generated appointments that are now invalid to null
     * (prevents them from being deleted)
     * Adds new repeat-rule appointments as needed
     * @param appointments 
     * @param appointmentOld 
     * 
     * @param appointment: already modified appointment
     * @param startTemporalAdjuster: adjusts startLocalDateTime
     * @param endTemporalAdjuster: adjusts endLocalDateTime
     * @return
     */
    protected void updateAppointments(Collection<Appointment> appointments
            , Appointment appointment
            , Appointment appointmentOld
            , TemporalAdjuster startTemporalAdjuster
            , TemporalAdjuster endTemporalAdjuster)
    {
        // Modify old date time to new, so I can keep as many modified appointments as possible
        getAppointments()
                .stream()
                .filter(a -> a != appointment) // filter already changed appointment
                .sequential()
                .forEach(a -> {                       // adjust date and time
                    LocalDateTime newStart = a.getStartLocalDateTime().with(startTemporalAdjuster);
                    LocalDateTime newEnd = a.getEndLocalDateTime().with(endTemporalAdjuster);
                    a.setStartLocalDateTime(newStart);
                    a.setEndLocalDateTime(newEnd);
                    if (a.isRepeatMade())
                    { // copy all changed data
                        getAppointmentData().copyNonDateFieldsInto(a);
                    } else { // copy only non-unique data
                        getAppointmentData().copyNonDateFieldsInto(a, appointmentOld);
                    }
                });
        updateAppointments(appointments, appointment);
    }
    
    /**
     * Updates repeat-rule appointments with new repeat rule from startDate on.
     * Deletes repeat-rule generated appointments that don't meet the current repeat rule.
     * Changes the attached Repeat for non-repeat generated appointments that are now invalid to null (prevents them
     * from being deleted)
     * Adds new repeat-rule appointments as needed
     * @param appointments 
     * 
     * @param appointment: already modified appointment
     * @param startTemporalAdjuster: adjusts startLocalDateTime
     * @param endTemporalAdjuster: adjusts endLocalDateTime
     * @return
     */
    public void updateAppointments(Collection<Appointment> appointments, Appointment appointment)
    {
        final LocalDateTime firstDateTime = getStartLocalDate().atTime(getStartLocalTime());
        final Iterator<LocalDateTime> validDateTimeIterator = Stream                      // iterator
                .iterate(firstDateTime, (d) -> { return d.with(new NextAppointment()); }) // generate infinite stream of valid dates
                .iterator();                                                              // make iterator
        final Iterator<Appointment> appointmentIterator = getAppointments()
                .stream() // appointments sorted by date
                .sorted(Comparator.comparing(a -> a.getStartLocalDateTime()))
                .iterator();
        Set<Appointment> invalidAppointments = new HashSet<Appointment>();
        LocalDateTime validDateTime = validDateTimeIterator.next();
        while (appointmentIterator.hasNext())
        {
            Appointment myAppointment = appointmentIterator.next();
            LocalDateTime appointmentDateTime = myAppointment.getStartLocalDateTime();
            while (validDateTime.isBefore(appointmentDateTime))
            { // advance valid dates to get to myDateTime
                validDateTime = validDateTimeIterator.next();
                if (getEndCriteria() != EndCriteria.NEVER)
                {
                    if (validDateTime.isAfter(getEndOnDate().atTime(getStartLocalTime())))
                    { // appointment is invalid - too late
                        invalidAppointments.add(myAppointment);
                        break;
                    }
                }
            }
            if (! validDateTime.equals(appointmentDateTime))
            { // appointment is invalid - start time doesn't match
                invalidAppointments.add(myAppointment);
            }
        }
        
        // Change unique appointment to individual
        boolean writeAppointmentsNeeded = getAppointments()
                .stream()
                .filter(a -> ! isValidAppointment(a.getStartLocalDateTime())) // invalid date time
                .filter(a -> ! a.isRepeatMade())                              // is not repeat made
                .peek(a -> a.setRepeat(null))                                 // reset Repeat to null (make moved appointments individual when date is now invalid due to repeat change)
                .anyMatch(a -> true);                                         // if any appointments past filters set flag to write appointments

        // Check for any appointments that have the Repeat, but are not repeat made.
        boolean writeAppointmentsNeeded2 = getAppointments()
                .stream()
                .anyMatch(a -> (a.hasRepeat()) && (! a.isRepeatMade()));

        for (Appointment a : invalidAppointments)
        {
//            if (a.getStudentKeys().isEmpty())
//            { // DELETE EXISTING INVALID APPOINTMENT
            System.out.println("delete " + a.getStartLocalDateTime());
                appointments.remove(a);
                getAppointments().remove(a);
//            } else { // LEAVE EXISTING APPOINTMENT BECAUSE HAS ATTENDANCE
//                getAppointmentData().copyInto(a);
//            }
        }
        makeAppointments(appointments); // add any new appointments needed
        
        if (writeAppointmentsNeeded || writeAppointmentsNeeded2) AppointmentFactory.writeToFile(appointments);
    }

    /**
     * Checks if repeat contains only one appointment and converts the appointment to an individual appointment.
     * 
     * @param repeats
     * @param appointments 
     * @return
     */
    public boolean oneAppointmentToIndividual(Collection<Repeat> repeats, Collection<Appointment> appointments)
    {
        if (getEndCriteria() != EndCriteria.NEVER)
        { // Count number of valid appointment start dates, stop when after end date or more than one appointment date
            final Iterator<LocalDate> i = Stream                                            // appointment iterator
                    .iterate(getStartLocalDate(), (a) -> { return a.with(new NextAppointment()); }) // infinite stream of valid dates
                    .filter(a -> ! getDeletedDates().contains(a))                             // filter out deleted dates
                    .iterator();                                                            // make iterator
            int appointmentCounter = 0;
            while (i.hasNext())
            { // find date
                final LocalDate s = i.next();
                if (s.isAfter(getEndOnDate())) break; // exit loop when beyond date without match
                if (appointmentCounter > 1) break;
                appointmentCounter++;
            }
            
            if (appointmentCounter == 1)
            {
                RepeatableUtilities.removeOne(repeats, this);
                if (getAppointments().size() == 1)
                {
                    Appointment myAppointment = getAppointments().iterator().next();
                    myAppointment.setRepeatMade(false);
                    myAppointment.setRepeat(null);
                    getAppointments().clear();
                } else if (getAppointments().size() == 0)
                { // make individual appointment because it is not in current date range
                    this.makeAppointments(appointments, getStartLocalDate(), getEndOnDate());
                    getAppointments().iterator().next().setRepeatMade(false);
                }
                return true;
            }
        }
        return false;
    }
    
    /**
     * Returns a stream of valid start dates.  Ends when endOnDate is exceeded
     * I wonder if it could be done more efficiently without the iterator.
     * 
     * @return
     */
    public Stream<LocalDate> validDateStreamWithEnd()
    {
        List<LocalDate> startDateList = new ArrayList<LocalDate>();
        final Iterator<LocalDate> i = Stream                                            // appointment iterator
                .iterate(getStartLocalDate(), (a) -> { return a.with(new NextAppointment()); }) // infinite stream of valid dates
                .filter(a -> ! getDeletedDates().contains(a))                             // filter out deleted dates
                .iterator();                                                            // make iterator
        while (i.hasNext())
        { // find date
            final LocalDate s = i.next();
            if (s.isAfter(getEndOnDate())) break; // exit loop when beyond date without match
            startDateList.add(s);
        }
        return startDateList.stream();
    }

    /**
     * Returns a stream of valid start dates.
     * 
     * @return
     */
    public Stream<LocalDate> validDateStreamEndless()
    {
        return Stream
            .iterate(getStartLocalDate(), (a) -> { return a.with(new NextAppointment()); }) // infinite stream of valid dates
            .filter(a -> ! getDeletedDates().contains(a));                             // filter out deleted dates
    }
    
    /**
     * Checks appointment LocalDateTime to see if it follows the repeat rules.
     * true = valid date, false = invalid
     * 
     * @param initialDateTime
     * @return
     */
    private boolean isValidAppointment(LocalDateTime startDateTime, LocalDateTime endDateTime)
    {
        LocalDate firstMatchDateTime = getStartLocalDate();
            final Iterator<LocalDate> i = Stream                                            // appointment iterator
                    .iterate(firstMatchDateTime, (a) -> { return a.with(new NextAppointment()); }) // infinite stream of valid dates
    //                .filter(a -> ! getDeletedDates().contains(a))                             // filter out deleted dates
                    .iterator();                                                            // make iterator
    
            while (i.hasNext())
            { // Check date
                final LocalDateTime s = i.next().atTime(getStartLocalTime());
                final LocalDateTime e = i.next().atTime(getEndLocalTime());
                if (s.isAfter(startDateTime)) return false; // exit loop when beyond date without match
                if (s.equals(startDateTime) && ((endDateTime == null) || e.equals(endDateTime))) return true;
            }
        return false;
    }
    private boolean isValidAppointment(LocalDateTime initialDateTime)
    {
        return isValidAppointment(initialDateTime, null);
    }
    private boolean isValidAppointment(LocalDate initialDate)
    {
        return isValidAppointment(initialDate.atTime(getStartLocalTime()), null);
    }
    private boolean isValidAppointment(Appointment myAppointment)
    {
        boolean tooLate = (getEndCriteria() == EndCriteria.NEVER) ? false :
            myAppointment.getStartLocalDateTime().isAfter(getEndOnDate().atTime(getStartLocalTime()));
        if (tooLate) {
            return false;
        } else {
            return isValidAppointment(myAppointment.getStartLocalDateTime(), myAppointment.getEndLocalDateTime());
        }
    }
    
    public LocalDate nextValidDate(LocalDate inputDate)
    {
        return nextValidDateSlow(inputDate); // TODO - replace with nextValidDateFast when finished
    }

    /**
     * Faster version without iterating
     * DOES NOT WORK PROPERLY - MONTHLY DAY OF WEEK IS BROKEN (use slow one for now)
     * 
     * @param inputDate
     * @return
     */
    @Deprecated
    public LocalDate nextValidDateFast(LocalDate inputDate)
    {
        LocalDate firstMatchDate = null;
        firstMatchDate = inputDate;//.minusDays(getIntervalUnit().getValue() * this.getRepeatFrequency());
        switch (getIntervalUnit())
        {
            case DAILY:
                break;
            case WEEKLY:
                firstMatchDate = inputDate.minusDays(7 * this.getRepeatFrequency());
                break;
            case MONTHLY:
                long mod = 0;
                long totalMonths = 0;
            switch (this.getMonthlyRepeat())
                {
                case DAY_OF_MONTH:
                    totalMonths = (inputDate.getMonthValue() - getStartLocalDate().getMonthValue())
                        + (inputDate.getYear() - getStartLocalDate().getYear()) * 12;
                    mod = (totalMonths % getRepeatFrequency());
                    if (mod == 0 && inputDate.getDayOfMonth() <= getStartLocalDate().getDayOfMonth()) mod = getRepeatFrequency(); // adjust mod to repeatFrequency if date is before match for current month
                    int dayOfMonth = getStartLocalDate().getDayOfMonth();
                    firstMatchDate = firstMatchDate
                            .minusMonths(mod)
                            .withDayOfMonth(dayOfMonth)
                            .with(new NextAppointment());
                    break;
                case DAY_OF_WEEK: // TODO - PAIN IN THE ASS
                    // get date of ordinal match for current month
//                    totalMonths = (inputDate.getMonthValue() - getStartLocalDate().getMonthValue())
//                    + (inputDate.getYear() - getStartLocalDate().getYear()) * 12;
//                    totalMonths = ChronoUnit.MONTHS.between
//                        (getStartLocalDate().with(TemporalAdjusters.firstDayOfMonth())
//                        , inputDate.with(TemporalAdjusters.firstDayOfMonth()));
//                    totalMonths = ChronoUnit.MONTHS.between
//                            (getStartLocalDate()
//                            , inputDate);
                    totalMonths = ChronoUnit.MONTHS.between
                    (getStartLocalDate() //.withDayOfMonth(d)
                    , inputDate);
                    
                    mod = (totalMonths % getRepeatFrequency());

                    LocalDate myDate = inputDate
                            .minusMonths(mod)
                            .with(TemporalAdjusters.firstDayOfMonth());
                    final DayOfWeek dayOfWeek = getStartLocalDate().getDayOfWeek();
                    int o = (myDate.getDayOfWeek() == dayOfWeek) ? 1 : 0;
                    for (; o < ordinal; o++) {
                        myDate = myDate.with(TemporalAdjusters.next(dayOfWeek));
                    }
                    int d = myDate.getDayOfMonth();
                    boolean pastOrdinal = (inputDate.getDayOfMonth() >= d);


                    totalMonths = ChronoUnit.MONTHS.between
                            (getStartLocalDate() //.withDayOfMonth(d)
                            , inputDate);

                    mod = (totalMonths % getRepeatFrequency());

                    if (pastOrdinal) {
//                        totalMonths++;
                        mod++;
                        firstMatchDate = firstMatchDate
                                .minusMonths(mod)
//                                .with(TemporalAdjusters.firstDayOfMonth())
                                .with(new NextAppointment());
                    } else {
                        firstMatchDate = firstMatchDate
                                .minusMonths(mod)
                                .with(new NextAppointment());
                    }
                    
                    
//                    firstMatchDate = firstMatchDate
//                        .minusMonths(mod)
////                        .with(TemporalAdjusters.firstDayOfMonth())
//                        .with(new NextAppointment());
                    
                    System.out.println(inputDate + " totalMonths " + totalMonths + " mod " + mod +
                            " firstMatchDate " + firstMatchDate + " actual " + nextValidDateSlow(inputDate)
                            + " " + pastOrdinal + " " + myDate);
//                    firstMatchDate = firstMatchDate
                    break;
                default:
                    break;
                }
//            System.out.println(inputDate + " mod " + mod + " " + firstMatchDate.minusMonths(mod) + " totalMonths " + totalMonths + " " );
//            System.out.println("getStartLocalDate(), inputDate  " + getStartLocalDate() + " " + inputDate);
//            System.out.println("getStartLocalDate(), inputDateA " + getStartLocalDate().with(TemporalAdjusters.firstDayOfMonth()) + " " + inputDate.with(TemporalAdjusters.firstDayOfMonth()));

                break;
            case YEARLY:
                break;
            default:
                throw new InvalidParameterException("Unknown intervalUnit " + getIntervalUnit());
        }
//        firstMatchDate = getStartLocalDate();
        final Iterator<LocalDate> i = Stream                                            // appointment iterator
                .iterate(firstMatchDate, (a) -> { return a.with(new NextAppointment()); }) // infinite stream of valid dates
                .filter(a -> ! getDeletedDates().contains(a))                             // filter out deleted dates
//                .limit(3)
                .iterator();                                                            // make iterator

        while (i.hasNext())
        { // find date
            final LocalDate s = i.next();
//            System.out.println(s + " " + inputDate + " " + i.next());
            if (! s.isBefore(inputDate)) return s; // exit loop when beyond date without match
//            break;
        }
        return null; // should never get here
    }

    /**
     * Returns next valid date starting with inputed date.  If inputed date is valid it is returned.
     * Iterates from first date until it passes the inputDate.  This make take a long time if the date
     * is far in the future.
     * 
     * @param inputDate
     * @return
     */
    public LocalDate nextValidDateSlow(LocalDate inputDate)
    {
        if (inputDate.isBefore(getStartLocalDate())) return getStartLocalDate();
        LocalDate firstMatchDate = getStartLocalDate();
        final Iterator<LocalDate> i = Stream                                            // appointment iterator
                .iterate(firstMatchDate, (a) -> { return a.with(new NextAppointment()); }) // infinite stream of valid dates
                .filter(a -> ! getDeletedDates().contains(a))                             // filter out deleted dates
                .iterator();                                                            // make iterator
        while (i.hasNext())
        { // find date
            LocalDate s = i.next();
            if (s.isAfter(inputDate)) return s; // exit loop when beyond date without match
        }
        throw new InvalidParameterException("Can't find valid date starting at " + inputDate);
    }

    /**
     * Removes the deleted dates and appointments outside the start and end dates
     * @param appointments 
     */
    public void fixCollectionDates(Collection<Appointment> appointments)
    {
        // keep only deletedDates within start and end dates
        final Set<LocalDate> dates = getDeletedDates()
                .stream()
                .filter(a -> ! a.isAfter(getEndOnDate()))        // keep dates before and equal to endOnDate
                .filter(a -> ! a.isBefore(getStartLocalDate()))  // keep dates after and equal to startLocalDate
                .filter(a -> isValidAppointment(a))      // keep valid dates
                .collect(Collectors.toSet());                    // make new set
        setDeletedDates(dates);                                  // keep new set
          
          // keep only appointments within start and end dates
          Iterator<Appointment> i = getAppointments().iterator();
          while (i.hasNext())
          {
              Appointment a = i.next();
              LocalDate s = a.getStartLocalDateTime().toLocalDate();
              boolean tooEarly = s.isBefore(getStartLocalDate());
              boolean tooLate = (getEndCriteria() == EndCriteria.NEVER) ? false : s.isAfter(getEndOnDate());
              boolean notValid = ! isValidAppointment(a.getStartLocalDateTime());
              if (tooEarly || tooLate || notValid) i.remove();
          }
  
          getAppointments().stream()
                           .forEach(a -> a.setRepeat(this));
    }
    

    /**
     * Copy's current object's fields into passed parameter
     * 
     * @param repeat
     * @return
     * @throws CloneNotSupportedException
     */
    public Repeat copyInto(Repeat repeat) {
        repeat.setIntervalUnit(getIntervalUnit());
        repeat.setRepeatDayOfMonth(isRepeatDayOfMonth());
        repeat.setRepeatDayOfWeek(isRepeatDayOfWeek());
        repeat.setRepeatFrequency(getRepeatFrequency());
        getDayOfWeekMap().entrySet()
                              .stream()
                              .forEach(a -> {
                                  DayOfWeek d = a.getKey();
                                  boolean value = a.getValue().get();
                                  repeat.setDayOfWeek(d, value);   
                              });
        repeat.setDeletedDates(getDeletedDates());
        repeat.setStartLocalDate(getStartLocalDate());
        repeat.setStartLocalTime(getStartLocalTime());
        repeat.setEndLocalTime(getEndLocalTime());
        repeat.setEndAfterEvents(getEndAfterEvents());
        repeat.setEndCriteria(getEndCriteria());
        repeat.setEndOnDate(getEndOnDate());
        getAppointmentData().copyNonDateFieldsInto(repeat.getAppointmentData());
        getAppointments().stream()
                         .forEach(a -> repeat.getAppointments().add(a));
        return repeat;
    }
    
    Appointment copyInto(Appointment a) {
        getAppointmentData().copyNonDateFieldsInto(a);
        LocalDate myDate = a.getStartLocalDateTime().toLocalDate();
        a.setStartLocalDateTime(myDate.atTime(this.getStartLocalTime()));
        a.setEndLocalDateTime(myDate.atTime(this.getEndLocalTime()));
        return a;
    }
    
    /**
     * Adjust start date time and end time due to editing repeatable appointment
     * 
     * @param adjustStartDate: when true shift startDate by startTemporalAdjuster
     * @param startTemporalAdjuster
     * @param endTemporalAdjuster
     */
    public void adjustDateTime(boolean adjustStartDate, TemporalAdjuster startTemporalAdjuster, TemporalAdjuster endTemporalAdjuster)
    {
        // time adjustments
        final LocalDateTime newStartLocalDateTime = getStartLocalDate()
                .atTime(getStartLocalTime())
                .with(startTemporalAdjuster);
        final LocalTime newEndLocalTime = getStartLocalDate()
                .atTime(getEndLocalTime())
                .with(endTemporalAdjuster)
                .toLocalTime();
        setStartLocalTime(newStartLocalDateTime.toLocalTime());
        setEndLocalTime(newEndLocalTime);

        if (adjustStartDate)
        {
            if (getIntervalUnit() == IntervalUnit.WEEKLY)
            { // if new start has shifted then move it
                final LocalDate earliestDate = newStartLocalDateTime.toLocalDate();
                final DayOfWeek d1 = earliestDate.getDayOfWeek();
                final Iterator<DayOfWeek> daysIterator = Stream
                    .iterate(d1, (a) ->  a.plus(1))              // infinite stream of days of the week
                    .limit(7)                                    // next valid day should be found within 7 days
                    .iterator();
                int dayShift = 0;
                while (daysIterator.hasNext())
                {
                    DayOfWeek d = daysIterator.next();
                    if (getDayOfWeek(d)) break;
                    dayShift++;
                }
                setStartLocalDate(earliestDate.plusDays(dayShift));
            } else { // edit startDate for all other IntervalUnit types
                setStartLocalDate(newStartLocalDateTime.toLocalDate());            
            }   
        }
    }
    /**
     * Adjust start date, time and end time due to editing repeatable appointment
     * 
     * @param startTemporalAdjuster
     * @param endTemporalAdjuster
     */
    public void adjustDateTime(TemporalAdjuster startTemporalAdjuster, TemporalAdjuster endTemporalAdjuster)
    {
        adjustDateTime(true, startTemporalAdjuster, endTemporalAdjuster);
    }
    
//    /**
//     * Returns new Repeat object with all fields copied from input parameter myRepeat
//     * 
//     * @param myRepeat
//     * @return
//     * @throws CloneNotSupportedException
//     */
//    public static Repeat copy(Repeat myRepeat)
//    {
////        if(!(myRepeat instanceof Cloneable)) throw new CloneNotSupportedException("Invalid cloning");
//        Repeat copyRepeat = new Repeat(myRepeat);
////        myRepeat.copyInto(copyRepeat);
//        myRepeat.getAppointmentData().copyInto(copyRepeat.getAppointmentData());
//        return copyRepeat;
//    }


//
//    private boolean myEquals(Object o1, Object o2)
//    {
//        if ((o1 == null) && (o2 == null)) return true; // both null
//        if (o1 == null || o2 == null) return false; // one null
//        return o1.equals(o2);
//    }


    /**
     * removes bindings on all properties, including the embedded appointment object
     */
    public void unbindAll() {
        this.endAfterEventsProperty().unbind();
        this.endCriteriaProperty().unbind();
        this.endLocalTimeProperty().unbind();
        this.endOnDateProperty().unbind();
        this.intervalUnitProperty().unbind();
        this.repeatDayOfMonthProperty().unbind();
        this.repeatDayOfWeekProperty().unbind();
        this.repeatFrequencyProperty().unbind();
        this.getDayOfWeekMap().entrySet()
                                   .stream()
                                   .forEach(a -> a.getValue().unbind());
        this.startLocalDateProperty().unbind();
        this.startLocalTimeProperty().unbind();
//        this.getAppointmentData().unbindAll(); // I'll probably have to use listeners instead of bindings.  I need some way to remove listeners
    }
    
    /**
     * Adjust date to become next date based on the Repeat rule.  Needs a input temporal on a valid date.
     * 
     * @return
     */
    private class NextAppointment implements TemporalAdjuster
    {
        @Override
        public Temporal adjustInto(Temporal temporal)
        {
            final int maxI = getRepeatFrequency();
            final TemporalField weekOfYear;
            final int initialWeek;
            int currentWeek = 0;
            if (getIntervalUnit() == IntervalUnit.WEEKLY)
            {
                weekOfYear = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();
                initialWeek = LocalDate.from(temporal).get(weekOfYear);
                currentWeek = initialWeek;
            } else { // variables not used in not a WEEKLY repeat, but still must be initialized
                weekOfYear = null;
                initialWeek = -1;
            }
            int i=0;
            do
            { // loop that counts number of valid dates for total time interval (repeatFrequency)
                temporal = temporal.with(new TemporalAdjuster()
                { // anonymous inner class that finds next valid date
                    @Override
                    public Temporal adjustInto(Temporal temporal)
                    {
                        LocalDate inputDate = LocalDate.from(temporal);
                        switch (getIntervalUnit())
                        {
                        case DAILY:
                            return temporal.plus(Period.ofDays(1));
                        case WEEKLY:
                            final DayOfWeek d1 = inputDate.plusDays(1).getDayOfWeek();
//                            System.out.println("d1 " + d1);
                            final Iterator<DayOfWeek> daysIterator = Stream
                                .iterate(d1, (a) ->  a.plus(1))              // infinite stream of valid days of the week
                                .limit(7)                                    // next valid day should be found within 7 days
                                .iterator();
                            while (daysIterator.hasNext()) {
                                DayOfWeek d = daysIterator.next();
//                                System.out.println(d);
                                if (getDayOfWeek(d)) return temporal.with(TemporalAdjusters.next(d));
                            }
                            return temporal; // only happens if no day of the week are selected (true)
                            case MONTHLY:
                                switch (getMonthlyRepeat())
                                {
                                case DAY_OF_MONTH:
//                                    System.out.println(temporal.plus(Period.ofMonths(1)));
                                    return temporal.plus(Period.ofMonths(1));
                                case DAY_OF_WEEK:
                                    DayOfWeek dayOfWeek = getStartLocalDate().getDayOfWeek();
//                                    System.out.println("ordinal " + ordinal);
                                    return temporal.plus(Period.ofMonths(1))
                                            .with(TemporalAdjusters.dayOfWeekInMonth(ordinal, dayOfWeek));
                                }
                        case YEARLY:
                            return temporal.plus(Period.ofYears(1));
                        default:
//                            Main.log.log(Level.SEVERE, "Unknown intervalUnit " + getIntervalUnit());
                            return temporal;
                        }
                    }
                }); // end of anonymous inner TemporalAdjuster class

                // Increment repeat frequency counter
                if (getIntervalUnit() == IntervalUnit.WEEKLY)
                { // increment counter for weekly repeat when week number changes
                    int newWeekNumber = LocalDate.from(temporal).get(weekOfYear);
                    if (newWeekNumber == initialWeek) return temporal; // return new temporal if still in current week (assumes temporal starts on valid date)
                    if (newWeekNumber != currentWeek)
                    {
                        currentWeek = newWeekNumber;
                        i++;
                    }
                } else
                { // all other IntervalUnit types (not WEEKLY) increment counter i for every cycle of anonymous inner class TemporalAdjuster
                    i++;
                }
            } while (i < maxI); // end of while looping anonymous inner class
        return temporal;
        }
    }
    
}
