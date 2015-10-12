package jfxtras.labs.samples.repeatagenda.scene.control.agenda;

import jfxtras.labs.samples.repeatagenda.MyRepeat;

public final class RepeatFactory {

    private RepeatFactory() { }
        
    public static MyRepeat newRepeat() {
        return new MyRepeat();
    }
    
    public static MyRepeat newRepeat(Repeat r) {
        return new MyRepeat(r);
    }
}
