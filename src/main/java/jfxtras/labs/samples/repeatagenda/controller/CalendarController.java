package jfxtras.labs.samples.repeatagenda.controller;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.Collection;
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
import javafx.util.Callback;
import jfxtras.internal.scene.control.skin.agenda.AgendaDaySkin;
import jfxtras.internal.scene.control.skin.agenda.AgendaSkin;
import jfxtras.internal.scene.control.skin.agenda.AgendaWeekSkin;
import jfxtras.labs.repeatagenda.internal.scene.control.skin.repeatagenda.base24hour.RepeatMenu;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.Repeat;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.Repeat.EndCriteria;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.Repeat.Frequency;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.Repeat.MonthlyRepeat;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.RepeatImpl;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.RepeatableAgenda;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.RepeatableAgenda.RepeatableAppointment;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.RepeatableAppointmentImpl;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.Settings;
import jfxtras.labs.samples.repeatagenda.Main;
import jfxtras.labs.samples.repeatagenda.MyData;
import jfxtras.scene.control.LocalDatePicker;
import jfxtras.scene.control.agenda.Agenda.Appointment;
import jfxtras.scene.control.agenda.Agenda.LocalDateTimeRange;


/**
 * @author David Bal
 *
 * Instantiates and setups the Agenda.
 * Contains listeners to write changes due to calendar interaction.  Properties are in Agenda class.
 */
public class CalendarController {

    private MyData data;

     public RepeatableAgenda<RepeatableAppointment> agenda = new RepeatableAgenda<RepeatableAppointment>();
     private final Callback<Collection<Appointment>, Void> appointmentWriteCallback =
             a -> { RepeatableAppointmentImpl.writeToFile(a, Settings.APPOINTMENTS_FILE); return null; };
     private final Callback<Collection<Repeat>, Void> repeatWriteCallback =
             r -> { RepeatImpl.writeToFile(r); return null; };

     private LocalDateTimeRange dateTimeRange;
     private RepeatMenu repeatMenu;
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
    
    boolean editDone = false;
    
