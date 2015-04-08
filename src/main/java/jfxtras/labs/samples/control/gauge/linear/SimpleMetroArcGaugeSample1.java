package jfxtras.labs.samples.control.gauge.linear;

import javafx.collections.FXCollections;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jfxtras.labs.scene.control.gauge.linear.SimpleMetroArcGauge;
import jfxtras.scene.layout.GridPane;

public class SimpleMetroArcGaugeSample1 extends AbstractLinearGaugeSample1
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
        VBox root = new VBox(20);
        root.setPadding(new Insets(30, 30, 30, 30));

        root.getChildren().addAll(simpleMetroArcGauge);

        return root;
    }

    @Override
    public Node getControlPanel() {
    	GridPane lGridPane = super.getControlPanel(simpleMetroArcGauge);
    	
        // Colorschemes
        {
            lGridPane.add(new Label("Colorscheme"), new GridPane.C().row(lRowIdx).col(0).halignment(HPos.RIGHT));
            ChoiceBox<String> lChoiceBox = new ChoiceBox(FXCollections.observableArrayList("colorscheme-blue-to-red-5"
            		 , "colorscheme-red-to-blue-5"
            		 , "colorscheme-green-to-darkgreen-6"
            		 , "colorscheme-green-to-red-6"
            		 , "colorscheme-red-to-green-6"
            		 , "colorscheme-purple-to-red-6" 
            		 , "colorscheme-blue-to-red-6"
            		 , "colorscheme-green-to-red-7"
            		 , "colorscheme-red-to-green-7"
            		 , "colorscheme-green-to-red-10"
            		 , "colorscheme-red-to-green-10"
            		 , "colorscheme-purple-to-cyan-10"
            		 , "colorscheme-first-grey-rest-transparent-10"));
            lGridPane.add(lChoiceBox, new GridPane.C().row(lRowIdx).col(1));
            lChoiceBox.valueProperty().addListener( (observable) -> {
            	simpleMetroArcGauge.getStyleClass().remove(colorSchemeClass);
                colorSchemeClass = lChoiceBox.getValue();
                simpleMetroArcGauge.getStyleClass().add(colorSchemeClass);
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
		return "http://jfxtras.org/doc/8.0/jfxtras-controls/" + SimpleMetroArcGauge.class.getName().replace(".", "/") + ".html";
    }

    public static void main(String[] args) {
        launch(args);
    }
}