package jfxtras.samples.layout;

import java.util.HashMap;
import java.util.Map;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import jfxtras.labs.samples.JFXtrasLabsSampleBase;
import jfxtras.samples.JFXtrasSampleBase;
import jfxtras.scene.layout.GridPane;

public class GridPaneSample1 extends JFXtrasSampleBase
{
    /**
     *
     */
    public GridPaneSample1() {
		gridPane = new GridPane()
			.withHGap(5)
			.withVGap(5)
			.withPadding(new Insets(10))
			.withGridLinesVisible(true);
		showConstruction();

		gridPane.add(new Text("SingleCell"), new GridPane.C().col(1).row(0));
		setupDescriptorOnLastNode("gridPane.add(new Text(\"text\"), 1, 0));", "gridPane.add(new Text(\"text\"), new GridPane.C().col(1).row(0));");
		gridPane.add(new Text("RIGHT"), new GridPane.C().col(2).row(0).halignment(HPos.RIGHT));
		setupDescriptorOnLastNode("Text t = new Text(\"text\");\ngridPane.add(t, 2, 0));\nGridPane.setHAlignment(t, HPos.RIGHT);", "gridPane.add(new Text(\"text\"), new GridPane.C().col(2).row(0)\n\t.halignment(HPos.RIGHT));");

		gridPane.add(new Text("Span2Row\nSpan2Row\nSpan2Row"), new GridPane.C().col(0).row(0).colSpan(1).rowSpan(2));
		setupDescriptorOnLastNode("gridPane.add(new Text(\"text\"), 0, 0, 1, 2));", "gridPane.add(new Text(\"text\"), new GridPane.C().col(0).row(0)\n\t.rowSpan(2));");

		gridPane.add(new Text("Span2Columns Span2Columns"), new GridPane.C().col(1).row(1).colSpan(2).rowSpan(1));
		setupDescriptorOnLastNode("gridPane.add(new Text(\"text\"), 1, 1, 2, 1));", "gridPane.add(new Text(\"text\"), new GridPane.C().col(1).row(1)\n\t.colSpan(2));");

		gridPane.add(new Text("Single"), new GridPane.C().col(0).row(2));
		setupDescriptorOnLastNode("gridPane.add(new Text(\"text\"), 0, 2));", "gridPane.add(new Text(\"text\"), new GridPane.C().col(0).row(2));");

		gridPane.add(new Text("Span2Col2RowCenter\nSpan2Col2RowCenter\nSpan2Col2RowCenter\nSpan2Col2RowCenter\nSpan2Col2RowCenter"), new GridPane.C().col(1).row(2).colSpan(2).rowSpan(2).halignment(HPos.CENTER));
		setupDescriptorOnLastNode("Text t = new Text(\"text\");\ngridPane.add(t, 1, 2, 2, 2));\nGridPane.setHAlignment(t, HPos.CENTER);", "gridPane.add(new Text(\"text\"), new GridPane.C().col(1).row(2)\n\t.colSpan(2).rowSpan(2)\n\t.halignment(HPos.CENTER));");

		gridPane.add(new Text("BOTTOM"), new GridPane.C().col(0).row(3).valignment(VPos.BOTTOM));
		setupDescriptorOnLastNode("Text t = new Text(\"text\");\ngridPane.add(t, 0, 3));\nGridPane.setVAlignment(t, VPos.BOTTOM);", "gridPane.add(new Text(\"text\"), new GridPane.C().col(0).row(3)\n\t.valignment(VPos.BOTTOM));");

		gridPane.add(new Text("TOP"), new GridPane.C().col(3).row(3).valignment(VPos.TOP));
		setupDescriptorOnLastNode("Text t = new Text(\"text\");\ngridPane.add(t, 3, 3));\nGridPane.setVAlignment(t, VPos.TOP);", "gridPane.add(new Text(\"text\"), new GridPane.C().col(3).row(3)\n\t.valignment(VPos.TOP));");
	}
	final GridPane gridPane;
	final Map<Node, Descriptor> descriptorMap = new HashMap<>();

