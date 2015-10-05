//package jfxtras.labs.samples.repeatagenda.scene.control.agenda;
//
//import java.util.Map;
//
//import org.w3c.dom.Element;
//
//import javafx.beans.property.BooleanProperty;
//import javafx.beans.property.ObjectProperty;
//import javafx.beans.property.SimpleBooleanProperty;
//import javafx.beans.property.SimpleObjectProperty;
//import javafx.beans.property.SimpleStringProperty;
//import javafx.beans.property.StringProperty;
//import javafx.collections.ObservableList;
//import jfxtras.labs.samples.repeatagenda.scene.control.agenda.Agenda.Appointment;
//import jfxtras.labs.samples.repeatagenda.scene.control.agenda.Agenda.AppointmentGroup;
//import jfxtras.labs.samples.repeatagenda.scene.control.agenda.Agenda.Repeatable;
//
///**
// * Contains fields that apply to both a repeat rule and a saved appointment
// * @author David Bal
// *
// * @param <T>
// */
//public class RepeatableBase<T> implements Repeatable {
//
//    /** WholeDay: */
//    public BooleanProperty wholeDayProperty() { return wholeDayObjectProperty; }
//    final private BooleanProperty wholeDayObjectProperty = new SimpleBooleanProperty(this, "wholeDay", false);
//    public Boolean isWholeDay() { return wholeDayObjectProperty.getValue(); }
//    public void setWholeDay(Boolean value) { wholeDayObjectProperty.setValue(value); }
//    public T withWholeDay(Boolean value) { setWholeDay(value); return (T)this; } 
//    
//    /** Summary: */
//    public StringProperty summaryProperty() { return summaryObjectProperty; }
//    final private StringProperty summaryObjectProperty = new SimpleStringProperty(this, "summary", "");
//    public String getSummary() { return summaryObjectProperty.getValue(); }
//    public void setSummary(String value) { summaryObjectProperty.setValue(value); }
//    public T withSummary(String value) { setSummary(value); return (T)this; } 
//    
//    /** Description: */
//    public StringProperty descriptionProperty() { return descriptionObjectProperty; }
//    final private StringProperty descriptionObjectProperty = new SimpleStringProperty(this, "description", "");
//    public String getDescription() { return descriptionObjectProperty.getValue(); }
//    public void setDescription(String value) { descriptionObjectProperty.setValue(value); }
//    public T withDescription(String value) { setDescription(value); return (T)this; } 
//    
//      /** AppointmentGroupIndex: */
//    private int appointmentGroupIndex = 0; // only used privately - later matched up to an appointmentGroup
////    public IntegerProperty appointmentGroupIndexProperty() { return appointmentGroupIndexProperty; }
////    final private IntegerProperty appointmentGroupIndexProperty = new SimpleIntegerProperty(this, "appointmentGroup");
//    public Integer getAppointmentGroupIndex() { return appointmentGroupIndex; }
//    public void setAppointmentGroupIndex(Integer value) { appointmentGroupIndex = value; }
////    public T withAppointmentGroupIndex(Integer value) { setAppointmentGroupIndex(value); return (T)this; }
//
//    /** AppointmentGroup: */
//    public ObjectProperty<AppointmentGroup> appointmentGroupProperty() { return appointmentGroupObjectProperty; }
//    final private ObjectProperty<AppointmentGroup> appointmentGroupObjectProperty = new SimpleObjectProperty<AppointmentGroup>(this, "appointmentGroup");
//    public AppointmentGroup getAppointmentGroup() { return appointmentGroupObjectProperty.getValue(); }
//    public void setAppointmentGroup(AppointmentGroup value) { appointmentGroupObjectProperty.setValue(value); }
//    public T withAppointmentGroup(AppointmentGroup value) { setAppointmentGroup(value); return (T)this; }
//    public void assignAppointmentGroup(ObservableList<AppointmentGroup> appointmentGroups) { setAppointmentGroup(appointmentGroups.get(appointmentGroupIndex));  }
//   
//    /**
//     * Add the fields to an org.w3c.dom.Element as attributes
//     * @param myElement
//     * @return
//     */
//    protected Element marshal(Element myElement)
//    {
//        myElement.setAttribute("wholeDay", Boolean.toString(isWholeDay()));
//        myElement.setAttribute("summary", getSummary());
//        myElement.setAttribute("description", getDescription());
////        myElement.setAttribute("locationKey", Integer.toString(getLocationKey()));
//        myElement.setAttribute("groupIndex", Integer.toString(getAppointmentGroup().getKey()));
////        final String s = getStaffKeys().stream()
////                                 .map(a -> a.toString())
////                                 .collect(Collectors.joining(" "));
////        myElement.setAttribute("staffKeys", s);
////        myElement.setAttribute("styleKey", DataUtilities.myInt2String(getStyleKey()));
//        return myElement;
//    }
//
//    public Repeatable unmarshal(Map<String, String> appointmentAttributes, String errorMessage)
//    {
//        setDescription(DataUtilities.myGet(appointmentAttributes, "description", errorMessage));
//        setAppointmentGroupIndex(Integer.parseInt(DataUtilities.myGet(appointmentAttributes, "groupIndex", errorMessage)));
////        setLocationKey(Integer.parseInt(DataUtilities.myGet(appointmentAttributes, "locationKey", errorMessage)));
////        setStaffKeys(DataUtilities.myGetList(appointmentAttributes, "staffKeys", errorMessage));
////        setStyleKey(Integer.parseInt( DataUtilities.myGet(appointmentAttributes, "styleKey", errorMessage)));
//        setSummary( DataUtilities.myGet(appointmentAttributes, "summary", errorMessage));
//        setWholeDay(DataUtilities.myParseBoolean(DataUtilities.myGet(appointmentAttributes, "wholeDay", errorMessage)));
//
//        return this;
//    }
//
//    
//    /**
//     *  Copy's current object's fields into passed parameter
//     */
//    public Repeatable copyInto(Repeatable appointmentData) {
//        appointmentData.setAppointmentGroup(getAppointmentGroup());
//        appointmentData.setDescription(getDescription());
////        appointmentData.setLocationKey(getLocationKey());
////        appointmentData.getStaffKeys().addAll(getStaffKeys());
////        appointmentData.setStyleKey(getStyleKey());
//        appointmentData.setSummary(getSummary());
//        return appointmentData;
//    }
//    
//    /**
//     *  Copy's current object's fields into passed parameter
//     *  For copying a moved appointment that is a part of a Repeat, but is not repeat-made and has some unique data.
//     *  Only copy over the non-unique data. 
//     */
//    public Repeatable copyInto(Repeatable appointmentData, Repeatable appointmentOld) {
//        if (appointmentData.getAppointmentGroup().equals(appointmentOld.getAppointmentGroup())) {
//            appointmentData.setAppointmentGroup(getAppointmentGroup());            
//        }
//        if (appointmentData.getDescription().equals(appointmentOld.getDescription())) {
//            appointmentData.setDescription(getDescription());            
//        }
////        if (appointmentData.getLocationKey().equals(appointmentOld.getLocationKey())) {
////            appointmentData.setLocationKey(getLocationKey());
////        }
////        if (appointmentData.getStaffKeys().equals(appointmentOld.getStaffKeys())) {
////            appointmentData.getStaffKeys().addAll(getStaffKeys());
////        }
////        if (appointmentData.getStyleKey().equals(appointmentOld.getStyleKey())) {
////            appointmentData.setStyleKey(getStyleKey());
////        }
//        if (appointmentData.getSummary().equals(appointmentOld.getSummary())) {
//            appointmentData.setSummary(getSummary());
//        }
//        return appointmentData;
//    }
//    
//    @Override   // requires checking object property and, if not null, checking of wrapped value
//    public boolean equals(Object obj) {
//        if (obj == this) return true;
//        if((obj == null) || (obj.getClass() != getClass())) {
//            return false;
//        }
//        Appointment testObj = (Appointment) obj;
//        //        
//        return getAppointmentGroup().equals(testObj.getAppointmentGroup()) &&
//            getDescription().equals(testObj.getDescription()) &&
////            getLocationKey().equals(testObj.getLocationKey()) &&
////            getStaffKeys().equals(testObj.getStaffKeys()) &&
////            getStyleKey().equals(testObj.getStyleKey()) &&
//            getSummary().equals(testObj.getSummary());
//    }
//    
//    /**
//     * removes bindings on all properties
//     */
//    @Override
//    public void unbindAll() {
//        this.appointmentGroupProperty().unbind();
////        this.locationKeyProperty().unbind();
////        this.styleKeyProperty().unbind();
//        this.summaryProperty().unbind();
//        this.descriptionProperty().unbind();
//        this.wholeDayProperty().unbind();
//    }
//
//}
