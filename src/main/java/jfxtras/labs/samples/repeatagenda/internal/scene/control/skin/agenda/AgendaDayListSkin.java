package jfxtras.labs.samples.repeatagenda.internal.scene.control.skin.agenda;

import java.time.LocalDateTime;
import java.time.Period;

import javafx.print.PrinterJob;
import javafx.scene.control.SkinBase;
import jfxtras.labs.samples.repeatagenda.internal.scene.control.skin.agenda.AgendaSkin;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.AgendaMine;

public class AgendaDayListSkin extends SkinBase<AgendaMine>
implements AgendaSkin {

	protected AgendaDayListSkin(AgendaMine arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void refresh() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setupAppointments() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public LocalDateTime convertClickInSceneToDateTime(double x, double y) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void print(PrinterJob job) {
		// TODO Auto-generated method stub
		
	}

    @Override
    public Period shiftDuration() {
        // TODO Auto-generated method stub
        return null;
    }

}
