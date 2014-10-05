package jfxtras.samples.controls;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jfxtras.samples.JFXtrasSampleBase;
import jfxtras.scene.control.ListView;
import jfxtras.scene.control.ToggleGroupValue;
import jfxtras.scene.layout.GridPane;

public class ListViewSample1 extends JFXtrasSampleBase
{
    /**
     *
     */
	public ListViewSample1() {
		ObservableList<String> items = FXCollections.observableArrayList();
		for (int i = 0; i < 100; i++) {
			items.add("A list item numbered " + i);
		}
		listView = new ListView<String>();
		listView.setItems(items);
	}
	final ListView<String> listView;

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
        return "ListView is an extention on the standard ListView which adds a bindable 'selectedItem' property.\n"
             + "This allows for easy binding to other properties. You can see this in action by selecting in the list, or typing a value in the list in the textfield."
             ;
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
		root.getChildren().add(listView);
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
            lTextField.textProperty().bindBidirectional(listView.selectedItemProperty());
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