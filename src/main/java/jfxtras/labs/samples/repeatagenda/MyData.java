package jfxtras.labs.samples.repeatagenda;

import java.util.HashSet;
import java.util.Set;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.RepeatableAgenda;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.RepeatableAgenda.RepeatableAppointment;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.VComponent;
import jfxtras.scene.control.agenda.Agenda.AppointmentGroup;

public final class MyData {
	    
   private final ObservableList<RepeatableAppointment> appointments = FXCollections.observableArrayList();
   public ObservableList<RepeatableAppointment> getAppointments() { return appointments; }
   //    private final ObservableList<T> appointments = FXCollections.observableArrayList();
//    public ObservableList<T> getAppointments() { return appointments; }
    
//    private final ObservableList<AppointmentGroup> appointmentGroups = FXCollections.observableArrayList();
    private final ObservableList<AppointmentGroup> appointmentGroups = RepeatableAgenda.DEFAULT_APPOINTMENT_GROUPS;
    public ObservableList<AppointmentGroup> appointmentGroups() { return appointmentGroups; }
//    public void setAppointmentGroups( ObservableList<AppointmentGroup> list) { appointmentGroups.addAll(list); }

    /** All appointment repeat rules */
    private final Set<VComponent> repeats = new HashSet<VComponent>();
    public Set<VComponent> getVComponents() { return repeats; }
    
}
