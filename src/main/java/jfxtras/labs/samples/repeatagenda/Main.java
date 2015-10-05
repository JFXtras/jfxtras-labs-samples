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
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.Agenda.AppointmentGroup;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.AppointmentFactory;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.Settings;

public class Main extends Application {
	
    public MyData data = new MyData();  // All data - appointments, styles, members, etc.
    
    public static void main(String[] args) {
        launch(args);       
    }

	@Override
	public void start(Stage primaryStage) throws IOException, TransformerException, ParserConfigurationException, SAXException {
	    
        Locale myLocale = Locale.getDefault();
        ResourceBundle resources = ResourceBundle.getBundle("jfxtras.labs.samples.repeatagenda.Bundle", myLocale);
        Settings.setup(resources);

        // I/O and setup
        Path appointmentGroupsPath = Paths.get(Main.class.getResource("").getPath() + "appointmentGroups.xml");
        ObservableList<AppointmentGroup> appointmentGroups = AppointmentIO.readAppointmentGroups(appointmentGroupsPath.toFile());
        data.setAppointmentGroups(appointmentGroups);
        Path appointmentRepeatsPath = Paths.get(Main.class.getResource("").getPath() + "appointmentRepeats.xml");
        MyRepeat.readFromFile(appointmentRepeatsPath, appointmentGroups, data.getRepeats());
        MyAppointment.setupRepeats(data.getRepeats()); // must be done before appointments are read
//        AppointmentFactory.setupRepeats(data.getRepeats()); // must be done before appointments are read
        Path appointmentsPath = Paths.get(Main.class.getResource("").getPath() + "appointments.xml");
        AppointmentFactory.readFromFile(appointmentsPath, appointmentGroups, data.getAppointments());
//        MyAppointment.readFromFile(appointmentsPath.toFile(), appointmentGroups, data.getAppointments());
        data.getRepeats().stream().forEach(a -> a.collectAppointments(data.getAppointments())); // add individual appointments that have repeat rules to their Repeat objects
        data.getRepeats().stream().forEach(a -> a.makeAppointments(data.getAppointments())); // Make repeat appointments
//        System.out.println(data.getAppointments().size());	    
//        System.out.println(data.getRepeats().size());
//        System.exit(0);;
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
