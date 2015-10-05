package jfxtras.labs.samples.repeatagenda.scene.control.agenda;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.Agenda.Appointment;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.Agenda.AppointmentGroup;

/**
 * Contains fields that are specific to a saved appointment - not applicable to a repeat rule.
 * @author David Bal
 *
 * @param <T>
 */
public abstract class AppointmentImplBase<T> implements Appointment
{
//    // NOTE - THESE STATICS MAY BE A PROBLEM - THEY LIMIT THE USE OF AGENDA TO ONE PER APPLICATION
//    // IS THERE A BETTER WAY? (PUT THESE FIELDS AND METHODS ELSEWHERE?)
//    private static int nextKey = 0;
//    private static Map<Integer, Repeat> repeatMap = new HashMap<Integer, Repeat>(); // private map of repeats used to match Repeat objects to appointments
//    /** create map of Repeat objects and repeat keys.  Its used to find Repeat objects to attach to Appointment objects.
//     * Only used when setting up appointments from file */
//    protected static void setupRepeatMap(Collection<Repeat> repeats)
//    {
//        repeatMap = repeats.stream()
//                           .collect(Collectors.toMap(a -> a.getKey(), a -> a));
//    }

//    /** Unique appointment key */
//    private Integer key;
//    public Integer getKey() { return key; }
//    public void setKey(Integer value) { key = value; }
//    public boolean hasKey() { return key != null; }
//    public T withKey(Integer value) { setKey(value); return (T) this; }

    /** WholeDay: */
    public BooleanProperty wholeDayProperty() { return wholeDayObjectProperty; }
    final private BooleanProperty wholeDayObjectProperty = new SimpleBooleanProperty(this, "wholeDay", false);
    public Boolean isWholeDay() { return wholeDayObjectProperty.getValue(); }
    public void setWholeDay(Boolean value) { wholeDayObjectProperty.setValue(value); }
    public T withWholeDay(Boolean value) { setWholeDay(value); return (T)this; } 
    
    /** Summary: */
    public StringProperty summaryProperty() { return summaryObjectProperty; }
    final private StringProperty summaryObjectProperty = new SimpleStringProperty(this, "summary", "");
    public String getSummary() { return summaryObjectProperty.getValue(); }
    public void setSummary(String value) { summaryObjectProperty.setValue(value); }
    public T withSummary(String value) { setSummary(value); return (T)this; } 
    
    /** Description: */
    public StringProperty descriptionProperty() { return descriptionObjectProperty; }
    final private StringProperty descriptionObjectProperty = new SimpleStringProperty(this, "description", "");
    public String getDescription() { return descriptionObjectProperty.getValue(); }
    public void setDescription(String value) { descriptionObjectProperty.setValue(value); }
    public T withDescription(String value) { setDescription(value); return (T)this; } 
    
      /** AppointmentGroupIndex: */
    private int appointmentGroupIndex = 0; // only used privately - later matched up to an appointmentGroup
//    public IntegerProperty appointmentGroupIndexProperty() { return appointmentGroupIndexProperty; }
//    final private IntegerProperty appointmentGroupIndexProperty = new SimpleIntegerProperty(this, "appointmentGroup");
    public Integer getAppointmentGroupIndex() { return appointmentGroupIndex; }
    public void setAppointmentGroupIndex(Integer value) { appointmentGroupIndex = value; }
//    public T withAppointmentGroupIndex(Integer value) { setAppointmentGroupIndex(value); return (T)this; }

    /** AppointmentGroup: */
    public ObjectProperty<AppointmentGroup> appointmentGroupProperty() { return appointmentGroupObjectProperty; }
    final private ObjectProperty<AppointmentGroup> appointmentGroupObjectProperty = new SimpleObjectProperty<AppointmentGroup>(this, "appointmentGroup");
    public AppointmentGroup getAppointmentGroup() { return appointmentGroupObjectProperty.getValue(); }
    public void setAppointmentGroup(AppointmentGroup value) { appointmentGroupObjectProperty.setValue(value); }
    public T withAppointmentGroup(AppointmentGroup value) { setAppointmentGroup(value); return (T)this; }
    public void assignAppointmentGroup(ObservableList<AppointmentGroup> appointmentGroups) { setAppointmentGroup(appointmentGroups.get(appointmentGroupIndex));  }
   
    
    
//    
//    /** StudentKeys: */
//    final private ObservableList<Integer> studentKeys = FXCollections.observableArrayList();
//    public List<Integer> getStudentKeys() { return studentKeys; }
//    public void setStudentKeys(List<Integer> value) { studentKeys.setAll(value); }
//    public T withStudentKeys(List<Integer> value) { setStudentKeys(value); return (T)this; }
//    public ObservableList<Integer> studentKeysProperty() { return studentKeys; }

