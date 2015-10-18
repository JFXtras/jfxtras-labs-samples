package jfxtras.labs.samples.repeatagenda.internal.scene.control.skin.agenda.base24hour;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Popup;
import jfxtras.labs.samples.repeatagenda.Main;
import jfxtras.labs.samples.repeatagenda.internal.scene.control.skin.agenda.base24hour.controller.AppointmentPopupController;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.AgendaMine.Appointment;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.Settings;
import jfxtras.util.NodeUtil;

public class AppointmentPopup extends Popup {

    private AnchorPane appointmentManage;
    
    public AppointmentPopup(Pane pane
            , Appointment appointment
            , LayoutHelp layoutHelp) {
        
        // LOAD FXML
        FXMLLoader appointmentManageLoader = new FXMLLoader();
        appointmentManageLoader.setLocation(Main.class.getResource("internal/scene/control/skin/agenda/base24hour/view/AppointmentPopup.fxml"));
        appointmentManageLoader.setResources(Settings.resources);
        try {
            appointmentManage = appointmentManageLoader.load();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
                
        appointmentManage.getStylesheets().add(layoutHelp.skinnable.getUserAgentStylesheet());
        
        AppointmentPopupController appointmentManageController = appointmentManageLoader.getController();
        appointmentManageController.setupData(
                  layoutHelp.skinnable.appointments()
                , appointment
                , layoutHelp.skinnable.repeats()
                , pane
                , this);

        
        setAutoFix(true);
        setAutoHide(true);
        setHideOnEscape(true);
        getContent().add(appointmentManage);
        
        setX(NodeUtil.screenX(pane));
        setY(NodeUtil.screenY(pane) - pane.getHeight());
        
        setOnHidden( (windowEvent) -> {
//            System.out.println("close popup");
        });
        
        
    }
}
