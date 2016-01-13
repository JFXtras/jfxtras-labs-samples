package jfxtras.samples.control.gauge.linear;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jfxtras.scene.control.gauge.linear.SimpleMetroArcGauge;
import jfxtras.scene.layout.GridPane;

public class SimpleMetroArcGaugeSample1 extends AbstractLinearGaugeSample1<SimpleMetroArcGauge>
{
    /**
     *
     */
    public SimpleMetroArcGaugeSample1() {
    	simpleMetroArcGauge = new SimpleMetroArcGauge();
	}
    final SimpleMetroArcGauge simpleMetroArcGauge;

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
        return "SimpleMetroArcGauge is a simple flat possibly colorful (Microsoft Metro style) arc shaped gauge. The needle ranges from about 7 o'clock (min) clockwise to 5 o'clock (max)";
    }

    /**
     *
     * @param stage
     * @return
     */
    @Override
    public Node getPanel(Stage stage) {
    	super.setup(stage);
    	
        VBox root = new VBox(20);
        root.setPadding(new Insets(30, 30, 30, 30));

        root.getChildren().addAll(simpleMetroArcGauge);

        return root;
    }

    @Override
    public Node getControlPanel() {
    	GridPane lGridPane = super.getControlPanel(simpleMetroArcGauge);
    	
        // done
        return lGridPane;
    }

    /**
     *
     * @return
     */
    @Override
    public String getJavaDocURL() {
		return "http://jfxtras.org/doc/8.0/jfxtras-controls/" + SimpleMetroArcGauge.class.getName().replace(".", "/") + ".html";
    }

    public static void main(String[] args) {
        launch(args);
    }
}