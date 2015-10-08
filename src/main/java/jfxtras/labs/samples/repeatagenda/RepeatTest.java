package jfxtras.labs.samples.repeatagenda;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import jfxtras.labs.samples.repeatagenda.scene.control.agenda.Repeat;


public class RepeatTest extends RepeatTestAbstract {

    /**
     * Tests NextAppointment method to determine if it can return the startDate for any 
     * earlier date.
     */
    // Variable date tests - based on LocalDate.now()
    @Test
    public void canGetFirstDateDailyM1()
    {
        Repeat r = getRepeatDaily();
        LocalDate firstMatchDate = r.nextValidDateSlow(r.getStartLocalDate().minusDays(1));
        assertEquals(r.getStartLocalDate(), firstMatchDate);
    }
    @Test
    public void canGetFirstDateDailyM10()
    {
        Repeat r = getRepeatDaily();
        LocalDate firstMatchDate = r.nextValidDateSlow(r.getStartLocalDate().minusDays(10));
        assertEquals(r.getStartLocalDate(), firstMatchDate);
    }
    @Test
    public void canGetFirstDateDailyM100()
    {
        Repeat r = getRepeatDaily();
        LocalDate firstMatchDate = r.nextValidDateSlow(r.getStartLocalDate().minusDays(100));
        assertEquals(r.getStartLocalDate(), firstMatchDate);
    }
    @Test
    public void canGetFirstDateWeeklyM1()
    {
        Repeat r = getRepeatWeekly();
        LocalDate firstMatchDate = r.nextValidDateSlow(r.getStartLocalDate().minusDays(1));
        assertEquals(r.getStartLocalDate(), firstMatchDate);
    }
    @Test
    public void canGetFirstDateWeeklyM10()
    {
        Repeat r = getRepeatWeekly();
//        System.out.println("r.getStartLocalDate() " + r.getStartLocalDate());
        LocalDate firstMatchDate = r.nextValidDateSlow(r.getStartLocalDate().minusDays(10));
        assertEquals(r.getStartLocalDate(), firstMatchDate);
    }
    @Test
    public void canGetFirstDateWeeklyM100()
    {
        Repeat r = getRepeatWeekly();
        LocalDate firstMatchDate = r.nextValidDateSlow(r.getStartLocalDate().minusDays(100));
        assertEquals(r.getStartLocalDate(), firstMatchDate);
    }
    @Test
    public void canGetFirstDateMonthlyM1()
    {
        Repeat r = getRepeatMonthly();
        LocalDate firstMatchDate = r.nextValidDateSlow(r.getStartLocalDate().minusDays(1));
        assertEquals(r.getStartLocalDate(), firstMatchDate);
    }
    @Test
    public void canGetFirstDateMonthlyM10()
    {
        Repeat r = getRepeatMonthly();
        LocalDate firstMatchDate = r.nextValidDateSlow(r.getStartLocalDate().minusDays(10));
        assertEquals(r.getStartLocalDate(), firstMatchDate);
    }
    @Test
    public void canGetFirstDateMonthlyM100()
    {
        Repeat r = getRepeatMonthly();
        LocalDate firstMatchDate = r.nextValidDateSlow(r.getStartLocalDate().minusDays(100));
        assertEquals(r.getStartLocalDate(), firstMatchDate);
    }
    
