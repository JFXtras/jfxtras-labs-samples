package jfxtras.samples.menu;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import jfxtras.samples.JFXtrasSampleBase;
import jfxtras.samples.layout.CircularPaneSample1;
import jfxtras.scene.layout.GridPane;
import jfxtras.scene.menu.CirclePopupMenu;

public class CirclePopupMenuSample1 extends JFXtrasSampleBase
{
    /**
     *
     */
    public CirclePopupMenuSample1() {
	}
	final private MenuItem facebookMenuItem = registerAction(new MenuItem("Facebook", new ImageView(new Image(this.getClass().getResourceAsStream("social_facebook_button_blue.png")))));
	final private MenuItem googleMenuItem = registerAction(new MenuItem("Google", new ImageView(new Image(this.getClass().getResourceAsStream("social_google_button_blue.png")))));
	final private MenuItem skypeMenuItem = registerAction(new MenuItem("Skype", new ImageView(new Image(this.getClass().getResourceAsStream("social_skype_button_blue.png")))));
	final private MenuItem twitterMenuItem = registerAction(new MenuItem("Twitter", new ImageView(new Image(this.getClass().getResourceAsStream("social_twitter_button_blue.png")))));
	final private MenuItem windowsMenuItem = registerAction(new MenuItem("Windows", new ImageView(new Image(this.getClass().getResourceAsStream("social_windows_button.png")))));

    private MenuItem registerAction(MenuItem menuItem) {
    	menuItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
            	showPopup(stackPane, "You clicked the " + menuItem.getText() + " icon");
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
        return "Basic CirclePopupMenu usage; CirclePopupMenu is a full circle menu that is supposed to popup on a right mouse button click.";
    }

    /**
     *
     * @param stage
     * @return
     */
    @Override
    public Node getPanel(Stage stage) {
        stackPane = new StackPane();
        
        // create underlying button
    	Button lButton = new Button("Underlying");
    	stackPane.getChildren().add(lButton);
    	lButton.setOnAction( actionEvent -> {
			showPopup(lButton, "You clicked the underlying button");
    	});
    	
    	// create circle popup
        circlePopupMenu = new CirclePopupMenu(stackPane, MouseButton.SECONDARY);
    	circlePopupMenu.getItems().addAll(facebookMenuItem, googleMenuItem, skypeMenuItem, twitterMenuItem, windowsMenuItem);
    	
    	return stackPane;
    }
    private StackPane stackPane;
    private CirclePopupMenu circlePopupMenu;

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

        // Animation
        {
            lGridPane.add(new Label("Animation"), new GridPane.C().row(lRowIdx).col(0).halignment(HPos.RIGHT));
            lGridPane.add(animationChoiceBox, new GridPane.C().row(lRowIdx).col(1));
            
            animationChoiceBox.getSelectionModel().selectedItemProperty().addListener( (observable) -> {
            	circlePopupMenu.setAnimationInterpolation( CircularPaneSample1.convertAnimationInterPolation(animationChoiceBox) );
            });
            animationChoiceBox.getSelectionModel().select(CircularPaneSample1.Animations.OverTheArcWithFade.toString()); 
        	circlePopupMenu.setAnimationInterpolation( CircularPaneSample1.convertAnimationInterPolation(animationChoiceBox) );
        }
        lRowIdx++;
        
        // done
        return lGridPane;
    }
    private ChoiceBox<String> animationChoiceBox = CircularPaneSample1.animationChoiceBox();
    
    /**
     *
     * @return
     */
    @Override
    public String getJavaDocURL() {
		return "http://jfxtras.org/doc/8.0/jfxtras-controls/" + CirclePopupMenu.class.getName().replace(".", "/") + ".html";
    }

    public static void main(String[] args) {
        launch(args);
    }
}