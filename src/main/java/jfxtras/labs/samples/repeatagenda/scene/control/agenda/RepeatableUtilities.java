package jfxtras.labs.samples.repeatagenda.scene.control.agenda;

import java.security.InvalidParameterException;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.TemporalAdjuster;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.xml.parsers.ParserConfigurationException;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.util.Callback;
import jfxtras.labs.samples.repeatagenda.RepeatImpl;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.Repeat.EndCriteria;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.RepeatableAgenda.RepeatableAppointment;
import jfxtras.scene.control.agenda.Agenda.Appointment;

public final class RepeatableUtilities {
    
    private RepeatableUtilities() {}

    /**
     * If repeat criteria has changed display this alert to find out how to apply changes (one, all or future)
     * Can provide a custom choiceList, or omit the list and use the default choices.
     * 
     * @param resources
     * @param choiceList
     * @return
     */
    public static RepeatChange repeatChangeDialog(RepeatChange...choiceList)
    {
        ResourceBundle resources = Settings.resources;
        List<RepeatChange> choices;
        if (choiceList == null || choiceList.length == 0)
        { // use default choices
            choices = new ArrayList<RepeatChange>();
            choices.add(RepeatChange.ONE);
            choices.add(RepeatChange.ALL);
            choices.add(RepeatChange.FUTURE);
        } else { // use inputed choices
            choices = new ArrayList<RepeatChange>(Arrays.asList(choiceList));
        }
               
        ChoiceDialog<RepeatChange> dialog = new ChoiceDialog<>(choices.get(0), choices);
        dialog.setTitle(resources.getString("dialog.repeat.change.title"));
        dialog.setContentText(resources.getString("dialog.repeat.change.content"));
        dialog.setHeaderText(resources.getString("dialog.repeat.change.header"));

        Optional<RepeatChange> result = dialog.showAndWait();
        
        return (result.isPresent()) ? result.get() : RepeatChange.CANCEL;
    }
    
    /**
     * Alert to confirm delete appointments
     * 
     * @param resources
     * @param appointmentQuantity
     * @return
     */
    private static Boolean confirmDelete(String appointmentQuantity)
    {
        ResourceBundle resources = Settings.resources;
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(resources.getString("alert.repeat.delete.title"));
        alert.setContentText(resources.getString("alert.repeat.delete.content"));
        alert.setHeaderText(appointmentQuantity + " " + resources.getString("alert.repeat.delete.header"));

        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == ButtonType.OK;
    }
    
    /**
     * Handles deleting an appointment.  If appointment is repeatable displays a dialog
     * to find out if delete is for one, all, or future appointments.
     * Uses default callback for dialog
     * 
     * @param resources
     * @param appointments
     * @param appointment
     * @return 
     * @throws ParserConfigurationException 
     */
    public static WindowCloseType deleteAppointments(
              Collection<Appointment> appointments
            , RepeatableAppointment appointment
            , Collection<Repeat> repeats) throws ParserConfigurationException
    {
        return deleteAppointments(
                appointments
              , appointment
              , repeats
              , a -> repeatChangeDialog()
              , a -> confirmDelete(a)
              , a -> { AppointmentFactory.writeToFile(a); return null; }
              , r -> { RepeatImpl.writeToFile(r); return null; });
    }
    
