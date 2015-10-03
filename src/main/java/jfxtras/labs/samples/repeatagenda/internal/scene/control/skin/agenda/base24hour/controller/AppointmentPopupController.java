package jfxtras.labs.samples.repeatagenda.internal.scene.control.skin.agenda.base24hour.controller;


import java.util.ResourceBundle;

import javax.xml.parsers.ParserConfigurationException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Popup;
import jfxtras.labs.samples.repeatagenda.internal.scene.control.skin.agenda.base24hour.AppointmentMenu;
import jfxtras.labs.samples.repeatagenda.internal.scene.control.skin.agenda.base24hour.AppointmentUtilities;
import jfxtras.labs.samples.repeatagenda.internal.scene.control.skin.agenda.base24hour.LayoutHelp;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.RepeatableAppointmentUtilities;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.Agenda.Appointment;

/**
 * @author David Bal
 *
 */
public class AppointmentPopupController {
    
    private Pane pane;
    private Appointment appointment;
    private LayoutHelp layoutHelp;
    private Popup popup;
    
    @FXML private ResourceBundle resources; // ResourceBundle that was given to the FXMLLoader

    @FXML private Button editAppointmentButton;
    @FXML private Button deleteAppointmentButton;
    @FXML private Button attendanceButton;
    @FXML private Label appointmentTimeLabel;
    @FXML private TextField nameTextField;

    @FXML public void initialize() {
   }

    public void setupData(Pane pane
            , Appointment appointment
            , LayoutHelp layoutHelp
            , Popup popup) {

        this.pane = pane;
        this.appointment = appointment;
        this.layoutHelp = layoutHelp;
        this.popup = popup;
        
        appointmentTimeLabel.setText(AppointmentUtilities.makeAppointmentTime(appointment));
        nameTextField.setText(appointment.getSummary());
        nameTextField.textProperty().addListener((observable, oldValue, newValue) ->  {
            appointment.setSummary(newValue);
        });
    }
    
    @FXML private void handleEditAppointment() {
        AppointmentMenu appointmentMenu = new AppointmentMenu(pane, appointment, layoutHelp);
        appointmentMenu.showMenu(null);
    }

    @FXML private void handleDeleteAppointment() throws ParserConfigurationException {
        popup.hide();
        RepeatableAppointmentUtilities.deleteAppointments(layoutHelp.skinnable.appointments()
                , appointment
                , layoutHelp.skinnable.repeats()
                , resources);
        layoutHelp.skin.setupAppointments();    // refresh appointment graphics
    }
    
}
