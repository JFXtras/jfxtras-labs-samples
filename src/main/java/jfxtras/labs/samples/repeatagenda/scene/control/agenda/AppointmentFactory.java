package jfxtras.labs.samples.repeatagenda.scene.control.agenda;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import javafx.collections.ObservableList;
import jfxtras.labs.samples.repeatagenda.RepeatableAppointmentImpl;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.RepeatableAgenda.RepeatableAppointment;
import jfxtras.scene.control.agenda.Agenda.Appointment;
import jfxtras.scene.control.agenda.Agenda.AppointmentGroup;

/**
 * Provides new Appointment factory for implementation of Appointment interface
 * Also, provides location to specify read and write methods for appointment collections
 * @author David Bal
 *
 */
public final class AppointmentFactory {
    
    private AppointmentFactory() {}

    public static RepeatableAppointmentImpl newAppointment() {
        return new RepeatableAppointmentImpl();
    }
        
    public static RepeatableAppointment newAppointment(RepeatableAppointment appointment) {
        return new RepeatableAppointmentImpl(appointment);
    }

    public static RepeatableAppointmentImpl returnConcreteAppointment(Appointment myAppointment) {
        return (RepeatableAppointmentImpl) myAppointment;
    }


    /**
     * writes appointmentList to file
     */
    public static void writeToFile(Collection<RepeatableAppointment> appointments) {
//        System.out.println("writeToFile");
        RepeatableAppointmentImpl.writeToFile(appointments, Settings.APPOINTMENTS_FILE);
    }

    /**
     * writes appointmentList to file - temporary work around for type problem
     */
    public static void writeToFile(List<Appointment> appointments) {
        System.out.println("write appointmetns");
        List<RepeatableAppointment> appointments2 = appointments
                .stream()
                .map(a -> ((RepeatableAppointment) a)) // down cast all appointmetns to RepeatableAppointments
                .collect(Collectors.toList());
        System.out.println("repeat made = " + appointments2.stream().filter(a -> a.isRepeatMade()).count());
        RepeatableAppointmentImpl.writeToFile(appointments2, Settings.APPOINTMENTS_FILE);
    }
    

    /**
     * reads appointmentList from file
     * @param appointmentsPath 
     * @param appointmentGroups 
     * @throws IOException 
     * @throws SAXException 
     * @throws ParserConfigurationException 
     */
    public static Collection<RepeatableAppointment> readFromFile(Path appointmentsPath
            , ObservableList<AppointmentGroup> appointmentGroups
            , Collection<RepeatableAppointment> appointments)
            throws ParserConfigurationException, SAXException, IOException
    {
//        System.out.println("readFromFile");
        return RepeatableAppointmentImpl.readFromFile(appointmentsPath.toFile(), appointmentGroups, appointments);
    }
    
//    public static void setupRepeats(Collection<MyRepeat> repeats) {
//        MyAppointment.setupRepeats(repeats);
//    }
  
}
