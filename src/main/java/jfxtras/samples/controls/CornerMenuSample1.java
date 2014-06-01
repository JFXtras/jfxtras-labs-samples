package jfxtras.samples.controls;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
    	MenuItem lFacebookMenuItem = registerAction(new MenuItem("Facebook", new ImageView(new Image(this.getClass().getResourceAsStream("social_facebook_button_blue.png")))));
    	MenuItem lGoogleMenuItem = registerAction(new MenuItem("Google", new ImageView(new Image(this.getClass().getResourceAsStream("social_google_button_blue.png")))));
    	MenuItem lSkypeMenuItem = registerAction(new MenuItem("Skype", new ImageView(new Image(this.getClass().getResourceAsStream("social_skype_button_blue.png")))));
    	MenuItem lTwitterMenuItem = registerAction(new MenuItem("Twitter", new ImageView(new Image(this.getClass().getResourceAsStream("social_twitter_button_blue.png")))));
    	MenuItem lWindowsMenuItem = registerAction(new MenuItem("Windows", new ImageView(new Image(this.getClass().getResourceAsStream("social_windows_button.png")))));
        
    	cornerMenu = new CornerMenu();
    	cornerMenu.setOrientation(CornerMenu.Orientation.BOTTOM_RIGHT);
    	cornerMenu.getItems().addAll(lFacebookMenuItem, lGoogleMenuItem, lSkypeMenuItem, lTwitterMenuItem, lWindowsMenuItem);
	}
    final CornerMenu cornerMenu;

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
        return "Basic CornerMenu usage";
    }

    /**
     *
     * @param stage
     * @return
     */
    @Override
    public Node getPanel(Stage stage) {
    	GridPane lGridPane = new GridPane();
    	lGridPane.setStyle("-fx-border-color: green;");
    	
    	lGridPane.add(cornerMenu, new GridPane.C().col(0).row(0));
    	
        return lGridPane;
    }

    @Override
    public Node getControlPanel() {
        // the result
        GridPane lGridPane = new GridPane();
        lGridPane.setVgap(2.0);
        lGridPane.setHgap(2.0);

//        // setup the grid so all the labels will not grow, but the rest will
//        ColumnConstraints lColumnConstraintsAlwaysGrow = new ColumnConstraints();
//        lColumnConstraintsAlwaysGrow.setHgrow(Priority.ALWAYS);
//        ColumnConstraints lColumnConstraintsNeverGrow = new ColumnConstraints();
//        lColumnConstraintsNeverGrow.setHgrow(Priority.NEVER);
//        lGridPane.getColumnConstraints().addAll(lColumnConstraintsNeverGrow, lColumnConstraintsAlwaysGrow);
//        int lRowIdx = 0;
//
//        // cyclic
//        {
//            Label lLabel = new Label("Cyclic");
//            lGridPane.add(lLabel, new GridPane.C().row(lRowIdx).col(0).halignment(HPos.RIGHT));
//            CheckBox lCheckBox = new CheckBox();
//            lCheckBox.setTooltip(new Tooltip("When reaching the last in the list, cycle back to the first"));
//            lGridPane.add(lCheckBox, new GridPane.C().row(lRowIdx).col(1));
//            lCheckBox.selectedProperty().bindBidirectional(simpleStringListSpinner.cyclicProperty());
//			lCheckBox.selectedProperty().bindBidirectional(editableListSpinner.cyclicProperty());
//        }
//        lRowIdx++;

        // done
        return lGridPane;
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