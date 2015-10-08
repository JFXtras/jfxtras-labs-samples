package jfxtras.labs.samples.repeatagenda;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import jfxtras.labs.samples.repeatagenda.controller.CalendarController;
import jfxtras.labs.samples.repeatagenda.internal.scene.control.skin.agenda.base24hour.AppointmentIO;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.Agenda;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.Agenda.AppointmentGroup;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.AppointmentFactory;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.Settings;

public class Main extends Application {
	
    private static ObservableList<AppointmentGroup> appointmentGroups = Agenda.DEFAULT_APPOINTMENT_GROUPS;

    public MyData data = new MyData();  // All data - appointments, styles, members, etc.
    
    public static void main(String[] args) {
        launch(args);       
    }

	@Override
	public void start(Stage primaryStage) throws IOException, TransformerException, ParserConfigurationException, SAXException {
	    
//	    System.out.println(data.getAppointmentGroups().size());
	    RepeatTest r = new RepeatTest();
//	    r.test1();
//	    System.out.println(appointmentGroups.size());
	    r.canListWeeklyFixed2();
	    System.exit(0);
        Locale myLocale = Locale.getDefault();
        ResourceBundle resources = ResourceBundle.getBundle("jfxtras.labs.samples.repeatagenda.Bundle", myLocale);
        Settings.setup(resources);
        
        // I/O and setup
        ObservableList<AppointmentGroup> appointmentGroups = null;
        Path appointmentGroupsPath = Paths.get(Main.class.getResource("").getPath() + "appointmentGroups.xml");
        boolean isAppointmentGroupsNew = (appointmentGroupsPath.toFile().exists() && ! appointmentGroupsPath.toFile().isDirectory());
        if (! isAppointmentGroupsNew)
        { // add saved appointment groups
            appointmentGroups = AppointmentIO.readAppointmentGroups(appointmentGroupsPath.toFile());
            data.setAppointmentGroups(appointmentGroups);
        } else {
            // else leave defaults from Agenda
        }
        Path appointmentRepeatsPath = Paths.get(Main.class.getResource("").getPath() + "appointmentRepeats.xml");
//        boolean isAppointmentRepeatsNew = (appointmentRepeatsPath.toFile().exists() && ! appointmentRepeatsPath.toFile().isDirectory());
//        if (isAppointmentRepeatsNew)
//        { // add hard-coded repeats
//            data.getRepeats().add(e)
//        }
        MyRepeat.readFromFile(appointmentRepeatsPath, data.getAppointmentGroups(), data.getRepeats());
        MyAppointment.setupRepeats(data.getRepeats()); // must be done before appointments are read
        Path appointmentsPath = Paths.get(Main.class.getResource("").getPath() + "appointments.xml");
//        boolean isAppointmentsNew = (appointmentsPath.toFile().exists() && ! appointmentsPath.toFile().isDirectory());
//        if (isAppointmentsNew)
//        { // add hard-coded appointments
//            
//        }

        AppointmentFactory.readFromFile(appointmentsPath, data.getAppointmentGroups(), data.getAppointments());
//        MyAppointment.readFromFile(appointmentsPath.toFile(), appointmentGroups, data.getAppointments());
        data.getRepeats().stream().forEach(a -> a.collectAppointments(data.getAppointments())); // add individual appointments that have repeat rules to their Repeat objects
        data.getRepeats().stream().forEach(a -> a.makeAppointments(data.getAppointments())); // Make repeat appointments
 
        // TODO - ADD SOME HARD CODED REPEATS AND APPOINTMENTS IF NONE FROM FILE
        
        // ROOT PANE
        FXMLLoader mainLoader = new FXMLLoader();
        mainLoader.setLocation(Main.class.getResource("view/Calendar.fxml"));
        BorderPane root = mainLoader.load();
        CalendarController controller = mainLoader.getController();
        controller.setupData(data);

        Scene scene = new Scene(root, 1366, 768);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Repeatable Agenda Demo");
        primaryStage.show();
    }
	
}
