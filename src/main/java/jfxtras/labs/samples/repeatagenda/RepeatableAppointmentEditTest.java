package jfxtras.labs.samples.repeatagenda;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import javafx.util.Callback;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.Agenda.Appointment;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.AppointmentFactory;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.Repeat;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.Repeat.EndCriteria;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.Repeat.IntervalUnit;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.RepeatableUtilities;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.RepeatableUtilities.RepeatChange;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.RepeatableUtilities.WindowCloseType;

public class RepeatableAppointmentEditTest extends RepeatTestAbstract {

    private final Callback<Collection<Appointment>, Void> writeAppointmentsCallback = (a) -> { return null; };
    private final Callback<Collection<Repeat>, Void> writeRepeatsCallback = (r) -> { return null; };
    
    @Test
    public void editRepeatableAppointmentsDaily()
    {
        Repeat repeat = getRepeatDailyFixed();
        Set<Repeat> repeats = new HashSet<Repeat>(Arrays.asList(repeat));
        List<Appointment> appointments = new ArrayList<Appointment>();
        LocalDate startDate = LocalDate.of(2015, 11, 1);
        LocalDate endDate = LocalDate.of(2015, 11, 7);
        repeat.makeAppointments(appointments, startDate, endDate);
        Appointment madeAppointment = (appointments.size() == 1) ? appointments.get(0) : null;
        Appointment appointmentOld = AppointmentFactory.newAppointment(madeAppointment);
        LocalDate date = madeAppointment.getStartLocalDateTime().toLocalDate();
        madeAppointment.setStartLocalDateTime(date.atTime(9, 45));
        madeAppointment.setEndLocalDateTime(date.atTime(11, 0));

        Callback<RepeatChange[], RepeatChange> dialogStub = (a) -> RepeatChange.ALL;
        WindowCloseType windowCloseType = RepeatableUtilities.editAppointments(
                appointments
              , madeAppointment
              , appointmentOld
              , repeats
              , dialogStub
              , writeAppointmentsCallback
              , writeRepeatsCallback);
        assertEquals(WindowCloseType.CLOSE_WITH_CHANGE, windowCloseType); // check to see if close type is corect
        
        Appointment expectedAppointment = AppointmentFactory.newAppointment()
            .withStartLocalDateTime(LocalDate.of(2015, 11, 3).atTime(9, 45))
            .withEndLocalDateTime(LocalDate.of(2015, 11, 3).atTime(11, 0))
            .withAppointmentGroup(appointmentGroups.get(9))
            .withSummary("Daily Appointment Fixed")
            .withRepeatMade(true)
            .withRepeat(repeat);
        assertEquals(expectedAppointment, madeAppointment); // Check to see if repeat-generated appointment changed correctly
        
        Appointment a = new MyAppointment()
                .withAppointmentGroup(appointmentGroups.get(15))
                .withSummary("Daily Appointment Fixed")
                .withStartLocalDateTime(LocalDate.of(2015, 11, 3).atTime(9, 45))
                .withEndLocalDateTime(LocalDate.of(2015, 11, 3).atTime(11, 0));
        Repeat expectedRepeat =  new MyRepeat()
                .withStartLocalDate(LocalDate.of(2015, 10, 7))
                .withStartLocalTime(LocalTime.of(9, 45))
                .withEndLocalTime(LocalTime.of(11, 0))
                .withIntervalUnit(IntervalUnit.DAILY)
                .withRepeatFrequency(3)
                .withEndCriteria(EndCriteria.AFTER)
                .withEndAfterEvents(10)
                .withAppointmentData(a);
        assertEquals(expectedRepeat, repeat); // check to see if repeat rule changed correctly
    }

}
