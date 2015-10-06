package jfxtras.labs.samples.repeatagenda.scene.control.agenda;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;

import jfxtras.labs.samples.repeatagenda.scene.control.agenda.Repeat.IntervalUnit;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.RepeatableUtilities.RepeatChange;

public final class Settings {
    
    private Settings() {}

    public final static Path APPOINTMENTS_FILE = Paths.get("src/jfxtras.labs.samples.repeatagenda.appointments.xml");
    public final static Path APPOINTMENT_GROUPS_FILE = Paths.get("data/appointments/appointmentGroups.xml");
    public final static Path APPOINTMENT_REPEATS_FILE = Paths.get("data/appointments/appointmentRepeats.xml");
    
    public static DateTimeFormatter DATE_FORMAT1; // format for output files
    public static DateTimeFormatter DATE_FORMAT2; // fancy format for displaying
    public static DateTimeFormatter DATE_FORMAT_AGENDA;
    public static DateTimeFormatter DATE_FORMAT_AGENDA_START;
    public static DateTimeFormatter DATE_FORMAT_AGENDA_END;
    public static DateTimeFormatter TIME_FORMAT_AGENDA;
    public static final boolean PRETTY_XML = true;  // true for readable indented XML output, false for small files

    public static final Map<IntervalUnit, String> REPEAT_INTERVALS = new HashMap<IntervalUnit, String>();
    public static final Map<IntervalUnit, String> REPEAT_INTERVALS_PLURAL = new HashMap<IntervalUnit, String>();
    public static final Map<IntervalUnit, String> REPEAT_INTERVALS_SINGULAR = new HashMap<IntervalUnit, String>();
    public static final Map<RepeatChange, String> REPEAT_CHANGE_CHOICES = new LinkedHashMap<RepeatChange, String>();

    public static ResourceBundle resources;
   
    public static void setup(ResourceBundle resourcesIn)
    {
        resources = resourcesIn;
        
        DATE_FORMAT1 = DateTimeFormatter.ofPattern(resourcesIn.getString("date.format1"));
        DATE_FORMAT2 = DateTimeFormatter.ofPattern(resourcesIn.getString("date.format2"));
        DATE_FORMAT_AGENDA = DateTimeFormatter.ofPattern(resourcesIn.getString("date.format.agenda"));
        DATE_FORMAT_AGENDA_START = DateTimeFormatter.ofPattern(resourcesIn.getString("date.format.agenda.start"));
        DATE_FORMAT_AGENDA_END = DateTimeFormatter.ofPattern(resourcesIn.getString("date.format.agenda.end"));
        TIME_FORMAT_AGENDA = DateTimeFormatter.ofPattern(resourcesIn.getString("time.format.agenda"));
        
        REPEAT_INTERVALS.put(IntervalUnit.DAILY, resourcesIn.getString("daily"));
        REPEAT_INTERVALS.put(IntervalUnit.WEEKLY, resourcesIn.getString("weekly"));
        REPEAT_INTERVALS.put(IntervalUnit.MONTHLY, resourcesIn.getString("monthly"));
        REPEAT_INTERVALS.put(IntervalUnit.YEARLY, resourcesIn.getString("yearly"));
        
        REPEAT_INTERVALS_PLURAL.put(IntervalUnit.DAILY, resourcesIn.getString("days"));
        REPEAT_INTERVALS_PLURAL.put(IntervalUnit.WEEKLY, resourcesIn.getString("weeks"));
        REPEAT_INTERVALS_PLURAL.put(IntervalUnit.MONTHLY, resourcesIn.getString("months"));
        REPEAT_INTERVALS_PLURAL.put(IntervalUnit.YEARLY, resourcesIn.getString("years"));
        
        REPEAT_INTERVALS_SINGULAR.put(IntervalUnit.DAILY, resourcesIn.getString("day"));
        REPEAT_INTERVALS_SINGULAR.put(IntervalUnit.WEEKLY, resourcesIn.getString("week"));
        REPEAT_INTERVALS_SINGULAR.put(IntervalUnit.MONTHLY, resourcesIn.getString("month"));
        REPEAT_INTERVALS_SINGULAR.put(IntervalUnit.YEARLY, resourcesIn.getString("year"));

        REPEAT_CHANGE_CHOICES.put(RepeatableUtilities.RepeatChange.ONE, resources.getString("dialog.repeat.change.one"));
        REPEAT_CHANGE_CHOICES.put(RepeatableUtilities.RepeatChange.ALL, resources.getString("dialog.repeat.change.all"));
        REPEAT_CHANGE_CHOICES.put(RepeatableUtilities.RepeatChange.FUTURE, resources.getString("dialog.repeat.change.future"));
        
    }

}
