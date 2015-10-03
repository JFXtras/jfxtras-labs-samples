package jfxtras.labs.samples.repeatagenda.internal.scene.control.skin.agenda.base24hour;

import java.io.IOException;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import jfxtras.labs.samples.repeatagenda.Main;
import jfxtras.labs.samples.repeatagenda.internal.scene.control.skin.agenda.base24hour.controller.AppointmentEditController;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.Agenda.Appointment;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.Settings;
import jfxtras.util.NodeUtil;

/**
 * @author modified by David Bal - came with JFxtras Agenda
 *
 * Control for editing appointment and repeat rules.  Uses FXML and AppointmentEditController.
 */
public class AppointmentMenu extends Rectangle {

    private final LayoutHelp layoutHelp;
    private Stage newStage;
    private Control appointmentMenu;
    private BooleanProperty groupNameChanged = new SimpleBooleanProperty(false);
    
	/**
	 * 
	 * @param pane
	 * @param appointment
	 * @param repeat 
	 * @param layoutHelp
	 */
	public AppointmentMenu(Pane pane
	        , Appointment appointment
	        , LayoutHelp layoutHelp) {
		this.pane = pane;
		this.appointment = appointment;
		this.layoutHelp = layoutHelp;
		
		// layout
		setX(NodeUtil.snapXY(layoutHelp.paddingProperty.get()));
		setY(NodeUtil.snapXY(layoutHelp.paddingProperty.get()));
		setWidth(6);
		setHeight(3);
		
		// style
		getStyleClass().add("MenuIcon");
		
		// mouse
		layoutHelp.setupMouseOverAsBusy(this);
		setupMouseClick();
	}
	final Pane pane;
	final Appointment appointment;

	/**
	 * 
	 */
	private void setupMouseClick() {
		setOnMousePressed((mouseEvent) -> {
			mouseEvent.consume();
		});
		setOnMouseReleased((mouseEvent) -> {
			mouseEvent.consume();
		});
		setOnMouseClicked( (mouseEvent) -> {
			mouseEvent.consume();
			showMenu(mouseEvent);
		});
	}
	/**
	 * 
	 * @param mouseEvent
	 */
	public void showMenu(MouseEvent mouseEvent) {
		// has the client done his own popup?
		Callback<Appointment, Void> lEditCallback = layoutHelp.skinnable.getEditAppointmentCallback();
		if (lEditCallback != null) {
			lEditCallback.call(appointment);
			return;
		}
		
		// New stage for popup window
        newStage = new Stage();
        newStage.setTitle(AppointmentUtilities.makeAppointmentName(appointment));
        newStage.initModality(Modality.APPLICATION_MODAL);
		
        // LOAD FXML
        FXMLLoader appointmentMenuLoader = new FXMLLoader();
        appointmentMenuLoader.setLocation(Main.class.getResource("internal/scene/control/skin/agenda/base24hour/view/AppointmentEdit.fxml"));
        appointmentMenuLoader.setResources(Settings.resources);
        try {
            appointmentMenu = appointmentMenuLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        AppointmentEditController appointmentEditController = appointmentMenuLoader.getController();
        appointmentEditController.setupData(appointment, layoutHelp);
        Scene scene2 = new Scene(appointmentMenu);
        
        // data element change bindings
        groupNameChanged.bindBidirectional(appointmentEditController.groupNameChangedProperty());

        // listen for close event
        appointmentEditController.closeTypeProperty().addListener(
                (observable, oldSelection, newSelection) -> newStage.close());
        // when popup closes write changes if occurred
        newStage.setOnHidden( (windowEvent) -> {
            appointmentEditController.getRepeatableController().removeRepeatBindings();

            switch (appointmentEditController.getCloseType())
            {
            case CLOSE_WITH_CHANGE:
                if (groupNameChanged.getValue()) {    // write group name changes
                    System.out.println("group change write needed");
                    AppointmentIO.writeAppointmentGroups(layoutHelp.skinnable.appointmentGroups(), Settings.APPOINTMENT_GROUPS_FILE);
                }
                break;
            default: // restore previous state
                appointmentEditController.getAppointmentOld().copyInto(appointment);
                if (appointmentEditController.getAppointmentOld().hasRepeat())
                { // restore previous repeat
                    appointment.getRepeat().unbindAll();
                    appointmentEditController.getAppointmentOld().getRepeat().copyInto(appointment.getRepeat());
                } else {
                    System.out.println("null repeat");
                    appointment.setRepeat(null);
                }
                break;
            }
            layoutHelp.skin.setupAppointments();    // refresh appointment graphics
        });
        
        newStage.setScene(scene2);
        // show it just below the menu icon
        newStage.setX(NodeUtil.screenX(pane));
        newStage.setY(NodeUtil.screenY(pane));
        newStage.show();

	}
}