    // Fixed tests
    @Test
    public void canGetFirstDateWeeklyFixedM0()
    {
        Repeat r = getRepeatWeeklyFixed();
        LocalDate firstMatchDate = r.nextValidDateSlow(r.getStartLocalDate().minusDays(0));
        assertEquals(LocalDate.of(2015, 10, 9), firstMatchDate);
    }
    @Test
    public void canGetFirstDateWeeklyFixedM1()
    {
        Repeat r = getRepeatWeeklyFixed();
        LocalDate firstMatchDate = r.nextValidDateSlow(r.getStartLocalDate().minusDays(1));
        assertEquals(LocalDate.of(2015, 10, 7), firstMatchDate);
    }
    @Test
    public void canGetFirstDateWeeklyFixedM10()
    {
        Repeat r = getRepeatWeeklyFixed();
//        System.out.println("r.getStartLocalDate() " + r.getStartLocalDate());
        LocalDate firstMatchDate = r.nextValidDateSlow(r.getStartLocalDate().minusDays(10));
        assertEquals(LocalDate.of(2015, 10, 7), firstMatchDate);
    }
    @Test
    public void canGetFirstDateWeeklyFixedM100()
    {
        Repeat r = getRepeatWeeklyFixed();
        LocalDate firstMatchDate = r.nextValidDateSlow(r.getStartLocalDate().minusDays(100));
        assertEquals(LocalDate.of(2015, 10, 7), firstMatchDate);
    }
    @Test
    public void canGetFirstDateDailyFixedP0()
    {
        Repeat r = getRepeatDailyFixed();
        LocalDate firstMatchDate = r.nextValidDateSlow(r.getStartLocalDate().plusDays(0));
        assertEquals(LocalDate.of(2015, 10, 10), firstMatchDate);
    }
    @Test
    public void canGetFirstDateDailyFixedP1()
    {
        Repeat r = getRepeatDailyFixed();
        LocalDate firstMatchDate = r.nextValidDateSlow(r.getStartLocalDate().plusDays(1));
        assertEquals(LocalDate.of(2015, 10, 10), firstMatchDate);
    }
    @Test
    public void canGetFirstDateDailyFixedP10()
    {
        Repeat r = getRepeatDailyFixed();
//        System.out.println("r.getStartLocalDate() " + r.getStartLocalDate());
        LocalDate firstMatchDate = r.nextValidDateSlow(r.getStartLocalDate().plusDays(10));
        assertEquals(LocalDate.of(2015, 10, 19), firstMatchDate);
    }
    @Test
    public void canGetFirstDateDailyFixedP100()
    {
        Repeat r = getRepeatDailyFixed();
        LocalDate firstMatchDate = r.nextValidDateSlow(r.getStartLocalDate().plusDays(100));
        assertEquals(LocalDate.of(2016, 1, 17), firstMatchDate);
    }
    @Test
    public void canGetFirstDateWeeklyFixedP0()
    {
        Repeat r = getRepeatWeeklyFixed();
        LocalDate firstMatchDate = r.nextValidDateSlow(r.getStartLocalDate().plusDays(0));
        assertEquals(LocalDate.of(2015, 10, 9), firstMatchDate);
    }
    @Test
    public void canGetFirstDateWeeklyFixedP1()
    {
        Repeat r = getRepeatWeeklyFixed();
        LocalDate firstMatchDate = r.nextValidDateSlow(r.getStartLocalDate().plusDays(1));
        assertEquals(LocalDate.of(2015, 10, 9), firstMatchDate);
    }
    @Test
    public void canGetFirstDateWeeklyFixedP10()
    {
        Repeat r = getRepeatWeeklyFixed();
//        System.out.println("r.getStartLocalDate() " + r.getStartLocalDate());
        LocalDate firstMatchDate = r.nextValidDateSlow(r.getStartLocalDate().plusDays(10));
        assertEquals(LocalDate.of(2015, 10, 21), firstMatchDate);
    }
    @Test
    public void canGetFirstDateWeeklyFixedP100()
    {
        Repeat r = getRepeatWeeklyFixed();
        LocalDate firstMatchDate = r.nextValidDateSlow(r.getStartLocalDate().plusDays(100));
        assertEquals(LocalDate.of(2016, 1, 20), firstMatchDate);
    }
    @Test
    public void canGetFirstDateWeeklyFixed2P0()
    {
        Repeat r = getRepeatWeeklyFixed2();
//        r.dayOfWeekMap.entrySet().stream().forEach(System.out::println);
        LocalDate firstMatchDate = r.nextValidDateSlow(r.getStartLocalDate().plusDays(0));
        assertEquals(LocalDate.of(2015, 10, 7), firstMatchDate);
    }
    @Test
    public void canGetFirstDateWeeklyFixed2P1()
    {
        Repeat r = getRepeatWeeklyFixed2();
        LocalDate firstMatchDate = r.nextValidDateSlow(r.getStartLocalDate().plusDays(1));
        assertEquals(LocalDate.of(2015, 10, 7), firstMatchDate);
    }
    @Test
    public void canGetFirstDateWeeklyFixed2P10()
    {
        Repeat r = getRepeatWeeklyFixed2();
//        System.out.println("r.getStartLocalDate() " + r.getStartLocalDate());
        LocalDate firstMatchDate = r.nextValidDateSlow(r.getStartLocalDate().plusDays(10));
        assertEquals(LocalDate.of(2015, 10, 19), firstMatchDate);
    }
    @Test
    public void canGetFirstDateWeeklyFixed2P100()
    {
        Repeat r = getRepeatWeeklyFixed2();
        LocalDate firstMatchDate = r.nextValidDateSlow(r.getStartLocalDate().plusDays(100));
        assertEquals(LocalDate.of(2016, 1, 15), firstMatchDate);
    }
    @Test
    public void canGetFirstDateMonthlyFixedP0()
    {
        Repeat r = getRepeatMonthlyFixed();
        LocalDate firstMatchDate = r.nextValidDateSlow(r.getStartLocalDate().plusDays(0));
        assertEquals(LocalDate.of(2015, 11, 7), firstMatchDate);
    }
    @Test
    public void canGetFirstDateMonthlyFixedP1()
    {
        Repeat r = getRepeatMonthlyFixed();
        LocalDate firstMatchDate = r.nextValidDateSlow(r.getStartLocalDate().plusDays(1));
        assertEquals(LocalDate.of(2015, 11, 7), firstMatchDate);
    }
    @Test
    public void canGetFirstDateMonthlyFixedP10()
    {
        Repeat r = getRepeatMonthlyFixed();
//        System.out.println("r.getStartLocalDate() " + r.getStartLocalDate());
        LocalDate firstMatchDate = r.nextValidDateSlow(r.getStartLocalDate().plusDays(10));
        assertEquals(LocalDate.of(2015, 11, 7), firstMatchDate);
    }
    @Test
    public void canGetFirstDateMonthlyFixedP100()
    {
        Repeat r = getRepeatMonthlyFixed();
        LocalDate firstMatchDate = r.nextValidDateSlow(r.getStartLocalDate().plusDays(100));
        assertEquals(LocalDate.of(2016, 2, 7), firstMatchDate);
    }
    @Test
    public void canGetFirstDateMonthlyFixed2P0()
    {
        Repeat r = getRepeatMonthlyFixed2();
        LocalDate firstMatchDate = r.nextValidDateSlow(r.getStartLocalDate().plusDays(0));
        assertEquals(LocalDate.of(2015, 11, 19), firstMatchDate);
    }
    @Test
    public void canGetFirstDateMonthlyFixed2P1()
    {
        Repeat r = getRepeatMonthlyFixed2();
        LocalDate firstMatchDate = r.nextValidDateSlow(r.getStartLocalDate().plusDays(1));
        assertEquals(LocalDate.of(2015, 11, 19), firstMatchDate);
    }
    @Test
    public void canGetFirstDateMonthlyFixed2P10()
    {
        Repeat r = getRepeatMonthlyFixed2();
//        System.out.println("r.getStartLocalDate() " + r.getStartLocalDate());
        LocalDate firstMatchDate = r.nextValidDateSlow(r.getStartLocalDate().plusDays(10));
        assertEquals(LocalDate.of(2015, 11, 19), firstMatchDate);
    }
    @Test
    public void canGetFirstDateMonthlyFixed2P100()
    {
        Repeat r = getRepeatMonthlyFixed2();
        LocalDate firstMatchDate = r.nextValidDateSlow(r.getStartLocalDate().plusDays(100));
        assertEquals(LocalDate.of(2016, 2, 18), firstMatchDate);
    }
    
