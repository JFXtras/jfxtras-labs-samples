package jfxtras.labs.samples;

import fxsampler.SampleBase;
import javafx.collections.FXCollections;
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
import jfxtras.labs.internal.scene.control.skin.ListSpinnerCaspianSkin;
import jfxtras.labs.scene.control.ListSpinner;
import jfxtras.labs.scene.layout.GridPane;

public class ListSpinnerSample1 extends SampleBase
{
    /**
     *
     */
    public ListSpinnerSample1() {
        simplyStringListSpinner = new ListSpinner<String>("a", "b", "c");
    }
    final ListSpinner<String> simplyStringListSpinner;

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
        return "Basic ListSpinner usage";
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

        root.getChildren().addAll(simplyStringListSpinner);

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
            lLabel.setTooltip(new Tooltip("When reaching the last in the list, cycle back to the first"));
            lGridPane.add(lLabel, new GridPane.C().row(lRowIdx).col(0));
            CheckBox lCheckBox = new CheckBox();
            lGridPane.add(lCheckBox, new GridPane.C().row(lRowIdx).col(1));
            lCheckBox.selectedProperty().bindBidirectional(simplyStringListSpinner.cyclicProperty());
        }
        lRowIdx++;

        // Arrow direction
        {
            lGridPane.add(new Label("Arrow direction"), new GridPane.C().row(lRowIdx).col(0));
            ChoiceBox<ListSpinnerCaspianSkin.ArrowDirection> lChoiceBox = new ChoiceBox(FXCollections.observableArrayList(ListSpinnerCaspianSkin.ArrowDirection.values()));
            lGridPane.add(lChoiceBox, new GridPane.C().row(lRowIdx).col(1));
            lChoiceBox.valueProperty().addListener( (observable) -> {
                arrowDirectionStyle = "-fxx-arrow-direction:" + lChoiceBox.getValue() +";";
                setStyle();
            });
        }
        lRowIdx++;

        // Arrow position
        {
            lGridPane.add(new Label("Arrow position"), new GridPane.C().row(lRowIdx).col(0));
            ChoiceBox<ListSpinnerCaspianSkin.ArrowPosition> lChoiceBox = new ChoiceBox(FXCollections.observableArrayList(ListSpinnerCaspianSkin.ArrowPosition.values()));
            lGridPane.add(lChoiceBox, new GridPane.C().row(lRowIdx).col(1));
            lChoiceBox.valueProperty().addListener((observable) -> {
                arrowPositionStyle = "-fxx-arrow-position:" + lChoiceBox.getValue() + ";";
                setStyle();
            });
        }
        lRowIdx++;

        // Value alignment
        {
            lGridPane.add(new Label("Value alignment"), new GridPane.C().row(lRowIdx).col(0));
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
        simplyStringListSpinner.setStyle(arrowDirectionStyle + arrowPositionStyle + valueAlignmentStyle);
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
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public static void main(String[] args) {
        launch(args);
    }
}