    /** RepeatKey: only used privately */
    private Integer repeatKey;
    private Integer getRepeatKey() { return repeatKey; }
    private void setRepeatKey(Integer value) { repeatKey = value; }
    private boolean hasRepeatKey() { return getRepeatKey() != null; }
    
    /** Repeat rules, null if an individual appointment */
    private Repeat repeat;
    public void setRepeat(Repeat repeat) { this.repeat = repeat; }
    public Repeat getRepeat() { return repeat; }
    public boolean hasRepeat() { return repeat != null; }
    public T withRepeat(Repeat value) { setRepeat(value); return (T)this; }

    /**
     * true = a temporary appointment created by a repeat rule
     * false = a permanent appointment stored on disk
     */
    final private BooleanProperty repeatMade = new SimpleBooleanProperty(this, "repeatMade", false);
    public BooleanProperty repeatMadeProperty() { return repeatMade; }
    public boolean isRepeatMade() { return repeatMade.getValue(); }
    public void setRepeatMade(boolean b) {repeatMade.set(b); }
    public T withRepeatMade(boolean b) {repeatMade.set(b); return (T)this; }
    
//    /**
//     * Add the fields to an org.w3c.dom.Element as attributes
//     * @param myElement
//     * @return
//     */
//    public Element marshal(Element myElement)
//    {
//        super.marshal(myElement);
//        if (getKey() == null) setKey(nextKey++); // if it has no key (meaning its new) give it the next one
//        myElement.setAttribute("key", getKey().toString());
//        myElement.setAttribute("repeatKey", (getRepeat() == null) ? "" : getRepeat().getKey().toString());
//        final String s = getStudentKeys().stream()
//                                         .map(a -> a.toString())
//                                         .collect(Collectors.joining(" "));
//        myElement.setAttribute("studentKeys", s);
//        return myElement;
//    }
//    
//    public Appointment unmarshal(Map<String, String> appointmentAttributes, Integer expectedKey, String errorMessage) {
//        super.unmarshal(appointmentAttributes, errorMessage);
//        setRepeatKey(DataUtilities.myParseInt(DataUtilities.myGet(appointmentAttributes, "repeatKey", errorMessage)));
//        setKey(Integer.parseInt(DataUtilities.myGet(appointmentAttributes, "key", errorMessage)));
//        if (! (getKey() == expectedKey)) {
////            Main.log.log(Level.WARNING, "Appointment key does not match expected key. Appointment key = " + getKey()
////                    + " Expected appointment key = " + expectedKey + ". Using expected appointment key.", new IllegalArgumentException());
//        }
//        nextKey = Math.max(nextKey, getKey()) + 1;
//        if (hasRepeatKey()) setRepeat(repeatMap.get(getRepeatKey()));
//        setStudentKeys(DataUtilities.myGetList(appointmentAttributes, "studentKeys", errorMessage));
//
//        return this;
//    }
   
    
//    public Appointment copyInto(Appointment appointment)
//    {
//        super.copyInto(appointment);
//        if (getRepeat() != null) appointment.setRepeat(new Repeat(getRepeat()));
//        appointment.setKey(getKey());
//        for (Integer myStudentKey : getStudentKeys()) {
//            appointment.getStudentKeys().add(myStudentKey);
//        }
//        return appointment;
//    }
    
    @Override   // requires checking object property and, if not null, checking of wrapped value
    public boolean equals(Object obj) {
        Appointment testObj = (Appointment) obj;

        return super.equals(obj)
            && (hasRepeat() && getRepeat().equals(repeat))
            && getStartLocalDateTime().equals(getStartLocalDateTime());
//            && getStudentKeys().equals(getStudentKeys());
    }
    
}