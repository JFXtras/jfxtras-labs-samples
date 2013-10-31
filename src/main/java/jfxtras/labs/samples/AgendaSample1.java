package jfxtras.labs.samples;

import fxsampler.SampleBase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import jfxtras.labs.scene.control.Agenda;
import jfxtras.labs.scene.control.CalendarTextField;
import jfxtras.labs.scene.layout.GridPane;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class AgendaSample1 extends SampleBase
{
    public AgendaSample1() {
        agenda = new Agenda();
    }
    final Agenda agenda;

    @Override
    public String getSampleName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public String getSampleDescription() {
        return "Basic Agenda usage";
    }

    @Override
    public Node getPanel(Stage stage) {
        return agenda;
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

//        // Mode
//        {
//            lGridPane.add(new Label("Mode"), new GridPane.C().row(lRowIdx).col(0));
//            ChoiceBox<CalendarPicker.Mode> lChoiceBox = new ChoiceBox(FXCollections.observableArrayList(CalendarPicker.Mode.values()));
//            lGridPane.add(lChoiceBox, new GridPane.C().row(lRowIdx).col(1));
//            lChoiceBox.valueProperty().bindBidirectional(calendarPicker.modeProperty());
//        }
//        lRowIdx++;

        // done
        return lGridPane;
    }

    @Override
    public String getJavaDocURL() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public static void main(String[] args) {
        launch(args);
    }
}