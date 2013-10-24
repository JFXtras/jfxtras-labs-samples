package jfxtras.labs.samples;

import fxsampler.SampleBase;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jfxtras.labs.scene.control.CalendarPicker;
import jfxtras.labs.scene.layout.HBox;

public class CalendarPickerSample1 extends SampleBase
{
    public CalendarPickerSample1() {
        calendarPicker = new CalendarPicker();
    }
    final CalendarPicker calendarPicker;

    @Override
    public String getSampleName() {
        return this.getClass().getName();
    }

    @Override
    public String getSampleDescription() {
        return "Basic CalendarPicker usage";
    }

    @Override
    public Node getPanel(Stage stage) {
        VBox root = new VBox(20);
        root.setPadding(new Insets(30, 30, 30, 30));

        root.getChildren().addAll(calendarPicker);

        return root;
    }

    @Override
    public Node getControlPanel() {
        HBox root = new HBox(20);
        root.setPadding(new Insets(30, 30, 30, 30));

        root.getChildren().addAll(new Label("TEST"));

        return root;
    }

    @Override
    public String getJavaDocURL() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public static void main(String[] args) {
        launch(args);
    }
}