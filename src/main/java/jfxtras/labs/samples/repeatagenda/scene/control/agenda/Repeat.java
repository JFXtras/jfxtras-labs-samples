package jfxtras.labs.samples.repeatagenda.scene.control.agenda;

import java.io.IOException;
import java.nio.file.Path;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.util.StringConverter;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.Agenda.Appointment;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.Agenda.AppointmentGroup;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.Agenda.Repeatable;

/**
 * Contains rules for repeatable appointments in the calendar
 *  
 * @author David Bal
 */
public class Repeat implements Cloneable {
    
    private static int nextKey = 0;
    public boolean isEmpty() { return intervalUnit.getValue() == null; }

    public static Period repeatPeriod = Period.ofWeeks(1);
    
    // Dates for which appointments are to be generated.  Should match the dates displayed on the calendar.
    private LocalDate startDate;
    private LocalDate endDate;
    
    /** Unique number identifying this Repeat object. */
    private Integer key;
    public Integer getKey() { return key; }
    protected void setKey(Integer value) { key = value; } 
    public Repeat withKey(Integer value) { setKey(value); return this; }
    public boolean hasKey() { return (getKey() != null); } // new Repeat has no key
    
    final private ObjectProperty<IntervalUnit> intervalUnit = new SimpleObjectProperty<IntervalUnit>();
    public ObjectProperty<IntervalUnit> intervalUnitProperty() { return intervalUnit; }
    public IntervalUnit getIntervalUnit() { return intervalUnit.getValue(); }
    public void setIntervalUnit(IntervalUnit intervalUnit) { this.intervalUnit.set(intervalUnit); }
    public Repeat withIntervalUnit(IntervalUnit intervalUnit) { setIntervalUnit(intervalUnit); return this; }
    
    final private IntegerProperty repeatFrequency = new SimpleIntegerProperty();
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
    public Boolean isRepeatDayOfMonth() { return repeatDayOfMonth.getValue(); }
    public BooleanProperty repeatDayOfMonthProperty() { return repeatDayOfMonth; }
    public void setRepeatDayOfMonth(Boolean repeatDayOfMonth) { this.repeatDayOfMonth.set(repeatDayOfMonth); }
    public Repeat withRepeatDayOfMonth(Boolean repeatDayOfMonth) { setRepeatDayOfMonth(repeatDayOfMonth); return this; }

    final private BooleanProperty repeatDayOfWeek = new SimpleBooleanProperty(false);
    public Boolean isRepeatDayOfWeek() { return repeatDayOfWeek.getValue(); }
    public BooleanProperty repeatDayOfWeekProperty() { return repeatDayOfWeek; }
    public void setRepeatDayOfWeek(Boolean repeatDayOfWeek) { this.repeatDayOfWeek.set(repeatDayOfWeek); }
    public Repeat withRepeatDayOfWeek(Boolean repeatDayOfWeek) { setRepeatDayOfWeek(repeatDayOfWeek); return this; }

    private int ordinal; // used when repeatDayOfWeek is true, this is the number of weeks into the month the date is set (i.e 3rd Wednesday -> ordinal=3).
    
    final private MonthlyRepeat getMonthlyRepeat()
    { // returns MonthlyRepeat enum from boolean properties
        if (isRepeatDayOfMonth()) return MonthlyRepeat.DAY_OF_MONTH;
        if (isRepeatDayOfWeek()) return MonthlyRepeat.DAY_OF_WEEK;
        return null; // should not get here
    }

