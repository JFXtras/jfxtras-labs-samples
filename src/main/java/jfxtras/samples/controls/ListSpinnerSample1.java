package jfxtras.samples.controls;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jfxtras.internal.scene.control.skin.ListSpinnerSkin;
import jfxtras.labs.util.StringConverterFactory;
import jfxtras.samples.JFXtrasSampleBase;
import jfxtras.scene.control.ListSpinner;
import jfxtras.scene.layout.GridPane;

public class ListSpinnerSample1 extends JFXtrasSampleBase
{
    /**
     *
     */
    public ListSpinnerSample1() {
        simpleStringListSpinner = new ListSpinner<String>("a", "b", "c");

		final ObservableList<String> lObservableList = FXCollections.observableArrayList("a", "b", "c", "d", "e");
		editableListSpinner = new ListSpinner<String>( lObservableList )
				.withCyclic(true)
				.withEditable(true)
				.withStringConverter(StringConverterFactory.forString())
				.withAddCallback((text) -> {
					lObservableList.add(text);
					return lObservableList.size() - 1; // notify spinner the value is appended at the end
				});
	}
    final ListSpinner<String> simpleStringListSpinner;
	final ListSpinner<String> editableListSpinner;

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
        return "ListSpinner is exactly what is says: a spinner control based upon a list. It will render one value from the underlying list and allow you to cycle up and down (or left and right, if the arrows are setup horizontally).";
    }

    /**
     *
     * @param stage
     * @return
     */
    @Override
    public Node getPanel(Stage stage) {
        VBox root = new VBox(20);
        root.setPadding(new Insets(30, 30, 30, 30));

        root.getChildren().addAll(simpleStringListSpinner);
		root.getChildren().addAll(editableListSpinner);

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
            Label lLabel = new Label("Cyclic");
            lGridPane.add(lLabel, new GridPane.C().row(lRowIdx).col(0).halignment(HPos.RIGHT));
            CheckBox lCheckBox = new CheckBox();
            lCheckBox.setTooltip(new Tooltip("When reaching the last in the list, cycle back to the first"));
            lGridPane.add(lCheckBox, new GridPane.C().row(lRowIdx).col(1));
            lCheckBox.selectedProperty().bindBidirectional(simpleStringListSpinner.cyclicProperty());
			lCheckBox.selectedProperty().bindBidirectional(editableListSpinner.cyclicProperty());
        }
        lRowIdx++;

        // Arrow direction
        {
            lGridPane.add(new Label("Arrow direction"), new GridPane.C().row(lRowIdx).col(0).halignment(HPos.RIGHT));
            ChoiceBox<ListSpinnerSkin.ArrowDirection> lChoiceBox = new ChoiceBox(FXCollections.observableArrayList(ListSpinnerSkin.ArrowDirection.values()));
            lGridPane.add(lChoiceBox, new GridPane.C().row(lRowIdx).col(1));
            lChoiceBox.valueProperty().addListener( (observable) -> {
                arrowDirectionStyle = "-fxx-arrow-direction:" + lChoiceBox.getValue() +";";
                setStyle();
            });
        }
        lRowIdx++;

        // Arrow position
        {
            lGridPane.add(new Label("Arrow position"), new GridPane.C().row(lRowIdx).col(0).halignment(HPos.RIGHT));
            ChoiceBox<ListSpinnerSkin.ArrowPosition> lChoiceBox = new ChoiceBox(FXCollections.observableArrayList(ListSpinnerSkin.ArrowPosition.values()));
            lGridPane.add(lChoiceBox, new GridPane.C().row(lRowIdx).col(1));
            lChoiceBox.valueProperty().addListener((observable) -> {
                arrowPositionStyle = "-fxx-arrow-position:" + lChoiceBox.getValue() + ";";
                setStyle();
            });
        }
        lRowIdx++;

        // Value alignment
        {
            lGridPane.add(new Label("Value alignment"), new GridPane.C().row(lRowIdx).col(0).halignment(HPos.RIGHT));
            ChoiceBox<Pos> lChoiceBox = new ChoiceBox(FXCollections.observableArrayList(Pos.values()));
            lGridPane.add(lChoiceBox, new GridPane.C().row(lRowIdx).col(1));
            lChoiceBox.valueProperty().addListener((observable) -> {
                valueAlignmentStyle = "-fxx-value-alignment:" + lChoiceBox.getValue() + ";";
                setStyle();
            });
        }
        lRowIdx++;

        // done
        return lGridPane;
    }

    /**
     *
     */
    private void setStyle() {
        simpleStringListSpinner.setStyle(arrowDirectionStyle + arrowPositionStyle + valueAlignmentStyle);
		editableListSpinner.setStyle(arrowDirectionStyle + arrowPositionStyle + valueAlignmentStyle);
    }
    private String arrowDirectionStyle = "";
    private String arrowPositionStyle = "";
    private String valueAlignmentStyle = "";

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