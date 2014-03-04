package jfxtras.samples.controls.datetime;

import java.time.LocalDateTime;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import jfxtras.internal.scene.control.skin.CalendarPickerControlSkin;
import jfxtras.internal.scene.control.skin.ListSpinnerSkin;
import jfxtras.labs.samples.JFXtrasLabsSampleBase;
import jfxtras.samples.JFXtrasSampleBase;
import jfxtras.scene.control.LocalDateTimePicker;
import jfxtras.scene.control.LocalDateTimeTextField;
import jfxtras.scene.layout.GridPane;
import jfxtras.scene.layout.VBox;

public class LocalDateTimePickerSample1 extends JFXtrasSampleBase
{
    public LocalDateTimePickerSample1() {
        localDateTimePicker = new LocalDateTimePicker();
    }
    final LocalDateTimePicker localDateTimePicker;

    @Override
    public String getSampleName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public String getSampleDescription() {
        return "Basic LocalDateTimePicker usage";
    }

    @Override
    public Node getPanel(Stage stage) {
		this.stage = stage;

        VBox root = new VBox(20);
        root.setPadding(new Insets(30, 30, 30, 30));

        root.getChildren().addAll(localDateTimePicker);

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
            lComboBox.valueProperty().bindBidirectional(localDateTimePicker.localeProperty());
        }
        lRowIdx++;

        // nullAllowed
        {
            Label lLabel = new Label("Null allowed");
            lLabel.setTooltip(new Tooltip("Is the control allowed to hold null (or have no DateTime deselected)"));
            lGridPane.add(lLabel, new GridPane.C().row(lRowIdx).col(0).halignment(HPos.RIGHT));
            CheckBox lCheckBox = new CheckBox();
            lGridPane.add(lCheckBox, new GridPane.C().row(lRowIdx).col(1));
            lCheckBox.selectedProperty().bindBidirectional(localDateTimePicker.allowNullProperty());
        }
        lRowIdx++;

        // DateTime
        {
            Label lLabel = new Label("Value");
            lLabel.setTooltip(new Tooltip("The currently selected date (single mode)"));
            lGridPane.add(lLabel, new GridPane.C().row(lRowIdx).col(0).halignment(HPos.RIGHT));
            final LocalDateTimeTextField lDateTimeTextField = new LocalDateTimeTextField();
            lDateTimeTextField.setDisable(true);
            lGridPane.add(lDateTimeTextField, new GridPane.C().row(lRowIdx).col(1));
            lDateTimeTextField.localDateTimeProperty().bindBidirectional(localDateTimePicker.localDateTimeProperty());
        }
        lRowIdx++;

        // highlight
		{
			lRowIdx = addObservableListManagementControlsToGridPane("Highlighted", "All highlighted dates", lGridPane, lRowIdx, localDateTimePicker.highlightedLocalDateTimes(), new LocalDateTimeTextField()
				, (Control c) -> {
					LocalDateTimeTextField lLocalDateTimeTextField = (LocalDateTimeTextField)c;
					LocalDateTime lLocalDateTime = lLocalDateTimeTextField.getLocalDateTime();
					lLocalDateTimeTextField.setLocalDateTime(null);
					return lLocalDateTime;
				}
				, (LocalDateTime t) -> {
					return t == null ? "" : DateTimeFormatter.ISO_DATE_TIME.format(t);
				}
			);
		}

        // disabled
		{
			lRowIdx = addObservableListManagementControlsToGridPane("Disabled", "All disabled dates", lGridPane, lRowIdx, localDateTimePicker.disabledLocalDateTimes(), new LocalDateTimeTextField()
				, (Control c) -> {
					LocalDateTimeTextField lLocalDateTimeTextField = (LocalDateTimeTextField)c;
					LocalDateTime lLocalDateTime = lLocalDateTimeTextField.getLocalDateTime();
					lLocalDateTimeTextField.setLocalDateTime(null);
					return lLocalDateTime;
				}
				, (LocalDateTime t) -> {
					return t == null ? "" : DateTimeFormatter.ISO_DATE_TIME.format(t);
				}
			);
		}

		// stylesheet
		{		
			Label lLabel = new Label("Stage Stylesheet");
			lLabel.setTooltip(new Tooltip("To test how CSS will modify the displayed nodes"));
			lGridPane.add(lLabel, new GridPane.C().row(lRowIdx).col(0).halignment(HPos.RIGHT).valignment(VPos.TOP));
			TextArea lTextArea = createTextAreaForCSS(stage, FXCollections.observableArrayList(
				".LocalDateTimePicker {\n\t-fxx-show-weeknumbers:NO; /* " +  Arrays.toString(CalendarPickerControlSkin.ShowWeeknumbers.values()) + " */\n}",
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
		return "http://jfxtras.org/doc/8.0/" + LocalDateTimePicker.class.getName().replace(".", "/") + ".html";
    }

    public static void main(String[] args) {
        launch(args);
    }
}