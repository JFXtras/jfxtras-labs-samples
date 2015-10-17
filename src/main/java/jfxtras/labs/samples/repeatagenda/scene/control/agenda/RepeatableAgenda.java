package jfxtras.labs.samples.repeatagenda.scene.control.agenda;

import java.util.Collection;

public class RepeatableAgenda extends Agenda {

    /** Repeat rules */
    Collection<Repeat> repeats;
    public Collection<Repeat> repeats() { return repeats; }
    public void setRepeats(Collection<Repeat> repeats) { this.repeats = repeats; }
}
