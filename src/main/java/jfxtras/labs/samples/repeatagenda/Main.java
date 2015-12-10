package jfxtras.labs.samples.repeatagenda;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.Locale;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.RepeatableAppointmentImpl;
import jfxtras.labs.samples.repeatagenda.controller.CalendarController;
import jfxtras.scene.control.agenda.Agenda.Appointment;
import jfxtras.scene.control.agenda.Agenda.LocalDateTimeRange;

public class Main extends Application {
	
//    private static ObservableList<AppointmentGroup> appointmentGroups = RepeatableAgenda.DEFAULT_APPOINTMENT_GROUPS;
    
    public final static Callback<LocalDateTimeRange, Appointment> NEW_APPOINTMENT_CALLBACK = range -> 
    {
        return new RepeatableAppointmentImpl()
                .withStartLocalDateTime(range.getStartLocalDateTime())
                .withEndLocalDateTime(range.getEndLocalDateTime());        
    };
    
    private static LocalDate firstDayOfWeekLocalDate = getFirstDayOfWeekLocalDate();
    private static LocalDate getFirstDayOfWeekLocalDate()
    { // copied from AgendaWeekSkin
        Locale.setDefault(Locale.US);
        Locale myLocale = Locale.getDefault();
        WeekFields lWeekFields = WeekFields.of(myLocale);
        int lFirstDayOfWeek = lWeekFields.getFirstDayOfWeek().getValue();
        LocalDate lDisplayedDateTime = LocalDate.now();
        int lCurrentDayOfWeek = lDisplayedDateTime.getDayOfWeek().getValue();

        if (lFirstDayOfWeek <= lCurrentDayOfWeek) {
            lDisplayedDateTime = lDisplayedDateTime.plusDays(-lCurrentDayOfWeek + lFirstDayOfWeek);
        }
        else {
            lDisplayedDateTime = lDisplayedDateTime.plusDays(-lCurrentDayOfWeek - (7-lFirstDayOfWeek));
        }
        
        return lDisplayedDateTime;
    }

    public MyData data = new MyData();  // All data - appointments, styles, members, etc.
    
    public static void main(String[] args) {
        launch(args);       
    }

	@Override
	public void start(Stage primaryStage) throws IOException, TransformerException, ParserConfigurationException, SAXException
	{

//	    ICalendarEditTest r = new ICalendarEditTest();
//	    long s1 = System.currentTimeMillis();
//	    for (int i=0; i<1000; i++) {
//	    r.editFutureTimeAndDateDaily2();
//	    }
//        long s2 = System.currentTimeMillis();
//        System.out.println("time " + (s2-s1));
//        
//	    System.exit(0);
	            
        // ROOT PANE
        FXMLLoader mainLoader = new FXMLLoader();
        mainLoader.setLocation(Main.class.getResource("view/Calendar.fxml"));
        BorderPane root = mainLoader.load();
        CalendarController controller = mainLoader.getController();
        controller.setupData(data, firstDayOfWeekLocalDate, firstDayOfWeekLocalDate.plusDays(7));

        Scene scene = new Scene(root, 1366, 768);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Repeatable Agenda Demo");
        primaryStage.show();
	    
        // TODO - I/O AFTER SETUP
//        // I/O and setup
//        ObservableList<AppointmentGroup> appointmentGroups = null;
//        Path appointmentGroupsPath = Paths.get(Main.class.getResource("").getPath() + "appointmentGroups.xml");
//        boolean isAppointmentGroupsNew = (appointmentGroupsPath.toFile().exists() && ! appointmentGroupsPath.toFile().isDirectory());
//        if (! isAppointmentGroupsNew)
//        { // add saved appointment groups
//            appointmentGroups = AppointmentIO.readAppointmentGroups(appointmentGroupsPath.toFile());
//            data.setAppointmentGroups(appointmentGroups);
//        } else {
//            // else leave defaults from Agenda
//        }
//        Path appointmentRepeatsPath = Paths.get(Main.class.getResource("").getPath() + "appointmentRepeats.xml");
////        boolean isAppointmentRepeatsNew = (appointmentRepeatsPath.toFile().exists() && ! appointmentRepeatsPath.toFile().isDirectory());
////        if (isAppointmentRepeatsNew)
////        { // add hard-coded repeats
////            data.getRepeats().add(e)
////        }
//        RepeatImpl.readFromFile(appointmentRepeatsPath, data.appointmentGroups(), data.getVComponents());
////        RepeatableAppointmentImpl.setupRepeats(data.getRepeats()); // must be done before appointments are read
//        Path appointmentsPath = Paths.get(Main.class.getResource("").getPath() + "appointments.xml");
////        boolean isAppointmentsNew = (appointmentsPath.toFile().exists() && ! appointmentsPath.toFile().isDirectory());
////        if (isAppointmentsNew)
////        { // add hard-coded appointments
////            
////        }
//
/////        AppointmentFactory.readFromFile(appointmentsPath, data.getAppointmentGroups(), data.getAppointments());
//        RepeatableAppointmentImpl.readFromFile(appointmentsPath.toFile(), data.appointmentGroups(), data.getAppointments());
////        MyAppointment.readFromFile(appointmentsPath.toFile(), appointmentGroups, data.getAppointments());
//        data.getVComponents().stream().forEach(a -> a.collectAppointments(data.getAppointments())); // add individual appointments that have repeat rules to their Repeat objects
//        // ADD APPOINTMENTS WHEN AGENDA TIME UPDATES
////        data.getRepeats().stream().forEach(a ->
////        {
////            Collection<RepeatableAppointment> appointments = a.makeAppointments(LocalDate.now().minusWeeks(1), LocalDate.now().plusWeeks(1)); // Make repeat appointments (using default of one week before and one week after today - TODO - get dates from Agenda and make exact initial appointments)
////            data.getAppointments().addAll(appointments);
////        });


        
    }
}
