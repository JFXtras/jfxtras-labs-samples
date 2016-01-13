package jfxtras.samples.control.gauge.linear;

import javafx.collections.FXCollections;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jfxtras.scene.control.gauge.linear.BasicRoundDailGauge;
import jfxtras.scene.layout.GridPane;

public class BasicRoundDailGaugeSample1 extends AbstractLinearGaugeSample1<BasicRoundDailGauge>
{
    /**
     *
     */
    public BasicRoundDailGaugeSample1() {
    	basicRoundDailGauge = new BasicRoundDailGauge();
	}
    final BasicRoundDailGauge basicRoundDailGauge;

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
        return "BasicRoundDailGauge is a simple round arc shaped gauge. The needle ranges from about 7 o'clock (min) clockwise to 5 o'clock (max). The backpane and needle can have different colors.";
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

        root.getChildren().addAll(basicRoundDailGauge);

        return root;
    }

    @Override
    public Node getControlPanel() {
    	GridPane lGridPane = super.getControlPanel(basicRoundDailGauge);
    	
        // Colorschemes
        {
            lGridPane.add(new Label("Needle & backpane colorscheme"), new GridPane.C().row(lRowIdx).col(0).halignment(HPos.RIGHT));
            ChoiceBox<String> lChoiceBox = new ChoiceBox(FXCollections.observableArrayList("colorscheme-light"
            		 , "colorscheme-dark"
            		 , "colorscheme-green"
            		 , "colorscheme-red"));
            lGridPane.add(lChoiceBox, new GridPane.C().row(lRowIdx).col(1));
            lChoiceBox.valueProperty().addListener( (observable) -> {
            	basicRoundDailGauge.getStyleClass().remove(colorSchemeClass);
                colorSchemeClass = lChoiceBox.getValue();
                basicRoundDailGauge.getStyleClass().add(colorSchemeClass);
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
		return "http://jfxtras.org/doc/8.0/jfxtras-controls/" + BasicRoundDailGauge.class.getName().replace(".", "/") + ".html";
    }

    public static void main(String[] args) {
        launch(args);
    }
}