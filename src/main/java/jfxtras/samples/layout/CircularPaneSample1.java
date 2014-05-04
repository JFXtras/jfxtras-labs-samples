package jfxtras.samples.layout;

import com.sun.prism.impl.shape.ShapeUtil;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import jfxtras.labs.scene.layout.CircularPane;
import jfxtras.samples.JFXtrasSampleBase;
import jfxtras.scene.layout.GridPane;
import jfxtras.scene.layout.VBox;

public class CircularPaneSample1 extends JFXtrasSampleBase
{
    public CircularPaneSample1() {
    	circularPane = new CircularPane();
    	for (int i = 0; i < 12; i++) {
    		circularPane.add(new Rectangle(30, 30));
    	}
    }
    final CircularPane circularPane;

    @Override
    public String getSampleName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public String getSampleDescription() {
        return "Basic CircularPane usage";
    }

    @Override
    public Node getPanel(Stage stage) {
		this.stage = stage;
		
        VBox root = new VBox(20);
        root.setPadding(new Insets(30, 30, 30, 30));

        root.getChildren().addAll(circularPane);

        return root;
    }
	private Stage stage;
	
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

        // showDebug
        {
            Label lLabel = new Label("Show debug");
            lGridPane.add(lLabel, new GridPane.C().row(lRowIdx).col(0).halignment(HPos.RIGHT));
            CheckBox lCheckBox = new CheckBox();
            lCheckBox.setTooltip(new Tooltip("Show layout debug hints"));
            lGridPane.add(lCheckBox, new GridPane.C().row(lRowIdx).col(1));
            lCheckBox.selectedProperty().addListener( (invalidationEvent) -> {
            	circularPane.setShowDebug( lCheckBox.isSelected() ? Color.GREEN : null );
            });
        }
        lRowIdx++;
        
        // Mode
        {
            lGridPane.add(new Label("Shape"), new GridPane.C().row(lRowIdx).col(0).halignment(HPos.RIGHT));
            lGridPane.add(shapeChoiceBox, new GridPane.C().row(lRowIdx).col(1));
            shapeChoiceBox.getSelectionModel().select(0);
            shapeChoiceBox.getSelectionModel().selectedItemProperty().addListener((invalidationEvent) -> {
            	reconstructPane();
            });
        }
        lRowIdx++;

