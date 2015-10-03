package jfxtras.labs.samples.repeatagenda.internal.scene.control.skin.agenda.base24hour;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import jfxtras.labs.samples.repeatagenda.internal.scene.control.skin.agenda.base24hour.LayoutHelp;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.Agenda.Appointment;
import jfxtras.labs.samples.repeatagenda.scene.control.agenda.Agenda.AppointmentGroup;

//makes a group of colored squares used to select appointment group
public class AppointmentGroupGridPane extends GridPane {

 private LayoutHelp layoutHelp;
 private Pane[] lPane;
 private IntegerProperty appointmentGroupSelected = new SimpleIntegerProperty(-1);
 
 public AppointmentGroupGridPane() {
 }

 public AppointmentGroupGridPane(Appointment appointment, LayoutHelp layoutHelp) {
     setupData(appointment, layoutHelp);
 }
 
 public void setupData(Appointment appointment, LayoutHelp layoutHelp) {

     // construct a area of appointment groups
     this.layoutHelp = layoutHelp;
     getStylesheets().add(layoutHelp.skinnable.getUserAgentStylesheet());
//     String css = this.getClass().getResource("../Agenda.css").toExternalForm(); 
//     this.getStylesheets().add(css);
     this.getStyleClass().add("AppointmentGroups");
     this.setHgap(2);
     this.setVgap(2);
     lPane = new Pane[layoutHelp.skinnable.appointmentGroups().size()];
     
     int lCnt = 0;
     for (AppointmentGroup lAppointmentGroup : layoutHelp.skinnable.appointmentGroups())
     {
         lPane[lCnt] = lAppointmentGroup.getIcon();
         // create the appointment group
//         lPane[lCnt] = new Pane();
//         lPane[lCnt].setPrefSize(20, 20);
//         lPane[lCnt].getStyleClass().addAll("AppointmentGroup", lAppointmentGroup.getStyleClass());
         this.add(lPane[lCnt], lCnt % 12, lCnt / 12 );

         // tooltip
         updateToolTip(lCnt);

         // mouse 
         layoutHelp.setupMouseOverAsBusy(lPane[lCnt]);
         lPane[lCnt].setOnMouseClicked( (mouseEvent) -> {
             mouseEvent.consume(); // consume before anything else, in case there is a problem in the handling
             appointmentGroupSelected.set(layoutHelp.skinnable.appointmentGroups().indexOf(lAppointmentGroup));

             // assign appointment group
             AppointmentGroup g = layoutHelp.skinnable.appointmentGroups().get(appointmentGroupSelected.getValue());
             appointment.setAppointmentGroup(g);
//             layoutHelp.skin.setupAppointments();    // updates all appointments - TODO better to update individual appointment - don't update because other changes may make display wrong
         });
         lCnt++;
     }
   //int index = layoutHelp.skinnable.appointmentGroups().indexOf(appointment.getAppointmentGroup());
     int index = appointment.getAppointmentGroupIndex();
     setAppointmentGroupSelected(index);
     setLPane(index);
     
     // change listener - runs when new color is selected
     appointmentGroupSelected.addListener((observable, oldSelection, newSelection) ->  {
       int oldS = (int) oldSelection;
       int newS = (int) newSelection;
       setLPane(newS);
       unsetLPane(oldS);
     });
 }

 // blue border in selection
 private void unsetLPane(int i) {
     lPane[i].setStyle("-fx-border-color: WHITE");
 }
 private void setLPane(int i) {
      final String cssDefault = "-fx-border-color: blue;\n"
             + "-fx-border-insets: -1;\n"
             + "-fx-border-width: 2;\n";
     lPane[i].setStyle(cssDefault);
 }
 
 public void updateToolTip(int i) {
     AppointmentGroup a = layoutHelp.skinnable.appointmentGroups().get(i);
     if (a.getDescription() != "" && a.getDescription() != null) {
         Tooltip.install(lPane[i], new Tooltip(a.getDescription()));
//         System.out.println("new tooltip " + a.getDescription());
//         setLPane(i);
     } 
 }

 public IntegerProperty appointmentGroupSelectedProperty() {
     return appointmentGroupSelected;
 }

 public void setAppointmentGroupSelected(Integer i) {
     appointmentGroupSelected.set(i);
 }
 
 public Integer getAppointmentGroupSelected() {
     return appointmentGroupSelected.getValue();
 }   
}
