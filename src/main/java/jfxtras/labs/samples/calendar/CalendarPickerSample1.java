package jfxtras.labs.samples.calendar;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import jfxtras.labs.samples.JFXtrasSampleBase;
import jfxtras.labs.scene.control.CalendarPicker;
import jfxtras.labs.scene.control.CalendarTextField;
import jfxtras.labs.scene.layout.GridPane;
import jfxtras.labs.scene.layout.VBox;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;
import jfxtras.labs.internal.scene.control.skin.CalendarPickerControlSkin;
import jfxtras.labs.internal.scene.control.skin.ListSpinnerCaspianSkin;

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
        return "Basic CalendarPicker usage";
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
            ChoiceBox<CalendarPicker.Mode> lChoiceBox = new ChoiceBox(FXCollections.observableArrayList(CalendarPicker.Mode.values()));
            lGridPane.add(lChoiceBox, new GridPane.C().row(lRowIdx).col(1));
            lChoiceBox.valueProperty().bindBidirectional(calendarPicker.modeProperty());
        }
        lRowIdx++;

        // Locale
        {
            lGridPane.add(new Label("Locale"), new GridPane.C().row(lRowIdx).col(0).halignment(HPos.RIGHT));
            ObservableList<Locale> lLocales = FXCollections.observableArrayList(Locale.getAvailableLocales());
            FXCollections.sort(lLocales,  (o1, o2) -> { return o1.toString().compareTo(o2.toString()); } );
            ComboBox<Locale> lComboBox = new ComboBox( lLocales );
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

        // nullAllowed
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
            final ListView lListView = new ListView();
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

		// stylesheet
		{		
			Label lLabel = new Label("Stage Stylesheet");
			lGridPane.add(lLabel, new GridPane.C().row(lRowIdx).col(0).halignment(HPos.RIGHT).valignment(VPos.TOP));
			TextArea lTextArea = createTextAreaForCSS(stage, FXCollections.observableArrayList(
				".CalendarPicker {\n\t-fxx-show-weeknumbers:NO; /* " +  Arrays.toString(CalendarPickerControlSkin.ShowWeeknumbers.values()) + " */\n}",
				".CalendarPicker {\n\t-fxx-label-dateformat:\"D\"; /* See SimpleDateFormat, e.g. 'D' for day-of-year */\n}",				
				".ListSpinner {\n\t-fxx-arrow-position:SPLIT; /* " + Arrays.toString(ListSpinnerCaspianSkin.ArrowPosition.values()) + " */ \n}",
				".ListSpinner {\n\t-fxx-arrow-direction:VERTICAL; /* " + Arrays.toString(ListSpinnerCaspianSkin.ArrowDirection.values()) + " */ \n}"));
			lGridPane.add(lTextArea, new GridPane.C().row(lRowIdx).col(1).vgrow(Priority.ALWAYS).minHeight(100.0));
		}
        lRowIdx++;

		// done
        return lGridPane;
    }
    final TextField labelDateFormatTextField = new TextField("d");

    @Override
    public String getJavaDocURL() {
		return "http://jfxtras.org/doc/8.0/" + CalendarPicker.class.getName().replace(".", "/") + ".html";
    }

    public static void main(String[] args) {
        launch(args);
    }
}