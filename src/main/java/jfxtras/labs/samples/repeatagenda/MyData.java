package jfxtras.labs.samples.repeatagenda;

import java.util.HashSet;
import java.util.Set;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.Agenda.Appointment;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.Agenda.AppointmentGroup;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.Repeat;

public final class MyData {
	    
    private final ObservableList<Appointment> appointments = FXCollections.observableArrayList();
    public ObservableList<Appointment> getAppointments() { return appointments; }
    
    private final ObservableList<AppointmentGroup> appointmentGroups = FXCollections.observableArrayList();
    public ObservableList<AppointmentGroup> getAppointmentGroups() { return appointmentGroups; }
    public void setAppointmentGroups( ObservableList<AppointmentGroup> list) { appointmentGroups.addAll(list); }

    /** All appointment repeat rules */
    private final Set<Repeat> repeats = new HashSet<Repeat>();
    public Set<Repeat> getRepeats() { return repeats; }
    
}
