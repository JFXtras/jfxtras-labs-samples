package jfxtras.labs.samples.calendar;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import jfxtras.labs.samples.JFXtrasSampleBase;
import jfxtras.labs.scene.control.CalendarTimeTextField;
import jfxtras.labs.scene.layout.GridPane;
import jfxtras.labs.scene.layout.VBox;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class CalendarTimeTextFieldSample1 extends JFXtrasSampleBase
{
    public CalendarTimeTextFieldSample1() {
        calendarTimeTextField = new CalendarTimeTextField();
		calendarTimeTextField.showLabelsProperty().set(false);
		// TODO: make show labels work
		// TODO: make locale AM/PM work
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
//            lGridPane.add(new Label("Locale"), new GridPane.C().row(lRowIdx).col(0).halignment(HPos.RIGHT));
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
//            lGridPane.add(localeComboBox, new GridPane.C().row(lRowIdx).col(1));
			// once the date format has been set manually, changing the local has no longer any effect, so binding the property is useless
			localeComboBox.valueProperty().addListener( (observable) -> {
				setDateFormat();
			});
        }
        lRowIdx++;

//        // nullAllowed
//        {
//            Label lLabel = new Label("Null allowed");
//            lLabel.setTooltip(new Tooltip("Is the control allowed to hold null (or have no calendar deselected)"));
//            lGridPane.add(lLabel, new GridPane.C().row(lRowIdx).col(0).halignment(HPos.RIGHT));
//            CheckBox lCheckBox = new CheckBox();
//            lGridPane.add(lCheckBox, new GridPane.C().row(lRowIdx).col(1));
//            lCheckBox.selectedProperty().bindBidirectional(calendarTimeTextField.allowNullProperty());
//        }
//        lRowIdx++;

        // done
        return lGridPane;
    }
 	private ComboBox<Locale> localeComboBox;

	private void setDateFormat() {
		Locale lLocale = localeComboBox.valueProperty().get();
		if (lLocale == null) {
			lLocale = Locale.getDefault();
		}
		calendarTimeTextField.dateFormatProperty().set( SimpleDateFormat.getTimeInstance(SimpleDateFormat.LONG, lLocale) );
	}

    @Override
    public String getJavaDocURL() {
		return "http://jfxtras.org/doc/8.0/" + CalendarTimeTextField.class.getName().replace(".", "/") + ".html";
    }

    public static void main(String[] args) {
        launch(args);
    }
}