package jfxtras.labs.samples.repeatagenda.scene.control.agenda;

import jfxtras.labs.samples.repeatagenda.RepeatImpl;

public final class RepeatFactory {

    private RepeatFactory() { }
        
    public static RepeatImpl newRepeat() {
        return new RepeatImpl();
    }
    
    public static RepeatImpl newRepeat(Repeat r) {
        return new RepeatImpl(r);
    }
}
