package jfxtras.labs.samples.repeatagenda.internal.scene.control.skin.agenda.base24hour;

import java.time.LocalDateTime;
import java.util.ListIterator;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Popup;
import javafx.util.Callback;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.AgendaMine.Appointment;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.AppointmentFactory;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.Settings;
import jfxtras.scene.control.ImageViewButton;
import jfxtras.scene.control.LocalDateTimeTextField;
import jfxtras.util.NodeUtil;

/**
 * 
 * @author david
 * Modified from jfxtras.labs.samples.repeatagenda.
 * Changed to operate more like Google Calendar
 * 
 * When new appointment is added by drag, a popup immediately appears.
 * Closing the popup by clicking on the X cancels the appointment
 *
 */

//TODO: CONVERT TO FXML
public class AppointmentMenuSmall extends Rectangle {

	/**
	 * 
	 * @param pane
	 * @param appointment
	 * @param layoutHelp
	 */
	public AppointmentMenuSmall(Pane pane, Appointment appointment, LayoutHelp layoutHelp) {
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
	final LayoutHelp layoutHelp;
    private boolean writeAppointment = true;

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
	void showMenu(MouseEvent mouseEvent) {
		// has the client done his own popup?
        Callback<Appointment, Void> lEditCallback = layoutHelp.skinnable.getEditAppointmentCallback();
        if (lEditCallback != null) {
//            AppointmentEditData data = new AppointmentEditData(appointment, layoutHelp, pane);
            lEditCallback.call(appointment);
            return;
        }

		// only if not already showing
		if (popup != null && popup.isShowing()) {
			return;
		}
		
		// create popup
		popup = new Popup();
		popup.setAutoFix(true);
		popup.setAutoHide(true);
		popup.setHideOnEscape(true);
		popup.setOnHidden( (windowEvent) -> {
			layoutHelp.skin.setupAppointments();
		});

		// popup contents
		BorderPane lBorderPane = new BorderPane() {
			// As of 1.8.0_40 CSS files are added in the scope of a control, the popup does not fall under the control, so the stylesheet must be reapplied 
			// When JFxtras is based on 1.8.0_40+: @Override 
			public String getUserAgentStylesheet() {
				return layoutHelp.skinnable.getUserAgentStylesheet();
			}
		};
        lBorderPane.getStyleClass().add("Agenda" + "Popup");
		popup.getContent().add(lBorderPane);

		// close icon
		lBorderPane.setRight(createCloseIcon());
		
		// initial layout
		VBox lVBox = new VBox(layoutHelp.paddingProperty.get());
		lVBox.setSpacing(10);
		lBorderPane.setCenter(lVBox);

		// start and end
		String start = Settings.DATE_FORMAT_AGENDA_START.format(appointment.getStartLocalDateTime());
        String end = Settings.DATE_FORMAT_AGENDA_END.format(appointment.getEndLocalDateTime());
		lVBox.getChildren().add(new Label("Time: " + start + end + " "));
		
		// summary
		HBox whatBox = new HBox();
		whatBox.setAlignment(Pos.CENTER_LEFT);
		Label nameLabel = new Label("Name: ");
		nameLabel.setPrefWidth(70);
		whatBox.getChildren().add(nameLabel);
		whatBox.getChildren().add(createSummaryTextField());
        lVBox.getChildren().add(whatBox);

        // Style
        HBox styleBox = new HBox();
        styleBox.setAlignment(Pos.CENTER_LEFT);
        Label styleLabel = new Label("Style: ");
        styleLabel.setPrefWidth(70);
        styleBox.getChildren().add(styleLabel);
        lVBox.getChildren().add(styleBox);
		
        final AppointmentGroupGridPane appointmentGroupGridPane
            = new AppointmentGroupGridPane(appointment, layoutHelp.skinnable.appointmentGroups());
        lVBox.getChildren().add(appointmentGroupGridPane);
        
		Button saveButton = new Button(Settings.resources.getString("button.save"));
        Button advancedEditButton = new Button(Settings.resources.getString("button.add.more.details"));
        HBox buttonBox = new HBox();
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setSpacing(10.0);
        buttonBox.getChildren().addAll(saveButton, advancedEditButton);
        lVBox.getChildren().add(buttonBox);
        
        saveButton.setOnAction((event) -> {
            popup.hide();
            if (writeAppointment ) AppointmentFactory.writeToFile(layoutHelp.skinnable.appointments());
        });

        advancedEditButton.setOnAction((event) -> {
            popup.hide();
            if (writeAppointment ) AppointmentFactory.writeToFile(layoutHelp.skinnable.appointments());
            AppointmentMenu appointmentMenu = new AppointmentMenu(pane, appointment, layoutHelp);
            appointmentMenu.showMenu(mouseEvent);
        });
		
		// show it just below the menu icon
		popup.show(pane, NodeUtil.screenX(pane), NodeUtil.screenY(pane));
        

        popup.setOnAutoHide((event) -> {
                if (writeAppointment ) AppointmentFactory.writeToFile(layoutHelp.skinnable.appointments());
//                layoutHelp.skinnable.setWriteAppointmentNeeded(true);
            }
        );
        
	}
	private Popup popup;
	
	/**
	 * 
	 * @param popup
	 * @return
	 */
	private ImageViewButton createCloseIcon() {
		closeIconImageView = new ImageViewButton();
		closeIconImageView.getStyleClass().add("close-icon");
		closeIconImageView.setPickOnBounds(true);
		closeIconImageView.setOnMouseClicked( (mouseEvent2) -> {
			popup.hide();
			int s = layoutHelp.skinnable.appointments().size();
	        ListIterator<? extends Appointment> i = layoutHelp.skinnable.appointments().listIterator(s);
            while (i.hasPrevious()) {
                Appointment a = i.previous();
                if (a == appointment) {
                    i.remove();
                    break;
                }
            }
			// refresh
		});
		return closeIconImageView;
	}
	private ImageViewButton closeIconImageView = null;

	/**
	 * 
	 * @return
	 */
	private LocalDateTimeTextField createEndTextField() {
		endTextField = new LocalDateTimeTextField();
		endTextField.setLocale(layoutHelp.skinnable.getLocale());
		endTextField.setLocalDateTime(appointment.getEndLocalDateTime());
		endTextField.setVisible(appointment.getEndLocalDateTime() != null);

		endTextField.localDateTimeProperty().addListener( (observable, oldValue, newValue) ->  {
			appointment.setEndLocalDateTime(newValue);
			// refresh is done upon popup close
		});

		return endTextField;
	}
	private LocalDateTimeTextField endTextField = null;

	/**
	 * 
	 * @return
	 */
	private CheckBox createWholedayCheckbox() {
		wholedayCheckBox = new CheckBox("Wholeday");
		wholedayCheckBox.setId("wholeday-checkbox");
		wholedayCheckBox.selectedProperty().set(appointment.isWholeDay());

		wholedayCheckBox.selectedProperty().addListener( (observable, oldValue, newValue) ->  {
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
			// refresh is done upon popup close
		});
		
		return wholedayCheckBox;
	}
	private CheckBox wholedayCheckBox = null;

	/**
	 * 
	 * @return
	 */
	private TextField createSummaryTextField() {
		summaryTextField = new TextField();
		summaryTextField.setText(appointment.getSummary());
		summaryTextField.textProperty().addListener( (observable, oldValue, newValue) ->  {
			appointment.setSummary(newValue);
			// refresh is done upon popup close
		});
		return summaryTextField;
	}
	private TextField summaryTextField = null;

	/**
	 * 
	 * @return
	 */
	private ImageViewButton createActionButton(String styleClass, String tooltipText) {
		ImageViewButton lImageViewButton = new ImageViewButton();
		lImageViewButton.getStyleClass().add(styleClass);
		lImageViewButton.setPickOnBounds(true);
		Tooltip.install(lImageViewButton, new Tooltip(tooltipText)); 
		return lImageViewButton;
	}

}