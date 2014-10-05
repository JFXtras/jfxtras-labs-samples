package jfxtras.samples.controls;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jfxtras.samples.JFXtrasSampleBase;
import jfxtras.scene.control.ToggleGroupValue;
import jfxtras.scene.layout.GridPane;

public class ToggleGroupValueSample1 extends JFXtrasSampleBase
{
    /**
     *
     */
	public ToggleGroupValueSample1() {
		toggleGroupValue = new ToggleGroupValue<>();
		
		int idx = 0;
		for (int i = 0; i < 3; i++) {
			RadioButton lButton = new RadioButton("" + idx);
			toggleGroupValue.add(lButton, "value" + idx);
			toggles.add(lButton);
			idx++;
		}
		for (int i = 0; i < 3; i++) {
			ToggleButton lButton = new ToggleButton("" + idx);
			toggleGroupValue.add(lButton, "value" + idx);
			toggles.add(lButton);
			idx++;
		}
	}
	final ToggleGroupValue<String> toggleGroupValue;
	final List<ToggleButton> toggles = new ArrayList<>();

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
        return "ToggleGroupValue example";
    }

    /**
     *
     * @param stage
     * @return
     */
    @Override
    public Node getPanel(Stage stage) {
        VBox root = new VBox(5);
        root.setPadding(new Insets(30, 30, 30, 30));
		root.getChildren().addAll(toggles);
        return root;
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

        // cyclic
        {
            Label lLabel = new Label("Value");
            lGridPane.add(lLabel, new GridPane.C().row(lRowIdx).col(0).halignment(HPos.RIGHT));
            TextField lTextField = new TextField();
            lTextField.setTooltip(new Tooltip("The value corresponding to the selected toggle button"));
            lGridPane.add(lTextField, new GridPane.C().row(lRowIdx).col(1));
            lTextField.textProperty().bindBidirectional(toggleGroupValue.valueProperty());
        }
        lRowIdx++;

        // done
        return lGridPane;
    }

    /**
     *
     * @return
     */
    @Override
    public String getJavaDocURL() {
		return "http://jfxtras.org/doc/8.0/jfxtras-controls/" + ToggleGroupValue.class.getName().replace(".", "/") + ".html";
    }

    public static void main(String[] args) {
        launch(args);
    }
}