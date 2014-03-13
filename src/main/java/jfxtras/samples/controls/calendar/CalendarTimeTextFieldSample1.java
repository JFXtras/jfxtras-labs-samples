package jfxtras.samples.controls.calendar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import jfxtras.samples.JFXtrasSampleBase;
import jfxtras.scene.control.CalendarTimeTextField;
import jfxtras.scene.layout.GridPane;
import jfxtras.scene.layout.VBox;

public class CalendarTimeTextFieldSample1 extends JFXtrasSampleBase
{
    public CalendarTimeTextFieldSample1() {
        calendarTimeTextField = new CalendarTimeTextField();
    }
    final CalendarTimeTextField calendarTimeTextField;

    @Override
    public String getSampleName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public String getSampleDescription() {
        return "Basic CalendarTimeTextField usage";
    }

    @Override
    public Node getPanel(Stage stage) {
        VBox root = new VBox(20);
        root.setPadding(new Insets(30, 30, 30, 30));

        root.getChildren().addAll(calendarTimeTextField);

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
				calendarTimeTextField.setLocale(determineLocale());
			});
        }
        lRowIdx++;

        // date format
        {
            Label lLabel = new Label("Date format");
            TextField lDateFormatTextField = new TextField();
            lDateFormatTextField.setTooltip(new Tooltip("A SimpleDateFormat used to render and parse the text"));
            lGridPane.add(lLabel, new GridPane.C().row(lRowIdx).col(0).halignment(HPos.RIGHT));
            lGridPane.add(lDateFormatTextField, new GridPane.C().row(lRowIdx).col(1));
            lDateFormatTextField.focusedProperty().addListener( (observable) -> {
        		DateFormat lDateFormat = (lDateFormatTextField.getText().length() == 0 ? null : new SimpleDateFormat(lDateFormatTextField.getText(), determineLocale()) );
        		calendarTimeTextField.dateFormatProperty().set( lDateFormat );
			});
        }
        lRowIdx++;

//		// stylesheet
//		{		
//			Label lLabel = new Label("Stage Stylesheet");
//			lGridPane.add(lLabel, new GridPane.C().row(lRowIdx).col(0).halignment(HPos.RIGHT).valignment(VPos.TOP));
//			TextArea lTextArea = createTextAreaForCSS(stage, FXCollections.observableArrayList(
//				".CalendarTimePicker {\n\t-fxx-show-ticklabels:NO; /* " +  Arrays.toString(CalendarTimePickerSkin.ShowTickLabels.values()) + " */\n}",
//				".CalendarTimePicker {\n\t-fxx-label-dateformat:\"hh:mm a\"; /* See SimpleDateFormat, e.g. 'HH' for 24 hours per day */\n}") 
//			);
//			lGridPane.add(lTextArea, new GridPane.C().row(lRowIdx).col(1).vgrow(Priority.ALWAYS).minHeight(100.0));
//		}
//        lRowIdx++;

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
		return "http://jfxtras.org/doc/8.0/" + CalendarTimeTextField.class.getName().replace(".", "/") + ".html";
    }

    public static void main(String[] args) {
        launch(args);
    }
}