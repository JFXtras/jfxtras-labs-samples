package jfxtras.labs.samples.repeatagenda.controller;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.Locale;
import java.util.ResourceBundle;

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
import jfxtras.labs.samples.repeatagenda.MyAppointment;
import jfxtras.labs.samples.repeatagenda.MyData;
import jfxtras.labs.samples.repeatagenda.MyRepeat;
import jfxtras.labs.samples.repeatagenda.internal.scene.control.skin.agenda.AgendaDaySkin;
import jfxtras.labs.samples.repeatagenda.internal.scene.control.skin.agenda.AgendaSkin;
import jfxtras.labs.samples.repeatagenda.internal.scene.control.skin.agenda.AgendaWeekSkin;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.Agenda;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.Agenda.Appointment;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.Agenda.LocalDateTimeRange;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.AppointmentFactory;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.Repeat;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.Repeat.EndCriteria;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.Repeat.IntervalUnit;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.Repeat.MonthlyRepeat;
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
        
//        // setup appointment groups
//        final Map<String, Agenda.AppointmentGroup> lAppointmentGroupMap = new TreeMap<String, Agenda.AppointmentGroup>();
//        for (Agenda.AppointmentGroup lAppointmentGroup : agenda.appointmentGroups()) {
//            lAppointmentGroupMap.put(lAppointmentGroup.getDescription(), lAppointmentGroup);
//        }

        // accept new appointments
        agenda.newAppointmentCallbackProperty().set((LocalDateTimeRange dateTimeRange) -> AppointmentFactory.newAppointment()
            .withStartLocalDateTime( dateTimeRange.getStartLocalDateTime())
            .withEndLocalDateTime( dateTimeRange.getEndLocalDateTime())
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
                    .removeOutsideRangeAppointments(data.getAppointments()));                 // remove outside range appointments
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
        if (! data.getAppointmentGroups().isEmpty()) 
        { // overwrite default appointmentGroups with ones read from file if not empty
            agenda.setAppointmentGroups(data.getAppointmentGroups()); 
        }
        
        if (data.getAppointments().isEmpty())
        { // add example appointment if none read in from file
//            data.getAppointments().add(new MyAppointment()
//                    .withStartLocalDateTime(LocalDateTime.now())
//                    .withEndLocalDateTime(LocalDateTime.now().plusHours(1))
//                    .withSummary("example")
//                    .withAppointmentGroup(agenda.appointmentGroups().get(0)));
        }
        agenda.setAppointments(data.getAppointments());

//        if (data.getRepeats().isEmpty())
        { // add Repeats if none read in from file
            Appointment a1 = new MyAppointment()
                    .withAppointmentGroup(agenda.appointmentGroups().get(5))
                    .withSummary("Weekly Appointment");
            data.getRepeats().add(new MyRepeat()
                    .withStartLocalDate(LocalDate.now())
                    .withStartLocalTime(LocalTime.now().plusHours(3))
                    .withEndLocalTime(LocalTime.now().plusHours(5))
                    .withEndCriteria(EndCriteria.NEVER)
                    .withIntervalUnit(IntervalUnit.WEEKLY)
                    .withDayOfWeek(LocalDate.now().getDayOfWeek(), true)
                    .withDayOfWeek(LocalDate.now().plusDays(2).getDayOfWeek(), true)
                    .withAppointmentData(a1));
            Appointment a2 = new MyAppointment()
                    .withAppointmentGroup(agenda.appointmentGroups().get(9))
                    .withSummary("Monthly Appointment");
            data.getRepeats().add(new MyRepeat()
                    .withStartLocalDate(LocalDate.now().minusDays(1))
                    .withStartLocalTime(LocalTime.now().minusHours(5))
                    .withEndLocalTime(LocalTime.now().minusHours(3))
                    .withEndCriteria(EndCriteria.ON)
                    .withEndOnDate(LocalDate.now().minusDays(1).plusMonths(3))
                    .withIntervalUnit(IntervalUnit.MONTHLY)
                    .withMonthlyRepeat(MonthlyRepeat.DAY_OF_MONTH)
                    .withAppointmentData(a2));
            Appointment a3 = new MyAppointment()
                    .withAppointmentGroup(agenda.appointmentGroups().get(15))
                    .withSummary("Daily Appointment");
            data.getRepeats().add(new MyRepeat()
                    .withStartLocalDate(LocalDate.now().minusDays(2))
                    .withStartLocalTime(LocalTime.now().plusHours(4))
                    .withEndLocalTime(LocalTime.now().plusHours(7))
                    .withEndCriteria(EndCriteria.AFTER)
                    .withIntervalUnit(IntervalUnit.DAILY)
                    .withRepeatFrequency(2)
                    .withEndAfterEvents(5)
                    .withAppointmentData(a3));
            Repeat r = data.getRepeats().iterator().next();
          System.out.println(r.getAppointmentData().getAppointmentGroup());
            data.getRepeats().stream().forEach(a -> a.collectAppointments(data.getAppointments())); // add individual appointments that have repeat rules to their Repeat objects
            data.getRepeats().stream().forEach(a -> a.makeAppointments(data.getAppointments())); // Make repeat appointments
//            data.getAppointments().stream().forEach(a -> System.out.println(a.getAppointmentGroup()));
//            System.out.println("here " + data.getAppointments().size());
//            System.exit(0);
        }
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
