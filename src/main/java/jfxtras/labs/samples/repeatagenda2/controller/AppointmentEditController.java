package jfxtras.labs.samples.repeatagenda2.controller;


import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.util.Callback;
import jfxtras.labs.repeatagenda.internal.scene.control.skin.repeatagenda.base24hour.AppointmentGroupGridPaneOld;
import jfxtras.labs.repeatagenda.internal.scene.control.skin.repeatagenda.base24hour.RepeatPaneLoader;
import jfxtras.labs.repeatagenda.internal.scene.control.skin.repeatagenda.base24hour.controller.RepeatableControllerOld;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.Repeat;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.RepeatableAgenda.AppointmentFactory;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.RepeatableAgenda.RepeatableAppointment;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.RepeatableUtilities;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.RepeatableUtilities.WindowCloseType;
import jfxtras.labs.samples.repeatagenda2.RepeatableAppointmentImpl;
import jfxtras.scene.control.LocalDateTimeTextField;
import jfxtras.scene.control.agenda.Agenda.Appointment;
import jfxtras.scene.control.agenda.Agenda.AppointmentGroup;
import jfxtras.scene.control.agenda.Agenda.LocalDateTimeRange;

public class AppointmentEditController {
    
    private RepeatableAppointmentImpl appointment;
    private RepeatableAppointmentImpl appointmentOld;
//    public RepeatableAppointmentImpl getAppointmentOld() { return appointmentOld; }
    private Collection<Appointment> appointments;
    private Collection<Repeat> repeats;
//    private Map<Appointment, Repeat> repeatMap;
    private Callback<Collection<Appointment>, Void> appointmentWriteCallback;
    private Callback<Collection<Repeat>, Void> repeatWriteCallback;
    private Callback<Void, Void> refreshCallback;

    // Change properties
    // TODO - make iterator containing collection of changed appointments 
    private BooleanProperty groupNameEdited = new SimpleBooleanProperty(false);
    public BooleanProperty groupNameEditedProperty() { return groupNameEdited; }
//    private BooleanProperty appointmentEdited = new SimpleBooleanProperty(false);
//    public BooleanProperty appointmentEditedProperty() { return appointmentEdited; }
//    private BooleanProperty repeatEdited = new SimpleBooleanProperty(false);
//    public BooleanProperty repeatEditedProperty() { return repeatEdited; }

    private ObjectProperty<WindowCloseType> closeType = new SimpleObjectProperty<WindowCloseType>(WindowCloseType.X); // default to X, meaning click on X to close window
    public ObjectProperty<WindowCloseType> closeTypeProperty() { return closeType; }
    private void setCloseType(WindowCloseType value) { closeType.set(value); }
    public WindowCloseType getCloseType() { return closeType.getValue(); }
    
    @FXML private ResourceBundle resources; // ResourceBundle that was given to the FXMLLoader
    
//    @FXML private LocalDateTimeTextField startTextField;
//    @FXML private LocalDateTimeTextField endTextField;
    @FXML private LocalDateTimeTextField startTextField; // DTSTART
    @FXML private LocalDateTimeTextField endTextField; // DTEND
    @FXML private AppointmentGroupGridPaneOld appointmentGroupGridPane;
    
    @FXML private TextField customTextField;
    @FXML private TextField locationTextField; // LOCATION
    @FXML private TextField summaryTextField; // SUMMARY
    @FXML private TextArea descriptionTextArea; // DESCRIPTION
    @FXML private CheckBox wholeDayCheckBox;
    @FXML private TextField groupTextField; // TODO - Should this be an X-PROP or in CATEGORIES - I'm think CATEGORIES
    @FXML private Button closeAppointmentButton;
    @FXML private Button cancelAppointmentButton;
    @FXML private Button closeRepeatButton;
    @FXML private Button cancelRepeatButton;
    @FXML private Button saveAdvancedButton;
    
    @FXML private AnchorPane repeatable; // repeat rule options

    // Controller for repeat rules
    private RepeatableControllerOld repeatableController;
    public RepeatableControllerOld getRepeatableController() { return repeatableController; }    

    @FXML public void initialize() {
        
        wholeDayCheckBox.selectedProperty().addListener((observable, oldSelection, newSelection) ->  {
            endTextField.setDisable(newSelection);
            endTextField.setLocalDateTime(startTextField.getLocalDateTime());
        });
    }
    
//    ChangeListener<? super LocalDateTime> startDateListener = (observable, oldValue, newValue)
//            -> appointment.setStartLocalDateTime(newValue);

