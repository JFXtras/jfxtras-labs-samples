package jfxtras.labs.samples.repeatagenda.scene.control.agenda;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.Agenda.Appointment;

/**
 *  LocalTime start and end time for an appointment
 * @author David Bal
 *
 */
public class AppointmentImplLocal extends AppointmentImplBase<AppointmentImplLocal>
implements Appointment
{
    private static Map<Integer, Integer> appointmentGroupCount = new HashMap<Integer, Integer>();
    
    /** StartDateTime: */
    public ObjectProperty<LocalDateTime> startLocalDateTimeProperty() { return startLocalDateTime; }
    final private ObjectProperty<LocalDateTime> startLocalDateTime = new SimpleObjectProperty<LocalDateTime>(this, "startDateTime");
    public LocalDateTime getStartLocalDateTime() { return startLocalDateTime.getValue(); }
    public void setStartLocalDateTime(LocalDateTime value) { startLocalDateTime.setValue(value); }
    public AppointmentImplLocal withStartLocalDateTime(LocalDateTime value) { setStartLocalDateTime(value); return this; }
    
    /** EndDateTime: */
    public ObjectProperty<LocalDateTime> endLocalDateTimeProperty() { return endLocalDateTime; }
    protected final ObjectProperty<LocalDateTime> endLocalDateTime = new SimpleObjectProperty<LocalDateTime>(this, "endDateTime");
    public LocalDateTime getEndLocalDateTime() { return endLocalDateTime.getValue(); }
    public void setEndLocalDateTime(LocalDateTime value) { endLocalDateTime.setValue(value); }
    public AppointmentImplLocal withEndLocalDateTime(LocalDateTime value) { setEndLocalDateTime(value); return this; } 
    
    /** Location: */
    // I'M NOT USING THESE
    public ObjectProperty<String> locationProperty() { return locationObjectProperty; }
    final private ObjectProperty<String> locationObjectProperty = new SimpleObjectProperty<String>(this, "location");
    public String getLocation() { return locationObjectProperty.getValue(); }
    public void setLocation(String value) { locationObjectProperty.setValue(value); }
    public AppointmentImplLocal withLocation(String value) { setLocation(value); return this; } 
    public AppointmentImplLocal() { }

    /**
     * Copy constructor for a repeatable appointment
     * 
     * @param repeat
     * @param date 
     */
    public AppointmentImplLocal(Repeat repeat, LocalDate date)
    {
        repeat.getAppointmentData().copyInto(this);
        LocalDateTime myStartDateTime = date.atTime(repeat.getStartLocalTime());
        LocalDateTime myEndDateTime = date.atTime(repeat.getEndLocalTime());
        
        this.withStartLocalDateTime(myStartDateTime)
            .withEndLocalDateTime(myEndDateTime)
            .withRepeat(repeat)
            .withRepeatMade(true);
    }
    
    /**
     * Copy constructor
     * 
     * @param appointment
     */
    public AppointmentImplLocal(Appointment appointment) {
        appointment.copyInto(this);
    }
    
    public String toString()
    {
        return super.toString()
             + ", "
             + this.getStartLocalDateTime()
             + " - "
             + this.getEndLocalDateTime()
             ;
    }



    /**
     * Copy factory
     * Returns new Appointment object with all fields copied from input parameter myAppointment
     * 
     * @param appointment
     * @return
     * @throws CloneNotSupportedException 
     */
    public Appointment copyInto(Appointment appointment) {
//        super.copyInto(appointment);
        appointment.setEndLocalDateTime(getEndLocalDateTime());
        appointment.setStartLocalDateTime(getStartLocalDateTime());
        return appointment;
    }
    
    /**
     *  Copy's current object's fields into passed parameter
     *  
     */
    public Repeat copyInto(Repeat repeat) {
        copyInto(repeat.getAppointmentData());
        repeat.setStartLocalDate(getStartLocalDateTime().toLocalDate());
        repeat.setStartLocalTime(getStartLocalDateTime().toLocalTime());
        repeat.setEndLocalTime(getEndLocalDateTime().toLocalTime());
        DayOfWeek d = getStartLocalDateTime().getDayOfWeek();
        repeat.setDayOfWeek(d, true);
        return repeat;
    }
   
    @Override   // requires checking object property and, if not null, checking of wrapped value
    public boolean equals(Object obj) {
        Appointment testObj = (Appointment) obj;

        return super.equals(obj) &&
                getEndLocalDateTime().equals(testObj.getEndLocalDateTime()) &&
                getStartLocalDateTime().equals(getStartLocalDateTime());
    }


}
