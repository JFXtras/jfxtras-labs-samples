package jfxtras.samples.layout;

import java.util.HashMap;
import java.util.Map;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import jfxtras.labs.samples.JFXtrasLabsSampleBase;
import jfxtras.samples.JFXtrasSampleBase;
import jfxtras.scene.layout.GridPane;
import jfxtras.scene.layout.HBox;

public class HBoxSample1 extends JFXtrasSampleBase
{
    /**
     *
     */
    public HBoxSample1() {
		hbox = new HBox(5.0);
		hbox.add(new Button("grow\nCLICK ME"), new HBox.C().hgrow(Priority.ALWAYS));
		setupDescriptorOnLastButton("Button b = new Button(\"text\");\nhbox.getChildren().add(b);\nHBox.setHgrow(b, Priority.ALWAYS);", "hbox.add(new Button(\"text\"), new HBox.C()\n\t.hgrow(Priority.ALWAYS));");
		hbox.add(new Button("margin 5 grow\nCLICK ME"), new HBox.C().margin(new Insets(5.0)).hgrow(Priority.ALWAYS));
		setupDescriptorOnLastButton("Button b = new Button(\"text\");\nhbox.getChildren().add(b);\nHBox.setMargin(b, new Insets(5.0));\nHBox.setHgrow(b1, Priority.ALWAYS);", "hbox.add(new Button(\"text\"), new HBox.C()\n\t.margin(new Insets(5.0))\n\t.hgrow(Priority.ALWAYS));");
		hbox.add(new Button("grow maxheight 50\nCLICK ME"), new HBox.C().hgrow(Priority.ALWAYS).maxHeight(50.0));
		setupDescriptorOnLastButton("Button b = new Button(\"text\");\nb.setMaxHeight(50.0);\nhbox.getChildren().add(b);\nHBox.setHgrow(b1, Priority.ALWAYS);", "hbox.add(new Button(\"text\"), new HBox.C()\n\t.maxHeight(50.0)\n\t.hgrow(Priority.ALWAYS));");
		hbox.setMaxHeight(100);
		hbox.setStyle("-fx-border-color: black;");
	}
	final HBox hbox;
	final Map<Button, Descriptor> descriptorMap = new HashMap<>();

	class Descriptor {
		public Descriptor(String oldStyle, String newStyle) {
			this.oldStyle = oldStyle;
			this.newStyle = newStyle;
		}
		final String oldStyle;
		final String newStyle;
	}

	private void setupDescriptorOnLastButton(String oldStyle, String newStyle) {
		final Button lButton = (Button)hbox.getChildren().get(hbox.getChildren().size() - 1);
		descriptorMap.put(lButton, new Descriptor(oldStyle, newStyle));
		lButton.onMouseClickedProperty().set((actionEvent) -> {
			oldTextArea.textProperty().set( descriptorMap.get(lButton).oldStyle );
			newTextArea.textProperty().set( descriptorMap.get(lButton).newStyle );
		});
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
        return "HBox API improvements";
    }

    /**
     *
     * @param stage
     * @return
     */
    @Override
    public Node getPanel(Stage stage) {
		return hbox;
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
		return "http://jfxtras.org/doc/8.0/jfxtras-common/" + HBox.class.getName().replace(".", "/") + ".html";
    }

    public static void main(String[] args) {
        launch(args);
    }
}