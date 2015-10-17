package jfxtras.labs.samples.repeatagenda.scene.control.agenda;

public interface RepeatableAppointment extends Agenda.Appointment
{

    boolean isRepeatMade();
    void setRepeatMade(boolean b);

    void setRepeat(Repeat repeat);
    Repeat getRepeat();
////    boolean hasRepeat();
    boolean repeatFieldsEquals(Object obj);
    
    /**
     * Copies all fields into parameter appointment
     * 
     * @param appointment
     * @return
     */
    default RepeatableAppointment copyInto(RepeatableAppointment appointment) {
        appointment.setEndLocalDateTime(getEndLocalDateTime());
        appointment.setStartLocalDateTime(getStartLocalDateTime());
        copyNonDateFieldsInto(appointment);
//        Iterator<DayOfWeek> dayOfWeekIterator = Arrays 
//                .stream(DayOfWeek.values())
//                .limit(7)
//                .iterator();
//            while (dayOfWeekIterator.hasNext())
//            {
//                DayOfWeek key = dayOfWeekIterator.next();
//                boolean b1 = this.getRepeat().getDayOfWeekMap().get(key).get();
//                boolean b2 = appointment.getRepeat().getDayOfWeekMap().get(key).get();
//                System.out.println("copied day of week2 " + key + " " + b1 + " " + b2);
//            }
        return appointment;
    }
    
    /**
     * Copies this Appointment non-time fields into parameter appointment
     * 
     * @param appointment
     * @return
     */
    default RepeatableAppointment copyNonDateFieldsInto(RepeatableAppointment appointment) {
        appointment.setAppointmentGroup(getAppointmentGroup());
        appointment.setDescription(getDescription());
        appointment.setSummary(getSummary());
//        boolean b1 = getRepeat() == null;
//        boolean b2 = appointment.getRepeat() == null;
//        System.out.println("repeats " + b1 + " " + b2);
//        if (getRepeat() == null) return appointment;
//        if (appointment.getRepeat() == null)
//        {
//            appointment.setRepeat(RepeatFactory.newRepeat(getRepeat()));
//        } else
//        {
//            getRepeat().copyInto(appointment.getRepeat());
//        }
        return appointment;
    }
    
    /**
     * Copies this Appointment non-time fields into parameter appointment
     * Used when some of fields are unique and should not be copied.
     * 
     * @param appointment
     * @return
     */
    default RepeatableAppointment copyNonDateFieldsInto(RepeatableAppointment appointment, RepeatableAppointment appointmentOld) {
        if (appointment.getAppointmentGroup().equals(appointmentOld.getAppointmentGroup())) {
            appointment.setAppointmentGroup(getAppointmentGroup());
        }
        if (appointment.getDescription().equals(appointmentOld.getDescription())) {
            appointment.setDescription(getDescription());
        }
        if (appointment.getSummary().equals(appointmentOld.getSummary())) {
            appointment.setSummary(getSummary());
        }
        getRepeat().copyInto(appointment.getRepeat());
//        repeatMap.get(this).copyInto(repeatMap.get(appointment));
        return appointment;
    }
}