    /**
     * Handles deleting an appointment.  If appointment is repeatable displays a dialog
     * to find out if delete is for one, all, or future appointments.
     * 
     * @param resources
     * @param appointments
     * @param appointment
     * @return 
     * @throws ParserConfigurationException 
     */
    public static WindowCloseType deleteAppointments(Collection<Appointment> appointments
            , Appointment appointmentInput
            , Collection<Repeat> repeats
            , Callback<RepeatChange[], RepeatChange> changeDialogCallback
            , Callback<String, Boolean> confirmDeleteCallback
            , Callback<Collection<Appointment>, Void> writeAppointmentsCallback
            , Callback<Collection<Repeat>, Void> writeRepeatsCallback) throws ParserConfigurationException
    {
        RepeatableAppointment appointment = (RepeatableAppointment) appointmentInput;
        ResourceBundle resources = Settings.resources;
        final Repeat repeat = (appointment instanceof RepeatableAppointment) ? ((RepeatableAppointment) appointment).getRepeat() : null;
        final AppointmentType appointmentType = (repeat == null)
                ? AppointmentType.INDIVIDUAL : AppointmentType.WITH_EXISTING_REPEAT;
        boolean writeAppointments = false;
        boolean writeRepeats = false;
        
        RepeatChange changeResponse;
        RepeatChange[] choices = null;
        switch (appointmentType)
        {
        case INDIVIDUAL:
            if (confirmDeleteCallback.call("1"))
            { // remove individual appointment that has no repeat rule
                writeAppointments = removeOne(appointments, appointment);
                if (! writeAppointments) throw new IllegalArgumentException("Appointment can't be deleted - not found ("
                        + appointment.getSummary() + ")");
            }
            break;
        case WITH_EXISTING_REPEAT:
            // TODO - IF APPOINTMENT SELECTED IS LAST APPOINTMENT IN SERIES ONLY OFFER INDIVIDUAL AND ALL - NO FUTURE
            if (appointment.getStartLocalDateTime().toLocalDate().equals(repeat.getStartLocalDate()))
            {
                choices = new RepeatChange[] {RepeatChange.ONE, RepeatChange.ALL};
            }
            changeResponse = changeDialogCallback.call(choices);

            if (changeResponse == RepeatChange.CANCEL) return WindowCloseType.CLOSE_WITHOUT_CHANGE; // cancel selected
            final Predicate<? super Appointment> myFilter;
            final LocalDate startDate = appointment.getStartLocalDateTime().toLocalDate();
            final String matchingAppointmentsString;
            final int matchingAppointments;
            int deletedAppointments = 0;
            switch (changeResponse)
            {
            case ONE:
                if (confirmDeleteCallback.call("1"))
                {
                    writeRepeats = removeOne(appointments, appointment);
                    if (writeRepeats) removeOne(repeat.getAppointments(), appointment);
                    if (startDate.equals(repeat.getEndOnDate()))
                    { // deleted appointment is on end date, adjust end date and number of appointments
                        repeat.setEndOnDate(startDate.minusDays(1));
                        if (repeat.getEndCriteria().equals(EndCriteria.AFTER))
                        { // decrement end after events
                            repeat.setEndAfterEvents(repeat.getEndAfterEvents()-1);
                        }
                    } else { // deleted appointment is not end date, add date to deleted dates list
                        repeat.getDeletedDates().add(appointment.getStartLocalDateTime().toLocalDate());
                    }
                    writeRepeats = true;
                }
                break;
            case ALL:
//                myFilter = (a) -> a.getRepeat() == repeat; // predicate to filter out all appointments with repeat
                myFilter = (a) -> {
                    RepeatableAppointment a2 = (RepeatableAppointment) a;
                    return a2.getRepeat() == repeat; // predicate to filter out all appointments with repeat
                };
                matchingAppointments = (int) repeat.validDateStreamWithEnd().count();
                matchingAppointmentsString = (repeat.getEndCriteria() == EndCriteria.NEVER)
                        ? resources.getString("infinite") : Integer.toString(matchingAppointments);
                if (confirmDeleteCallback.call(matchingAppointmentsString))
                {
                    appointments.removeIf(myFilter);
//                    appointments.stream()
//                        .forEach(a -> {
//                            RepeatableAppoitment a2 = 
//                        });
                    deletedAppointments = matchingAppointments;
                    repeats.remove(repeat);
                    writeRepeats = true;
                }
                break;
            case FUTURE:
                myFilter = (a) ->
                { // predicate to filter out all appointments with repeat and are equal or after startDate
                    LocalDate myDate = a.getStartLocalDateTime().toLocalDate();
                    RepeatableAppointment a2 = (RepeatableAppointment) a;
                    return ((a2.getRepeat() == repeat) && (myDate.isAfter(startDate) || myDate.equals(startDate)));
                };
                matchingAppointments = (int) repeat
                        .validDateStreamWithEnd()
                        .filter(a -> (a.isAfter(startDate) || a.equals(startDate)))
                        .count();
                matchingAppointmentsString = (repeat.getEndCriteria() == EndCriteria.NEVER)
                        ? resources.getString("infinite") : Integer.toString(matchingAppointments);
                if (confirmDeleteCallback.call(matchingAppointmentsString))
                {
                    appointments.removeIf(myFilter);
                    deletedAppointments = matchingAppointments;
                    switch (repeat.getEndCriteria())
                    {
                        case NEVER: // convert to end ON
                            repeat.setEndCriteria(EndCriteria.ON);
                            repeat.setEndOnDate(startDate.minusDays(1));
//                            repeat.makeEndAfterEventsFromEndOnDate();
                            break;
                        case AFTER: // reduce quantity by deleted quantity
                            repeat.setEndAfterEvents(repeat.getEndAfterEvents() - deletedAppointments);
                            // drop through
                        case ON:
                            repeat.setEndOnDate(startDate.minusDays(1));
                            break;
                        default:
                            break;
                    }
                    repeat.updateAppointments(appointments, appointment);
                    writeRepeats = true;
                }
                break;
            default:
                break;
            }
            // Check if repeat has only one appointment and should become individual
            if (repeat.oneAppointmentToIndividual(repeats, appointments)) writeAppointments = true;
            break;
        default:
            break; // shouldn't get here (unknown AppointmentType)
        }
        
        // Write changes that occurred
//        if (writeAppointments && (writeAppointmentsCallback != null)) writeAppointmentsCallback.call(appointments); // write appointment changes
//        if (writeRepeats && (writeRepeatsCallback != null)) writeRepeatsCallback.call(repeats);                     // write repeat changes
        
        return (writeAppointments || writeRepeats) ? WindowCloseType.CLOSE_WITH_CHANGE : WindowCloseType.CLOSE_WITHOUT_CHANGE;
    }

    
    /**
     * Edit appointments with parameters for the callbacks.
     * To do testing the two write callbacks should be set to stubs that do nothing.  Also, the changeDialogCallback
     * should be sent to return the RepeatChange option being tested (i.e. ALL).
     * 
     * @param appointments
     * @param appointment
     * @param appointmentOld
     * @param repeats
     * @param changeDialogCallback - code for the choice dialog selecting editing ALL, FUTURE, or ONE.  For testing return the RepeatChange being tested
     * @param writeAppointmentsCallback - code for writing appointments IO.  For testing do nothing
     * @param writeRepeatsCallback - code for writing repeats IO.  For testing do nothing
     * @return
     */
    // Works for by drag-and-drop on the agenda and for editing from AppointmentEditController
    public static WindowCloseType editAppointments(
              RepeatableAppointment appointmentInput
            , RepeatableAppointment appointmentOldInput
            , Collection<Appointment> appointments
            , Collection<Repeat> repeats
            , Callback<RepeatChange[], RepeatChange> changeDialogCallback
//            , Collection<RepeatableAppointment> editedAppointments
//            , Collection<Repeat> editedRepeats)
            , Callback<Collection<Appointment>, Void> writeAppointmentsCallback
            , Callback<Collection<Repeat>, Void> writeRepeatsCallback)
    {
        RepeatableAppointment appointment = (RepeatableAppointment) appointmentInput;
        RepeatableAppointment appointmentOld = (RepeatableAppointment) appointmentOldInput;
        final ResourceBundle resources = Settings.resources;
        final Repeat repeat = appointment.getRepeat(); // repeat with new changes
//        System.out.println("repeat start " + repeat.getStartLocalTime());
        final Repeat repeatOld = appointmentOld.getRepeat(); // repeat prior to changes
        Set<RepeatableAppointment> editedAppointmentsTemp = new HashSet<RepeatableAppointment>();
        
        final boolean appointmentChanged = ! appointment.equals(appointmentOld);
        final boolean repeatChanged = (repeat == null) ? false : ! repeat.equals(repeatOld);
        if (! appointmentChanged && ! repeatChanged) return WindowCloseType.CLOSE_WITHOUT_CHANGE;

        // Make temporal adjusters for time and/or day shift
        final LocalDate startDate = appointment.getStartLocalDateTime().toLocalDate();
        final LocalDate startDateOld = appointmentOld.getStartLocalDateTime().toLocalDate();
        final int dayShift = Period.between(startDateOld, startDate).getDays();
        final int startMinuteShift = (int) Duration.between(appointmentOld.getStartLocalDateTime()
                                               , appointment.getStartLocalDateTime()).toMinutes();
        final int endMinuteShift = (int) Duration.between(appointmentOld.getEndLocalDateTime()
                , appointment.getEndLocalDateTime()).toMinutes();
        final TemporalAdjuster startTemporalAdjuster = temporal ->
        { // adjusts original startLocalDateTime to new
              LocalDateTime t = LocalDateTime.from(temporal);
              t = t.plusMinutes(startMinuteShift);
              return t;
        };
        final TemporalAdjuster endTemporalAdjuster = temporal ->
        { // adjusts original endLocalDateTime to new
              LocalDateTime t = LocalDateTime.from(temporal);
              t = t.plusMinutes(endMinuteShift);
              return t;
        };

        RepeatChange[] choices = null;
        RepeatChange changeResponse;
        boolean editedAppointmentsFlag = false;
        boolean editedRepeatsFlag = false;

        // FIND OUT WHICH TYPE OF APPOINTMENT IS BEING EDITED
        final AppointmentType appointmentType = makeAppointmentType(repeat, repeatOld);
        System.out.println("appointmentType " + appointmentType);
        switch (appointmentType)
        {
        case INDIVIDUAL:
            editedAppointmentsFlag = true;
            editedAppointmentsTemp.add(appointment);
            break;
        case HAD_REPEAT_BECOMING_INDIVIDUAL:
//            changeResponse = repeatChangeDialog();
            changeResponse = changeDialogCallback.call(choices);
            switch (changeResponse)
            {
            case ONE: // remove repeatKey from appointment, add date to skip dates in repeat, write both
                repeatOld.getDeletedDates().add(startDate);
                break;
            case ALL: // remove repeatKey from appointment, delete repeat
                repeats.remove(repeat);
                break;
            case FUTURE: // change end of repeat to appointment date (I'm not sure what the user expects in this case)
                final Repeat repeatOriginal = appointmentOld.getRepeat();
                final Set<LocalDate> dates = repeatOriginal.getDeletedDates()
                         .stream()
                         .filter(a -> a.isBefore(startDate))
                         .collect(Collectors.toSet());
                repeatOriginal.setDeletedDates(dates);
                repeatOriginal.setEndOnDate(startDate.minusDays(1));
                switch (repeatOriginal.getEndCriteria())
                {
                case NEVER:
                    repeatOriginal.setEndCriteria(EndCriteria.ON);
                    break;
                case AFTER:
                    repeatOriginal.makeEndAfterEventsFromEndOnDate();
                    break;
                default:
                    break;
                }
                repeatOriginal.updateAppointments(appointments, appointmentOld); // is appointmentOld correct?
                break;
            case CANCEL:
                return WindowCloseType.CLOSE_WITHOUT_CHANGE;
            }
            appointment.setRepeatMade(false);
            editedRepeatsFlag = true;
            editedAppointmentsFlag = true;
            editedAppointmentsTemp.add(appointment);
            break;
        case WITH_NEW_REPEAT: // don't display edit dialog - just make appointments
//            System.out.println("WITH_NEW_REPEAT");
          repeat.unbindAll();
          appointment.setRepeat(repeat);
          repeat.getAppointments().add(appointment);
//          System.out.println(repeat.getStartLocalDate());
//          System.exit(0);
          Collection<RepeatableAppointment> newAppointments = repeat.makeAppointments();
          System.out.println("newAppointments " + newAppointments.size() + " " + repeat.getAppointments());
          appointments.addAll(newAppointments);
          appointment.copyNonDateFieldsInto(repeat.getAppointmentData()); // copy any appointment changes (i.e. description, group, location, etc)
          repeats.add(repeat);
          appointment.setRepeatMade(true);
          editedRepeatsFlag = true;
          if (! appointmentOld.isRepeatMade()) {
              editedAppointmentsFlag = true; // only edit appointments if appointment existed previously
          }
          break;
        case WITH_EXISTING_REPEAT:
            if (! appointmentChanged) choices = new RepeatChange[] {RepeatChange.ALL, RepeatChange.FUTURE};
//            changeResponse = repeatChangeDialog(choices);
            changeResponse = changeDialogCallback.call(choices);
            switch (changeResponse)
            {
            case ONE:
                appointment.setRepeatMade(false);
                appointment.setInPlaceOfRecurrance(appointmentOld.getStartLocalDateTime());
                if (startMinuteShift != 0 || endMinuteShift != 0)
                { // if appointment has new day or time make it individual
                    appointment.setRepeat(null); // make appointment individual if time changes
                    repeat.getDeletedDates().add(startDateOld);
                    editedRepeatsFlag = true;
                }
//                writeAppointments = true;
                editedAppointmentsTemp.add(appointment);
                if (repeatOld != null)
                {
                    repeat.unbindAll();
                    repeatOld.copyInto(repeat);   // restore original repeat rule
                }
                break;
            case ALL:
                repeat.unbindAll();
                if (appointment.isRepeatMade())
                { // copy all appointment changes (i.e. description, group, location, etc)
                    appointment.copyNonDateFieldsInto(repeat.getAppointmentData());
//                    repeat.copyInto(appointment.getRepeat());
                } else { // copy non-unique appointment changes (i.e. description, group, location, etc)
//                    appointment.copyInto(repeat.getAppointmentData(), appointmentOld);
                    repeat.getAppointmentData().copyNonDateFieldsInto(appointment, appointmentOld);
//                    repeat.copyAppointmentInto(appointment, appointmentOld);
//                    appointment.copyInto(repeat.getAppointmentData(), appointmentOld);
                }
                switch (repeat.getIntervalUnit())
                {
                case DAILY: // fall through
                case MONTHLY: // fall through
                case YEARLY:
                    repeat.adjustDateTime(true, startTemporalAdjuster, endTemporalAdjuster);
                    switch (repeat.getEndCriteria())
                    {
                    case AFTER:
                        repeat.makeEndOnDateFromEndAfterEvents();
                        break;
                    case ON:
//                        repeat.makeEndAfterEventsFromEndOnDate();
                        break;
                    case NEVER:
                        break;
                    }
//                    System.out.println("before " + repeat.getStartLocalDate() + " " + repeat.getStartLocalTime());
//                    repeat.adjustDateTime(true, startTemporalAdjuster, endTemporalAdjuster);
//                    System.out.println("after " + repeat.getStartLocalDate() + " " + repeat.getStartLocalTime());
                    break;
                case WEEKLY:
//                    System.out.println("dayShift " + dayShift);
                    if (dayShift != 0)
                    { // change selected day of week if there is a day shift
                        final DayOfWeek dayOfWeekOld = appointmentOld.getStartLocalDateTime().getDayOfWeek();
                        final DayOfWeek dayOfWeekNew = appointment.getStartLocalDateTime().getDayOfWeek();
                        repeat.setDayOfWeek(dayOfWeekOld, false);
                        repeat.setDayOfWeek(dayOfWeekNew, true);
                    }
//                    boolean adjustStartDate = dayShift == 0;
                    boolean adjustStartDate = startDate.equals(repeat.getStartLocalDate());
//                    System.out.println("startDateOld " + startDateOld + " " + startDate);
                    repeat.adjustDateTime(adjustStartDate, startTemporalAdjuster, endTemporalAdjuster);
                }
//                System.out.println("size1 " + appointments.size());
                repeat.updateAppointments(appointments, appointment, appointmentOld
                        , startTemporalAdjuster, endTemporalAdjuster);
                editedAppointmentsTemp.addAll(repeat
                      .getAppointments()
                      .stream()
                      .filter(a -> ! a.isRepeatMade())
                      .collect(Collectors.toSet()));
//                System.out.println("size2 " + appointments.size());
                editedRepeatsFlag = true;
                break;
            case FUTURE:
                repeat.unbindAll();
                // Copy changes to repeat  (i.e. description, group, location, etc)
                repeat.setStartLocalDate(startDate);
                if (appointment.isRepeatMade())
                { // copy all appointment changes
                    appointment.copyNonDateFieldsInto(repeat.getAppointmentData());
                } else { // copy non-unique appointment changes
                    repeat.getAppointmentData().copyNonDateFieldsInto(appointment, appointmentOld);
//                    repeat.copyAppointmentInto(appointment, appointmentOld);
//                    appointment.copyAppointmentInto(repeat.getAppointmentData(), appointmentOld);
                }
                
                // Split deleted dates between repeat and repeatOld
                repeatOld.getDeletedDates().clear();
                final Iterator<LocalDate> dateIterator = repeat.getDeletedDates().iterator();
                while (dateIterator.hasNext())
                {
                    LocalDate d = dateIterator.next();
                    if (d.isBefore(startDate))
                    {
                        dateIterator.remove();
                    } else {
                        repeatOld.getDeletedDates().add(d);
                    }
                }
                
                // Split appointments between repeat and repeatOld
                repeatOld.getAppointments().clear();
                Iterator<RepeatableAppointment> appointmentIterator = repeat.getAppointments().iterator();
//                int counter = 0;
                while (appointmentIterator.hasNext())
                {
                    RepeatableAppointment a = appointmentIterator.next();
                    if (a.getStartLocalDateTime().toLocalDate().isBefore(startDate))
                    {
                        appointmentIterator.remove();
                        repeatOld.getAppointments().add(a);
//                    } else {
//                        counter++;                                    
                    }
                }
                
                // Modify start and end date for repeat and repeatOld.  Adjust IntervalUnit specific data
                repeatOld.setEndCriteria(EndCriteria.ON);
                repeatOld.setEndAfterEvents(null); // criteria changed to ON so null endAfterEvents
//                repeatOld.setEndOnDate(startDate.minusDays(1)); //TODO - NEED TO SET TO LAST VALID DATE
                repeatOld.setEndOnDate(repeatOld.previousValidDate(startDateOld));
//                boolean adjustStartDate;
                switch (repeat.getIntervalUnit())
                {
                case DAILY: // fall through
                case MONTHLY: // fall through
                case YEARLY:
//                    adjustStartDate = startDate.equals(repeat.getStartLocalDate());
//                    System.out.println("repeat.getEndAfterEvents( " + repeat.getEndAfterEvents() + " " + repeat.getEndOnDate() + " " + dayShift);
                    repeat.adjustDateTime(false, startTemporalAdjuster, endTemporalAdjuster);
                    if (repeat.getEndCriteria() != EndCriteria.NEVER)
                    {
                        LocalDate newEndOnDate = repeat.getEndOnDate().plusDays(dayShift);
                        repeat.setEndOnDate(newEndOnDate);
                    }
                    if (repeat.getEndCriteria() == EndCriteria.AFTER) repeat.makeEndAfterEventsFromEndOnDate();
//                    System.out.println("repeat.getEndAfterEvents( " + repeat.getEndAfterEvents() + " " + repeat.getEndOnDate());
//                    switch (repeat.getEndCriteria())
//                    {
//                    case AFTER:
//                        repeat.makeEndOnDateFromEndAfterEvents();
//                        repeat.makeEndAfterEventsFromEndOnDate();
//                        break;
//                    case ON:
//                        repeat.makeEndAfterEventsFromEndOnDate();
//                        break;
//                    case NEVER:
//                        break;
//                    }
//                    System.out.println("start list");
//                    appointments.stream().forEach(a -> System.out.println(a.getStartLocalDateTime()));
//                    System.out.println("before update " + appointments.size());
                    repeatOld.updateAppointments(appointments, appointment);
//                    appointments.stream().forEach(a -> System.out.println(a.getStartLocalDateTime()));
//                    System.out.println("after update " + appointments.size());
//                    System.out.println(repeat);
                    break;
                case WEEKLY:
                    if (dayShift != 0)
                    { // change selected day of week if there is a day shift
                        final DayOfWeek dayOfWeekOld = appointmentOld.getStartLocalDateTime().getDayOfWeek();
                        final DayOfWeek dayOfWeekNew = appointment.getStartLocalDateTime().getDayOfWeek();
                        repeat.setDayOfWeek(dayOfWeekOld, false);
                        repeat.setDayOfWeek(dayOfWeekNew, true);
                    }
//                    adjustStartDate = startDate.equals(repeat.getStartLocalDate());
                    repeat.adjustDateTime(false, startTemporalAdjuster, endTemporalAdjuster);
                }
                if (repeat.getEndCriteria() != EndCriteria.NEVER) repeat.makeEndAfterEventsFromEndOnDate();
                repeat.updateAppointments(appointments, appointment, appointmentOld
                        , startTemporalAdjuster, endTemporalAdjuster);
                repeats.add(repeatOld);
//                appointments.stream().forEach(a -> System.out.println(a.getStartLocalDateTime()));
//                System.out.println("after update2 " + appointments.size());
//                System.out.println("repeatOld.getEndOnDate() " + repeatOld.getEndOnDate() + " " + repeatOld.getStartLocalDate());
//                System.out.println("repeat.getEndOnDate() " + repeat.getEndOnDate() + " " + repeat.getStartLocalDate());
//              System.exit(0);                
                editedRepeatsFlag = true;
                if (repeatOld.oneAppointmentToIndividual(repeats, appointments)) editedAppointmentsFlag = true;  // Check if any repeats have only one appointment and should become individual
                // TODO - CHANGES OF CONVERT TO ONE APPOINTMENT
                break;
            case CANCEL: // restore old appointment and repeat rule
                appointmentOld.copyInto(appointment);
                repeatOld.copyInto(appointment.getRepeat());
//              Iterator<DayOfWeek> dayOfWeekIterator = Arrays 
//              .stream(DayOfWeek.values())
//              .limit(7)
//              .iterator();
//          while (dayOfWeekIterator.hasNext())
//          {
//              DayOfWeek key = dayOfWeekIterator.next();
//              boolean b1 = repeat.getDayOfWeekMap().get(key).get();
//              boolean b2 = repeatOld.getDayOfWeekMap().get(key).get();
//              System.out.println("copied day of week2 " + key + " " + b1 + " " + b2);
//          }
            default: // do nothing
                return WindowCloseType.CLOSE_WITHOUT_CHANGE;
            }
            // Check if any repeats have only one appointment and should become individual
            if (repeat.oneAppointmentToIndividual(repeats, appointments)) editedAppointmentsFlag = true;
            break;
        default:
            throw new InvalidParameterException("Invalid Appointment Type");
        }
        
        // Write changes that occurred
        System.out.println("write flags " + editedAppointmentsFlag +  " " + editedRepeatsFlag);
//        if (writeAppointments) AppointmentFactory.writeToFile(appointments);
//        if (writeRepeats) MyRepeat.writeToFile(repeats);

        if (editedAppointmentsFlag && (writeAppointmentsCallback != null)) writeAppointmentsCallback.call(appointments); // write appointment changes
        if (editedRepeatsFlag && (writeRepeatsCallback != null)) writeRepeatsCallback.call(repeats);                     // write repeat changes

//        if (editedAppointmentsFlag) editedAppointments.addAll(editedAppointmentsTemp);
//        if (editedRepeatsFlag) editedRepeats.add(repeat);                     // write repeat changes

        
        return (editedAppointmentsFlag || editedRepeatsFlag) ? WindowCloseType.CLOSE_WITH_CHANGE : WindowCloseType.CLOSE_WITHOUT_CHANGE;
        
    }

