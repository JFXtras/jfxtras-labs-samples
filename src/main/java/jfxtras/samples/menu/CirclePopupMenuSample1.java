package jfxtras.samples.menu;

import javafx.collections.FXCollections;
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
import jfxtras.labs.scene.layout.CircularPane;
import jfxtras.labs.scene.menu.CirclePopupMenu;
import jfxtras.samples.JFXtrasSampleBase;
import jfxtras.scene.layout.GridPane;

import org.controlsfx.dialog.Dialogs;

public class CirclePopupMenuSample1 extends JFXtrasSampleBase
{
    /**
     *
     */
    public CirclePopupMenuSample1() {
        stackPane = new StackPane();
        
        // create underlying button
    	Button lButton = new Button("Underlying");
    	stackPane.getChildren().add(lButton);
    	lButton.setOnAction( actionEvent -> {
        	Dialogs.create().masthead("Click").message( "You clicked the underlying button").showInformation();
    	});
    	
    	// create circle popup
        circlePopupMenu = new CirclePopupMenu(stackPane, MouseButton.SECONDARY);
    	circlePopupMenu.getItems().addAll(facebookMenuItem, googleMenuItem, skypeMenuItem, twitterMenuItem, windowsMenuItem);
	}
    final private StackPane stackPane;
    final private CirclePopupMenu circlePopupMenu;
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
        return "Basic CirclePopupMenu usage; CirclePopupMenu is a full circle menu that is supposed to popup on a right mouse button click.";
    }

    /**
     *
     * @param stage
     * @return
     */
    @Override
    public Node getPanel(Stage stage) {
    	return stackPane;
    }

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
            animationChoiceBox.getSelectionModel().select(0);
            lGridPane.add(animationChoiceBox, new GridPane.C().row(lRowIdx).col(1));
            
            // run the animation
            animationChoiceBox.getSelectionModel().selectedItemProperty().addListener( (observable) -> {
            	if (Animations.OverTheArc.toString().equals(animationChoiceBox.getSelectionModel().getSelectedItem())) {
            		circlePopupMenu.setAnimationInterpolation(CirclePopupMenu::animateOverTheArc);
            	}
            	else if (Animations.FromOrigin.toString().equals(animationChoiceBox.getSelectionModel().getSelectedItem())) {
            		circlePopupMenu.setAnimationInterpolation(CirclePopupMenu::animateFromTheOrigin);
            	}
            	else if (Animations.Appear.toString().equals(animationChoiceBox.getSelectionModel().getSelectedItem())) {
            		circlePopupMenu.setAnimationInterpolation(CircularPane::animateAppear);
            	}
            	else {
            		circlePopupMenu.setAnimationInterpolation(null);
            	}
            });
        }
        lRowIdx++;
        
        // done
        return lGridPane;
    }
    enum Animations {OverTheArc, FromOrigin, Appear, None};
    private ChoiceBox<String> animationChoiceBox = new ChoiceBox<>(FXCollections.observableArrayList(Animations.FromOrigin.toString(), Animations.OverTheArc.toString(), Animations.Appear.toString(), Animations.None.toString()));
    
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