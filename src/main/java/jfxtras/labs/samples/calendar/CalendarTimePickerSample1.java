package jfxtras.labs.samples.calendar;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import jfxtras.labs.samples.JFXtrasSampleBase;
import jfxtras.labs.scene.control.CalendarTimePicker;
import jfxtras.labs.scene.layout.GridPane;
import jfxtras.labs.scene.layout.VBox;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class CalendarTimePickerSample1 extends JFXtrasSampleBase
{
    public CalendarTimePickerSample1() {
        calendarTimePicker = new CalendarTimePicker();
		calendarTimePicker.showLabelsProperty().set(true);
    }
    final CalendarTimePicker calendarTimePicker;

    @Override
    public String getSampleName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public String getSampleDescription() {
        return "Basic CalendarTimePicker usage";
    }

    @Override
    public Node getPanel(Stage stage) {
        VBox root = new VBox(20);
        root.setPadding(new Insets(30, 30, 30, 30));

        root.getChildren().addAll(calendarTimePicker);

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


        // showLabels
        {
            Label lLabel = new Label("Show labels");
            //lLabel.setTooltip(new Tooltip("Only in SINGLE mode"));
            lGridPane.add(lLabel, new GridPane.C().row(lRowIdx).col(0).halignment(HPos.RIGHT));
			CheckBox lCheckBox = new CheckBox();
            lGridPane.add(lCheckBox, new GridPane.C().row(lRowIdx).col(1));
			lCheckBox.selectedProperty().bindBidirectional( calendarTimePicker.showLabelsProperty() );
        }
        lRowIdx++;

        // done
        return lGridPane;
    }

    @Override
    public String getJavaDocURL() {
		return "http://jfxtras.org/doc/8.0/" + CalendarTimePicker.class.getName().replace(".", "/") + ".html";
    }

    public static void main(String[] args) {
        launch(args);
    }
}