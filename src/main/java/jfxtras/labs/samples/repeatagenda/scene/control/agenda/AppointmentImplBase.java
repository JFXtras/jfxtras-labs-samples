package jfxtras.labs.samples.repeatagenda.scene.control.agenda;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.w3c.dom.Element;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.Agenda.Appointment;

/**
 * Contains fields that are specific to a saved appointment - not applicable to a repeat rule.
 * @author David Bal
 *
 * @param <T>
 */
public abstract class AppointmentImplBase<T> extends RepeatableBase<T> implements Appointment
{
    // NOTE - THESE STATICS MAY BE A PROBLEM - THEY LIMIT THE USE OF AGENDA TO ONE PER APPLICATION
    // IS THERE A BETTER WAY? (PUT THESE FIELDS AND METHODS ELSEWHERE?)
    private static int nextKey = 0;
    private static Map<Integer, Repeat> repeatMap = new HashMap<Integer, Repeat>(); // private map of repeats used to match Repeat objects to appointments
    /** create map of Repeat objects and repeat keys.  Its used to find Repeat objects to attach to Appointment objects.
     * Only used when setting up appointments from file */
    protected static void setupRepeatMap(Collection<Repeat> repeats)
    {
        repeatMap = repeats.stream()
                           .collect(Collectors.toMap(a -> a.getKey(), a -> a));
    }

    /** Unique appointment key */
    private Integer key;
    public Integer getKey() { return key; }
    public void setKey(Integer value) { key = value; }
    public boolean hasKey() { return key != null; }
    public T withKey(Integer value) { setKey(value); return (T) this; }
    
    /** StudentKeys: */
    final private ObservableList<Integer> studentKeys = FXCollections.observableArrayList();
    public List<Integer> getStudentKeys() { return studentKeys; }
    public void setStudentKeys(List<Integer> value) { studentKeys.setAll(value); }
    public T withStudentKeys(List<Integer> value) { setStudentKeys(value); return (T)this; }
    public ObservableList<Integer> studentKeysProperty() { return studentKeys; }

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
    
    /**
     * Add the fields to an org.w3c.dom.Element as attributes
     * @param myElement
     * @return
     */
    protected Element marshal(Element myElement)
    {
        super.marshal(myElement);
        if (getKey() == null) setKey(nextKey++); // if it has no key (meaning its new) give it the next one
        myElement.setAttribute("key", getKey().toString());
        myElement.setAttribute("repeatKey", (getRepeat() == null) ? "" : getRepeat().getKey().toString());
        final String s = getStudentKeys().stream()
                                         .map(a -> a.toString())
                                         .collect(Collectors.joining(" "));
        myElement.setAttribute("studentKeys", s);
        return myElement;
    }
    
    protected Appointment unmarshal(Map<String, String> appointmentAttributes, Integer expectedKey, String errorMessage) {
        super.unmarshal(appointmentAttributes, errorMessage);
        setRepeatKey(DataUtilities.myParseInt(DataUtilities.myGet(appointmentAttributes, "repeatKey", errorMessage)));
        setKey(Integer.parseInt(DataUtilities.myGet(appointmentAttributes, "key", errorMessage)));
        if (! (getKey() == expectedKey)) {
//            Main.log.log(Level.WARNING, "Appointment key does not match expected key. Appointment key = " + getKey()
//                    + " Expected appointment key = " + expectedKey + ". Using expected appointment key.", new IllegalArgumentException());
        }
        nextKey = Math.max(nextKey, getKey()) + 1;
        if (hasRepeatKey()) setRepeat(repeatMap.get(getRepeatKey()));
        setStudentKeys(DataUtilities.myGetList(appointmentAttributes, "studentKeys", errorMessage));

        return this;
    }
   
    
    public Appointment copyInto(Appointment appointment)
    {
        super.copyInto(appointment);
        if (getRepeat() != null) appointment.setRepeat(new Repeat(getRepeat()));
        appointment.setKey(getKey());
        for (Integer myStudentKey : getStudentKeys()) {
            appointment.getStudentKeys().add(myStudentKey);
        }
        return appointment;
    }
    
    @Override   // requires checking object property and, if not null, checking of wrapped value
    public boolean equals(Object obj) {
        Appointment testObj = (Appointment) obj;

        return super.equals(obj)
            && (hasRepeat() && getRepeat().equals(repeat))
            && getStartLocalDateTime().equals(getStartLocalDateTime())
            && getStudentKeys().equals(getStudentKeys());
    }
    
}