    /**
     * Edit repeatable appointment.
     * Uses default callbacks for dialog, write appointments and write repeats
     * 
     * @param appointments
     * @param appointment
     * @param appointmentOld
     * @param repeats
     * @return
     */
    public static WindowCloseType editAppointments(
              Collection<Appointment> appointments
            , RepeatableAppointment appointment
            , RepeatableAppointment appointmentOld
            , Collection<Repeat> repeats)
//            , Collection<RepeatableAppointment> editedAppointments
//            , Collection<Repeat> editedRepeats)
    {
        return editAppointments(
                appointment
              , appointmentOld
              , appointments
              , repeats
              , a -> repeatChangeDialog()
//              , editedAppointments
//              , editedRepeats);
              , a -> { AppointmentFactory.writeToFile(a); return null; }
              , r -> { RepeatImpl.writeToFile(r); return null; });
    }
    
    
    /**
     * Edit repeatable appointment.
     * Uses default callbacks for dialog, write appointments and write repeats
     * 
     * @param appointments
     * @param appointment
     * @param appointmentOld
     * @param repeats
     * @return
     */
    public static WindowCloseType editAppointments(
            RepeatableAppointment appointment
          , RepeatableAppointment appointmentOld
          , Collection<Appointment> appointments
          , Collection<Repeat> repeats
          , Callback<Collection<Appointment>, Void> writeAppointmentsCallback
          , Callback<Collection<Repeat>, Void> writeRepeatsCallback)
    {
        return editAppointments(
                appointment
              , appointmentOld
              , appointments
              , repeats
              , a -> repeatChangeDialog()
              , writeAppointmentsCallback
              , writeRepeatsCallback);
    }

    
    
    
    private static AppointmentType makeAppointmentType(Repeat repeat, Repeat repeatOld)
    {

        if (repeat == null)
        {
            if (repeatOld == null)
            { // doesn't have repeat or have old repeat either
                return AppointmentType.INDIVIDUAL;
            } else {
                return AppointmentType.HAD_REPEAT_BECOMING_INDIVIDUAL;
            }
        } else
        {
            if (repeat.isNew())
            {
                return AppointmentType.WITH_NEW_REPEAT;                
            } else {
                return AppointmentType.WITH_EXISTING_REPEAT;
            }
        }
    }

    private enum AppointmentType {
        INDIVIDUAL
      , WITH_EXISTING_REPEAT
      , WITH_NEW_REPEAT
      , HAD_REPEAT_BECOMING_INDIVIDUAL
  }
    
    /**
     * Options available when changing a repeatable appointment
     * ONE: Change only selected appointment
     * ALL: Change all appointments with repeat rule
     * FUTURE: Change future appointments with repeat rule
     * @author David Bal
     *
     */
    public enum RepeatChange {
        ONE, ALL, FUTURE, CANCEL;

        @Override
        public String toString() {
            return Settings.REPEAT_CHANGE_CHOICES.get(this);
        }
    }
    
    public enum WindowCloseType {
        X, CANCEL, CLOSE_WITH_CHANGE, CLOSE_WITHOUT_CHANGE
        
    }
    
    /**
     * Removes an element from a collection.
     * Similar to removeIf, but quits when one remove occurs
     * 
     * @param collection
     * @param element
     * @return
     */
    public static <T> boolean removeOne(Collection<T> collection, T element) {
        Iterator<T> i = collection.iterator();
        while (i.hasNext()) {
            T a = i.next();
            if (a == element) {
                i.remove();
                return true;
            }
        }
        return false;
    }
    
}
