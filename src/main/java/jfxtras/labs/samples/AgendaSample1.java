package jfxtras.labs.samples;

import fxsampler.SampleBase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;
import jfxtras.labs.scene.control.Agenda;
import jfxtras.labs.scene.control.CalendarTextField;
import jfxtras.labs.scene.layout.GridPane;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class AgendaSample1 extends JFXtrasSampleBase
{
    public AgendaSample1() {
        agenda = new Agenda();

        // setup appointment groups
        final Map<String, Agenda.AppointmentGroup> lAppointmentGroupMap = new TreeMap<String, Agenda.AppointmentGroup>();
        lAppointmentGroupMap.put("group00", new Agenda.AppointmentGroupImpl().withStyleClass("group0"));
        lAppointmentGroupMap.put("group01", new Agenda.AppointmentGroupImpl().withStyleClass("group1"));
        lAppointmentGroupMap.put("group02", new Agenda.AppointmentGroupImpl().withStyleClass("group2"));
        lAppointmentGroupMap.put("group03", new Agenda.AppointmentGroupImpl().withStyleClass("group3"));
        lAppointmentGroupMap.put("group04", new Agenda.AppointmentGroupImpl().withStyleClass("group4"));
        lAppointmentGroupMap.put("group05", new Agenda.AppointmentGroupImpl().withStyleClass("group5"));
        lAppointmentGroupMap.put("group06", new Agenda.AppointmentGroupImpl().withStyleClass("group6"));
        lAppointmentGroupMap.put("group07", new Agenda.AppointmentGroupImpl().withStyleClass("group7"));
        lAppointmentGroupMap.put("group08", new Agenda.AppointmentGroupImpl().withStyleClass("group8"));
        lAppointmentGroupMap.put("group09", new Agenda.AppointmentGroupImpl().withStyleClass("group9"));
        lAppointmentGroupMap.put("group10", new Agenda.AppointmentGroupImpl().withStyleClass("group10"));
        lAppointmentGroupMap.put("group11", new Agenda.AppointmentGroupImpl().withStyleClass("group11"));
        lAppointmentGroupMap.put("group12", new Agenda.AppointmentGroupImpl().withStyleClass("group12"));
        lAppointmentGroupMap.put("group13", new Agenda.AppointmentGroupImpl().withStyleClass("group13"));
        lAppointmentGroupMap.put("group14", new Agenda.AppointmentGroupImpl().withStyleClass("group14"));
        lAppointmentGroupMap.put("group15", new Agenda.AppointmentGroupImpl().withStyleClass("group15"));
        lAppointmentGroupMap.put("group16", new Agenda.AppointmentGroupImpl().withStyleClass("group16"));
        lAppointmentGroupMap.put("group17", new Agenda.AppointmentGroupImpl().withStyleClass("group17"));
        lAppointmentGroupMap.put("group18", new Agenda.AppointmentGroupImpl().withStyleClass("group18"));
        lAppointmentGroupMap.put("group19", new Agenda.AppointmentGroupImpl().withStyleClass("group19"));
        lAppointmentGroupMap.put("group20", new Agenda.AppointmentGroupImpl().withStyleClass("group20"));
        lAppointmentGroupMap.put("group21", new Agenda.AppointmentGroupImpl().withStyleClass("group21"));
        lAppointmentGroupMap.put("group22", new Agenda.AppointmentGroupImpl().withStyleClass("group22"));
        lAppointmentGroupMap.put("group23", new Agenda.AppointmentGroupImpl().withStyleClass("group23"));
        for (String lId : lAppointmentGroupMap.keySet())
        {
            Agenda.AppointmentGroup lAppointmentGroup = lAppointmentGroupMap.get(lId);
            lAppointmentGroup.setDescription(lId);
            agenda.appointmentGroups().add(lAppointmentGroup);
        }

        // accept new appointments
        agenda.createAppointmentCallbackProperty().set(new Callback<Agenda.CalendarRange, Agenda.Appointment>()
        {
            @Override
            public Agenda.Appointment call(Agenda.CalendarRange calendarRange)
            {
                return new Agenda.AppointmentImpl()
                        .withStartTime(calendarRange.getStartCalendar())
                        .withEndTime(calendarRange.getEndCalendar())
                        .withSummary("new")
                        .withDescription("new")
                        .withAppointmentGroup(lAppointmentGroupMap.get("group01"));
            }
        });

        // initial set
        Calendar lFirstDayOfWeekCalendar = getFirstDayOfWeekCalendar(agenda.getLocale(), agenda.getDisplayedCalendar());
        int lFirstDayOfWeekYear = lFirstDayOfWeekCalendar.get(Calendar.YEAR);
        int lFirstDayOfWeekMonth = lFirstDayOfWeekCalendar.get(Calendar.MONTH);
        int FirstDayOfWeek = lFirstDayOfWeekCalendar.get(Calendar.DATE);
        Calendar lToday = agenda.getDisplayedCalendar();
        int lTodayYear = lToday.get(Calendar.YEAR);
        int lTodayMonth = lToday.get(Calendar.MONTH);
        int lTodayDay = lToday.get(Calendar.DATE);
        agenda.appointments().addAll(
		/*
		 *  . . . .
		 *  . . . . 
		 *  A . . .  8:00
		 *  A B C .  8:30
		 *  A B C D  9:00
		 *  A B . D  9:30
		 *  A . . D 10:00
		 *  A E . D 10:30
		 *  A . . D 11:00
		 *  . . . D 11:30
		 *  . . . D 12:00
		 *  F . . D 12:30
		 *  F H . D 13:00
		 *  . . . . 13:30
		 *  G . . . 14:00
		 *  . . . . 14:30
		 * 
		 */
        new Agenda.AppointmentImpl()
                .withStartTime(new GregorianCalendar(lTodayYear, lTodayMonth, lTodayDay, 8, 00))
                .withEndTime(new GregorianCalendar(lTodayYear, lTodayMonth, lTodayDay, 11, 30))
                .withSummary("A")
                .withDescription("A much longer test description")
                .withAppointmentGroup(lAppointmentGroupMap.get("group07"))
        , 	new Agenda.AppointmentImpl()
            .withStartTime(new GregorianCalendar(lTodayYear, lTodayMonth, lTodayDay, 8, 30))
            .withEndTime(new GregorianCalendar(lTodayYear, lTodayMonth, lTodayDay, 10, 00))
            .withSummary("B")
            .withDescription("A description 2")
            .withAppointmentGroup(lAppointmentGroupMap.get("group08"))
        , 	new Agenda.AppointmentImpl()
            .withStartTime(new GregorianCalendar(lTodayYear, lTodayMonth, lTodayDay, 8, 30))
            .withEndTime(new GregorianCalendar(lTodayYear, lTodayMonth, lTodayDay, 9, 30))
            .withSummary("C")
            .withDescription("A description 3")
            .withAppointmentGroup(lAppointmentGroupMap.get("group09"))
        , 	new Agenda.AppointmentImpl()
            .withStartTime(new GregorianCalendar(lTodayYear, lTodayMonth, lTodayDay, 9, 00))
            .withEndTime(new GregorianCalendar(lTodayYear, lTodayMonth, lTodayDay, 13, 30))
            .withSummary("D")
            .withDescription("A description 4")
            .withAppointmentGroup(lAppointmentGroupMap.get("group07"))
        , 	new Agenda.AppointmentImpl()
            .withStartTime(new GregorianCalendar(lTodayYear, lTodayMonth, lTodayDay, 10, 30))
            .withEndTime(new GregorianCalendar(lTodayYear, lTodayMonth, lTodayDay, 11, 00))
            .withSummary("E")
            .withDescription("A description 4")
            .withAppointmentGroup(lAppointmentGroupMap.get("group07"))
        , 	new Agenda.AppointmentImpl()
            .withStartTime(new GregorianCalendar(lTodayYear, lTodayMonth, lTodayDay, 12, 30))
            .withEndTime(new GregorianCalendar(lTodayYear, lTodayMonth, lTodayDay, 13, 30))
            .withSummary("F")
            .withDescription("A description 4")
            .withAppointmentGroup(lAppointmentGroupMap.get("group07"))
        , 	new Agenda.AppointmentImpl()
            .withStartTime(new GregorianCalendar(lTodayYear, lTodayMonth, lTodayDay, 13, 00))
            .withEndTime(new GregorianCalendar(lTodayYear, lTodayMonth, lTodayDay, 13, 30))
            .withSummary("H")
            .withDescription("A description 4")
            .withAppointmentGroup(lAppointmentGroupMap.get("group07"))
        , 	new Agenda.AppointmentImpl()
            .withStartTime(new GregorianCalendar(lTodayYear, lTodayMonth, lTodayDay, 14, 00))
            .withEndTime(new GregorianCalendar(lTodayYear, lTodayMonth, lTodayDay, 14, 45))
            .withSummary("G")
            .withDescription("A description 4")
            .withAppointmentGroup(lAppointmentGroupMap.get("group07"))
        , 	new Agenda.AppointmentImpl()
            .withStartTime(new GregorianCalendar(lTodayYear, lTodayMonth, lTodayDay, 8, 10))
            .withEndTime(null)
            .withSummary("K asfsfd dsfsdfs fsfds sdgsds dsdfsd ")
            .withDescription("A description 4")
            .withAppointmentGroup(lAppointmentGroupMap.get("group07"))
        , 	new Agenda.AppointmentImpl()
            .withStartTime(new GregorianCalendar(lTodayYear, lTodayMonth, lTodayDay, 19, 00))
            .withEndTime(null)
            .withSummary("L asfsfd dsfsdfs fsfds sdgsds dsdfsd ")
            .withDescription("A description 4")
            .withAppointmentGroup(lAppointmentGroupMap.get("group07"))
        , 	new Agenda.AppointmentImpl()
            .withStartTime(new GregorianCalendar(lTodayYear, lTodayMonth, lTodayDay, 15, 00))
            .withEndTime(new GregorianCalendar(lTodayYear, lTodayMonth, lTodayDay, 16, 00))
            .withSummary("I")
            .withDescription("A description 4")
            .withAppointmentGroup(lAppointmentGroupMap.get("group07"))
        , 	new Agenda.AppointmentImpl()
            .withStartTime(new GregorianCalendar(lTodayYear, lTodayMonth, lTodayDay, 15, 30))
            .withEndTime(new GregorianCalendar(lTodayYear, lTodayMonth, lTodayDay, 16, 00))
            .withSummary("J")
            .withDescription("A description 4")
            .withAppointmentGroup(lAppointmentGroupMap.get("group07"))
            // -----
        , 	new Agenda.AppointmentImpl()
            .withStartTime(new GregorianCalendar(lTodayYear, lTodayMonth, lTodayDay, 20, 30))
            .withEndTime(new GregorianCalendar(lTodayYear, lTodayMonth, lTodayDay, 20, 31))
            .withSummary("S")
            .withDescription("Too short")
            .withAppointmentGroup(lAppointmentGroupMap.get("group07"))
            // -----
        , 	new Agenda.AppointmentImpl()
            .withStartTime(new GregorianCalendar(lTodayYear, lTodayMonth, lTodayDay))
            .withSummary("all day1")
            .withDescription("A description")
            .withAppointmentGroup(lAppointmentGroupMap.get("group07"))
            .withWholeDay(true)
        , 	new Agenda.AppointmentImpl()
            .withStartTime(new GregorianCalendar(lTodayYear, lTodayMonth, lTodayDay))
            .withSummary("all day2")
            .withDescription("A description")
            .withAppointmentGroup(lAppointmentGroupMap.get("group08"))
            .withWholeDay(true)
        , 	new Agenda.AppointmentImpl()
            .withStartTime(new GregorianCalendar(lTodayYear, lTodayMonth, lTodayDay))
            .withSummary("all day3")
            .withDescription("A description3")
            .withAppointmentGroup(lAppointmentGroupMap.get("group09"))
            .withWholeDay(true)
        , 	new Agenda.AppointmentImpl()
            .withStartTime(new GregorianCalendar(lTodayYear, lTodayMonth, lTodayDay + 1))
            .withSummary("all day")
            .withDescription("A description3")
            .withAppointmentGroup(lAppointmentGroupMap.get("group03"))
            .withWholeDay(true)
        );
    }
    final Agenda agenda;

    @Override
    public String getSampleName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public String getSampleDescription() {
        return "Basic Agenda usage";
    }

    @Override
    public Node getPanel(Stage stage) {
        return agenda;
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

        // week
        {
            lGridPane.add(new Label("Week of"), new GridPane.C().row(lRowIdx).col(0));
            CalendarTextField lCalendarTextField = new CalendarTextField();
            lGridPane.add(lCalendarTextField, new GridPane.C().row(lRowIdx).col(1));
            lCalendarTextField.calendarProperty().bindBidirectional(agenda.displayedCalendar());
        }
        lRowIdx++;

        // done
        return lGridPane;
    }

    @Override
    public String getJavaDocURL() {
// TODO: get the javadoc from the javadoc artifact
//		String s = "/" + Agenda.class.getName().replace(".", File.separator) + ".html";
//		System.out.println("!!! " + s);
//		s = Agenda.class.getResource(s).toExternalForm();
//		System.out.println("!!! " + s);
//		return s;
		return null;
    }

    public static void main(String[] args) {
        launch(args);
    }


    /**
     * get the calendar for the first day of the week
     */
    static private Calendar getFirstDayOfWeekCalendar(Locale locale, Calendar c)
    {
        // result
        int lFirstDayOfWeek = Calendar.getInstance(locale).getFirstDayOfWeek();
        int lCurrentDayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        int lDelta = 0;
        if (lFirstDayOfWeek <= lCurrentDayOfWeek)
        {
            lDelta = -lCurrentDayOfWeek + lFirstDayOfWeek;
        }
        else
        {
            lDelta = -lCurrentDayOfWeek - (7-lFirstDayOfWeek);
        }
        c = ((Calendar)c.clone());
        c.add(Calendar.DATE, lDelta);
        return c;
    }

}