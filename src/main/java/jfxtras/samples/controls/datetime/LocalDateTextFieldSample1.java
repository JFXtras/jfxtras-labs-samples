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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;
import jfxtras.internal.scene.control.skin.CalendarPickerControlSkin;
import jfxtras.internal.scene.control.skin.ListSpinnerSkin;
import jfxtras.samples.JFXtrasSampleBase;
import jfxtras.scene.control.LocalDateTextField;
import jfxtras.scene.layout.GridPane;
import jfxtras.scene.layout.HBox;
import jfxtras.scene.layout.VBox;

import org.controlsfx.dialog.Dialogs;

public class LocalDateTextFieldSample1 extends JFXtrasSampleBase
{
    public LocalDateTextFieldSample1() {
        localDateTextField = new LocalDateTextField();
    }
    final LocalDateTextField localDateTextField;

    @Override
    public String getSampleName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public String getSampleDescription() {
        return "Basic LocalDateTextField usage";
    }

    @Override
    public Node getPanel(Stage stage) {
		this.stage = stage;
		
        VBox root = new VBox(20);
        root.setPadding(new Insets(30, 30, 30, 30));

        root.getChildren().addAll(localDateTextField);

		localDateTextField.parseErrorCallbackProperty().set( (Callback<Throwable, Void>) (Throwable p) -> {
			Dialogs.create()
				.owner( stage )
				.title("Parse error")
				.message( p.getLocalizedMessage() )
				.showError();
			return null;
		});

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

        // allowNull
        {
            Label lLabel = new Label("Null allowed");
            lGridPane.add(lLabel, new GridPane.C().row(lRowIdx).col(0).halignment(HPos.RIGHT));
            CheckBox lCheckBox = new CheckBox();
            lCheckBox.setTooltip(new Tooltip("Is the control allowed to hold null (or have no calendar deselected)"));
            lGridPane.add(lCheckBox, new GridPane.C().row(lRowIdx).col(1));
            lCheckBox.selectedProperty().bindBidirectional(localDateTextField.allowNullProperty());
        }
        lRowIdx++;

        // Locale
        {
            lGridPane.add(new Label("Locale"), new GridPane.C().row(lRowIdx).col(0).halignment(HPos.RIGHT));
            final ObservableList<Locale> lLocales = FXCollections.observableArrayList(Locale.getAvailableLocales());
            FXCollections.sort(lLocales,  (o1, o2) -> { return o1.toString().compareTo(o2.toString()); } );
            localeComboBox = new ComboBox( lLocales );
            localeComboBox.converterProperty().set(new StringConverter<Locale>() {
				@Override
				public String toString(Locale locale) {
					return locale == null ? "-" : locale.toString();
				}

				@Override
				public Locale fromString(String s) {
					if ("-".equals(s)) return null;
					// this goes wrong with upper and lowercase, so we do a toString search in the list: return new Locale(s);
					for (Locale l : lLocales) {
						if (l.toString().equalsIgnoreCase(s)) {
							return l;
						}
					}
					throw new IllegalArgumentException(s);
				}
			});
            localeComboBox.setEditable(true);
            lGridPane.add(localeComboBox, new GridPane.C().row(lRowIdx).col(1));
			// once the date format has been set manually, changing the local has no longer any effect, so binding the property is useless
			localeComboBox.valueProperty().addListener( (observable) -> {
				localDateTextField.setLocale(determineLocale());
			});
        }
        lRowIdx++;

        // date time format
        {
            Label lLabel = new Label("Date formatter");
            lGridPane.add(lLabel, new GridPane.C().row(lRowIdx).col(0).halignment(HPos.RIGHT));
            TextField lDateTimeFormatterTextField = new TextField();
            lDateTimeFormatterTextField.setTooltip(new Tooltip("A DateTimeFormatter used to render and parse the text"));
            lGridPane.add(lDateTimeFormatterTextField, new GridPane.C().row(lRowIdx).col(1));
            lDateTimeFormatterTextField.focusedProperty().addListener( (observable) -> {
            	localDateTextField.setDateTimeFormatter( lDateTimeFormatterTextField.getText().length() == 0 ? null : DateTimeFormatter.ofPattern(lDateTimeFormatterTextField.getText()).withLocale(determineLocale()) );
			});
        }
        lRowIdx++;

        // DateTimeFormatters
        {
			lRowIdx = addObservableListManagementControlsToGridPane("Parse only formatters", "Alternate DateTimeFormatters patterns only for parsing the typed text", lGridPane, lRowIdx, localDateTextField.dateTimeFormattersProperty(), (String s) -> {
				return DateTimeFormatter.ofPattern(s).withLocale(determineLocale());
			});
        }

        // highlight
		{
			lRowIdx = addObservableListManagementControlsToGridPane("Highlighted", "All highlighted dates", lGridPane, lRowIdx, localDateTextField.highlightedLocalDates(), new LocalDateTextField()
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
			lRowIdx = addObservableListManagementControlsToGridPane("Disabled", "All disabled dates", lGridPane, lRowIdx, localDateTextField.disabledLocalDates(), new LocalDateTextField()
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

        // localDateRangeCallback
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
            		localDateTextField.setLocalDateRangeCallback( (range) -> {
            			lTextField.setText(range.getStartLocalDate() + " - " + range.getEndLocalDate());
						return null;
					});
            	}
            	else {
            		localDateTextField.setLocalDateRangeCallback(null);
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
				".LocalDatePicker {\n\t-fxx-show-weeknumbers:NO; /* " +  Arrays.toString(CalendarPickerControlSkin.ShowWeeknumbers.values()) + " */\n}",
				".ListSpinner {\n\t-fxx-arrow-position:SPLIT; /* " + Arrays.toString(ListSpinnerSkin.ArrowPosition.values()) + " */ \n}",
				".ListSpinner {\n\t-fxx-arrow-direction:VERTICAL; /* " + Arrays.toString(ListSpinnerSkin.ArrowDirection.values()) + " */ \n}"));
			lGridPane.add(lTextArea, new GridPane.C().row(lRowIdx).col(1).vgrow(Priority.ALWAYS).minHeight(100.0));
		}
        lRowIdx++;

        // done
        return lGridPane;
    }
 	private ComboBox<Locale> localeComboBox;

	private Locale determineLocale() {
		Locale lLocale = localeComboBox.valueProperty().get();
		if (lLocale == null) {
			lLocale = Locale.getDefault();
		}
		return lLocale;
	}
	
    @Override
    public String getJavaDocURL() {
		return "http://jfxtras.org/doc/8.0/jfxtras-controls/" + LocalDateTextField.class.getName().replace(".", "/") + ".html";
    }

    public static void main(String[] args) {
        launch(args);
    }
}