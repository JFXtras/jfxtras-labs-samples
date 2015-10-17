package jfxtras.labs.samples.repeatagenda.scene.control.agenda;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.Agenda.Appointment;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.Agenda.AppointmentGroup;

public abstract class RepeatableAppointmentImplBase<T> {

    /** Repeat rules, null if an individual appointment */
    private Repeat repeat;
    public void setRepeat(Repeat repeat) { this.repeat = repeat; }
    public Repeat getRepeat() { return repeat; }
    public T withRepeat(Repeat value) { setRepeat(value); return (T)this; }
    
    /**
     * true = a temporary appointment created by a repeat rule
     * false = a individual permanent appointment
     */
    final private BooleanProperty repeatMade = new SimpleBooleanProperty(this, "repeatMade", false); // defaults to a individual permanent appointment
    public BooleanProperty repeatMadeProperty() { return repeatMade; }
    public boolean isRepeatMade() { return repeatMade.getValue(); }
    public void setRepeatMade(boolean b) {repeatMade.set(b); }
    public T withRepeatMade(boolean b) {repeatMade.set(b); return (T)this; }
    
    /** WholeDay: */
    public ObjectProperty<Boolean> wholeDayProperty() { return wholeDayObjectProperty; }
    final private ObjectProperty<Boolean> wholeDayObjectProperty = new SimpleObjectProperty<Boolean>(this, "wholeDay", false);
    public Boolean isWholeDay() { return wholeDayObjectProperty.getValue(); }
    public void setWholeDay(Boolean value) { wholeDayObjectProperty.setValue(value); }
    public T withWholeDay(Boolean value) { setWholeDay(value); return (T)this; } 
    
    /** Summary: */
    public ObjectProperty<String> summaryProperty() { return summaryObjectProperty; }
    final private ObjectProperty<String> summaryObjectProperty = new SimpleObjectProperty<String>(this, "summary");
    public String getSummary() { return summaryObjectProperty.getValue(); }
    public void setSummary(String value) { summaryObjectProperty.setValue(value); }
    public T withSummary(String value) { setSummary(value); return (T)this; } 
    
    /** Description: */
    public ObjectProperty<String> descriptionProperty() { return descriptionObjectProperty; }
    final private ObjectProperty<String> descriptionObjectProperty = new SimpleObjectProperty<String>(this, "description");
    public String getDescription() { return descriptionObjectProperty.getValue(); }
    public void setDescription(String value) { descriptionObjectProperty.setValue(value); }
    public T withDescription(String value) { setDescription(value); return (T)this; } 
    
    /** Location: */
    public ObjectProperty<String> locationProperty() { return locationObjectProperty; }
    final private ObjectProperty<String> locationObjectProperty = new SimpleObjectProperty<String>(this, "location");
    public String getLocation() { return locationObjectProperty.getValue(); }
    public void setLocation(String value) { locationObjectProperty.setValue(value); }
    public T withLocation(String value) { setLocation(value); return (T)this; } 
    
    /** AppointmentGroup: */
    public ObjectProperty<AppointmentGroup> appointmentGroupProperty() { return appointmentGroupObjectProperty; }
    final private ObjectProperty<AppointmentGroup> appointmentGroupObjectProperty = new SimpleObjectProperty<AppointmentGroup>(this, "appointmentGroup");
    public AppointmentGroup getAppointmentGroup() { return appointmentGroupObjectProperty.getValue(); }
    public void setAppointmentGroup(AppointmentGroup value) { appointmentGroupObjectProperty.setValue(value); }
    public T withAppointmentGroup(AppointmentGroup value) { setAppointmentGroup(value); return (T)this; }

  // used for Assert methods in testing
  @Override
  public boolean equals(Object obj) {
      if (obj == this) return true;
      if((obj == null) || (obj.getClass() != getClass())) {
          return false;
      }
      Appointment testObj = (Appointment) obj;

      boolean descriptionEquals = (getDescription() == null)
              ? (testObj.getDescription() == null) : getDescription().equals(testObj.getDescription());
      boolean locationEquals = (getLocation() == null)
              ? (testObj.getLocation() == null) : getLocation().equals(testObj.getLocation());
      boolean summaryEquals = (getSummary() == null)
              ? (testObj.getSummary() == null) : getSummary().equals(testObj.getSummary());
//      boolean repeatEquals = (getRepeat() == null)
//              ? (testObj.getRepeat() == null) : getRepeat().equals(testObj.getRepeat());
      boolean appointmentGroupEquals = (getAppointmentGroup() == null)
              ? (testObj.getAppointmentGroup() == null) : getAppointmentGroup().equals(testObj.getAppointmentGroup());              
//       System.out.println("repeat appointment " + descriptionEquals + " " + locationEquals + " " + summaryEquals + " " +  " " + appointmentGroupEquals);
      return descriptionEquals && locationEquals && summaryEquals && appointmentGroupEquals;
  }
  
  /** Checks if fields relevant for the repeat rule (non-time fields) are equal. */
  // needs to be overridden by any class implementing Appointment or extending AppointmentImplBase
  // Note: Location field is a problem - I think it should be removed.
  public boolean repeatFieldsEquals(Object obj) {
      return equals(obj);
  }
}
