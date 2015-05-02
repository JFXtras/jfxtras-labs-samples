package jfxtras.labs.samples.control.gauge.linear;

import javafx.collections.FXCollections;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jfxtras.labs.scene.control.gauge.linear.BasicArcGauge;
import jfxtras.labs.scene.control.gauge.linear.LinearGauge;
import jfxtras.scene.layout.GridPane;

public class BasicArcGaugeSample1 extends AbstractLinearGaugeSample1<BasicArcGauge>
{
    /**
     *
     */
    public BasicArcGaugeSample1() {
    	basicArcGauge = new BasicArcGauge();
	}
    final BasicArcGauge basicArcGauge;

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
        return "BasicArcGauge is a simple round arc shaped gauge. The needle ranges from about 7 o'clock (min) clockwise to 5 o'clock (max). The backpane and needle can have different colors.";
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

        root.getChildren().addAll(basicArcGauge);

        return root;
    }

    @Override
    public Node getControlPanel() {
    	GridPane lGridPane = super.getControlPanel(basicArcGauge);
    	
        // Colorschemes
        {
            lGridPane.add(new Label("Needle & backpane colorscheme"), new GridPane.C().row(lRowIdx).col(0).halignment(HPos.RIGHT));
            ChoiceBox<String> lChoiceBox = new ChoiceBox(FXCollections.observableArrayList("colorscheme-light"
            		 , "colorscheme-dark"
            		 , "colorscheme-green"
            		 , "colorscheme-red"));
            lGridPane.add(lChoiceBox, new GridPane.C().row(lRowIdx).col(1));
            lChoiceBox.valueProperty().addListener( (observable) -> {
            	basicArcGauge.getStyleClass().remove(colorSchemeClass);
                colorSchemeClass = lChoiceBox.getValue();
                basicArcGauge.getStyleClass().add(colorSchemeClass);
            });
        }
        lRowIdx++;
        
        // done
        return lGridPane;
    }
    private String colorSchemeClass = "";

    /**
     *
     * @return
     */
    @Override
    public String getJavaDocURL() {
		return "http://jfxtras.org/doc/8.0/jfxtras-controls/" + BasicArcGauge.class.getName().replace(".", "/") + ".html";
    }

    public static void main(String[] args) {
        launch(args);
    }
}