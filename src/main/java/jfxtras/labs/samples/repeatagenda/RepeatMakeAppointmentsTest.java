package jfxtras.labs.samples.repeatagenda;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;

import jfxtras.labs.samples.repeatagenda.scene.control.agenda.Agenda.Appointment;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.AppointmentFactory;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.Repeat;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.RepeatableAgenda.RepeatableAppointment;

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
        List<RepeatableAppointment> appointments = new ArrayList<RepeatableAppointment>();
        LocalDate startDate = LocalDate.of(2015, 11, 1);
        LocalDate endDate = LocalDate.of(2015, 11, 7);
        Collection<RepeatableAppointment> newAppointments = repeat.makeAppointments(startDate, endDate);
        appointments.addAll(newAppointments);
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
        List<RepeatableAppointment> appointments = new ArrayList<RepeatableAppointment>();
        LocalDate startDate = LocalDate.of(2017, 11, 1);
        LocalDate endDate = LocalDate.of(2017, 11, 7);
        Collection<RepeatableAppointment> newAppointments = repeat.makeAppointments(startDate, endDate);
        appointments.addAll(newAppointments);
        assertTrue(appointments.size() == 0);
    }
    @Test
    public void makeAppointmentsMonthly2()
    {
        Repeat repeat = getRepeatMonthlyFixed2();
        List<RepeatableAppointment> appointments = new ArrayList<RepeatableAppointment>();
        LocalDate startDate = LocalDate.of(2015, 12, 13);
        LocalDate endDate = LocalDate.of(2015, 12, 19);
        Collection<RepeatableAppointment> newAppointments = repeat.makeAppointments(startDate, endDate);
        appointments.addAll(newAppointments);
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
        List<RepeatableAppointment> appointments = new ArrayList<RepeatableAppointment>();
        LocalDate startDate = LocalDate.of(2015, 12, 13);
        LocalDate endDate = LocalDate.of(2015, 12, 19);
        Collection<RepeatableAppointment> newAppointments = repeat.makeAppointments(startDate, endDate);
        appointments.addAll(newAppointments);

        RepeatableAppointment madeAppointment1 = (appointments.size() >= 1) ? appointments.get(0) : null;
        Appointment expectedAppointment1 = AppointmentFactory.newAppointment()
                .withStartLocalDateTime(LocalDate.of(2015, 12, 16).atTime(18, 0))
                .withEndLocalDateTime(LocalDate.of(2015, 12, 16).atTime(18, 45))
                .withAppointmentGroup(appointmentGroups.get(3))
                .withSummary("Weekly Appointment Fixed")
                .withRepeatMade(true)
                .withRepeat(repeat);
        assertEquals(expectedAppointment1, madeAppointment1);   
        
        RepeatableAppointment madeAppointment2 = (appointments.size() >= 2) ? appointments.get(1) : null;
        Appointment expectedAppointment2 = AppointmentFactory.newAppointment()
                .withStartLocalDateTime(LocalDate.of(2015, 12, 18).atTime(18, 0))
                .withEndLocalDateTime(LocalDate.of(2015, 12, 18).atTime(18, 45))
                .withAppointmentGroup(appointmentGroups.get(3))
                .withSummary("Weekly Appointment Fixed")
                .withRepeatMade(true)
                .withRepeat(repeat);
        assertEquals(expectedAppointment2, madeAppointment2);     
    }
    
    /**
     * Tests changing start and end date to see appointments get deleted and restored
     * Also confirms doesn't make appointments when outside repeat rule range
     */
    @Test
    public void makeAppointmentsMonthly3()
    {
        Repeat repeat = getRepeatMonthlyFixed2();
        List<RepeatableAppointment> appointments = new ArrayList<RepeatableAppointment>();
        LocalDate startDate = LocalDate.of(2015, 12, 13);
        LocalDate endDate = LocalDate.of(2015, 12, 19);
        Collection<RepeatableAppointment> newAppointments = repeat.makeAppointments(startDate, endDate);
        appointments.addAll(newAppointments);
        Appointment madeAppointment = (appointments.size() == 1) ? appointments.get(0) : null;
        Appointment expectedAppointment = AppointmentFactory.newAppointment()
                .withStartLocalDateTime(LocalDate.of(2015, 12, 17).atTime(8, 45))
                .withEndLocalDateTime(LocalDate.of(2015, 12, 17).atTime(10, 15))
                .withAppointmentGroup(appointmentGroups.get(9))
                .withSummary("Monthly Appointment Fixed2")
                .withRepeatMade(true)
                .withRepeat(repeat);
        assertEquals(expectedAppointment, madeAppointment);
        
        startDate = LocalDate.of(2015, 12, 6);
        endDate = LocalDate.of(2015, 12, 12);
        repeat.removeOutsideRangeAppointments(appointments, startDate, endDate);
        assertEquals(0, appointments.size());

        startDate = LocalDate.of(2016, 1, 17);
        endDate = LocalDate.of(2016, 1, 23);
        Collection<RepeatableAppointment> newAppointments2 = repeat.makeAppointments(startDate, endDate);
        appointments.addAll(newAppointments2);
        assertEquals(1, appointments.size());
        
        Appointment madeAppointment2 = appointments.get(0);
        Appointment expectedAppointment2 = AppointmentFactory.newAppointment()
                .withStartLocalDateTime(LocalDate.of(2016, 1, 21).atTime(8, 45))
                .withEndLocalDateTime(LocalDate.of(2016, 1, 21).atTime(10, 15))
                .withAppointmentGroup(appointmentGroups.get(9))
                .withSummary("Monthly Appointment Fixed2")
                .withRepeatMade(true)
                .withRepeat(repeat);
        assertEquals(expectedAppointment2, madeAppointment2); // Check to see if repeat-generated appointment changed correctly
        
        startDate = LocalDate.of(2015, 9, 1); // test dates before startDate to confirm doesn't make appointments
        endDate = LocalDate.of(2015, 9, 30);
        repeat.removeOutsideRangeAppointments(appointments, startDate, endDate);
        Collection<RepeatableAppointment> newAppointments3 = repeat.makeAppointments(startDate, endDate);
        appointments.addAll(newAppointments3);
        assertEquals(0, appointments.size());

        startDate = LocalDate.of(2017, 9, 1); // test dates after endDate to confirm doesn't make appointments
        endDate = LocalDate.of(2017, 9, 30);
        repeat.removeOutsideRangeAppointments(appointments, startDate, endDate);
        Collection<RepeatableAppointment> newAppointments4 = repeat.makeAppointments(startDate, endDate);
        appointments.addAll(newAppointments4);
        assertEquals(0, appointments.size());
    }
}
