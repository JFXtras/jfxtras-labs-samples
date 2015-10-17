package jfxtras.labs.samples.repeatagenda.internal.scene.control.skin.agenda.base24hour;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jfxtras.labs.samples.repeatagenda.Main;
import jfxtras.labs.samples.repeatagenda.internal.scene.control.skin.agenda.base24hour.controller.AppointmentEditController;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.Agenda.Appointment;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.Agenda.AppointmentGroup;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.Repeat;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.Settings;

// New stage for popup window
public class RepeatMenuStage extends Stage {

//    final private AppointmentEditController appointmentEditController;
//    final private LayoutHelp layoutHelp;
    private BooleanProperty groupNameChanged = new SimpleBooleanProperty(false);
    private Pane pane;
    private List<AppointmentGroup> appointmentGroups;
    private Collection<Repeat> repeats;
    private Collection<Appointment> appointments;
    private Appointment appointment;

    public RepeatMenuStage(Collection<Appointment> appointments
            , Collection<Repeat> repeats
            , List<AppointmentGroup> appointmentGroups
            , Pane pane) {
//        this.layoutHelp = layoutHelp;
        this.pane = pane;
        this.appointments = appointments;
        this.repeats = repeats;
        this.appointmentGroups = appointmentGroups;
    }
    
    public void setup(Appointment appointment)
    {
//        LayoutHelp layoutHelp = data.layoutHelp;
//        Appointment appointment = data.appointment;
//        Pane pane = data.pane;
        setTitle(AppointmentUtilities.makeAppointmentName(appointment));
        initModality(Modality.APPLICATION_MODAL);
        
        // LOAD FXML
        FXMLLoader appointmentMenuLoader = new FXMLLoader();
        appointmentMenuLoader.setLocation(Main.class.getResource("internal/scene/control/skin/agenda/base24hour/view/AppointmentEdit.fxml"));
        appointmentMenuLoader.setResources(Settings.resources);
        Control appointmentMenu = null;
        try {
            appointmentMenu = appointmentMenuLoader.load();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        AppointmentEditController appointmentEditController = appointmentMenuLoader.getController();
        appointmentEditController.setupData(appointment, appointments, repeats, appointmentGroups);
        Scene scene = new Scene(appointmentMenu);

        // data element change bindings
        groupNameChanged.bindBidirectional(appointmentEditController.groupNameChangedProperty());

        // listen for close event
        appointmentEditController.closeTypeProperty().addListener((observable, oldSelection, newSelection) -> close());

        // when popup closes write changes if occurred
        setOnHidden((windowEvent) -> 
        {
            appointmentEditController.getRepeatableController().removeRepeatBindings();

            switch (appointmentEditController.getCloseType())
            {
            case CLOSE_WITH_CHANGE:
                if (groupNameChanged.getValue()) {    // write group name changes
                    System.out.println("group change write needed");
                    AppointmentIO.writeAppointmentGroups(appointmentGroups, Settings.APPOINTMENT_GROUPS_FILE);
                }
                break;
            }
//            layoutHelp.skin.setupAppointments();    // refresh appointment graphics
            System.out.println("RepeatMenuStage refresh" ); // refresh - use callback?
        });
        
        setScene(scene);
        // show it just below the menu icon
//        setX(NodeUtil.screenX(pane));
//        setY(NodeUtil.screenY(pane));
    }

    
//    public void setAppointmentAndShow(Appointment appointment)
//    {
//        
//        
//        show();
//    }
    
    
    
    
}
 
