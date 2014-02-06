package jfxtras.labs.samples.datetime;

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
import jfxtras.labs.scene.control.LocalDateTimePicker;
import jfxtras.labs.scene.control.LocalDateTimeTextField;
import jfxtras.labs.scene.layout.GridPane;
import jfxtras.labs.scene.layout.VBox;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.time.LocalDateTime;
import java.util.Arrays;
import jfxtras.labs.internal.scene.control.skin.CalendarPickerControlSkin;
import jfxtras.labs.internal.scene.control.skin.ListSpinnerCaspianSkin;

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
            lDateTimeTextField.setDateFormat( SimpleDateFormat.getDateTimeInstance() );
        }
        lRowIdx++;

		// highlight
		{
			Label lLabel = new Label("Highlighted");
			lLabel.setTooltip(new Tooltip("All highlighted dates"));
			lGridPane.add(lLabel, new GridPane.C().row(lRowIdx).col(0).halignment(HPos.RIGHT).valignment(VPos.TOP));

			// text field
			final LocalDateTimeTextField lLocalDateTimeTextField = new LocalDateTimeTextField();
			lGridPane.add(lLocalDateTimeTextField, new GridPane.C().row(lRowIdx).col(1));
			// add button
			{
				Button lButton = new Button("add");
				lGridPane.add(lButton, new GridPane.C().row(lRowIdx).col(2).valignment(VPos.TOP).hgrow(Priority.ALWAYS));
				lButton.onActionProperty().set( (actionEvent) -> {
					LocalDateTime c = lLocalDateTimeTextField.getLocalDateTime();
					if (c != null) {
						localDateTimePicker.highlightedLocalDateTimes().add(c);
						lLocalDateTimeTextField.setLocalDateTime(null);
					}
				});
			}

			lRowIdx++;

			final ListView<LocalDateTime> lListView = new ListView<LocalDateTime>();
			lListView.setItems(localDateTimePicker.highlightedLocalDateTimes());
			lListView.setCellFactory(TextFieldListCell.forListView(new StringConverter<LocalDateTime>() {
				@Override
				public String toString(LocalDateTime o) {
					return o == null ? "" : DateTimeFormatter.ISO_DATE_TIME.format(o);
				}

				@Override
				public LocalDateTime fromString(String s) {
					return null;  //never used
				}
			}));
			lGridPane.add(lListView, new GridPane.C().row(lRowIdx).col(1));
			// remove button
			{
				Button lButton = new Button("remove");
				lGridPane.add(lButton, new GridPane.C().row(lRowIdx).col(2).valignment(VPos.TOP).hgrow(Priority.ALWAYS));
				lButton.onActionProperty().set( (actionEvent) -> {
					LocalDateTime c = lListView.getSelectionModel().getSelectedItem();
					if (c != null) {
						localDateTimePicker.highlightedLocalDateTimes().remove(c);
					}
				});
			}
		}
		lRowIdx++;

		// disabled
		{
			Label lLabel = new Label("Disabled");
			lLabel.setTooltip(new Tooltip("All disabled dates"));
			lGridPane.add(lLabel, new GridPane.C().row(lRowIdx).col(0).halignment(HPos.RIGHT).valignment(VPos.TOP));

			// text field
			final LocalDateTimeTextField lLocalDateTimeTextField = new LocalDateTimeTextField();
			lGridPane.add(lLocalDateTimeTextField, new GridPane.C().row(lRowIdx).col(1));
			// add button
			{
				Button lButton = new Button("add");
				lGridPane.add(lButton, new GridPane.C().row(lRowIdx).col(2).valignment(VPos.TOP).hgrow(Priority.ALWAYS));
				lButton.onActionProperty().set( (actionEvent) -> {
					LocalDateTime c = lLocalDateTimeTextField.getLocalDateTime();
					if (c != null) {
						localDateTimePicker.disabledLocalDateTimes().add(c);
						lLocalDateTimeTextField.setLocalDateTime(null);
					}
				});
			}

			lRowIdx++;

			final ListView<LocalDateTime> lListView = new ListView<LocalDateTime>();
			lListView.setItems(localDateTimePicker.disabledLocalDateTimes());
			lListView.setCellFactory(TextFieldListCell.forListView(new StringConverter<LocalDateTime>() {
				@Override
				public String toString(LocalDateTime o) {
					return o == null ? "" : DateTimeFormatter.ISO_DATE_TIME.format(o);
				}

				@Override
				public LocalDateTime fromString(String s) {
					return null;  //never used
				}
			}));
			lGridPane.add(lListView, new GridPane.C().row(lRowIdx).col(1));
			// remove button
			{
				Button lButton = new Button("remove");
				lGridPane.add(lButton, new GridPane.C().row(lRowIdx).col(2).valignment(VPos.TOP).hgrow(Priority.ALWAYS));
				lButton.onActionProperty().set( (actionEvent) -> {
					LocalDateTime c = lListView.getSelectionModel().getSelectedItem();
					if (c != null) {
						localDateTimePicker.disabledLocalDateTimes().remove(c);
					}
				});
			}
		}
		lRowIdx++;

		// stylesheet
		{		
			Label lLabel = new Label("Stage Stylesheet");
			lLabel.setTooltip(new Tooltip("To test how CSS will modify the displayed nodes"));
			lGridPane.add(lLabel, new GridPane.C().row(lRowIdx).col(0).halignment(HPos.RIGHT).valignment(VPos.TOP));
			TextArea lTextArea = createTextAreaForCSS(stage, FXCollections.observableArrayList(
				".LocalDateTimePicker {\n\t-fxx-show-weeknumbers:NO; /* " +  Arrays.toString(CalendarPickerControlSkin.ShowWeeknumbers.values()) + " */\n}",
				".ListSpinner {\n\t-fxx-arrow-position:SPLIT; /* " + Arrays.toString(ListSpinnerCaspianSkin.ArrowPosition.values()) + " */ \n}",
				".ListSpinner {\n\t-fxx-arrow-direction:VERTICAL; /* " + Arrays.toString(ListSpinnerCaspianSkin.ArrowDirection.values()) + " */ \n}"));
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