package jfxtras.samples.controls.datetime;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
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
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import jfxtras.internal.scene.control.skin.CalendarPickerControlSkin;
import jfxtras.internal.scene.control.skin.ListSpinnerSkin;
import jfxtras.samples.JFXtrasSampleBase;
import jfxtras.scene.control.LocalDatePicker;
import jfxtras.scene.control.LocalDateTextField;
import jfxtras.scene.layout.GridPane;
import jfxtras.scene.layout.VBox;

public class LocalDatePickerSample1 extends JFXtrasSampleBase
{
    public LocalDatePickerSample1() {
        localDatePicker = new LocalDatePicker();
    }
    final private LocalDatePicker localDatePicker;

    @Override
    public String getSampleName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public String getSampleDescription() {
        return "Basic LocalDatePicker usage";
    }

    @Override
    public Node getPanel(Stage stage) {
		this.stage = stage;

		VBox root = new VBox(20);
        root.setPadding(new Insets(30, 30, 30, 30));

        root.getChildren().addAll(localDatePicker);

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
            ChoiceBox<LocalDatePicker.Mode> lChoiceBox = new ChoiceBox(FXCollections.observableArrayList(LocalDatePicker.Mode.values()));
            lGridPane.add(lChoiceBox, new GridPane.C().row(lRowIdx).col(1));
            lChoiceBox.valueProperty().bindBidirectional(localDatePicker.modeProperty());
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
            lComboBox.valueProperty().bindBidirectional(localDatePicker.localeProperty());
        }
        lRowIdx++;

        // nullAllowed
        {
            Label lLabel = new Label("Null allowed");
            lLabel.setTooltip(new Tooltip("Is the control allowed to hold null (or have no DateTime deselected)"));
            lGridPane.add(lLabel, new GridPane.C().row(lRowIdx).col(0).halignment(HPos.RIGHT));
            CheckBox lCheckBox = new CheckBox();
            lGridPane.add(lCheckBox, new GridPane.C().row(lRowIdx).col(1));
            lCheckBox.selectedProperty().bindBidirectional(localDatePicker.allowNullProperty());
        }
        lRowIdx++;

        // DateTime
        {
            Label lLabel = new Label("Value");
            lLabel.setTooltip(new Tooltip("The currently selected date (single mode)"));
            lGridPane.add(lLabel, new GridPane.C().row(lRowIdx).col(0).halignment(HPos.RIGHT));
            final LocalDateTextField lDateTimeTextField = new LocalDateTextField();
            lDateTimeTextField.setDisable(true);
            lGridPane.add(lDateTimeTextField, new GridPane.C().row(lRowIdx).col(1));
            lDateTimeTextField.localDateProperty().bindBidirectional(localDatePicker.localDateProperty());
        }
        lRowIdx++;

        // localDates
        {
            Label lLabel = new Label("Selected");
            lLabel.setTooltip(new Tooltip("All selected dates (multiple or range mode)"));
            lGridPane.add(lLabel, new GridPane.C().row(lRowIdx).col(0).halignment(HPos.RIGHT).valignment(VPos.TOP));
            final ListView lListView = new ListView();

            lListView.setItems(localDatePicker.localDates());
            lListView.setCellFactory(TextFieldListCell.forListView(new StringConverter<LocalDate>() {
                @Override
                public String toString(LocalDate o) {
                    return o == null ? "" : DateTimeFormatter.ISO_DATE.format(o);
                }

                @Override
                public LocalDate fromString(String s) {
                    return null;  //never used
                }
            }));
            lGridPane.add(lListView, new GridPane.C().row(lRowIdx).col(1));
        }
        lRowIdx++;

        // highlight
		{
			lRowIdx = addObservableListManagementControlsToGridPane("Highlighted", "All highlighted dates", lGridPane, lRowIdx, localDatePicker.highlightedLocalDates(), new LocalDateTextField()
				, (Control c) -> {
					LocalDateTextField lLocalDateTextField = (LocalDateTextField)c;
					LocalDate lLocalDate = lLocalDateTextField.getLocalDate();
					lLocalDateTextField.setLocalDate(null);
					return lLocalDate;
				}
				, (LocalDate t) -> {
					return t == null ? "" : DateTimeFormatter.ISO_DATE.format(t);
				}
			);
		}

        // disabled
		{
			lRowIdx = addObservableListManagementControlsToGridPane("Disabled", "All disabled dates", lGridPane, lRowIdx, localDatePicker.disabledLocalDates(), new LocalDateTextField()
				, (Control c) -> {
					LocalDateTextField lLocalDateTextField = (LocalDateTextField)c;
					LocalDate lLocalDate = lLocalDateTextField.getLocalDate();
					lLocalDateTextField.setLocalDate(null);
					return lLocalDate;
				}
				, (LocalDate t) -> {
					return t == null ? "" : DateTimeFormatter.ISO_DATE.format(t);
				}
			);
		}

		// stylesheet
		{		
			Label lLabel = new Label("Stage Stylesheet");
			lLabel.setTooltip(new Tooltip("To test how CSS will modify the displayed nodes"));
			lGridPane.add(lLabel, new GridPane.C().row(lRowIdx).col(0).halignment(HPos.RIGHT).valignment(VPos.TOP));
			TextArea lTextArea = createTextAreaForCSS(stage, FXCollections.observableArrayList(
				".LocalDatePicker {\n\t-fxx-show-weeknumbers:NO; /* " +  Arrays.toString(CalendarPickerControlSkin.ShowWeeknumbers.values()) + " */\n}",
				".ListSpinner {\n\t-fxx-arrow-position:SPLIT; /* " + Arrays.toString(ListSpinnerSkin.ArrowPosition.values()) + " */ \n}",
				".ListSpinner {\n\t-fxx-arrow-direction:VERTICAL; /* " + Arrays.toString(ListSpinnerSkin.ArrowDirection.values()) + " */ \n}"));
			lGridPane.add(lTextArea, new GridPane.C().row(lRowIdx).col(1).vgrow(Priority.ALWAYS).minHeight(100.0));
		}
        lRowIdx++;
		
        // done
        return lGridPane;
    }

    @Override
    public String getJavaDocURL() {
		return "http://jfxtras.org/doc/8.0/jfxtras-controls/" + LocalDatePicker.class.getName().replace(".", "/") + ".html";
    }

    public static void main(String[] args) {
        launch(args);
    }
}