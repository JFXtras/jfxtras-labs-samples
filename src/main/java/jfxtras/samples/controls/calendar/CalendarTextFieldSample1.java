package jfxtras.samples.controls.calendar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Locale;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
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
import jfxtras.scene.control.CalendarTextField;
import jfxtras.scene.layout.GridPane;
import jfxtras.scene.layout.VBox;

import org.controlsfx.dialog.Dialogs;

public class CalendarTextFieldSample1 extends JFXtrasSampleBase
{
    public CalendarTextFieldSample1() {
        calendarTextField = new CalendarTextField();
    }
    final CalendarTextField calendarTextField;

    @Override
    public String getSampleName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public String getSampleDescription() {
        return "Basic CalendarTextField usage";
    }

    @Override
    public Node getPanel(Stage stage) {
		this.stage = stage;
		
        VBox root = new VBox(20);
        root.setPadding(new Insets(30, 30, 30, 30));

        root.getChildren().addAll(calendarTextField);

		calendarTextField.parseErrorCallbackProperty().set( (Callback<Throwable, Void>) (Throwable p) -> {
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

        // Locale
        {
            lGridPane.add(new Label("Locale"), new GridPane.C().row(lRowIdx).col(0).halignment(HPos.RIGHT));
            final ObservableList<Locale> lLocales = FXCollections.observableArrayList(Locale.getAvailableLocales());
            FXCollections.sort(lLocales,  (o1, o2) -> { return o1.toString().compareTo(o2.toString()); } );
            localeComboBox = new ComboBox<>( lLocales );
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
				calendarTextField.setLocale(determineLocale());
			});
        }
        lRowIdx++;

        // date format
        {
            Label lLabel = new Label("Date format");
            TextField lDateFormatTextField = new TextField();
            lDateFormatTextField.setTooltip(new Tooltip("A SimpleDateFormat used to render and parse the text, also use this to show the time (hh:mm)"));
            lGridPane.add(lLabel, new GridPane.C().row(lRowIdx).col(0).halignment(HPos.RIGHT));
            lGridPane.add(lDateFormatTextField, new GridPane.C().row(lRowIdx).col(1));
            lDateFormatTextField.focusedProperty().addListener( (observable) -> {
        		DateFormat lDateFormat = (lDateFormatTextField.getText().length() == 0 ? null : new SimpleDateFormat(lDateFormatTextField.getText(), determineLocale()) );
        		calendarTextField.dateFormatProperty().set( lDateFormat );
			});
        }
        lRowIdx++;

        // DateFormats
        {
			lRowIdx = addObservableListManagementControlsToGridPane("Parse only formats", "Alternate SimpleDateFormat patterns only for parsing the typed text", lGridPane, lRowIdx, calendarTextField.dateFormatsProperty(), (String s) -> new SimpleDateFormat(s));
        }

		// stylesheet
		{		
			Label lLabel = new Label("Stage Stylesheet");
			lGridPane.add(lLabel, new GridPane.C().row(lRowIdx).col(0).halignment(HPos.RIGHT).valignment(VPos.TOP));
			TextArea lTextArea = createTextAreaForCSS(stage, FXCollections.observableArrayList(
				".CalendarPicker {\n\t-fxx-show-weeknumbers:NO; /* " +  Arrays.toString(CalendarPickerControlSkin.ShowWeeknumbers.values()) + " */\n}",
				".CalendarPicker {\n\t-fxx-label-dateformat:\"D\"; /* See SimpleDateFormat, e.g. 'D' for day-of-year */\n}",				
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
		return "http://jfxtras.org/doc/8.0/" + CalendarTextField.class.getName().replace(".", "/") + ".html";
    }

    public static void main(String[] args) {
        launch(args);
    }
}