        // Children are circular
        {
            Label lLabel = new Label("Children are circular");
            lGridPane.add(lLabel, new GridPane.C().row(lRowIdx).col(0).halignment(HPos.RIGHT));
            CheckBox lCheckBox = new CheckBox();
            lCheckBox.setTooltip(new Tooltip("Enable the optimized rendering for when all children are circular (or smaller)"));
            lGridPane.add(lCheckBox, new GridPane.C().row(lRowIdx).col(1));
            circularPane.childrenAreCircularProperty().bind(lCheckBox.selectedProperty());
            lCheckBox.selectedProperty().addListener( (invalidationEvent) -> {
            	System.out.println("invalidated");
            });
            lCheckBox.selectedProperty().addListener( (e, o, n) -> {
            	System.out.println("changed " + o + " -> " + n);
            });
            circularPane.childrenAreCircularProperty().addListener( (invalidationEvent) -> {
            	System.out.println("invalidated cp");
            });
            circularPane.childrenAreCircularProperty().addListener( (e, o, n) -> {
            	System.out.println("changed cp " + o + " -> " + n);
            });
        }
        lRowIdx++;
        
//        // Locale
//        {
//            lGridPane.add(new Label("Locale"), new GridPane.C().row(lRowIdx).col(0).halignment(HPos.RIGHT));
//            ObservableList<Locale> lLocales = FXCollections.observableArrayList(Locale.getAvailableLocales());
//            FXCollections.sort(lLocales,  (o1, o2) -> { return o1.toString().compareTo(o2.toString()); } );
//            ComboBox<Locale> lComboBox = new ComboBox<>( lLocales );
//            lComboBox.converterProperty().set(new StringConverter<Locale>() {
//                @Override
//                public String toString(Locale locale) {
//                    return locale == null ? "-"  : locale.toString();
//                }
//
//                @Override
//                public Locale fromString(String s) {
//                    if ("-".equals(s)) return null;
//                    return new Locale(s);
//                }
//            });
//            lComboBox.setEditable(true);
//            lGridPane.add(lComboBox, new GridPane.C().row(lRowIdx).col(1));
//            lComboBox.valueProperty().bindBidirectional(calendarPicker.localeProperty());
//        }
//        lRowIdx++;
//
//        // nullAllowed
//        {
//            Label lLabel = new Label("Null allowed");
//            lGridPane.add(lLabel, new GridPane.C().row(lRowIdx).col(0).halignment(HPos.RIGHT));
//            CheckBox lCheckBox = new CheckBox();
//            lCheckBox.setTooltip(new Tooltip("Is the control allowed to hold null (or have no calendar deselected)"));
//            lGridPane.add(lCheckBox, new GridPane.C().row(lRowIdx).col(1));
//            lCheckBox.selectedProperty().bindBidirectional(calendarPicker.allowNullProperty());
//        }
//        lRowIdx++;
//
//        // calendar
//        {
//            Label lLabel = new Label("Value");
//            lGridPane.add(lLabel, new GridPane.C().row(lRowIdx).col(0).halignment(HPos.RIGHT));
//            final CalendarTextField lCalendarTextField = new CalendarTextField();
//            lCalendarTextField.setTooltip(new Tooltip("The currently selected date (single mode)"));
//            lCalendarTextField.setDisable(true);
//            lGridPane.add(lCalendarTextField, new GridPane.C().row(lRowIdx).col(1));
//            lCalendarTextField.calendarProperty().bindBidirectional(calendarPicker.calendarProperty());
//            calendarPicker.showTimeProperty().addListener( (observable) -> {
//                lCalendarTextField.setDateFormat( calendarPicker.getShowTime() ? SimpleDateFormat.getDateTimeInstance() : SimpleDateFormat.getDateInstance() );
//            });
//            lCalendarTextField.setDateFormat( calendarPicker.getShowTime() ? SimpleDateFormat.getDateTimeInstance() : SimpleDateFormat.getDateInstance() );
//        }
//        lRowIdx++;
//
//        // calendars
//        {
//            Label lLabel = new Label("Selected");
//            lGridPane.add(lLabel, new GridPane.C().row(lRowIdx).col(0).halignment(HPos.RIGHT).valignment(VPos.TOP));
//            final ListView<java.util.Calendar> lListView = new ListView<>();
//            lListView.setTooltip(new Tooltip("All selected dates (multiple or range mode)"));
//            lListView.setItems(calendarPicker.calendars());
//            lListView.setCellFactory(TextFieldListCell.forListView(new StringConverter<java.util.Calendar>() {
//                @Override
//                public String toString(java.util.Calendar o) {
//                    DateFormat lDateFormat = calendarPicker.getShowTime() ? SimpleDateFormat.getDateTimeInstance() : SimpleDateFormat.getDateInstance();
//                    return o == null ? "" : lDateFormat.format(o.getTime());
//                }
//
//                @Override
//                public java.util.Calendar fromString(String s) {
//                    return null;  //never used
//                }
//            }));
//            lGridPane.add(lListView, new GridPane.C().row(lRowIdx).col(1));
//        }
//        lRowIdx++;
//
//		// stylesheet
//		{		
//			Label lLabel = new Label("Stage Stylesheet");
//			lGridPane.add(lLabel, new GridPane.C().row(lRowIdx).col(0).halignment(HPos.RIGHT).valignment(VPos.TOP));
//			TextArea lTextArea = createTextAreaForCSS(stage, FXCollections.observableArrayList(
//				".CalendarPicker {\n\t-fxx-show-weeknumbers:NO; /* " +  Arrays.toString(CalendarPickerControlSkin.ShowWeeknumbers.values()) + " */\n}",
//				".CalendarPicker {\n\t-fxx-label-dateformat:\"D\"; /* See SimpleDateFormat, e.g. 'D' for day-of-year */\n}",				
//				".CalendarTimePicker {\n\t-fxx-label-dateformat:\"HH:mm:ss\"; /* See SimpleDateFormat, e.g. 'HH' for 24 hour day */\n}",				
//				".ListSpinner {\n\t-fxx-arrow-position:SPLIT; /* " + Arrays.toString(ListSpinnerSkin.ArrowPosition.values()) + " */ \n}",
//				".ListSpinner {\n\t-fxx-arrow-direction:VERTICAL; /* " + Arrays.toString(ListSpinnerSkin.ArrowDirection.values()) + " */ \n}")
//			);
//			lGridPane.add(lTextArea, new GridPane.C().row(lRowIdx).col(1).vgrow(Priority.ALWAYS).minHeight(100.0));
//		}
//        lRowIdx++;

		// done
    	reconstructPane();
        return lGridPane;
    }
    private ChoiceBox<String> shapeChoiceBox = new ChoiceBox<>(FXCollections.observableArrayList(Rectangle.class.getSimpleName(), Circle.class.getSimpleName()));
    
    private void reconstructPane() {
    	circularPane.getChildren().clear();
    	for (int i = 0; i < 12; i++) {
    		String lShapeName = shapeChoiceBox.getSelectionModel().getSelectedItem();    		
    		if (Circle.class.getSimpleName().equals(lShapeName)) {
        		circularPane.add(new Circle(20));
    		}
    		if (Rectangle.class.getSimpleName().equals(lShapeName)) {
    			circularPane.add(new Rectangle(30, 30));
    		}
    	}
    }

    @Override
    public String getJavaDocURL() {
        return "http://jfxtras.org/doc/8.0labs/" + CircularPane.class.getName().replace(".", "/") + ".html";
		//return "http://jfxtras.org/doc/8.0/jfxtras-common/" + CalendarPicker.class.getName().replace(".", "/") + ".html";
    }
}