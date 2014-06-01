package jfxtras.samples.controls;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import jfxtras.labs.scene.control.CornerMenu;
import jfxtras.samples.JFXtrasSampleBase;
import jfxtras.scene.control.ListSpinner;
import jfxtras.scene.layout.GridPane;

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
    	createCircularPane();
        return pane;
    }
    private Pane pane = new Pane();

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
            lOrientationChoiceBox.getSelectionModel().select(CornerMenu.Orientation.TOP_LEFT);
            lGridPane.add(lOrientationChoiceBox, new GridPane.C().row(lRowIdx).col(1));
            lOrientationChoiceBox.valueProperty().addListener( (observable) -> {
            	createCircularPane();
            });
        }
        lRowIdx++;

        // done
        return lGridPane;
    }
    private ChoiceBox<CornerMenu.Orientation> lOrientationChoiceBox =  new ChoiceBox<CornerMenu.Orientation>(FXCollections.observableArrayList(CornerMenu.Orientation.values()));;
    
    private void createCircularPane() {
    	pane.getChildren().clear();
    	
    	cornerMenu = new CornerMenu();
    	cornerMenu.setOrientation(lOrientationChoiceBox.getValue());
		if (CornerMenu.Orientation.TOP_LEFT.equals(lOrientationChoiceBox.getValue())) {
		}
		else if (CornerMenu.Orientation.TOP_RIGHT.equals(lOrientationChoiceBox.getValue())) {
			cornerMenu.layoutXProperty().bind( pane.widthProperty().subtract(cornerMenu.widthProperty()));
		}
		else if (CornerMenu.Orientation.BOTTOM_RIGHT.equals(lOrientationChoiceBox.getValue())) {
			cornerMenu.layoutXProperty().bind( pane.widthProperty().subtract(cornerMenu.widthProperty()));
	    	cornerMenu.layoutYProperty().bind( pane.heightProperty().subtract(cornerMenu.heightProperty()));
		}
		else if (CornerMenu.Orientation.BOTTOM_LEFT.equals(lOrientationChoiceBox.getValue())) {
	    	cornerMenu.layoutYProperty().bind( pane.heightProperty().subtract(cornerMenu.heightProperty()));
		}
    	cornerMenu.getItems().addAll(facebookMenuItem, googleMenuItem, skypeMenuItem, twitterMenuItem, windowsMenuItem);

    	pane.getChildren().add(cornerMenu);
    }
    private CornerMenu cornerMenu;
    
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