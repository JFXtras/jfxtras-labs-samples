package jfxtras.labs.samples.repeatagenda;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import jfxtras.labs.samples.repeatagenda.scene.control.agenda.Agenda.Appointment;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.AppointmentFactory;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.Repeat;

/**
 * Tests if the makeAppointments is producing correct Appointments
 * 
 * @author David Bal
 *
 */
public class RepeatMakeAppointmentsTest extends RepeatTestAbstract {

    // Make Appointments tests
    @Test
    public void makeAppointmentsMonthly()
    {
        Repeat repeat = getRepeatMonthlyFixed();
        List<Appointment> appointments = new ArrayList<Appointment>();
        LocalDate startDate = LocalDate.of(2015, 11, 1);
        LocalDate endDate = LocalDate.of(2015, 11, 7);
        repeat.makeAppointments(appointments, startDate, endDate);
        Appointment madeAppointment = (appointments.size() == 1) ? appointments.get(0) : null;
        Appointment expectedAppointment = AppointmentFactory.newAppointment()
                .withStartLocalDateTime(LocalDate.of(2015, 11, 7).atTime(8, 45))
                .withEndLocalDateTime(LocalDate.of(2015, 11, 7).atTime(10, 15))
                .withAppointmentGroup(appointmentGroups.get(9))
                .withSummary("Monthly Appointment Fixed")
                .withRepeatMade(true)
                .withRepeat(repeat);
        assertEquals(expectedAppointment, madeAppointment);     
    }
    @Test
    public void makeAppointmentsMonthlyOutsideDateBounds()
    {
        Repeat repeat = getRepeatMonthlyFixed();
        List<Appointment> appointments = new ArrayList<Appointment>();
        LocalDate startDate = LocalDate.of(2017, 11, 1);
        LocalDate endDate = LocalDate.of(2017, 11, 7);
        repeat.makeAppointments(appointments, startDate, endDate);
        assertTrue(appointments.size() == 0);
    }
    @Test
    public void makeAppointmentsMonthly2()
    {
        Repeat repeat = getRepeatMonthlyFixed2();
        List<Appointment> appointments = new ArrayList<Appointment>();
        LocalDate startDate = LocalDate.of(2015, 12, 13);
        LocalDate endDate = LocalDate.of(2015, 12, 19);
        repeat.makeAppointments(appointments, startDate, endDate);
        Appointment madeAppointment = (appointments.size() == 1) ? appointments.get(0) : null;
        Appointment expectedAppointment = AppointmentFactory.newAppointment()
                .withStartLocalDateTime(LocalDate.of(2015, 12, 17).atTime(8, 45))
                .withEndLocalDateTime(LocalDate.of(2015, 12, 17).atTime(10, 15))
                .withAppointmentGroup(appointmentGroups.get(9))
                .withSummary("Monthly Appointment Fixed2")
                .withRepeatMade(true)
                .withRepeat(repeat);
        assertEquals(expectedAppointment, madeAppointment);     
    }
    @Test
    public void makeAppointmentsWeekly()
    {
        Repeat repeat = getRepeatWeeklyFixed();
        List<Appointment> appointments = new ArrayList<Appointment>();
        LocalDate startDate = LocalDate.of(2015, 12, 13);
        LocalDate endDate = LocalDate.of(2015, 12, 19);
        repeat.makeAppointments(appointments, startDate, endDate);

        Appointment madeAppointment1 = (appointments.size() >= 1) ? appointments.get(0) : null;
        Appointment expectedAppointment1 = AppointmentFactory.newAppointment()
                .withStartLocalDateTime(LocalDate.of(2015, 12, 16).atTime(18, 0))
                .withEndLocalDateTime(LocalDate.of(2015, 12, 16).atTime(18, 45))
                .withAppointmentGroup(appointmentGroups.get(9))
                .withSummary("Weekly Appointment Fixed")
                .withRepeatMade(true)
                .withRepeat(repeat);
        assertEquals(expectedAppointment1, madeAppointment1);   
        
        Appointment madeAppointment2 = (appointments.size() >= 2) ? appointments.get(1) : null;
        Appointment expectedAppointment2 = AppointmentFactory.newAppointment()
                .withStartLocalDateTime(LocalDate.of(2015, 12, 18).atTime(18, 0))
                .withEndLocalDateTime(LocalDate.of(2015, 12, 18).atTime(18, 45))
                .withAppointmentGroup(appointmentGroups.get(9))
                .withSummary("Weekly Appointment Fixed")
                .withRepeatMade(true)
                .withRepeat(repeat);
        assertEquals(expectedAppointment2, madeAppointment2);     
    }
}