	class Descriptor {
		public Descriptor(String oldStyle, String newStyle) {
			this.oldStyle = oldStyle;
			this.newStyle = newStyle;
		}
		final String oldStyle;
		final String newStyle;
	}

	private void setupDescriptorOnLastNode(String oldStyle, String newStyle) {
		final Node lNode = gridPane.getChildren().get(gridPane.getChildren().size() - 1);
		descriptorMap.put(lNode, new Descriptor(oldStyle, newStyle));
		lNode.onMouseClickedProperty().set((actionEvent) -> {
			oldTextArea.textProperty().set( descriptorMap.get(lNode).oldStyle );
			newTextArea.textProperty().set( descriptorMap.get(lNode).newStyle );
		});
	}

	private void showConstruction() {
		oldTextArea.textProperty().set("gridPane = new GridPane();\n"
									   + "gridPane.setHGap(5);\n"
									   + "gridPane.setVGap(5);\n"
									   + "gridPane.setPadding(new Insets(10));\n"
									   + "gridPane.setGridLinesVisible(true);");
		newTextArea.textProperty().set("gridPane = new GridPane()\n"
									   + "\t.withHGap(5)\n"
									   + "\t.withVGap(5)\n"
									   + "\t.withPadding(new Insets(10))\n"
									   + "\t.withGridLinesVisible(true);");
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
        return "GridPane API improvements";
    }

    /**
     *
     * @param stage
     * @return
     */
    @Override
    public Node getPanel(Stage stage) {
		return gridPane;
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

		// oldStyle
		{
			oldTextArea.setEditable(false);
			lGridPane.add(new Label("Official API"), new GridPane.C().row(lRowIdx).col(0).halignment(HPos.RIGHT).valignment(VPos.TOP));
			lGridPane.add(oldTextArea, new GridPane.C().row(lRowIdx).col(1));
		}
		lRowIdx++;

		// oldStyle
		{
			newTextArea.setEditable(false);
			lGridPane.add(new Label("JFXtras API"), new GridPane.C().row(lRowIdx).col(0).halignment(HPos.RIGHT).valignment(VPos.TOP));
			lGridPane.add(newTextArea, new GridPane.C().row(lRowIdx).col(1));
		}
		lRowIdx++;

		// gridLines
		{
			Label lLabel = new Label("Show lines");
			lLabel.setTooltip(new Tooltip("Show debug lines"));
			lGridPane.add(lLabel, new GridPane.C().row(lRowIdx).col(0).halignment(HPos.RIGHT));
			CheckBox lCheckBox = new CheckBox();
			lGridPane.add(lCheckBox, new GridPane.C().row(lRowIdx).col(1));
			lCheckBox.selectedProperty().bindBidirectional(gridPane.gridLinesVisibleProperty());
		}
		lRowIdx++;

		// show construction
		{
			Label lLabel = new Label("Show construction");
			lLabel.setTooltip(new Tooltip("Show how the grid was setup"));
			lGridPane.add(lLabel, new GridPane.C().row(lRowIdx).col(0).halignment(HPos.RIGHT));
			Button lButton = new Button("Show construction");
			lGridPane.add(lButton, new GridPane.C().row(lRowIdx).col(1));
			lButton.onActionProperty().set((actionEvent) -> {
				showConstruction();
			});
		}

		// done
		return lGridPane;
	}
	final private TextArea oldTextArea = new TextArea();
	final private TextArea newTextArea = new TextArea();

    /**
     *
     * @return
     */
    @Override
    public String getJavaDocURL() {
		return "http://jfxtras.org/doc/8.0/" + GridPane.class.getName().replace(".", "/") + ".html";
    }

    public static void main(String[] args) {
        launch(args);
    }
}