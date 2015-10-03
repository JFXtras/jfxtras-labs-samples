package jfxtras.labs.samples.repeatagenda.scene.control.agenda;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Collection;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import javafx.collections.ObservableList;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.Agenda.Appointment;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.Agenda.AppointmentGroup;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.Agenda.Repeatable;

/**
 * Provides one location where some method of Appointment interface can be accessed.
 * These include creating new object, file read and write
 * @author David Bal
 *
 */
public final class AppointmentFactory {
    
//    public static int nextAppointmentKey;

    private AppointmentFactory() {}

    public static AppointmentImplLocal newAppointment() {
        return new AppointmentImplLocal();
    }
    
    public static Appointment newAppointment(Repeat repeat, LocalDate date) {
        return new AppointmentImplLocal(repeat, date);
    }
    
    public static Appointment newAppointment(Appointment appointment) {
        return new AppointmentImplLocal(appointment);
    }

    public static RepeatableBase<Repeatable> newAppointmentRepeatable() {
        return new RepeatableBase<Repeatable>();
   }

    public static RepeatableBase<Repeatable> returnRepeatable(Repeatable myRepeatable) {
        return (RepeatableBase<Repeatable>) myRepeatable;
    }

    public static AppointmentImplLocal returnConcreteAppointment(Appointment myAppointment) {
        return (AppointmentImplLocal) myAppointment;
    }


    /**
     * writes appointmentList to file
     */
    public static void writeToFile(Collection<Appointment> appointments) {
        AppointmentImplLocal.writeToFile(appointments, Settings.APPOINTMENTS_FILE);
    }
    

    /**
     * reads appointmentList from file
     * @param appointmentsPath 
     * @param appointmentGroups 
     * @throws IOException 
     * @throws SAXException 
     * @throws ParserConfigurationException 
     */
    public static Collection<Appointment> readFromFile(Path appointmentsPath, ObservableList<AppointmentGroup> appointmentGroups, Collection<Appointment> appointments)
            throws ParserConfigurationException, SAXException, IOException
    {
        return AppointmentImplLocal.readFromFile(appointmentsPath.toFile(), appointmentGroups, appointments);
    }
    
    public static void setupRepeatMap(Collection<Repeat> repeats) {
        AppointmentImplLocal.setupRepeatMap(repeats);
    }
  
}
