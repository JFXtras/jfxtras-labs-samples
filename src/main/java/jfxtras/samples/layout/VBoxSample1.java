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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import jfxtras.labs.samples.JFXtrasLabsSampleBase;
import jfxtras.samples.JFXtrasSampleBase;
import jfxtras.scene.layout.GridPane;
import jfxtras.scene.layout.VBox;

public class VBoxSample1 extends JFXtrasSampleBase
{
    /**
     *
     */
    public VBoxSample1() {
		vbox = new VBox(5.0);
		vbox.add(new Button("grow CLICK ME"), new VBox.C().vgrow(Priority.ALWAYS));
		setupDescriptorOnLastButton("Button b = new Button(\"text\");\nvbox.getChildren().add(b);\nVBox.setHgrow(b, Priority.ALWAYS);", "vbox.add(new Button(\"text\"), new VBox.C()\n\t.vgrow(Priority.ALWAYS));");
		vbox.add(new Button("margin 5 grow CLICK ME"), new VBox.C().margin(new Insets(5.0)).vgrow(Priority.ALWAYS));
		setupDescriptorOnLastButton("Button b = new Button(\"text\");\nvbox.getChildren().add(b);\nVBox.setMargin(new Insets(5.0));\nVBox.setHgrow(b, Priority.ALWAYS);", "vbox.add(new Button(\"text\"), new VBox.C()\n\t.margin(new Insets(5.0))\n\t.vgrow(Priority.ALWAYS));");
		vbox.add(new Button("grow maxwidth 200 CLICK ME"), new VBox.C().vgrow(Priority.ALWAYS).maxWidth(200.0));
		setupDescriptorOnLastButton("Button b = new Button(\"text\");\nb.setMaxWidth(200.0);\nvbox.getChildren().add(b);\nVBox.setHgrow(b, Priority.ALWAYS);", "vbox.add(new Button(\"text\"), new VBox.C()\n\t.maxWidth(200.0)\n\t.vgrow(Priority.ALWAYS));");
		vbox.setMaxWidth(250);
		vbox.setStyle("-fx-border-color: black;");
	}
	final VBox vbox;
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
		final Button lButton = (Button)vbox.getChildren().get(vbox.getChildren().size() - 1);
		descriptorMap.put(lButton, new Descriptor(oldStyle, newStyle));
		lButton.onMouseClickedProperty().set((actionEvent) -> {
			oldTextArea.textProperty().set( descriptorMap.get(lButton).oldStyle );
			newTextArea.textProperty().set(descriptorMap.get(lButton).newStyle);
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
		BorderPane lBorderPane = new BorderPane();
		lBorderPane.setCenter(vbox);
		lBorderPane.setPadding(new Insets(10));
		return lBorderPane;
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
		return "http://jfxtras.org/doc/8.0/jfxtras-common/" + VBox.class.getName().replace(".", "/") + ".html";
    }

    public static void main(String[] args) {
        launch(args);
    }
}