    // Setup up data for controls
    public void setupData(RepeatableAppointmentImpl inputAppointment
            , LocalDateTimeRange dateTimeRange
            , Collection<Appointment> appointments
            , Collection<Repeat> repeats
//            , Map<Appointment, Repeat> repeatMap
            , List<AppointmentGroup> appointmentGroups
            , Class<? extends RepeatableAppointment> appointmentClass
            , Class<? extends Repeat> repeatClass
            , Callback<Collection<Appointment>, Void> appointmentWriteCallback
            , Callback<Collection<Repeat>, Void> repeatWriteCallback
            , Callback<Void, Void> refreshCallback)
    {
//        this.layoutHelp = layoutHelp;
        Locale locale = Locale.getDefault();
        this.appointment = inputAppointment;
        this.appointments = appointments;
        this.repeats = repeats;
//        this.repeatMap = repeatMap;
        this.appointmentWriteCallback = appointmentWriteCallback;
        this.repeatWriteCallback = repeatWriteCallback;
        this.refreshCallback = refreshCallback;
//        repeats = layoutHelp.skinnable.repeats();
//        appointments = layoutHelp.skinnable.appointments();

        appointmentOld = (RepeatableAppointmentImpl) AppointmentFactory.newAppointment(appointmentClass);
        System.out.println("appointmentOld new " + appointmentOld);
//        appointmentOld = (RepeatableAppointment) newAppointmentCallback
//                .call(new LocalDateTimeRange(appointment.getStartLocalDateTime(), appointment.getEndLocalDateTime()));
        appointment.copyFieldsTo(appointmentOld);
//        appointmentOld = newAppointmentCallback.call(param)// AppointmentFactory.newAppointment(appointment);

        // Get repeat pane and add to repeatable AnchorPane (not needed if using default popup)
        FXMLLoader repeatPaneLoader = RepeatPaneLoader.newInstance();
        Pane repeatPane = null;
        try {
            repeatPane = repeatPaneLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        repeatable.getChildren().add(repeatPane);
        repeatableController = repeatPaneLoader.getController();
        repeatableController.setupData(appointment, dateTimeRange, appointmentClass, repeatClass);

        summaryTextField.setText(appointment.getSummary());
        summaryTextField.textProperty().addListener((observable, oldValue, newValue) ->  {
            appointment.setSummary(newValue);
        });

        locationTextField.setText(appointment.getLocation());
        locationTextField.textProperty().addListener((observable, oldValue, newValue) ->  {
            appointment.setLocation(newValue);
        });

        customTextField.setText(appointment.getCustom());
        customTextField.textProperty().addListener((observable, oldValue, newValue) ->  {
            appointment.setCustom(newValue);
        });
        
        descriptionTextArea.setText(appointment.getDescription());
        descriptionTextArea.textProperty().addListener((obs, oldValue, newValue) ->  appointment.setDescription(newValue));
        
        // START DATE TIME TEXT FIELD
        startTextField.setLocale(locale);
        startTextField.setLocalDateTime(appointment.getStartLocalDateTime());
        startTextField.setParseErrorCallback( (throwable) -> {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Invalid Date or Time");
            alert.setContentText("Please enter valid date and time");
            alert.showAndWait();
            return null;
        });
        
//        startTextField.localDateTimeProperty().bindBidirectional(appointment.startLocalDateTimeProperty());
//        startTextField.localDateTimeProperty().addListener(startDateListener);
        startTextField.localDateTimeProperty().addListener((obs, oldValue, newValue) -> appointment.setStartLocalDateTime(newValue));

        // END DATE TIME TEXT FIELD
        endTextField.setLocale(locale);
        endTextField.setLocalDateTime(appointment.getEndLocalDateTime());
        endTextField.setParseErrorCallback( (throwable) -> {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Invalid Date or Time");
            alert.setContentText("Please enter valid date and time");
            alert.showAndWait();
            return null;
        });
        endTextField.setVisible(appointment.getEndLocalDateTime() != null);

        endTextField.localDateTimeProperty().addListener((obs, oldValue, newValue) ->  {
            appointment.setEndLocalDateTime(newValue);
        });
        
        // Whole day
        wholeDayCheckBox.selectedProperty().set(appointment.isWholeDay());

        wholeDayCheckBox.selectedProperty().addListener( (obs, oldValue, newValue) ->  {
            appointment.setWholeDay(newValue);
            if (newValue == true) {
                appointment.setEndLocalDateTime(null);
            }
            else {
                LocalDateTime lEndTime = appointment.getStartLocalDateTime().plusMinutes(30);
                appointment.setEndLocalDateTime(lEndTime);
                endTextField.setLocalDateTime(appointment.getEndLocalDateTime());
            }
            endTextField.setVisible(appointment.getEndLocalDateTime() != null);
        });
        
        appointmentGroupGridPane.setupData(appointment, appointmentGroups);
        
        // store group name changes by each character typed
        appointmentGroupGridPane.appointmentGroupSelectedProperty().addListener(
            (observable, oldSelection, newSelection) ->  {
                Integer i = appointmentGroupGridPane.getAppointmentGroupSelected();
                String newText = appointmentGroups.get(i).getDescription();
                groupTextField.setText(newText);
                groupNameEdited.set(true);
            });

        groupTextField.textProperty().addListener((observable, oldSelection, newSelection) ->  {
            int i = appointmentGroupGridPane.getAppointmentGroupSelected();
            appointmentGroups.get(i).setDescription(newSelection);
            appointmentGroupGridPane.updateToolTip(i, appointmentGroups);
            groupNameEdited.set(true);
        });
            }

    // AFTER CLICK SAVE VERIFY REPEAT IS VALID, IF NOT PROMPT.
    @FXML private void handleCloseButton() {

        final WindowCloseType result = RepeatableUtilities.editAppointments(
                          appointment
                        , appointmentOld
                        , appointments
                        , repeats
//                        , repeatMap
                        , appointmentWriteCallback
                        , repeatWriteCallback);
        setCloseType(result);

        if (getCloseType() == WindowCloseType.CLOSE_WITH_CHANGE) {
            refreshCallback.call(null);
//            layoutHelp.skin.setupAppointments();    // refresh appointment graphics
            System.out.println("need to refresh");
            // use callback? 
        }
    }
    
    @FXML private void handleCancelButton() {
        setCloseType(WindowCloseType.CANCEL);
    }
    
    public AppointmentGroupGridPaneOld getAppointmentGroupGridPane() {
        return appointmentGroupGridPane;
    }
    
}
