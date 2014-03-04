package jfxtras.samples.controls.datetime;

import java.util.Arrays;

import javafx.collections.FXCollections;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import jfxtras.internal.scene.control.skin.CalendarTimePickerSkin;
import jfxtras.labs.samples.JFXtrasLabsSampleBase;
import jfxtras.samples.JFXtrasSampleBase;
import jfxtras.scene.control.LocalTimePicker;
import jfxtras.scene.layout.GridPane;
import jfxtras.scene.layout.VBox;

public class LocalTimePickerSample1 extends JFXtrasSampleBase
{
    public LocalTimePickerSample1() {
        localTimePicker = new LocalTimePicker();
//		localTimePicker.showLabelsProperty().set(true);
    }
    final LocalTimePicker localTimePicker;

    @Override
    public String getSampleName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public String getSampleDescription() {
        return "Basic LocalTimePicker usage";
    }

    @Override
    public Node getPanel(Stage stage) {
		this.stage = stage;

        VBox root = new VBox(20);
        root.setPadding(new Insets(30, 30, 30, 30));

        root.getChildren().addAll(localTimePicker);

        return root;
    }
	private Stage stage;
	
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

		// stylesheet
		{		
			Label lLabel = new Label("Stage Stylesheet");
			lGridPane.add(lLabel, new GridPane.C().row(lRowIdx).col(0).halignment(HPos.RIGHT).valignment(VPos.TOP));
			TextArea lTextArea = createTextAreaForCSS(stage, FXCollections.observableArrayList(
				".LocalTimePicker {\n\t-fxx-show-ticklabels:YES; /* " +  Arrays.toString(CalendarTimePickerSkin.ShowTickLabels.values()) + " */\n}",
				".LocalTimePicker {\n\t-fxx-label-dateformat:\"hh:mm a\"; /* See SimpleDateFormat, e.g. 'HH' for 24 hours per day */\n}") 
			);
			lGridPane.add(lTextArea, new GridPane.C().row(lRowIdx).col(1).vgrow(Priority.ALWAYS).minHeight(100.0));
		}
        lRowIdx++;

        // done
        return lGridPane;
    }

    @Override
    public String getJavaDocURL() {
		return "http://jfxtras.org/doc/8.0/" + LocalTimePicker.class.getName().replace(".", "/") + ".html";
    }

    public static void main(String[] args) {
        launch(args);
    }
}