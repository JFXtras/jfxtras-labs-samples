package jfxtras.samples.controls.agenda;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import javafx.util.Callback;
import jfxtras.labs.samples.JFXtrasLabsSampleBase;
import jfxtras.scene.control.CalendarTextField;
import jfxtras.scene.control.agenda.Agenda;
import jfxtras.scene.layout.GridPane;

public class AgendaSample1 extends JFXtrasLabsSampleBase
{
    public AgendaSample1() {
        agenda = new Agenda();

        // setup appointment groups
        final Map<String, Agenda.AppointmentGroup> lAppointmentGroupMap = new TreeMap<String, Agenda.AppointmentGroup>();
        for (int i = 0; i < 24; i++) {
        	lAppointmentGroupMap.put("group" + (i < 10 ? "0" : "") + i, new Agenda.AppointmentGroupImpl().withStyleClass("group" + i));
        }
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
        Calendar lToday = agenda.getDisplayedCalendar();
        int lTodayYear = lToday.get(Calendar.YEAR);
        int lTodayMonth = lToday.get(Calendar.MONTH);
        int lTodayDay = lToday.get(Calendar.DATE);
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
        agenda.appointments().addAll(
          new Agenda.AppointmentImpl()
            .withStartTime(new GregorianCalendar(lTodayYear, lTodayMonth, lTodayDay, 8, 00))
            .withEndTime(new GregorianCalendar(lTodayYear, lTodayMonth, lTodayDay, 11, 30))
            .withSummary("A")
            .withDescription("A much longer test description")
            .withAppointmentGroup(lAppointmentGroupMap.get("group07"))
        , new Agenda.AppointmentImpl()
            .withStartTime(new GregorianCalendar(lTodayYear, lTodayMonth, lTodayDay, 8, 30))
            .withEndTime(new GregorianCalendar(lTodayYear, lTodayMonth, lTodayDay, 10, 00))
            .withSummary("B")
            .withDescription("A description 2")
            .withAppointmentGroup(lAppointmentGroupMap.get("group08"))
        , new Agenda.AppointmentImpl()
            .withStartTime(new GregorianCalendar(lTodayYear, lTodayMonth, lTodayDay, 8, 30))
            .withEndTime(new GregorianCalendar(lTodayYear, lTodayMonth, lTodayDay, 9, 30))
            .withSummary("C")
            .withDescription("A description 3")
            .withAppointmentGroup(lAppointmentGroupMap.get("group09"))
        , new Agenda.AppointmentImpl()
            .withStartTime(new GregorianCalendar(lTodayYear, lTodayMonth, lTodayDay, 9, 00))
            .withEndTime(new GregorianCalendar(lTodayYear, lTodayMonth, lTodayDay, 13, 30))
            .withSummary("D")
            .withDescription("A description 4")
            .withAppointmentGroup(lAppointmentGroupMap.get("group07"))
        , new Agenda.AppointmentImpl()
            .withStartTime(new GregorianCalendar(lTodayYear, lTodayMonth, lTodayDay, 10, 30))
            .withEndTime(new GregorianCalendar(lTodayYear, lTodayMonth, lTodayDay, 11, 00))
            .withSummary("E")
            .withDescription("A description 4")
            .withAppointmentGroup(lAppointmentGroupMap.get("group07"))
        , new Agenda.AppointmentImpl()
            .withStartTime(new GregorianCalendar(lTodayYear, lTodayMonth, lTodayDay, 12, 30))
            .withEndTime(new GregorianCalendar(lTodayYear, lTodayMonth, lTodayDay, 13, 30))
            .withSummary("F")
            .withDescription("A description 4")
            .withAppointmentGroup(lAppointmentGroupMap.get("group07"))
        , new Agenda.AppointmentImpl()
            .withStartTime(new GregorianCalendar(lTodayYear, lTodayMonth, lTodayDay, 13, 00))
            .withEndTime(new GregorianCalendar(lTodayYear, lTodayMonth, lTodayDay, 13, 30))
            .withSummary("H")
            .withDescription("A description 4")
            .withAppointmentGroup(lAppointmentGroupMap.get("group07"))
        , new Agenda.AppointmentImpl()
            .withStartTime(new GregorianCalendar(lTodayYear, lTodayMonth, lTodayDay, 14, 00))
            .withEndTime(new GregorianCalendar(lTodayYear, lTodayMonth, lTodayDay, 14, 45))
            .withSummary("G")
            .withDescription("A description 4")
            .withAppointmentGroup(lAppointmentGroupMap.get("group07"))
        , new Agenda.AppointmentImpl()
            .withStartTime(new GregorianCalendar(lTodayYear, lTodayMonth, lTodayDay, 8, 10))
            .withEndTime(null)
            .withSummary("K asfsfd dsfsdfs fsfds sdgsds dsdfsd ")
            .withDescription("A description 4")
            .withAppointmentGroup(lAppointmentGroupMap.get("group07"))
        , new Agenda.AppointmentImpl()
            .withStartTime(new GregorianCalendar(lTodayYear, lTodayMonth, lTodayDay, 19, 00))
            .withEndTime(null)
            .withSummary("L asfsfd dsfsdfs fsfds sdgsds dsdfsd ")
            .withDescription("A description 4")
            .withAppointmentGroup(lAppointmentGroupMap.get("group07"))
        , new Agenda.AppointmentImpl()
            .withStartTime(new GregorianCalendar(lTodayYear, lTodayMonth, lTodayDay, 15, 00))
            .withEndTime(new GregorianCalendar(lTodayYear, lTodayMonth, lTodayDay, 16, 00))
            .withSummary("I")
            .withDescription("A description 4")
            .withAppointmentGroup(lAppointmentGroupMap.get("group07"))
        , new Agenda.AppointmentImpl()
            .withStartTime(new GregorianCalendar(lTodayYear, lTodayMonth, lTodayDay, 15, 30))
            .withEndTime(new GregorianCalendar(lTodayYear, lTodayMonth, lTodayDay, 16, 00))
            .withSummary("J")
            .withDescription("A description 4")
            .withAppointmentGroup(lAppointmentGroupMap.get("group07"))
            // -----
        , new Agenda.AppointmentImpl()
            .withStartTime(new GregorianCalendar(lTodayYear, lTodayMonth, lTodayDay, 20, 30))
            .withEndTime(new GregorianCalendar(lTodayYear, lTodayMonth, lTodayDay, 20, 31))
            .withSummary("S")
            .withDescription("Too short")
            .withAppointmentGroup(lAppointmentGroupMap.get("group07"))
            // -----
        , new Agenda.AppointmentImpl()
            .withStartTime(new GregorianCalendar(lTodayYear, lTodayMonth, lTodayDay))
            .withSummary("all day1")
            .withDescription("A description")
            .withAppointmentGroup(lAppointmentGroupMap.get("group07"))
            .withWholeDay(true)
        , new Agenda.AppointmentImpl()
            .withStartTime(new GregorianCalendar(lTodayYear, lTodayMonth, lTodayDay))
            .withSummary("all day2")
            .withDescription("A description")
            .withAppointmentGroup(lAppointmentGroupMap.get("group08"))
            .withWholeDay(true)
        , new Agenda.AppointmentImpl()
            .withStartTime(new GregorianCalendar(lTodayYear, lTodayMonth, lTodayDay))
            .withSummary("all day3")
            .withDescription("A description3")
            .withAppointmentGroup(lAppointmentGroupMap.get("group09"))
            .withWholeDay(true)
        , new Agenda.AppointmentImpl()
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
        return "Agenda is a Google calendar alike control. \n"
             + "It requires a list of appointments to be shown. An appointment is just an interface, so you can provide your own implementation (usually a domain entity), but there also is a default implementation avaiable for easy experimenting. \n"
             + "Similar to calendars in Google calendar, Agenda has the concept of AppointmentGroups. A group is used to style all the appointments in the same group identically, for example by setting the color in CSS. "
        	 ;
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
            lGridPane.add(new Label("Week of"), new GridPane.C().row(lRowIdx).col(0).halignment(HPos.RIGHT));
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
		return "http://jfxtras.org/doc/8.0/jfxtras-agenda/" + Agenda.class.getName().replace(".", "/") + ".html";
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