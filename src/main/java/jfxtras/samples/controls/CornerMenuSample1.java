package jfxtras.samples.controls;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import jfxtras.labs.scene.control.CornerMenu;
import jfxtras.samples.JFXtrasSampleBase;
import jfxtras.scene.control.ListSpinner;
import jfxtras.scene.layout.GridPane;
import jfxtras.util.PlatformUtil;

import org.controlsfx.dialog.Dialogs;

public class CornerMenuSample1 extends JFXtrasSampleBase
{
    /**
     *
     */
    public CornerMenuSample1() {
	}
	final private MenuItem facebookMenuItem = registerAction(new MenuItem("Facebook", new ImageView(new Image(this.getClass().getResourceAsStream("social_facebook_button_blue.png")))));
	final private MenuItem googleMenuItem = registerAction(new MenuItem("Google", new ImageView(new Image(this.getClass().getResourceAsStream("social_google_button_blue.png")))));
	final private MenuItem skypeMenuItem = registerAction(new MenuItem("Skype", new ImageView(new Image(this.getClass().getResourceAsStream("social_skype_button_blue.png")))));
	final private MenuItem twitterMenuItem = registerAction(new MenuItem("Twitter", new ImageView(new Image(this.getClass().getResourceAsStream("social_twitter_button_blue.png")))));
	final private MenuItem windowsMenuItem = registerAction(new MenuItem("Windows", new ImageView(new Image(this.getClass().getResourceAsStream("social_windows_button.png")))));

    private MenuItem registerAction(MenuItem menuItem) {
    	menuItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
            	 Dialogs.create().masthead("Click").message( "You clicked the " + menuItem.getText() + " icon").showInformation();
            }
        });
    	return menuItem;
    }
    
	/**
     *
     * @return
     */
    @Override
    public String getSampleName() {
        return this.getClass().getSimpleName();
    }

    /**
     *
     * @return
     */
    @Override
    public String getSampleDescription() {
        return "Basic CornerMenu usage; CornerMenu uses CircularPane to render a menu that can be placed in the corner of a screen.";
    }

    /**
     *
     * @param stage
     * @return
     */
    @Override
    public Node getPanel(Stage stage) {
    	stackPane.getChildren().add(new Label("Some stuff that is visible on the pane"));
    	return stackPane;
    }
    StackPane stackPane = new StackPane();
    private CornerMenu cornerMenu = new CornerMenu(CornerMenu.Orientation.TOP_LEFT);

    @Override
    public Node getControlPanel() {
        // the result
        GridPane lGridPane = new GridPane();
        lGridPane.setVgap(2.0);
        lGridPane.setHgap(2.0);

        // setup the grid so all the labels will not grow, but the rest will
        ColumnConstraints lColumnConstraintsAlwaysGrow = new ColumnConstraints();
        lColumnConstraintsAlwaysGrow.setHgrow(Priority.ALWAYS);
        ColumnConstraints lColumnConstraintsNeverGrow = new ColumnConstraints();
        lColumnConstraintsNeverGrow.setHgrow(Priority.NEVER);
        lGridPane.getColumnConstraints().addAll(lColumnConstraintsNeverGrow, lColumnConstraintsAlwaysGrow);
        int lRowIdx = 0;

        // Location
        {
            lGridPane.add(new Label("Location"), new GridPane.C().row(lRowIdx).col(0).halignment(HPos.RIGHT));
            orientationChoiceBox.getSelectionModel().select(CornerMenu.Orientation.TOP_LEFT);
            lGridPane.add(orientationChoiceBox, new GridPane.C().row(lRowIdx).col(1));
            orientationChoiceBox.valueProperty().addListener( (observable) -> {
            	createCornerMenu();
            });
        	createCornerMenu();
        }
        lRowIdx++;

        // shown
        {
            Label lLabel = new Label("Shown");
            lGridPane.add(lLabel, new GridPane.C().row(lRowIdx).col(0).halignment(HPos.RIGHT));
            
            shownCheckBox.setTooltip(new Tooltip("When reaching the last in the list, cycle back to the first"));
            lGridPane.add(shownCheckBox, new GridPane.C().row(lRowIdx).col(1));
			shownCheckBox.selectedProperty().addListener( (observable) -> {
				cornerMenu.setShown(shownCheckBox.selectedProperty().get());
            });
        }
        lRowIdx++;
        
        // done
        return lGridPane;
    }
    private ChoiceBox<CornerMenu.Orientation> orientationChoiceBox =  new ChoiceBox<CornerMenu.Orientation>(FXCollections.observableArrayList(CornerMenu.Orientation.values()));;
    CheckBox shownCheckBox = new CheckBox();
    
    private void createCornerMenu() {
    	// uninstall the current cornerMenu
    	if (cornerMenu != null) {
    		cornerMenu.install(null);
    	}
    	shownCheckBox.selectedProperty().set(true);
    	
    	// create a new one
    	cornerMenu = new CornerMenu(orientationChoiceBox.getValue());
    	//cornerMenu.setStyle("-fx-border-color: red;");
		if (CornerMenu.Orientation.TOP_LEFT.equals(cornerMenu.getOrientation())) {
	    	cornerMenu.getItems().addAll(facebookMenuItem, googleMenuItem, skypeMenuItem, twitterMenuItem, windowsMenuItem);
		}
		else if (CornerMenu.Orientation.TOP_RIGHT.equals(cornerMenu.getOrientation())) {
	    	cornerMenu.getItems().addAll(facebookMenuItem, googleMenuItem, skypeMenuItem, twitterMenuItem);
		}
		else if (CornerMenu.Orientation.BOTTOM_RIGHT.equals(cornerMenu.getOrientation())) {
	    	cornerMenu.getItems().addAll(facebookMenuItem, googleMenuItem, skypeMenuItem);
		}
		else if (CornerMenu.Orientation.BOTTOM_LEFT.equals(cornerMenu.getOrientation())) {
	    	cornerMenu.getItems().addAll(facebookMenuItem, googleMenuItem);
		}
    	cornerMenu.install(stackPane);
    }
    
    /**
     *
     * @return
     */
    @Override
    public String getJavaDocURL() {
		return "http://jfxtras.org/doc/8.0/jfxtras-controls/" + ListSpinner.class.getName().replace(".", "/") + ".html";
    }

    public static void main(String[] args) {
        launch(args);
    }
}