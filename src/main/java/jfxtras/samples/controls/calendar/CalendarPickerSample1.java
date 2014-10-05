package jfxtras.samples.controls.calendar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import jfxtras.internal.scene.control.skin.CalendarPickerControlSkin;
import jfxtras.internal.scene.control.skin.ListSpinnerSkin;
import jfxtras.samples.JFXtrasSampleBase;
import jfxtras.scene.control.CalendarPicker;
import jfxtras.scene.control.CalendarTextField;
import jfxtras.scene.layout.GridPane;
import jfxtras.scene.layout.HBox;
import jfxtras.scene.layout.VBox;

public class CalendarPickerSample1 extends JFXtrasSampleBase
{
    public CalendarPickerSample1() {
        calendarPicker = new CalendarPicker();
    }
    final CalendarPicker calendarPicker;

    @Override
    public String getSampleName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public String getSampleDescription() {
        return "CalendarPicker is a stand alone date picker, based on Java's Calendar class (hence the name CalendarPicker). It can be used stand alone, but also is used by CalendarTextField. \n"
        	 + "Other implementations are available as well, based on Java 8 Data API, like LocalDatePicker, LocalDateTimePicker, ..."
        	 ;
    }

    @Override
    public Node getPanel(Stage stage) {
		this.stage = stage;
		
        VBox root = new VBox(20);
        root.setPadding(new Insets(30, 30, 30, 30));

        root.getChildren().addAll(calendarPicker);

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

        // Mode
        {
            lGridPane.add(new Label("Mode"), new GridPane.C().row(lRowIdx).col(0).halignment(HPos.RIGHT));
            ChoiceBox<CalendarPicker.Mode> lChoiceBox = new ChoiceBox<>(FXCollections.observableArrayList(CalendarPicker.Mode.values()));
            lGridPane.add(lChoiceBox, new GridPane.C().row(lRowIdx).col(1));
            lChoiceBox.valueProperty().bindBidirectional(calendarPicker.modeProperty());
        }
        lRowIdx++;

        // Locale
        {
            lGridPane.add(new Label("Locale"), new GridPane.C().row(lRowIdx).col(0).halignment(HPos.RIGHT));
            ObservableList<Locale> lLocales = FXCollections.observableArrayList(Locale.getAvailableLocales());
            FXCollections.sort(lLocales,  (o1, o2) -> { return o1.toString().compareTo(o2.toString()); } );
            ComboBox<Locale> lComboBox = new ComboBox<>( lLocales );
            lComboBox.converterProperty().set(new StringConverter<Locale>() {
                @Override
                public String toString(Locale locale) {
                    return locale == null ? "-"  : locale.toString();
                }

                @Override
                public Locale fromString(String s) {
                    if ("-".equals(s)) return null;
                    return new Locale(s);
                }
            });
            lComboBox.setEditable(true);
            lGridPane.add(lComboBox, new GridPane.C().row(lRowIdx).col(1));
            lComboBox.valueProperty().bindBidirectional(calendarPicker.localeProperty());
        }
        lRowIdx++;

        // allowNull
        {
            Label lLabel = new Label("Null allowed");
            lGridPane.add(lLabel, new GridPane.C().row(lRowIdx).col(0).halignment(HPos.RIGHT));
            CheckBox lCheckBox = new CheckBox();
            lCheckBox.setTooltip(new Tooltip("Is the control allowed to hold null (or have no calendar deselected)"));
            lGridPane.add(lCheckBox, new GridPane.C().row(lRowIdx).col(1));
            lCheckBox.selectedProperty().bindBidirectional(calendarPicker.allowNullProperty());
        }
        lRowIdx++;

        // showTime
        {
            Label lLabel = new Label("Show time");
            lGridPane.add(lLabel, new GridPane.C().row(lRowIdx).col(0).halignment(HPos.RIGHT));
            CheckBox lCheckBox = new CheckBox();
            lCheckBox.setTooltip(new Tooltip("Only in SINGLE mode"));
            lGridPane.add(lCheckBox, new GridPane.C().row(lRowIdx).col(1));
            lCheckBox.selectedProperty().bindBidirectional(calendarPicker.showTimeProperty());
        }
        lRowIdx++;

        // calendar
        {
            Label lLabel = new Label("Value");
            lGridPane.add(lLabel, new GridPane.C().row(lRowIdx).col(0).halignment(HPos.RIGHT));
            final CalendarTextField lCalendarTextField = new CalendarTextField();
            lCalendarTextField.setTooltip(new Tooltip("The currently selected date (single mode)"));
            lCalendarTextField.setDisable(true);
            lGridPane.add(lCalendarTextField, new GridPane.C().row(lRowIdx).col(1));
            lCalendarTextField.calendarProperty().bindBidirectional(calendarPicker.calendarProperty());
            calendarPicker.showTimeProperty().addListener( (observable) -> {
                lCalendarTextField.setDateFormat( calendarPicker.getShowTime() ? SimpleDateFormat.getDateTimeInstance() : SimpleDateFormat.getDateInstance() );
            });
            lCalendarTextField.setDateFormat( calendarPicker.getShowTime() ? SimpleDateFormat.getDateTimeInstance() : SimpleDateFormat.getDateInstance() );
        }
        lRowIdx++;

        // calendars
        {
            Label lLabel = new Label("Selected");
            lGridPane.add(lLabel, new GridPane.C().row(lRowIdx).col(0).halignment(HPos.RIGHT).valignment(VPos.TOP));
            final ListView<java.util.Calendar> lListView = new ListView<>();
            lListView.setTooltip(new Tooltip("All selected dates (multiple or range mode)"));
            lListView.setItems(calendarPicker.calendars());
            lListView.setCellFactory(TextFieldListCell.forListView(new StringConverter<java.util.Calendar>() {
                @Override
                public String toString(java.util.Calendar o) {
                    DateFormat lDateFormat = calendarPicker.getShowTime() ? SimpleDateFormat.getDateTimeInstance() : SimpleDateFormat.getDateInstance();
                    return o == null ? "" : lDateFormat.format(o.getTime());
                }

                @Override
                public java.util.Calendar fromString(String s) {
                    return null;  //never used
                }
            }));
            lGridPane.add(lListView, new GridPane.C().row(lRowIdx).col(1));
        }
        lRowIdx++;

        // highlight
		{
			lRowIdx = addObservableListManagementControlsToGridPane("Highlighted", "All highlighted dates", lGridPane, lRowIdx, calendarPicker.highlightedCalendars(), new CalendarTextField()
				, (Control c) -> {
					CalendarTextField lCalendarTextField = (CalendarTextField)c;
					Calendar lCalendar = lCalendarTextField.getCalendar();
					lCalendarTextField.setCalendar(null);
					return lCalendar;
				}
				, (Calendar t) -> {
					DateFormat lDateFormat = calendarPicker.getShowTime() ? SimpleDateFormat.getDateTimeInstance() : SimpleDateFormat.getDateInstance();
					return t == null ? "" : lDateFormat.format(t.getTime());
				}
			);
		}

        // disabled
		{
			lRowIdx = addObservableListManagementControlsToGridPane("Disabled", "All disabled dates", lGridPane, lRowIdx, calendarPicker.disabledCalendars(), new CalendarTextField()
				, (Control c) -> {
					CalendarTextField lCalendarTextField = (CalendarTextField)c;
					Calendar lCalendar = lCalendarTextField.getCalendar();
					lCalendarTextField.setCalendar(null);
					return lCalendar;
				}
				, (Calendar t) -> {
					DateFormat lDateFormat = calendarPicker.getShowTime() ? SimpleDateFormat.getDateTimeInstance() : SimpleDateFormat.getDateInstance();
					return t == null ? "" : lDateFormat.format(t.getTime());
				}
			);
		}

        // calendarRangeCallback
        {
            Label lLabel = new Label("Range callback");
            lGridPane.add(lLabel, new GridPane.C().row(lRowIdx).col(0).halignment(HPos.RIGHT));
            HBox lHBox = new HBox();
            lGridPane.add(lHBox, new GridPane.C().row(lRowIdx).col(1));
            final CheckBox lCheckBox = new CheckBox();
            lHBox.add(lCheckBox);
            lCheckBox.setTooltip(new Tooltip("Register a callback and show what the range change data is"));
            final TextField lTextField = new TextField();
            lHBox.add(lTextField, new HBox.C().hgrow(Priority.ALWAYS));
            lCheckBox.selectedProperty().addListener( (invalidationEvent) -> {
            	if (lCheckBox.selectedProperty().get()) {
            		calendarPicker.setCalendarRangeCallback( (range) -> {
            			lTextField.setText(format(range.getStartCalendar()) + " - " + format(range.getEndCalendar()));
						return null;
					});
            	}
            	else {
            		calendarPicker.setCalendarRangeCallback(null);
        			lTextField.setText("");
            	}
            });
        }
        lRowIdx++;

		// stylesheet
		{		
			Label lLabel = new Label("Stage Stylesheet");
			lGridPane.add(lLabel, new GridPane.C().row(lRowIdx).col(0).halignment(HPos.RIGHT).valignment(VPos.TOP));
			TextArea lTextArea = createTextAreaForCSS(stage, FXCollections.observableArrayList(
				".CalendarPicker {\n\t-fxx-show-weeknumbers:NO; /* " +  Arrays.toString(CalendarPickerControlSkin.ShowWeeknumbers.values()) + " */\n}",
				".CalendarPicker {\n\t-fxx-label-dateformat:\"D\"; /* See SimpleDateFormat, e.g. 'D' for day-of-year */\n}",				
				".CalendarTimePicker {\n\t-fxx-label-dateformat:\"HH:mm:ss\"; /* See SimpleDateFormat, e.g. 'HH' for 24 hour day */\n}",				
				".ListSpinner {\n\t-fxx-arrow-position:SPLIT; /* " + Arrays.toString(ListSpinnerSkin.ArrowPosition.values()) + " */ \n}",
				".ListSpinner {\n\t-fxx-arrow-direction:VERTICAL; /* " + Arrays.toString(ListSpinnerSkin.ArrowDirection.values()) + " */ \n}")
			);
			lGridPane.add(lTextArea, new GridPane.C().row(lRowIdx).col(1).vgrow(Priority.ALWAYS).minHeight(100.0));
		}
        lRowIdx++;

		// done
        return lGridPane;
    }
    final TextField labelDateFormatTextField = new TextField("d");
    
    private String format(Calendar c) {
    	if (c == null) {
    		return "";
    	}
    	return simpleDateFormatYYYMMDD_HHMMSS.format(c.getTime());
    }
    final SimpleDateFormat simpleDateFormatYYYMMDD_HHMMSS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public String getJavaDocURL() {
		return "http://jfxtras.org/doc/8.0/jfxtras-controls/" + CalendarPicker.class.getName().replace(".", "/") + ".html";
    }

    public static void main(String[] args) {
        launch(args);
    }
}