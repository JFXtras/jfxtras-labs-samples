package jfxtras.labs.samples.repeatagenda;

import java.util.HashSet;
import java.util.Set;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.Repeat;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.RepeatableAgenda;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.RepeatableAgenda.RepeatableAppointment;
import jfxtras.scene.control.agenda.Agenda.AppointmentGroup;

public final class MyData {
	    
   private final ObservableList<RepeatableAppointment> appointments = FXCollections.observableArrayList();
   public ObservableList<RepeatableAppointment> getAppointments() { return appointments; }
   //    private final ObservableList<T> appointments = FXCollections.observableArrayList();
//    public ObservableList<T> getAppointments() { return appointments; }
    
//    private final ObservableList<AppointmentGroup> appointmentGroups = FXCollections.observableArrayList();
    private final ObservableList<AppointmentGroup> appointmentGroups = RepeatableAgenda.DEFAULT_APPOINTMENT_GROUPS;
    public ObservableList<AppointmentGroup> getAppointmentGroups() { return appointmentGroups; }
    public void setAppointmentGroups( ObservableList<AppointmentGroup> list) { appointmentGroups.addAll(list); }

    /** All appointment repeat rules */
    private final Set<Repeat> repeats = new HashSet<Repeat>();
    public Set<Repeat> getRepeats() { return repeats; }
    
}
