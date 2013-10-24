package jfxtras.labs.samples;

import fxsampler.Sample;
import fxsampler.SampleBase;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Sample1 extends SampleBase
{
    @Override
    public String getSampleName() {
        return "Sample1";
    }

    @Override
    public String getSampleDescription() {
        return "Sample2desc";
    }

    @Override
    public Node getPanel(Stage stage) {
        VBox root = new VBox(20);
        root.setPadding(new Insets(30, 30, 30, 30));

        root.getChildren().addAll(new Label("TEST"));

        return root;
    }

//    @Override
//    public Node getSidePanel() {
//        return null;  //To change body of implemented methods use File | Settings | File Templates.
//    }

    @Override
    public String getJavaDocURL() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public static void main(String[] args) {
        launch(args);
    }
}