    final private void setMonthlyRepeat(MonthlyRepeat monthlyRepeat)
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
            while (! myDay.isAfter(getStartLocalDate()))
            { // set ordinal number for day-of-week repeat
                ordinal++;
                myDay = myDay.with(TemporalAdjusters.next(dayOfWeek));
            }
        }
    }
    
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
    public Repeat withEndCriteria(EndCriteria endCriteria) { setEndCriteria(endCriteria); return this; }
    
    final private IntegerProperty endAfterEvents = new SimpleIntegerProperty();
    public Integer getEndAfterEvents() { return endAfterEvents.getValue(); }
    public IntegerProperty endAfterEventsProperty() { return endAfterEvents; }
    public void setEndAfterEvents(Integer endAfterEvents) { this.endAfterEvents.set(endAfterEvents); }
    public Repeat withEndAfterEvents(Integer endAfterEvents) { setEndAfterEvents(endAfterEvents); return this; }
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
    public Repeat withEndOnDate(LocalDate endOnDate) { setEndOnDate(endOnDate); return this; }
    
    private Set<LocalDate> deletedDates = new HashSet<LocalDate>();
    public Set<LocalDate> getDeletedDates() { return deletedDates; }
    public void setDeletedDates(Set<LocalDate> dates) { deletedDates = dates; }
    public Repeat withDeletedDates(Set<LocalDate> dates) { setDeletedDates(dates); return this; }
    
    /** Appointment-specific data */
    private Repeatable appointmentData = AppointmentFactory.newAppointmentRepeatable();
    public Repeatable getAppointmentData() { return appointmentData; }
    public Repeat withAppointmentData(Repeatable appointment) { appointment.copyInto(appointment); return this; }
    
    /** Appointments generated from this repeat rule.  Objects are a subset of appointments in main appointments list
     * used in the Agenda calendar.  Names myAppointments to differentiate it from main name appointments */
    final private Set<Appointment> myAppointments = new HashSet<Appointment>();
    public Set<Appointment> getAppointments() { return myAppointments; }
    public Repeat withAppointments(Set<Appointment> s) { getAppointments().addAll(s); return this; }

    /**
     * Determines if repeat rules make sense (true) or can't define a series (false)
     * Need to generate string of repeat rule
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
        DAILY(Period.ofDays(1))
      , WEEKLY(Period.ofWeeks(1))
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
    
    private enum MonthlyRepeat {
        DAY_OF_MONTH, DAY_OF_WEEK;
    }

    /**
     * Reads from a XML file a collection of all repeat rules, adds them to repeats
     * @param appointmentGroups 
     * 
     * @param inputFile: File originating in the Setting class
     * @param inputFile: File originating in the Setting class
     * @return the collection of repeats, to be put into KarateData.appointmentRepeatMap
     * @throws TransformerException
     * @throws ParserConfigurationException
     */
    public static Collection<Repeat> readFromFile(Path inputFile
            , List<AppointmentGroup> appointmentGroups
            , Collection<Repeat> repeats) throws TransformerException, ParserConfigurationException
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        try {
            Document doc = builder.parse(inputFile.toFile());
            Map<String, String> rootAttributes = DataUtilities.getAttributes(doc.getFirstChild(), "repeatRules");
            List<Integer> keys = DataUtilities.myGetList(rootAttributes, "keys", "");
            Iterator<Integer> keyIterator = keys.iterator();

            NodeList myNodeList = doc.getElementsByTagName("repeat");
            for (int n=0; n<myNodeList.getLength(); n++) {
                Node myNode = myNodeList.item(n);
                if (myNode.hasAttributes()) {
                    try {
                        Integer myKey = keyIterator.next();
                        nextKey = Math.max(nextKey, myKey);
                        Repeat myRepeat = new Repeat().unmarshal((Element) myNodeList.item(n), myKey);
                        Integer i = myRepeat.getAppointmentData().getAppointmentGroupIndex();
                        myRepeat.getAppointmentData().setAppointmentGroup(appointmentGroups.get(i));
                        repeats.add(myRepeat);
                    } catch (IllegalArgumentException e2) {
//                        Main.log.log(Level.WARNING, "Repeat rule skipped: " + inputFile.toString() + " key=" + keys.get(n), e2);                   
                    }
                }
            }
        } catch (SAXException | IOException e) {
//            Main.log.log(Level.WARNING, "Missing file: " + inputFile.toString(), e);
        }
        nextKey++;
        if (repeats.size() > 0) {
            nextKey = DataUtilities.checkNextKey(nextKey
                    , repeats.stream().map(r -> r.getKey()).collect(Collectors.toSet())
                    , "Repeat.nextRepeatKey");
        }
        return repeats;
    }
    
    /**
     * Unmarshal one org.w3c.dom.Element into a new Repeat object
     * @param expectedKey 
     * 
     * @param myElement: Element with one Repeat object's data
     * @return A Repeat object with all the data fields filled from the Element
     */
    private Repeat unmarshal(Element myElement, Integer expectedKey)
    {
        Map<String, String> repeatAttributes = DataUtilities.getAttributes(myElement, "repeat");

        setKey(Integer.valueOf(DataUtilities.myGet(repeatAttributes, "key", "")));
        if (! (getKey() == expectedKey)) {
//            Main.log.log(Level.WARNING, "Repeat key does not match expected key. Repeat key = " + getKey()
//                    + " Expected repeat key = " + expectedKey + ". Using expected repeat key.", new IllegalArgumentException());
        }
        String intervalUnitString = DataUtilities.myGet(repeatAttributes, "intervalUnit", "");
        IntervalUnit myIntervalUnit = IntervalUnit.valueOf(intervalUnitString);
        setIntervalUnit(myIntervalUnit);
        setRepeatFrequency(DataUtilities.myParseInt(DataUtilities.myGet(repeatAttributes, "repeatFrequency", "")));
        String endCriteriaString = DataUtilities.myGet(repeatAttributes, "endCriteria", "");
        EndCriteria myEndCriteria = EndCriteria.valueOf(endCriteriaString);
        setEndCriteria(myEndCriteria);
        setStartLocalDate(DataUtilities.myParseLocalDate(DataUtilities.myGet(repeatAttributes, "startDate", "")));
        setStartLocalTime(DataUtilities.myParseLocalTime(DataUtilities.myGet(repeatAttributes, "startTime", ""), Settings.TIME_FORMAT_AGENDA));
        setEndLocalTime(DataUtilities.myParseLocalTime(DataUtilities.myGet(repeatAttributes, "endTime", ""), Settings.TIME_FORMAT_AGENDA));
        setDeletedDates(DataUtilities.myGetSet(repeatAttributes, "deletedDates", "", Settings.DATE_FORMAT1));

        switch (myIntervalUnit) {
            case DAILY:
                break;
            case WEEKLY:
                Arrays.stream(DataUtilities.myGet(repeatAttributes, "daysOfWeek", "").split(" "))
                      .map(a -> DayOfWeek.valueOf(a))
                      .forEach(a -> getDayOfWeekMap().get(a).set(true));
                break;
            case MONTHLY:
                setMonthlyRepeat(MonthlyRepeat.valueOf(DataUtilities.myGet(repeatAttributes, "monthlyRepeat", "")));
                break;
            case YEARLY:
                break;
            default:
                break;
        }

        switch (myEndCriteria) {
            case NEVER:
                break;
            case AFTER:
                setEndAfterEvents(DataUtilities.myParseInt(DataUtilities.myGet(repeatAttributes, "endAfterEvents", "")));
                // fall through
            case ON:
                setEndOnDate(LocalDate.parse(DataUtilities.myGet(repeatAttributes, "endOnDate", ""), Settings.DATE_FORMAT1));
                break;
            default:
                break;
        }
        
        Element appointmentElement = (Element) myElement.getElementsByTagName("appointment").item(0);   // must be only one appointment element
        Map<String, String> appointmentAttributes = DataUtilities.getAttributes(appointmentElement, "appointment");
        AppointmentFactory.returnRepeatable(appointmentData).unmarshal(appointmentAttributes, "Repeat appointment settings");
        return this;
    }
    
    /**
     * Writes a set of repeat rules to a file.
     * 
     * @param appointmentRepeatMap: Map of all Repeat objects to be written (KarateDataUtilities.appointmentRepeatMap)
     * @param writeFile: File on disk for new data - overwrites automatically
     * @throws ParserConfigurationException
     */
    private static void writeToFile(Collection<Repeat> repeats, Path writeFile)
    {
        // XML document
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e1) {
            e1.printStackTrace();
        }
        Document doc = builder.newDocument();

        // root node
        Element rootElement = doc.createElement("repeatRules");
        doc.appendChild(rootElement);

        // map loop
        for(Repeat myRepeat : repeats)
        {
            Node myElement = myRepeat.marshal(doc);
            rootElement.appendChild(myElement);
        }

        String repeatKeys = repeats
                .stream()
                .map(a -> a.getKey().toString()).collect(Collectors.joining(" "));
        rootElement.setAttribute("keys", repeatKeys);

        try {
            DataUtilities.writeDocument(doc, writeFile);
        } catch (TransformerException e) {
//            Main.log.log(Level.SEVERE, "Appointment Repeat file " + writeFile + " can't be written");
        }
    }
    /**
     * Writes a map of repeat rules to a default file.
     * 
     * @param appointmentRepeatMap: Map of all Repeat objects to be written (KarateDataUtilities.appointmentRepeatMap)
     * @throws ParserConfigurationException
     */
    public static void writeToFile(Collection<Repeat> repeatSet)
    {
        writeToFile(repeatSet, Settings.APPOINTMENT_REPEATS_FILE);
    }
    
        
    /**
     * Marshal one Repeat object into one org.w3c.dom.Element
     * 
     * @param Document: org.w3c.dom.Document used to make a new element to be returned
     * @return: myElement populated with attributes containing Repeat object data
     */
    private Element marshal(Document doc)
    {
        Element myElement = doc.createElement("repeat");
        myElement.setAttribute("endCriteria", getEndCriteria().toString());
        myElement.setAttribute("intervalUnit", getIntervalUnit().toString());
        if (getKey() == null) setKey(nextKey++); // if it has no key (meaning its new) give it the next one
        myElement.setAttribute("key", Integer.toString(getKey()));
        myElement.setAttribute("repeatFrequency", Integer.toString(getRepeatFrequency()));
        myElement.setAttribute("startDate", DataUtilities.myFormatLocalDate(getStartLocalDate()));
        myElement.setAttribute("startTime", DataUtilities.myFormatLocalTime(getStartLocalTime()));
        myElement.setAttribute("endTime", DataUtilities.myFormatLocalTime(getEndLocalTime()));
        String d = getDeletedDates().stream()
                                    .map(a -> DataUtilities.myFormatLocalDate(a))
                                    .collect(Collectors.joining(" "));
        myElement.setAttribute("deletedDates", d);

        switch (getIntervalUnit())
        {
            case DAILY:
                break;
            case WEEKLY:
                String days = getDayOfWeekMap().entrySet()
                                                    .stream()
                                                    .filter(a -> a.getValue().get())
                                                    .map(a -> a.getKey().toString())
                                                    .collect(Collectors.joining(" "));
                myElement.setAttribute("daysOfWeek", days);
                break;
            case MONTHLY:
                myElement.setAttribute("monthlyRepeat", getMonthlyRepeat().toString());
                break;
            case YEARLY:
                break;
            default:
//                Main.log.log(Level.WARNING, "Unknown intervalUnit " + getIntervalUnit());
                break;
        }

        switch (getEndCriteria())
        {
            case NEVER:
                break;
            case AFTER:
                myElement.setAttribute("endAfterEvents", getEndAfterEvents().toString());
                if (getEndOnDate() == null) makeEndOnDateFromEndAfterEvents();  // new AFTER repeat rules need end dates calculated.
                // fall through
            case ON:
                myElement.setAttribute("endOnDate", getEndOnDate().toString());
                break;
            default:
                break;
        }
        
        Element appointmentElement = doc.createElement("appointment");
        AppointmentFactory.returnRepeatable(getAppointmentData()).marshal(appointmentElement);
        myElement.appendChild(appointmentElement);
        
        return myElement;
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
    
    public Repeat makeAppointments(Collection<Appointment> appointments)
    {
        if (startDate == null) startDate = LocalDate.now().minusWeeks(1);
        if (endDate == null) endDate = LocalDate.now().plusWeeks(1);
        return makeAppointments(appointments, startDate, endDate);
    }

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
                    .map(a -> AppointmentFactory.newAppointment(this, a))       // map to new appointments
                    .iterator();                                                // make iterator
            
            while (i.hasNext())
            { // Process new appointments
                final Appointment a = i.next();
                if (a.getStartLocalDateTime().toLocalDate().isAfter(myEndDate)) break; // exit loop when at end
                appointments.add(a);                                                   // add appointments to main collection
                getAppointments().add(a);                                              // add appointments to this repeat's collection
            }
        }
        
        return this;
    }
    
    public Repeat removeAppointments(Collection<Appointment> appointments)
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
            RepeatableAppointmentUtilities.removeOne(appointments, a);
            RepeatableAppointmentUtilities.removeOne(getAppointments(), a);
        }
        return this;
    }

    
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
            , Repeatable appointmentOld
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
                        getAppointmentData().copyInto(a);
                    } else { // copy only non-unique data
                        getAppointmentData().copyInto(a, appointmentOld);
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
            if (a.getStudentKeys().isEmpty())
            { // DELETE EXISTING INVALID APPOINTMENT
                appointments.remove(a);
                getAppointments().remove(a);
            } else { // LEAVE EXISTING APPOINTMENT BECAUSE HAS ATTENDANCE
                getAppointmentData().copyInto(a);
            }
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
                RepeatableAppointmentUtilities.removeOne(repeats, this);
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
     * Returns a stream of valid start dates.
     * I wonder if it could be done more efficiently without the iterator.
     * 
     * @return
     */
    public Stream<LocalDate> startDateStream()
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
    
    /**
     * Faster version without iterating
     * DOES NOT WORK PROPERLY - MONTHLY DAY OF WEEK IS BROKEN (use slow one for now)
     * 
     * @param inputDate
     * @return
     */
    @Deprecated
    public LocalDate nextValidDate(LocalDate inputDate)
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
        LocalDate firstMatchDate = getStartLocalDate()
                .minus(getIntervalUnit().getValue())
                .with(new NextAppointment());
        final Iterator<LocalDate> i = Stream                                            // appointment iterator
                .iterate(firstMatchDate, (a) -> { return a.with(new NextAppointment()); }) // infinite stream of valid dates
                .filter(a -> ! getDeletedDates().contains(a))                             // filter out deleted dates
                .iterator();                                                            // make iterator

        while (i.hasNext())
        { // find date
            final LocalDate s = i.next();
            if (! s.isBefore(inputDate)) 
            {
                if (s.isAfter(getStartLocalDate()))
                {
                    return s; // exit loop when beyond date without match                    
                } else {
//                    throw new InvalidParameterException("nextValidDate is before StartDate - fix algorithym");
                    return s;
                }
            }
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
        getAppointmentData().copyInto(repeat.getAppointmentData());
        getAppointments().stream()
                         .forEach(a -> repeat.getAppointments().add(a));
        return repeat;
    }
    
    Appointment copyInto(Appointment a) {
        getAppointmentData().copyInto(a);
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
   
    
    /**
     * Returns new Repeat object with all fields copied from input parameter myRepeat
     * 
     * @param myRepeat
     * @return
     * @throws CloneNotSupportedException
     */
    public static Repeat copy(Repeat myRepeat)
    {
//        if(!(myRepeat instanceof Cloneable)) throw new CloneNotSupportedException("Invalid cloning");
        Repeat copyRepeat = new Repeat(myRepeat);
//        myRepeat.copyInto(copyRepeat);
        myRepeat.getAppointmentData().copyInto(copyRepeat.getAppointmentData());
        return copyRepeat;
    }

    @Override   // requires checking object property and, if not null, checking of wrapped value
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if((obj == null) || (obj.getClass() != getClass())) {
            return false;
        }
        Repeat testObj = (Repeat) obj;
                
        return getEndAfterEvents().equals(testObj.getEndAfterEvents())
            && (getEndCriteria() == testObj.getEndCriteria())
            && (getIntervalUnit() == testObj.getIntervalUnit())
            && isRepeatDayOfMonth().equals(testObj.isRepeatDayOfMonth()) 
            && isRepeatDayOfWeek().equals(testObj.isRepeatDayOfWeek())
            && getRepeatFrequency().equals(testObj.getRepeatFrequency())
            && dayOfWeekMapEqual(testObj.getDayOfWeekMap())
            && myEquals(getStartLocalDate(), testObj.getStartLocalDate())
            && myEquals(getStartLocalTime(), (testObj.getStartLocalTime()));
    }

    private boolean myEquals(Object o1, Object o2)
    {
        if ((o1 == null) && (o2 == null)) return true; // both null
        if (o1 == null || o2 == null) return false; // one null
        return o1.equals(o2);
    }


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
        this.getAppointmentData().unbindAll();
    }
    
    /**
     * Adjust date to become next date based on the Repeat rule
     * 
     * @return
     */
    private class NextAppointment implements TemporalAdjuster
    {
        @Override
        public Temporal adjustInto(Temporal temporal)
        {
            LocalDate inputDate = LocalDate.from(temporal);
            int maxI = getRepeatFrequency();
            if (inputDate.isBefore(getStartLocalDate()))
            { // start no earlier than one day before startLocatDate
                Period p = Period.between(inputDate, getStartLocalDate().minusDays(1));
                temporal = temporal.plus(p);
                maxI = 1;
            }
            for (int i=0; i<maxI; i++)
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
                            final Iterator<DayOfWeek> daysIterator = Stream
                                .iterate(d1, (a) ->  a.plus(1))              // infinite stream of valid days of the week
                                .limit(7)                                    // next valid day should be found within 7 days
                                .iterator();
                            while (daysIterator.hasNext()) {
                                DayOfWeek d = daysIterator.next();
                                if (getDayOfWeek(d)) return temporal.with(TemporalAdjusters.next(d));
                            }
                            return temporal; // only happens if no day of the week are selected (true)
                            case MONTHLY:
                                switch (getMonthlyRepeat())
                                {
                                case DAY_OF_MONTH:
                                    return temporal.plus(Period.ofMonths(1));
                                case DAY_OF_WEEK:
                                    DayOfWeek dayOfWeek = getStartLocalDate().getDayOfWeek();
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
            }
        return temporal;
        }
    }
}
