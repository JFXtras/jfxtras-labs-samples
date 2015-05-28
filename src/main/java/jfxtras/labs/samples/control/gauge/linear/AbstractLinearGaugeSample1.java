package jfxtras.labs.samples.control.gauge.linear;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import jfxtras.labs.internal.scene.control.gauge.linear.skin.SimpleMetroArcGaugeSkin;
import jfxtras.labs.scene.control.BigDecimalField;
import jfxtras.labs.scene.control.gauge.linear.LinearGauge;
import jfxtras.labs.scene.control.gauge.linear.elements.PercentSegment;
import jfxtras.labs.scene.control.gauge.linear.elements.Segment;
import jfxtras.samples.JFXtrasSampleBase;
import jfxtras.scene.layout.GridPane;

abstract public class AbstractLinearGaugeSample1<T> extends JFXtrasSampleBase
{
	protected void setup(Stage stage) {
        stage.getScene().getStylesheets().add(LinearGauge.segmentColorschemeCSSPath());
	}

    public GridPane getControlPanel(LinearGauge<T> linearGauge) {
    	this.linearGauge = linearGauge;
    	
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
        lRowIdx = 0;

        // value
        {
            lGridPane.add(new Label("Value"), new GridPane.C().row(lRowIdx).col(0).halignment(HPos.RIGHT));
            final BigDecimalField lBigDecimalField = new BigDecimalField(BigDecimal.valueOf(linearGauge.getValue()));
            lGridPane.add(lBigDecimalField, new GridPane.C().row(lRowIdx).col(1));
            lBigDecimalField.numberProperty().addListener( (observable) -> {
                linearGauge.setValue(lBigDecimalField.getNumber().doubleValue());
            });
        }
        lRowIdx++;
        
        // min value
        {
            lGridPane.add(new Label("Min value"), new GridPane.C().row(lRowIdx).col(0).halignment(HPos.RIGHT));
            final BigDecimalField lBigDecimalField = new BigDecimalField(BigDecimal.valueOf(linearGauge.getMinValue()));
            lGridPane.add(lBigDecimalField, new GridPane.C().row(lRowIdx).col(1));
            lBigDecimalField.numberProperty().addListener( (observable) -> {
                linearGauge.setMinValue(lBigDecimalField.getNumber().doubleValue());
            });
        }
        lRowIdx++;
        
        // max value
        {
        	lGridPane.add(new Label("Max value"), new GridPane.C().row(lRowIdx).col(0).halignment(HPos.RIGHT));
            final BigDecimalField lBigDecimalField = new BigDecimalField(BigDecimal.valueOf(linearGauge.getMaxValue()));
            lGridPane.add(lBigDecimalField, new GridPane.C().row(lRowIdx).col(1));
            lBigDecimalField.numberProperty().addListener( (observable) -> {
                linearGauge.setMaxValue(lBigDecimalField.getNumber().doubleValue());
            });
        }
        lRowIdx++;
        
        // Animated
        {
            lGridPane.add(new Label("Animated"), new GridPane.C().row(lRowIdx).col(0).halignment(HPos.RIGHT));
            ChoiceBox<SimpleMetroArcGaugeSkin.Animated> lChoiceBox = new ChoiceBox(FXCollections.observableArrayList(SimpleMetroArcGaugeSkin.Animated.values()));
            lGridPane.add(lChoiceBox, new GridPane.C().row(lRowIdx).col(1));
            lChoiceBox.valueProperty().addListener( (observable) -> {
                animatedStyle = "-fxx-animated:" + lChoiceBox.getValue() +";";
                setStyle();
            });
            lChoiceBox.getSelectionModel().select(SimpleMetroArcGaugeSkin.Animated.YES);
        }
        lRowIdx++;
        
        // WarningIndicator
        {
            lGridPane.add(new Label("Warning"), new GridPane.C().row(lRowIdx).col(0).halignment(HPos.RIGHT));
            CheckBox lCheckBox = new CheckBox();
            lGridPane.add(lCheckBox, new GridPane.C().row(lRowIdx).col(1));
            lCheckBox.selectedProperty().addListener( (observable) -> {
            	warningIndicatorStyle = "-fxx-warning-indicator-visibility: " + (lCheckBox.isSelected() ? "visible" : "hidden") + ";";
                setStyle();
            });
            lCheckBox.setSelected(false);
        }
        lRowIdx++;
        
        // ErrorIndicator
        {
            lGridPane.add(new Label("Error"), new GridPane.C().row(lRowIdx).col(0).halignment(HPos.RIGHT));
            CheckBox lCheckBox = new CheckBox();
            lGridPane.add(lCheckBox, new GridPane.C().row(lRowIdx).col(1));
            lCheckBox.selectedProperty().addListener( (observable) -> {
            	errorIndicatorStyle = "-fxx-error-indicator-visibility: " + (lCheckBox.isSelected() ? "visible" : "hidden") + ";";
                setStyle();
            });
            lCheckBox.setSelected(false);
        }
        lRowIdx++;

// not working as expected        
//        // needle format
//        {
//            Label lLabel = new Label("Needle DecimalFormat");
//            TextField lDateFormatTextField = new TextField();
//            lDateFormatTextField.setTooltip(new Tooltip("A DecimalFormat used to render the value, for example: ##0.0W"));
//            lGridPane.add(lLabel, new GridPane.C().row(lRowIdx).col(0).halignment(HPos.RIGHT));
//            lGridPane.add(lDateFormatTextField, new GridPane.C().row(lRowIdx).col(1));
//            lDateFormatTextField.focusedProperty().addListener( (observable) -> {
//            	formatStyle = "-fxx-value-format:' " + lDateFormatTextField.getText() + "';";
//                setStyle();
//			});
//        }
//        lRowIdx++;

        // Segments
        {
        	GridPane lSegmentGridPane = new GridPane();

        	// add button
            lGridPane.add(new Label("Segment"), new GridPane.C().row(lRowIdx).col(0).halignment(HPos.RIGHT));
            Button lButton = new Button("Add new segment");
            lGridPane.add(lButton, new GridPane.C().row(lRowIdx).col(1));
            lButton.setOnAction( (actionEvent) -> {
            	// add a segment
            	BigDecimalField lBigDecimalField = new BigDecimalField(BigDecimal.valueOf(10.0));
				segmentBigDecimalFields.add(lBigDecimalField);
				lSegmentGridPane.add(lBigDecimalField, new GridPane.C().row(segmentBigDecimalFields.size()).col(1));
            	lBigDecimalField.numberProperty().addListener( (observable) -> {
            		setSegments();
            	});
            	setSegments();
            	
            	// remove a segment
            	Button lDeleteButton = new Button("delete");
                lDeleteButton.setOnAction( (actionEvent2) -> {
    				segmentBigDecimalFields.remove(lBigDecimalField);
    				lSegmentGridPane.remove(lBigDecimalField);
    				lSegmentGridPane.remove(lDeleteButton);
            		setSegments();
                });            	
				lSegmentGridPane.add(lDeleteButton, new GridPane.C().row(segmentBigDecimalFields.size()).col(2));
			});

            lRowIdx++;

            // list of segments
            lGridPane.add(new ScrollPane(lSegmentGridPane), new GridPane.C().row(lRowIdx).col(1));
        }
        lRowIdx++;
        
        // Segment Colorschemes
        {
            lGridPane.add(new Label("Segment colorscheme"), new GridPane.C().row(lRowIdx).col(0).halignment(HPos.RIGHT));
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
            	linearGauge.getStyleClass().remove(colorSchemeClass);
                colorSchemeClass = lChoiceBox.getValue();
                linearGauge.getStyleClass().add(colorSchemeClass);
            });
        }
        lRowIdx++;
        
        // done
        return lGridPane;
    }
    protected int lRowIdx = 0;
    private LinearGauge<T> linearGauge = null;
    private String colorSchemeClass = "";

    private void setSegments() {
    	// first calculate total
    	double lTotal = 0.0;
    	for (BigDecimalField lBigDecimalField : segmentBigDecimalFields) {
    		lTotal += lBigDecimalField.getNumber().doubleValue();
    	}
    	
    	linearGauge.segments().clear();
    	double lLastPercentage = 0.0;
    	for (BigDecimalField lBigDecimalField : segmentBigDecimalFields) {
    		double lNextPercentage = lLastPercentage + (lBigDecimalField.getNumber().doubleValue() / lTotal * 100.0);
    		Segment lSegment = new PercentSegment(linearGauge, lLastPercentage, lNextPercentage);
    		linearGauge.segments().add(lSegment);
    		lLastPercentage = lNextPercentage;
    	}
    }
    private final List<BigDecimalField> segmentBigDecimalFields = new ArrayList<>();
    

    /**
     *
     */
    protected String setStyle() {
        String style = animatedStyle + warningIndicatorStyle + errorIndicatorStyle + formatStyle;
		linearGauge.setStyle(style);
		return style;
    }
    private String animatedStyle = "";
    private String warningIndicatorStyle = "";
    private String errorIndicatorStyle = "";
    private String formatStyle = "";

}