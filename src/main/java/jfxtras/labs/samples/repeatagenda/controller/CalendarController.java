package jfxtras.labs.samples.repeatagenda.controller;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import jfxtras.labs.samples.repeatagenda.MyData;
import jfxtras.labs.samples.repeatagenda.internal.scene.control.skin.agenda.AgendaDaySkin;
import jfxtras.labs.samples.repeatagenda.internal.scene.control.skin.agenda.AgendaSkin;
import jfxtras.labs.samples.repeatagenda.internal.scene.control.skin.agenda.AgendaWeekSkin;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.Agenda;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.AppointmentFactory;
import jfxtras.scene.control.LocalDatePicker;


/**
 * @author David Bal
 *
 * Instantiates and setups the Agenda.
 * Contains listeners to write changes due to calendar interaction.  Properties are in Agenda class.
 */
public class CalendarController {

    private MyData data;

     private Agenda agenda = new Agenda();
    @FXML private ResourceBundle resources; // ResourceBundle that was given to the FXMLLoader
    @FXML private BorderPane agendaBorderPane;

    final ToggleGroup skinGroup = new ToggleGroup();
    @FXML private Label dateLabel;
    @FXML private ToggleButton daySkinButton;
    @FXML private ToggleButton weekSkinButton;
    @FXML private ToggleButton monthSkinButton;
    @FXML private ToggleButton agendaSkinButton;
    
    final private LocalDatePicker localDatePicker = new LocalDatePicker(LocalDate.now());
    private LocalDate startDate;
    private LocalDate endDate;
    
    public final ObjectProperty<LocalDate> selectedLocalDateProperty = new SimpleObjectProperty<LocalDate>();
    public final ObjectProperty<LocalDateTime> selectedLocalDateTimeProperty = new SimpleObjectProperty<LocalDateTime>(LocalDateTime.now());
    private Period shiftDuration = Period.ofWeeks(1);
    public final TemporalField dayOfWeekField = WeekFields.of(Locale.getDefault()).dayOfWeek();
    
    @FXML public void initialize() {
       
        daySkinButton.setToggleGroup(skinGroup);
        weekSkinButton.setToggleGroup(skinGroup);
        monthSkinButton.setToggleGroup(skinGroup);
        agendaSkinButton.setToggleGroup(skinGroup);
        weekSkinButton.selectedProperty().set(true);
        
        // setup appointment groups
        final Map<String, Agenda.AppointmentGroup> lAppointmentGroupMap = new TreeMap<String, Agenda.AppointmentGroup>();
        for (Agenda.AppointmentGroup lAppointmentGroup : agenda.appointmentGroups()) {
            lAppointmentGroupMap.put(lAppointmentGroup.getDescription(), lAppointmentGroup);
        }

        // accept new appointments
        agenda.newAppointmentCallbackProperty().set(dateTimeRange -> AppointmentFactory.newAppointment()
            .withStartLocalDateTime( dateTimeRange.getStartLocalDateTime() )
            .withEndLocalDateTime( dateTimeRange.getEndLocalDateTime() )
            .withSummary("New")
            .withDescription("")
            .withAppointmentGroup(agenda.appointmentGroups().get(0)));

        // create new repeat-made appointments when the range changes
        agenda.setLocalDateTimeRangeCallback(param -> {
            startDate = param.getStartLocalDateTime().toLocalDate();
            endDate = param.getEndLocalDateTime().toLocalDate();
            data.getAppointments().removeIf(a -> a.isRepeatMade());
            data.getRepeats().stream().forEach(a -> a.getAppointments().clear());
            data.getRepeats().stream().forEach(a -> a
                    .makeAppointments(data.getAppointments(), startDate, endDate) // Make repeat appointments inside range
                    .removeAppointments(data.getAppointments()));                 // remove outside range appointments
            return null; // return argument for the Callback
        });
        
        // action
        agenda.setActionCallback( (appointment) -> {
            System.out.println("Action on " + appointment);
            return null;
        });
        
        agendaBorderPane.setCenter(agenda);
        dateLabel.textProperty().bind(makeLocalDateBindings(localDatePicker.localDateProperty()));
        
        localDatePicker.setPadding(new Insets(20, 0, 5, 0)); //(top/right/bottom/left)
        agendaBorderPane.setLeft(localDatePicker);

      localDatePicker.localDateProperty().addListener((observable, oldSelection, newSelection)
      -> {
          if (newSelection != null)
          agenda.displayedLocalDateTime().set(newSelection.atStartOfDay());
      });

      // Enable month and year changing to move calendar
      localDatePicker.displayedLocalDateProperty().addListener((observable, oldSelection, newSelection)
              -> {
                  int dayOfMonth = localDatePicker.getLocalDate().getDayOfMonth();
                  localDatePicker.setLocalDate(newSelection.withDayOfMonth(dayOfMonth));
              });

        agenda.setPadding(new Insets(0, 0, 0, 5)); //(top/right/bottom/left)
            
    }

    public void setupData(MyData data) {

        this.data = data;
        agenda.setAppointmentGroups(data.getAppointmentGroups());
        agenda.setAppointments(data.getAppointments());
        agenda.setRepeats(data.getRepeats());
    }
    
    @FXML private void handleDateIncrement() {
        LocalDate oldLocalDate = localDatePicker.getLocalDate();
        localDatePicker.localDateProperty().set(oldLocalDate.plus(shiftDuration));
    }
    
    @FXML private void handleDateDecrement() {
        LocalDate oldLocalDate = localDatePicker.getLocalDate();
        localDatePicker.localDateProperty().set(oldLocalDate.minus(shiftDuration));
    }
    
    @FXML private void handleWeekSkin() {
        AgendaSkin skin = new AgendaWeekSkin(agenda);
        shiftDuration = skin.shiftDuration();
        agenda.setSkin(new AgendaWeekSkin(agenda));
    }

    @FXML private void handleDaySkin() {
        AgendaSkin skin = new AgendaWeekSkin(agenda);
        shiftDuration = skin.shiftDuration();
        agenda.setSkin(new AgendaDaySkin(agenda));
    }
    
    public static StringBinding makeLocalDateBindings(ObjectProperty<LocalDate> p)
    {
        final DateTimeFormatter DATE_FORMAT2 = DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy");
        return Bindings.createStringBinding(() -> DATE_FORMAT2.format(p.get()), p);
    }
    
}
