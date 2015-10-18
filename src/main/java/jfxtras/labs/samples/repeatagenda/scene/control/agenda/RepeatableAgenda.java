package jfxtras.labs.samples.repeatagenda.scene.control.agenda;

import java.util.Collection;
import java.util.List;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.Pane;

public class RepeatableAgenda extends Agenda {

    /** Repeat rules */
    Collection<Repeat> repeats;
    public Collection<Repeat> repeats() { return repeats; }
    public void setRepeats(Collection<Repeat> repeats) { this.repeats = repeats; }
    
    static public interface RepeatableAppointment extends Agenda.Appointment
    {

        boolean isRepeatMade();
        void setRepeatMade(boolean b);

        void setRepeat(Repeat repeat);
        Repeat getRepeat();
////        boolean hasRepeat();
        boolean repeatFieldsEquals(Object obj); // TODO - CAN THIS BE MADE DEFAULT OR REMOVED?
        
        /**
         * Copies all fields into parameter appointment
         * 
         * @param appointment
         * @return
         */
        default RepeatableAppointment copyInto(RepeatableAppointment appointment) {
            appointment.setEndLocalDateTime(getEndLocalDateTime());
            appointment.setStartLocalDateTime(getStartLocalDateTime());
            copyNonDateFieldsInto(appointment);
//            Iterator<DayOfWeek> dayOfWeekIterator = Arrays 
//                    .stream(DayOfWeek.values())
//                    .limit(7)
//                    .iterator();
//                while (dayOfWeekIterator.hasNext())
//                {
//                    DayOfWeek key = dayOfWeekIterator.next();
//                    boolean b1 = this.getRepeat().getDayOfWeekMap().get(key).get();
//                    boolean b2 = appointment.getRepeat().getDayOfWeekMap().get(key).get();
//                    System.out.println("copied day of week2 " + key + " " + b1 + " " + b2);
//                }
            return appointment;
        }
        
        /**
         * Copies this Appointment non-time fields into parameter appointment
         * 
         * @param appointment
         * @return
         */
        default RepeatableAppointment copyNonDateFieldsInto(RepeatableAppointment appointment) {
            appointment.setAppointmentGroup(getAppointmentGroup());
            appointment.setDescription(getDescription());
            appointment.setSummary(getSummary());
//            boolean b1 = getRepeat() == null;
//            boolean b2 = appointment.getRepeat() == null;
//            System.out.println("repeats " + b1 + " " + b2);
//            if (getRepeat() == null) return appointment;
//            if (appointment.getRepeat() == null)
//            {
//                appointment.setRepeat(RepeatFactory.newRepeat(getRepeat()));
//            } else
//            {
//                getRepeat().copyInto(appointment.getRepeat());
//            }
            return appointment;
        }
        
        /**
         * Copies this Appointment non-time fields into parameter appointment
         * Used when some of fields are unique and should not be copied.
         * 
         * @param appointment
         * @return
         */
        default RepeatableAppointment copyNonDateFieldsInto(RepeatableAppointment appointment, RepeatableAppointment appointmentOld) {
            if (appointment.getAppointmentGroup().equals(appointmentOld.getAppointmentGroup())) {
                appointment.setAppointmentGroup(getAppointmentGroup());
            }
            if (appointment.getDescription().equals(appointmentOld.getDescription())) {
                appointment.setDescription(getDescription());
            }
            if (appointment.getSummary().equals(appointmentOld.getSummary())) {
                appointment.setSummary(getSummary());
            }
            getRepeat().copyInto(appointment.getRepeat());
//            repeatMap.get(this).copyInto(repeatMap.get(appointment));
            return appointment;
        }
    }

    static public abstract class RepeatableAppointmentImplBase<T> {

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
        // TODO - THINK ABOUT ALTERNATIVE TESTING THAT WON'T REQUIRE THIS METHOD
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
//          boolean repeatEquals = (getRepeat() == null)
//                  ? (testObj.getRepeat() == null) : getRepeat().equals(testObj.getRepeat());
          boolean appointmentGroupEquals = (getAppointmentGroup() == null)
                  ? (testObj.getAppointmentGroup() == null) : getAppointmentGroup().equals(testObj.getAppointmentGroup());              
//           System.out.println("repeat appointment " + descriptionEquals + " " + locationEquals + " " + summaryEquals + " " +  " " + appointmentGroupEquals);
          return descriptionEquals && locationEquals && summaryEquals && appointmentGroupEquals;
      }
      
      /** Checks if fields relevant for the repeat rule (non-time fields) are equal. */
      // needs to be overridden by any class implementing Appointment or extending AppointmentImplBase
      // Note: Location field is a problem - I think it should be removed.
      public boolean repeatFieldsEquals(Object obj) {
          return equals(obj);
      }
    }
    
    
    /** Class to contain data for the appointment edit callback */
    static public class RepeatableAppointmentEditData extends AppointmentEditData
    {
        public Appointment appointment;
        public Collection<Appointment> appointments;
        public Collection<Repeat> repeats;
        public List<AppointmentGroup> appountmentGroups;
        public Pane pane;

//        public AppointmentEditData(Appointment appointment, LayoutHelp layoutHelp, Pane pane) {
//            this.appointment = appointment;
//            appointments = layoutHelp.skinnable.appointments();
//            repeats = layoutHelp.skinnable.repeats();
//            appountmentGroups = layoutHelp.skinnable.appointmentGroups();
////            this.layoutHelp = layoutHelp;
//            this.pane = pane;
//        }

    }
}