    // List of dates tests
    @Test
    public void canListWeeklyFixed2()
    {
        Repeat r = getRepeatWeeklyFixed2();
        List<LocalDate> madeDates = r.validDateStream()
            .limit(r.getEndAfterEvents())
            .collect(Collectors.toList());
        madeDates.stream().forEach(System.out::println);
        List<LocalDate> expectedDates = new ArrayList<LocalDate>(Arrays.asList(
                LocalDate.of(2015, 10, 5)
              , LocalDate.of(2015, 10, 7)
              , LocalDate.of(2015, 10, 9)
              , LocalDate.of(2015, 10, 19)
              , LocalDate.of(2015, 10, 21)
              , LocalDate.of(2015, 10, 23)
              , LocalDate.of(2015, 11, 2)
              , LocalDate.of(2015, 11, 4)
              , LocalDate.of(2015, 11, 6)
              , LocalDate.of(2015, 11, 16)
              , LocalDate.of(2015, 11, 18)
              , LocalDate.of(2015, 11, 20)
              , LocalDate.of(2015, 11, 30)
              , LocalDate.of(2015, 12, 2)
              , LocalDate.of(2015, 12, 4)
              , LocalDate.of(2015, 12, 14)
              , LocalDate.of(2015, 12, 16)
              , LocalDate.of(2015, 12, 18)
              , LocalDate.of(2015, 12, 28)
              , LocalDate.of(2015, 12, 30)
              , LocalDate.of(2016, 1, 1)
              , LocalDate.of(2016, 1, 11)
              , LocalDate.of(2016, 1, 13)
              , LocalDate.of(2016, 1, 15)
              , LocalDate.of(2016, 1, 25)
              , LocalDate.of(2016, 1, 27)
              , LocalDate.of(2016, 1, 29)
              , LocalDate.of(2016, 2, 8)
              , LocalDate.of(2016, 2, 10)
              , LocalDate.of(2016, 2, 12)
              , LocalDate.of(2016, 2, 22)
              , LocalDate.of(2016, 2, 24)
              , LocalDate.of(2016, 2, 26)
              , LocalDate.of(2016, 3, 7)
              , LocalDate.of(2016, 3, 9)
              , LocalDate.of(2016, 3, 11)
              , LocalDate.of(2016, 3, 21)
              , LocalDate.of(2016, 3, 23)
              , LocalDate.of(2016, 3, 25)
              , LocalDate.of(2016, 4, 4)
              , LocalDate.of(2016, 4, 6)
              , LocalDate.of(2016, 4, 8)
              , LocalDate.of(2016, 4, 18)
              , LocalDate.of(2016, 4, 20)
              , LocalDate.of(2016, 4, 22)
              , LocalDate.of(2016, 5, 2)
              , LocalDate.of(2016, 5, 4)
              , LocalDate.of(2016, 5, 6)
              , LocalDate.of(2016, 5, 16)
              , LocalDate.of(2016, 5, 18)
                ));
        assertEquals(madeDates, expectedDates);
    }
}
