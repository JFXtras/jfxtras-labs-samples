package jfxtras.labs.samples.control.gauge.linear;

import java.math.BigDecimal;

import javafx.collections.FXCollections;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jfxtras.internal.scene.control.skin.ListSpinnerSkin;
import jfxtras.labs.internal.scene.control.skin.gauge.linear.SimpleMetroArcGaugeSkin;
import jfxtras.labs.scene.control.BigDecimalField;
import jfxtras.labs.scene.control.gauge.linear.SimpleMetroArcGauge;
import jfxtras.samples.JFXtrasSampleBase;
import jfxtras.scene.layout.GridPane;

public class SimpleMetroArcGaugeSample1 extends JFXtrasSampleBase
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

        // value
        {
            lGridPane.add(new Label("Value"), new GridPane.C().row(lRowIdx).col(0).halignment(HPos.RIGHT));
            final BigDecimalField lBigDecimalField = new BigDecimalField(BigDecimal.valueOf(simpleMetroArcGauge.getValue()));
            lGridPane.add(lBigDecimalField, new GridPane.C().row(lRowIdx).col(1));
            lBigDecimalField.numberProperty().addListener( (observable) -> {
                simpleMetroArcGauge.setValue(lBigDecimalField.getNumber().doubleValue());
            });
        }
        lRowIdx++;
        
        // min value
        {
            lGridPane.add(new Label("Min value"), new GridPane.C().row(lRowIdx).col(0).halignment(HPos.RIGHT));
            final BigDecimalField lBigDecimalField = new BigDecimalField(BigDecimal.valueOf(simpleMetroArcGauge.getMinValue()));
            lGridPane.add(lBigDecimalField, new GridPane.C().row(lRowIdx).col(1));
            lBigDecimalField.numberProperty().addListener( (observable) -> {
                simpleMetroArcGauge.setMinValue(lBigDecimalField.getNumber().doubleValue());
            });
        }
        lRowIdx++;
        
        // max value
        {
        	lGridPane.add(new Label("Max value"), new GridPane.C().row(lRowIdx).col(0).halignment(HPos.RIGHT));
            final BigDecimalField lBigDecimalField = new BigDecimalField(BigDecimal.valueOf(simpleMetroArcGauge.getMaxValue()));
            lGridPane.add(lBigDecimalField, new GridPane.C().row(lRowIdx).col(1));
            lBigDecimalField.numberProperty().addListener( (observable) -> {
                simpleMetroArcGauge.setMaxValue(lBigDecimalField.getNumber().doubleValue());
            });
        }
        lRowIdx++;
        
        // Animated
        {
            lGridPane.add(new Label("Animated"), new GridPane.C().row(lRowIdx).col(0).halignment(HPos.RIGHT));
            ChoiceBox<SimpleMetroArcGaugeSkin.Animated> lChoiceBox = new ChoiceBox(FXCollections.observableArrayList(SimpleMetroArcGaugeSkin.Animated.values()));
            lGridPane.add(lChoiceBox, new GridPane.C().row(lRowIdx).col(1));
            lChoiceBox.valueProperty().addListener( (observable) -> {
                arrowDirectionStyle = "-fxx-animated:" + lChoiceBox.getValue() +";";
                setStyle();
            });
            lChoiceBox.getSelectionModel().select(SimpleMetroArcGaugeSkin.Animated.YES);
        }
        lRowIdx++;

        // done
        return lGridPane;
    }

    /**
     *
     */
    private void setStyle() {
        simpleMetroArcGauge.setStyle(arrowDirectionStyle);
    }
    private String arrowDirectionStyle = "";

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