    @FXML public void initialize() {
       
        daySkinButton.setToggleGroup(skinGroup);
        weekSkinButton.setToggleGroup(skinGroup);
        monthSkinButton.setToggleGroup(skinGroup);
        agendaSkinButton.setToggleGroup(skinGroup);
        weekSkinButton.selectedProperty().set(true);
        
        // Set I/O callbacks
        agenda.setAppointmentWriteCallback(appointmentWriteCallback);
        agenda.setRepeatWriteCallback(repeatWriteCallback);
//        // setup appointment groups
//        final Map<String, Agenda.AppointmentGroup> lAppointmentGroupMap = new TreeMap<String, Agenda.AppointmentGroup>();
//        for (Agenda.AppointmentGroup lAppointmentGroup : agenda.appointmentGroups()) {
//            lAppointmentGroupMap.put(lAppointmentGroup.getDescription(), lAppointmentGroup);
//        }

        // accept new appointments
        agenda.newAppointmentCallbackProperty().set((LocalDateTimeRange dateTimeRange) -> 
        {
//            System.out.println("new appointment calllback");
//            RepeatableAppointment appointment = AppointmentFactory.newAppointment(RepeatableAppointmentImpl.class)
            RepeatableAppointment appointment = new RepeatableAppointmentImpl()
                .withStartLocalDateTime( dateTimeRange.getStartLocalDateTime())
                .withEndLocalDateTime( dateTimeRange.getEndLocalDateTime())
                .withSummary("New")
                .withDescription("")
                .withAppointmentGroup(agenda.appointmentGroups().get(0));
            
//            // TODO - POSITION MENU SO IT DOESN'T COVER UP APPOINTMENT
//            // Produce repeat edit popup on create new appointment
//            repeatMenu = new RepeatMenu(
//                    (RepeatableAppointment) appointment
//                    , agenda.dateTimeRange()
//                    , agenda.appointments()
//                    , agenda.getRepeats()
//                    , agenda.appointmentGroups()
//                    , agenda.getNewAppointmentCallback()
//                    , appointmentWriteCallback
//                    , repeatWriteCallback);
//            repeatMenu.show();
            
            return appointment;
        });

//        agenda.setEditAppointmentCallback((AppointmentEditData a) -> {
//            System.out.println("start edit callback");
//            Stage stage = new RepeatMenuStage2(a);
//            stage.show();
//            System.out.println("end edit callback");
//            return null;
//        });

//        agenda.setEditAppointmentCallback((Appointment appointment) -> {
//            repeatMenu = new RepeatMenu(
//                    (RepeatableAppointment) appointment
//                    , agenda.dateTimeRange()
//                    , agenda.appointments()
//                    , agenda.getRepeats()
//                    , agenda.appointmentGroups()
//                    , a -> { AppointmentFactory.writeToFile(a); return null; }
//                    , r -> { RepeatImpl.writeToFile(r); return null; }); // make new object when closed (problem with passing pane - null for now)
//            repeatMenu.show();
//            return null;
//        });
        
//        // manage repeat-made appointments when the range changes
//        agenda.setLocalDateTimeRangeCallback(dateTimeRange -> {
//            this.dateTimeRange = dateTimeRange;
//            LocalDate startDate = dateTimeRange.getStartLocalDateTime().toLocalDate();
//            LocalDate endDate = dateTimeRange.getEndLocalDateTime().toLocalDate();
//            System.out.println("dates changed " + startDate + " " + endDate);
//            System.out.println("2agenda.appointments().size() " + appointments().size());
//            appointments().removeIf(a -> ((RepeatableAppointment) a).isRepeatMade());
//            getRepeats().stream().forEach(r -> r.getAppointments().clear());
//            getRepeats().stream().forEach(r ->
//            { // Make new repeat-made appointments inside range
//                Collection<RepeatableAppointment> newAppointments = r.makeAppointments(startDate, endDate);
//                appointments().addAll(newAppointments);
////                agenda.appointments().addAll(newAppointments);
//                System.out.println("newAppointments " + newAppointments.size());
////                r.removeOutsideRangeAppointments(data.getAppointments());                 // remove outside range appointments
//            });
//            System.out.println("3agenda.appointments().size() " + appointments().size());
////            System.exit(0);
//            return null; // return argument for the Callback
//        });
                
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

    public void setupData(MyData data, LocalDate startDate, LocalDate endDate) {

        this.data = data;
        this.startDate = startDate;
        this.endDate = endDate;
        agenda.setIndividualAppointments(data.getAppointments());
        agenda.setRepeats(data.getRepeats());
//        agenda.setWriteAppointmentsCallback(a -> { AppointmentFactory.writeToFile(a); return null; });
//        agenda.setWriteRepeatsCallback(r -> { RepeatImpl.writeToFile(r); return null; });

//        repeatMenu = new RepeatMenuStage(agenda.appointments(), agenda.repeats(), agenda.appointmentGroups(), null);
        if (! data.getAppointmentGroups().isEmpty()) 
        { // overwrite default appointmentGroups with ones read from file if not empty
//            agenda.setAppointmentGroups(data.getAppointmentGroups());
            agenda.appointmentGroups().clear();
            agenda.appointmentGroups().addAll(data.getAppointmentGroups());
        }
        
        if (data.getAppointments().isEmpty())
        { // add example appointment if none read in from file
//            data.getAppointments().add(new MyAppointment()
//                    .withStartLocalDateTime(LocalDateTime.now())
//                    .withEndLocalDateTime(LocalDateTime.now().plusHours(1))
//                    .withSummary("example")
//                    .withAppointmentGroup(agenda.appointmentGroups().get(0)));
        }
//        ObservableList<Appointment> castAppointments = javafx.collections.FXCollections.observableArrayList(data.getAppointments().stream().map(a -> (Appointment) a).collect(Collectors.toList()));
//        agenda.setAppointments(castAppointments);
//        System.out.println("data.getAppointments().size()) " + data.getAppointments().size());
//      System.out.println("here " + data.getAppointments().size());
//      System.exit(0);
        
//        agenda.setAppointments(data.getAppointments());
        System.out.println("start appointment size " + data.getAppointments().size());

        if (data.getRepeats().isEmpty())
        { // add Repeats if none read in from file
            RepeatableAppointment a1 = new RepeatableAppointmentImpl() //AppointmentFactory.newAppointment()
                    .withAppointmentGroup(agenda.appointmentGroups().get(5))
                    .withSummary("Weekly Appointment");
            data.getRepeats().add(new RepeatImpl(Main.NEW_APPOINTMENT_CALLBACK)
                    .withStartLocalDate(LocalDateTime.now())
                    .withDurationInSeconds(3600)
//                    .withStartLocalTime(LocalTime.now().plusHours(3))
//                    .withEndLocalTime(LocalTime.now().plusHours(5))
                    .withEndCriteria(EndCriteria.NEVER)
                    .withFrequency(Frequency.WEEKLY)
                    .withDayOfWeek(LocalDate.now().getDayOfWeek(), true)
                    .withDayOfWeek(LocalDate.now().plusDays(2).getDayOfWeek(), true)
                    .withAppointmentData(a1));
            RepeatableAppointment a2 = new RepeatableAppointmentImpl()
                    .withAppointmentGroup(agenda.appointmentGroups().get(9))
                    .withSummary("Monthly Appointment");
            data.getRepeats().add(new RepeatImpl(Main.NEW_APPOINTMENT_CALLBACK)
                    .withStartLocalDate(LocalDateTime.now().minusDays(1))
                    .withDurationInSeconds(7200)
//                    .withStartLocalTime(LocalTime.now().minusHours(5))
//                    .withEndLocalTime(LocalTime.now().minusHours(3))
                    .withEndCriteria(EndCriteria.UNTIL)
                    .withUntilLocalDateTime(LocalDateTime.now().minusDays(1).plusMonths(3))
                    .withFrequency(Frequency.MONTHLY)
                    .withMonthlyRepeat(MonthlyRepeat.DAY_OF_MONTH)
                    .withAppointmentData(a2));
            RepeatableAppointment a3 = new RepeatableAppointmentImpl()
                    .withAppointmentGroup(agenda.appointmentGroups().get(15))
                    .withSummary("Daily Appointment");
            data.getRepeats().add(new RepeatImpl(Main.NEW_APPOINTMENT_CALLBACK)
                    .withKey(0)
                    .withStartLocalDate(LocalDateTime.now().minusDays(2))
                    .withDurationInSeconds(5400)
//                    .withStartLocalTime(LocalTime.of(8, 00))
//                    .withEndLocalTime(LocalTime.of(9, 30))
                    .withEndCriteria(EndCriteria.AFTER)
                    .withFrequency(Frequency.DAILY)
                    .withInterval(2)
                    .withAppointmentData(a3)
                    .withCount(5));
            
//            Repeat r = data.getRepeats().iterator().next();
//          System.out.println(r.getAppointmentData().getAppointmentGroup());
//            System.out.println("start appointment size " + data.getAppointments().size());
//            data.getRepeats().stream().forEach(a -> a.collectAppointments(data.getAppointments())); // add individual appointments that have repeat rules to their Repeat objects
//            System.out.println("start appointment size " + data.getAppointments().size());
//            data.getRepeats().stream().forEach(r -> 
//            {
//                Collection<RepeatableAppointment> newAppointments = r.makeAppointments(startDate, endDate); // Make repeat appointments   
//                data.getAppointments().addAll(newAppointments);
//                newAppointments.stream().forEach(b -> System.out.println(b.getStartLocalDateTime()));
////                System.exit(0);
//                System.out.println("repeat appointment size " + newAppointments.size() + " " + r.getAppointmentData().getSummary() + " " + startDate + " " + endDate);
//            });
//            System.out.println("total appointment size " + data.getAppointments().size());
////            agenda.appointments().addAll(data.getAppointments());
////            data.getAppointments().stream().forEach(a -> System.out.println(a.getAppointmentGroup()));
////            System.out.println("here " + data.getAppointments().size());
////            System.exit(0);
        }
//        agenda.getItems().addAll(data.getAppointments());
//        System.out.println("agenda.appointments().size() " + agenda.appointments().size());
//        agenda.setRepeats(data.getRepeats());
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
//        shiftDuration = skin.shiftDuration();
        shiftDuration = Period.ofWeeks(1);
        agenda.setSkin(new AgendaWeekSkin(agenda));
    }

    @FXML private void handleDaySkin() {
        AgendaSkin skin = new AgendaWeekSkin(agenda);
//        shiftDuration = skin.shiftDuration();
        shiftDuration = Period.ofDays(1);
        agenda.setSkin(new AgendaDaySkin(agenda));
    }
    
    public static StringBinding makeLocalDateBindings(ObjectProperty<LocalDate> p)
    {
        final DateTimeFormatter DATE_FORMAT2 = DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy");
        return Bindings.createStringBinding(() -> DATE_FORMAT2.format(p.get()), p);
    }
    
}
