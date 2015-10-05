package jfxtras.labs.samples.repeatagenda.scene.control.agenda;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import javafx.collections.ObservableList;
import jfxtras.labs.samples.repeatagenda.MyAppointment;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.Agenda.Appointment;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.Agenda.AppointmentGroup;

/**
 * Provides one location where some method of Appointment interface can be accessed.
 * These include creating new object, file read and write
 * @author David Bal
 *
 */
public final class AppointmentFactory {
    
//    public static int nextAppointmentKey;

    private AppointmentFactory() {}

    public static MyAppointment newAppointment() {
        return new MyAppointment();
    }
    
//    public static Appointment newAppointment(Repeat repeat, LocalDate date) {
//        return new MyAppointment(repeat, date);
//    }

//    /**
//     * Copy constructor for a repeatable appointment
//     * 
//     * @param repeat
//     * @param date 
//     */
//    default Appointment(Repeat repeat, LocalDate date)
//    {
//        repeat.getAppointmentData().copyInto(this);
//        LocalDateTime myStartDateTime = date.atTime(repeat.getStartLocalTime());
//        LocalDateTime myEndDateTime = date.atTime(repeat.getEndLocalTime());
//        
//        this.withStartLocalDateTime(myStartDateTime)
//            .withEndLocalDateTime(myEndDateTime)
//            .withRepeat(repeat)
//            .withRepeatMade(true);
//    }
    
    public static Appointment newAppointment(Appointment appointment) {
        return new MyAppointment(appointment);
    }

//    public static RepeatableBase<Repeatable> newAppointmentRepeatable() {
//        return new RepeatableBase<Repeatable>();
//   }

//    public static RepeatableBase<Repeatable> returnRepeatable(Repeatable myRepeatable) {
//        return (RepeatableBase<Repeatable>) myRepeatable;
//    }

    public static MyAppointment returnConcreteAppointment(Appointment myAppointment) {
        return (MyAppointment) myAppointment;
    }


    /**
     * writes appointmentList to file
     */
    public static void writeToFile(Collection<Appointment> appointments) {
        MyAppointment.writeToFile(appointments, Settings.APPOINTMENTS_FILE);
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
        return MyAppointment.readFromFile(appointmentsPath.toFile(), appointmentGroups, appointments);
    }
    
//    public static void setupRepeats(Collection<MyRepeat> repeats) {
//        MyAppointment.setupRepeats(repeats);
//    }
  
}
