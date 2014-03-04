/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jfxtras.labs.samples.matrixpanel;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Accordion;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import jfxtras.labs.samples.JFXtrasLabsSampleBase;
import jfxtras.labs.scene.control.gauge.Content;
import jfxtras.labs.scene.control.gauge.ContentBuilder;
import jfxtras.labs.scene.control.gauge.MatrixPanel;
import jfxtras.labs.scene.control.gauge.MatrixPanelBuilder;
import jfxtras.scene.layout.GridPane;

/**
 *
 * @author José Pereda Llamas
 * Created on 26-dic-2013 - 15:02:33
 */
public class MatrixPanelSample extends JFXtrasLabsSampleBase {

    private final Content content1= ContentBuilder.create()
                    .color(Content.MatrixColor.RGB)
                    .type(Content.Type.IMAGE)
                    .origin(0, 1)
                    .area(0, 0, 184, 74)
                    .bmpName("/jfxtras/labs/samples/matrixpanel/Javafx-logo.bmp")
                    .effect(Content.Effect.SCROLL_DOWN).lapse(20)
                    .postEffect(Content.PostEffect.PAUSE).pause(3000)
                    .build();
    private final Content content2=ContentBuilder.create()
                    .color(Content.MatrixColor.BLUE)
                    .type(Content.Type.TEXT)
                    .origin(0, 1)
                    .area(0, 75, 250, 100)
                    .txtContent("HELLO JAVAFX SAMPLES!!!   ")
                    .font(Content.MatrixFont.FF_10x16)
                    .fontGap(Content.Gap.DOUBLE)
                    .align(Content.Align.RIGHT)
                    .effect(Content.Effect.SCROLL_LEFT).lapse(20)
                    .postEffect(Content.PostEffect.PAUSE).pause(1000)
                    .order(Content.RotationOrder.FIRST).clear(false)
                    .build();
    private final MatrixPanel panel = MatrixPanelBuilder.create()
                .ledWidth(184).ledHeight(100).prefWidth(1050d).prefHeight(1050d)
                .frameVisible(true).frameDesign(MatrixPanel.FrameDesign.SHINY_METAL)
                .frameCustomPath("/jfxtras/labs/samples/matrixpanel/black.jpg")
                .frameBaseColor(Color.ALICEBLUE).contents(new Content[] {content1,content2}).build();
    
    public MatrixPanelSample(){
        panel.setEffect(new DropShadow());
    }
    /**
     *
     * @return
     */
    @Override
    public String getSampleName() {
        return this.getClass().getSimpleName();
    }
    
    /**
     *
     * @return
     */
    @Override
    public String getSampleDescription() {
        return "Basic MatrixPanel usage. \nSample contains two type of Contents: Image and Text. \nParameters related to Frame or Contents (Text and Image) can be changed dynamically";
    }

    @Override
    public Node getPanel(Stage stage) {
        VBox root = new VBox(20);
        root.setPadding(new Insets(30, 30, 30, 30));

        root.getChildren().addAll(panel);

        return root;
    }

    @Override
    public Node getControlPanel() {
        Accordion accordion=new Accordion();
        /****************************
        **** TITLEDPANE 1. FRAME ****
        *****************************/
        TitledPane tpFrame=new TitledPane();
        tpFrame.setText("Frame");
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
        
        TextField txtLedWidth=new TextField(new Integer(panel.getLedWidth()).toString());
        TextField txtLedHeight=new TextField(new Integer(panel.getLedHeight()).toString());
        CheckBox chkFrame=new CheckBox();
        chkFrame.setSelected(panel.isFrameVisible());
        ColorPicker cpCustomFrame=new ColorPicker(panel.getFrameBaseColor());
        cpCustomFrame.disableProperty().bind(panel.frameVisibleProperty().not().or(panel.frameDesignProperty().isEqualTo(MatrixPanel.FrameDesign.SHINY_METAL).not()));
        ChoiceBox chbFrameDesign=new ChoiceBox();
        chbFrameDesign.setItems(FXCollections.observableArrayList(MatrixPanel.FrameDesign.values()));
        chbFrameDesign.getSelectionModel().select(panel.getFrameDesign());
        chbFrameDesign.disableProperty().bind(panel.frameVisibleProperty().not());
        TextField txtPathFrame=new TextField(panel.getFrameCustomPath());
        txtPathFrame.disableProperty().bind(panel.frameVisibleProperty().not().or(panel.frameDesignProperty().isEqualTo(MatrixPanel.FrameDesign.CUSTOM_DESIGN).not()));
        
        // ledWidth
        {
            Label lLabel = new Label("Led Width: ");
            lLabel.setTooltip(new Tooltip("Insert number of leds per row"));
            lGridPane.add(lLabel, new GridPane.C().row(lRowIdx).col(0).halignment(HPos.RIGHT));
            txtLedWidth.setPrefWidth(100);
            txtLedWidth.focusedProperty().addListener((ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) -> {
                if(!t1.booleanValue() && t.booleanValue()){
                    try{
                        int led=Integer.parseInt(txtLedWidth.getText());
                        panel.setLedWidth(led);
                    } catch(NumberFormatException nfe){
                        txtLedWidth.setText(new Integer(panel.getLedWidth()).toString());
                    }
                }
            });
            lGridPane.add(txtLedWidth, new GridPane.C().row(lRowIdx).col(1));
        }
        lRowIdx++;

        // ledHeight
        {
            Label lLabel = new Label("Led Height: ");
            lLabel.setTooltip(new Tooltip("Insert number of leds per column"));
            lGridPane.add(lLabel, new GridPane.C().row(lRowIdx).col(0).halignment(HPos.RIGHT));
            txtLedHeight.setPrefWidth(100);
            txtLedHeight.focusedProperty().addListener((ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) -> {
                if(!t1.booleanValue() && t.booleanValue()){
                    try{
                        int led=Integer.parseInt(txtLedHeight.getText());
                        panel.setLedHeight(led);
                    } catch(NumberFormatException nfe){
                        txtLedHeight.setText(new Integer(panel.getLedHeight()).toString());
                    }
                }
            });
            lGridPane.add(txtLedHeight, new GridPane.C().row(lRowIdx).col(1));
        }
        lRowIdx++;
        
        // frameVisible
        {
            Label lLabel = new Label("Frame Visible: ");
            lLabel.setTooltip(new Tooltip("Select if the frame is visible or not"));
            lGridPane.add(lLabel, new GridPane.C().row(lRowIdx).col(0).halignment(HPos.RIGHT));
            chkFrame.selectedProperty().addListener((ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) -> {
                panel.setFrameVisible(t1.booleanValue());
            });
            lGridPane.add(chkFrame, new GridPane.C().row(lRowIdx).col(1));
        }
        lRowIdx++;

        // frameDesign
        {
            Label lLabel = new Label("Frame Design: ");
            lLabel.setTooltip(new Tooltip("Select the frame design"));
            lGridPane.add(lLabel, new GridPane.C().row(lRowIdx).col(0).halignment(HPos.RIGHT));
            chbFrameDesign.getSelectionModel().selectedItemProperty().addListener((ObservableValue ov, Object t, Object t1) -> {
                panel.setFrameDesign((MatrixPanel.FrameDesign)t1);
            });
            lGridPane.add(chbFrameDesign, new GridPane.C().row(lRowIdx).col(1));
        }
        lRowIdx++;

        // Color for shinyFrame
        {
            Label lLabel = new Label("Shiny Frame: ");
            lLabel.setTooltip(new Tooltip("Pick a color or define a custom one for the Shiny frame design"));
            lGridPane.add(lLabel, new GridPane.C().row(lRowIdx).col(0).halignment(HPos.RIGHT));
            cpCustomFrame.valueProperty().addListener((ObservableValue<? extends Color> ov, Color t, Color t1) -> {
                panel.setFrameBaseColor(t1);
            });
            lGridPane.add(cpCustomFrame, new GridPane.C().row(lRowIdx).col(1));
        }
        lRowIdx++;
        
        // frameCustom
        {
            Label lLabel = new Label("Custom Frame: ");
            lLabel.setTooltip(new Tooltip("Provide the path of a valid image from url or local jar"));
            lGridPane.add(lLabel, new GridPane.C().row(lRowIdx).col(0).halignment(HPos.RIGHT));
            txtPathFrame.focusedProperty().addListener((ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) -> {
                if(!t1.booleanValue() && t.booleanValue()){
                    panel.setFrameCustomPath(txtPathFrame.getText());
                }
            });
            lGridPane.add(txtPathFrame, new GridPane.C().row(lRowIdx).col(1));
        }
        lRowIdx++;
        
        tpFrame.setContent(lGridPane);
        
        /***************************************
        **** TITLEDPANE 2. CONTENT 1. IMAGE ****
        ****************************************/
        TitledPane tpContent1=new TitledPane();
        tpContent1.setText("Content 1. Image");
        // the result
        GridPane rGridPane = new GridPane();
        rGridPane.setVgap(2.0);
        rGridPane.setHgap(2.0);

        // setup the grid so all the labels will not grow, but the rest will
        rGridPane.getColumnConstraints().addAll(lColumnConstraintsNeverGrow, lColumnConstraintsAlwaysGrow);
        int rRowIdx = 0;
        
        ChoiceBox chbImgType=new ChoiceBox();
        chbImgType.setItems(FXCollections.observableArrayList(Content.Type.values()));
        chbImgType.getSelectionModel().select(content1.getType());
        chbImgType.setDisable(true);
        ChoiceBox chbImgColor=new ChoiceBox();
        chbImgColor.setItems(FXCollections.observableArrayList(Content.MatrixColor.values()));
        chbImgColor.getSelectionModel().select(content1.getColor());
        TextField txtImgOriginX=new TextField(new Double(content1.getOrigin().getX()).toString());
        TextField txtImgOriginY=new TextField(new Double(content1.getOrigin().getY()).toString());
        TextField txtImgAreaX1=new TextField(new Double(content1.getArea().getX()).toString());
        TextField txtImgAreaY1=new TextField(new Double(content1.getArea().getY()).toString());
        TextField txtImgAreaX2=new TextField(new Double(content1.getArea().getWidth()).toString());
        TextField txtImgAreaY2=new TextField(new Double(content1.getArea().getHeight()).toString());
        TextField txtImgPath=new TextField(content1.getBmpName());
        ChoiceBox chbImgEffect=new ChoiceBox();
        chbImgEffect.setItems(FXCollections.observableArrayList(Content.Effect.values()));
        chbImgEffect.getSelectionModel().select(content1.getEffect());
        TextField txtImgLapse=new TextField(new Integer(content1.getLapse()).toString());
        txtImgLapse.disableProperty().bind(content1.effectProperty().isEqualTo(Content.Effect.NONE));
        ChoiceBox chbImgPostEffect=new ChoiceBox();
        chbImgPostEffect.setItems(FXCollections.observableArrayList(Content.PostEffect.values()));
        chbImgPostEffect.getSelectionModel().select(content1.getPostEffect());
        TextField txtImgPause=new TextField(new Integer(content1.getPause()).toString());
        txtImgPause.disableProperty().bind(content1.postEffectProperty().isEqualTo(Content.PostEffect.PAUSE).not());
        ChoiceBox chbImgOrder=new ChoiceBox();
        chbImgOrder.setItems(FXCollections.observableArrayList(Content.RotationOrder.values()));
        chbImgOrder.getSelectionModel().select(content1.getOrder());
        CheckBox chkImgClear=new CheckBox();
        chkImgClear.setSelected(content1.getClear());
        
        // content1 - type
        {
            Label lLabel = new Label("Type: ");
            lLabel.setTooltip(new Tooltip("Select the type of content"));
            rGridPane.add(lLabel, new GridPane.C().row(rRowIdx).col(0).halignment(HPos.RIGHT));
            rGridPane.add(chbImgType, new GridPane.C().row(rRowIdx).col(1));
        }
        rRowIdx++;
        
        // path bmp
        {
            Label lLabel = new Label("BMP Name: ");
            lLabel.setTooltip(new Tooltip("Provide the path of a valid BMP image from local file or jar"));
            rGridPane.add(lLabel, new GridPane.C().row(rRowIdx).col(0).halignment(HPos.RIGHT));
            txtImgPath.focusedProperty().addListener((ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) -> {
                if(!t1.booleanValue() && t.booleanValue()){
                    content1.setBmpName(txtImgPath.getText());
                }
            });
            rGridPane.add(txtImgPath, new GridPane.C().row(rRowIdx).col(1));
        }
        rRowIdx++;
        
        // content1 - leds color
        {
            Label lLabel = new Label("Matrix Color: ");
            lLabel.setTooltip(new Tooltip("Select the color for the Leds"));
            rGridPane.add(lLabel, new GridPane.C().row(rRowIdx).col(0).halignment(HPos.RIGHT));
            chbImgColor.getSelectionModel().selectedItemProperty().addListener((ObservableValue ov, Object t, Object t1) -> {
                content1.setColor((Content.MatrixColor)t1);
            });
            rGridPane.add(chbImgColor, new GridPane.C().row(rRowIdx).col(1));
        }
        rRowIdx++;
        
        // content1 - Origin
        {
            Label lLabel = new Label("Origin ");
            rGridPane.add(lLabel, new GridPane.C().row(rRowIdx).col(0).halignment(HPos.LEFT));
        }
        rRowIdx++;
        
        // content1 - origin X
        {
            Label lLabel = new Label("  X: ");
            lLabel.setTooltip(new Tooltip("Insert the left coordinate of the starting point"));
            rGridPane.add(lLabel, new GridPane.C().row(rRowIdx).col(0).halignment(HPos.RIGHT));
            txtImgOriginX.focusedProperty().addListener((ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) -> {
                if(!t1.booleanValue() && t.booleanValue()){
                    try{
                        double x=Double.parseDouble(txtImgOriginX.getText());
                        double y=Double.parseDouble(txtImgOriginY.getText());
                        content1.setOrigin(new Point2D(x,y));
                    } catch(NumberFormatException nfe){
                        txtImgOriginX.setText(new Double(content1.getOrigin().getX()).toString());
                    }
                }
            });
            rGridPane.add(txtImgOriginX, new GridPane.C().row(rRowIdx).col(1));
        }
        rRowIdx++;
        
        // content1 - originY
        {
            Label rLabel = new Label("  Y: ");
            rLabel.setTooltip(new Tooltip("Insert the upper coordinate of the starting point"));
            rGridPane.add(rLabel, new GridPane.C().row(rRowIdx).col(0).halignment(HPos.RIGHT));
            txtImgOriginY.focusedProperty().addListener((ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) -> {
                if(!t1.booleanValue() && t.booleanValue()){
                    try{
                        double x=Double.parseDouble(txtImgOriginX.getText());
                        double y=Double.parseDouble(txtImgOriginY.getText());
                        content1.setOrigin(new Point2D(x,y));
                    } catch(NumberFormatException nfe){
                        txtImgOriginY.setText(new Double(content1.getOrigin().getY()).toString());
                    }
                }
            });
            rGridPane.add(txtImgOriginY, new GridPane.C().row(rRowIdx).col(1));
        }
        rRowIdx++;

        // content1 - Area
        {
            Label lLabel = new Label("Area ");
            rGridPane.add(lLabel, new GridPane.C().row(rRowIdx).col(0).halignment(HPos.LEFT));
        }
        rRowIdx++;
        
        // content1 - area X1
        {
            Label lLabel = new Label("  X1: ");
            lLabel.setTooltip(new Tooltip("Insert the left coordinate of the area"));
            rGridPane.add(lLabel, new GridPane.C().row(rRowIdx).col(0).halignment(HPos.RIGHT));
            txtImgAreaX1.focusedProperty().addListener((ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) -> {
                if(!t1.booleanValue() && t.booleanValue()){
                    try{
                        double x1=Double.parseDouble(txtImgAreaX1.getText());
                        double y1=Double.parseDouble(txtImgAreaY1.getText());
                        double x2=Double.parseDouble(txtImgAreaX2.getText());
                        double y2=Double.parseDouble(txtImgAreaY2.getText());
                        content1.setArea(new Rectangle(x1,y1,x2,y2));
                    } catch(NumberFormatException nfe){
                        txtImgAreaX1.setText(new Double(content1.getArea().getX()).toString());
                    }
                }
            });
            rGridPane.add(txtImgAreaX1, new GridPane.C().row(rRowIdx).col(1));
        }
        rRowIdx++;
        
        // content1 - area Y1
        {
            Label rLabel = new Label("  Y1: ");
            rLabel.setTooltip(new Tooltip("Insert the upper coordinate of the area"));
            rGridPane.add(rLabel, new GridPane.C().row(rRowIdx).col(0).halignment(HPos.RIGHT));
            txtImgAreaY1.focusedProperty().addListener((ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) -> {
                if(!t1.booleanValue() && t.booleanValue()){
                    try{
                        double x1=Double.parseDouble(txtImgAreaX1.getText());
                        double y1=Double.parseDouble(txtImgAreaY1.getText());
                        double x2=Double.parseDouble(txtImgAreaX2.getText());
                        double y2=Double.parseDouble(txtImgAreaY2.getText());
                        content1.setArea(new Rectangle(x1,y1,x2,y2));
                    } catch(NumberFormatException nfe){
                        txtImgAreaY1.setText(new Double(content1.getArea().getY()).toString());
                    }
                }
            });
            rGridPane.add(txtImgAreaY1, new GridPane.C().row(rRowIdx).col(1));
        }
        rRowIdx++;
        
        // content1 - area X2
        {
            Label lLabel = new Label("  X2: ");
            lLabel.setTooltip(new Tooltip("Insert the right coordinate of the area"));
            rGridPane.add(lLabel, new GridPane.C().row(rRowIdx).col(0).halignment(HPos.RIGHT));
            txtImgAreaX2.focusedProperty().addListener((ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) -> {
                if(!t1.booleanValue() && t.booleanValue()){
                    try{
                        double x1=Double.parseDouble(txtImgAreaX1.getText());
                        double y1=Double.parseDouble(txtImgAreaY1.getText());
                        double x2=Double.parseDouble(txtImgAreaX2.getText());
                        double y2=Double.parseDouble(txtImgAreaY2.getText());
                        content1.setArea(new Rectangle(x1,y1,x2,y2));
                    } catch(NumberFormatException nfe){
                        txtImgAreaX2.setText(new Double(content1.getArea().getWidth()).toString());
                    }
                }
            });
            rGridPane.add(txtImgAreaX2, new GridPane.C().row(rRowIdx).col(1));
        }
        rRowIdx++;
        
        // content1 - area Y1
        {
            Label rLabel = new Label("  Y2: ");
            rLabel.setTooltip(new Tooltip("Insert the lower coordinate of the area"));
            rGridPane.add(rLabel, new GridPane.C().row(rRowIdx).col(0).halignment(HPos.RIGHT));
            txtImgAreaY2.focusedProperty().addListener((ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) -> {
                if(!t1.booleanValue() && t.booleanValue()){
                    try{
                        double x1=Double.parseDouble(txtImgAreaX1.getText());
                        double y1=Double.parseDouble(txtImgAreaY1.getText());
                        double x2=Double.parseDouble(txtImgAreaX2.getText());
                        double y2=Double.parseDouble(txtImgAreaY2.getText());
                        content1.setArea(new Rectangle(x1,y1,x2,y2));
                    } catch(NumberFormatException nfe){
                        txtImgAreaY2.setText(new Double(content1.getArea().getHeight()).toString());
                    }
                }
            });
            rGridPane.add(txtImgAreaY2, new GridPane.C().row(rRowIdx).col(1));
        }
        rRowIdx++;
        
        rGridPane.add(new Separator(), new GridPane.C().row(rRowIdx).col(0).colSpan(2));
        rRowIdx++;
        
        // image effect
        {
            Label lLabel = new Label("Image Effect: ");
            lLabel.setTooltip(new Tooltip("Select the image effect"));
            rGridPane.add(lLabel, new GridPane.C().row(rRowIdx).col(0).halignment(HPos.RIGHT));
            chbImgEffect.getSelectionModel().selectedItemProperty().addListener((ObservableValue ov, Object t, Object t1) -> {
                content1.setEffect((Content.Effect)t1);
            });
            rGridPane.add(chbImgEffect, new GridPane.C().row(rRowIdx).col(1));
        }
        rRowIdx++;

        // image lapse 
        {
            Label lLabel = new Label("Time Lapse (ms): ");
            lLabel.setTooltip(new Tooltip("Set the time lapse (ms) to perform the selected effect"));
            rGridPane.add(lLabel, new GridPane.C().row(rRowIdx).col(0).halignment(HPos.RIGHT));
            txtImgLapse.focusedProperty().addListener((ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) -> {
                if(!t1.booleanValue() && t.booleanValue()){
                    try{
                    content1.setLapse(Integer.parseInt(txtImgLapse.getText()));
                    }catch(NumberFormatException nfe){
                        txtImgLapse.setText(new Integer(content1.getLapse()).toString());
                    }
                }
            });
            rGridPane.add(txtImgLapse, new GridPane.C().row(rRowIdx).col(1));
        }
        rRowIdx++;
        
        // image post-effect
        {
            Label lLabel = new Label("Image Posteffect: ");
            lLabel.setTooltip(new Tooltip("Select the action after the effect has been performed"));
            rGridPane.add(lLabel, new GridPane.C().row(rRowIdx).col(0).halignment(HPos.RIGHT));
            chbImgPostEffect.getSelectionModel().selectedItemProperty().addListener((ObservableValue ov, Object t, Object t1) -> {
                content1.setPostEffect((Content.PostEffect)t1);
            });
            rGridPane.add(chbImgPostEffect, new GridPane.C().row(rRowIdx).col(1));
        }
        rRowIdx++;

        // image pause 
        {
            Label lLabel = new Label("Pause (ms): ");
            lLabel.setTooltip(new Tooltip("Set the time pause (ms) after the effect has been performed"));
            rGridPane.add(lLabel, new GridPane.C().row(rRowIdx).col(0).halignment(HPos.RIGHT));
            txtImgPause.focusedProperty().addListener((ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) -> {
                if(!t1.booleanValue() && t.booleanValue()){
                    try{
                    content1.setPause(Integer.parseInt(txtImgPause.getText()));
                    }catch(NumberFormatException nfe){
                        txtImgPause.setText(new Integer(content1.getPause()).toString());
                    }
                }
            });
            rGridPane.add(txtImgPause, new GridPane.C().row(rRowIdx).col(1));
        }
        rRowIdx++;
        
        // image order
        {
            Label lLabel = new Label("Rotation Order: ");
            lLabel.setTooltip(new Tooltip("Select the image order to rotate it with other content in the same area"));
            rGridPane.add(lLabel, new GridPane.C().row(rRowIdx).col(0).halignment(HPos.RIGHT));
            chbImgOrder.getSelectionModel().selectedItemProperty().addListener((ObservableValue ov, Object t, Object t1) -> {
                content1.setOrder((Content.RotationOrder)t1);
            });
            rGridPane.add(chbImgOrder, new GridPane.C().row(rRowIdx).col(1));
        }
        rRowIdx++;

        // image clear
        {
            Label lLabel = new Label("Clear image: ");
            lLabel.setTooltip(new Tooltip("Select if the area is cleaned or not after posteffect"));
            rGridPane.add(lLabel, new GridPane.C().row(rRowIdx).col(0).halignment(HPos.RIGHT));
            chkImgClear.selectedProperty().addListener((ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) -> {
                content1.setClear(t1.booleanValue());
            });
            rGridPane.add(chkImgClear, new GridPane.C().row(rRowIdx).col(1));
        }
        rRowIdx++;
        
        ScrollPane rp=new ScrollPane(rGridPane);
        tpContent1.setContent(rp);
        
        /***************************************
        **** TITLEDPANE 3. CONTENT 2. TEXT *****
        ****************************************/
        TitledPane tpContent2=new TitledPane();
        tpContent2.setText("Content 2. Text");
        // the result
        GridPane sGridPane = new GridPane();
        sGridPane.setVgap(2.0);
        sGridPane.setHgap(2.0);

        // setup the grid so all the labels will not grow, but the rest will
        sGridPane.getColumnConstraints().addAll(lColumnConstraintsNeverGrow, lColumnConstraintsAlwaysGrow);
        int sRowIdx = 0;
        
        ChoiceBox chbTextType=new ChoiceBox();
        chbTextType.setItems(FXCollections.observableArrayList(Content.Type.values()));
        chbTextType.getSelectionModel().select(content2.getType());
        chbTextType.setDisable(true);
        ChoiceBox chbTextColor=new ChoiceBox();
        chbTextColor.setItems(FXCollections.observableArrayList(Content.MatrixColor.values()));
        chbTextColor.getSelectionModel().select(content2.getColor());
        TextField txtTextOriginX=new TextField(new Double(content2.getOrigin().getX()).toString());
        TextField txtTextOriginY=new TextField(new Double(content2.getOrigin().getY()).toString());
        TextField txtTextAreaX1=new TextField(new Double(content2.getArea().getX()).toString());
        TextField txtTextAreaY1=new TextField(new Double(content2.getArea().getY()).toString());
        TextField txtTextAreaX2=new TextField(new Double(content2.getArea().getWidth()).toString());
        TextField txtTextAreaY2=new TextField(new Double(content2.getArea().getHeight()).toString());
        TextField txtTextContent=new TextField(content2.getTxtContent());
        ChoiceBox chbTextFont=new ChoiceBox();
        chbTextFont.setItems(FXCollections.observableArrayList(Content.MatrixFont.values()));
        chbTextFont.getSelectionModel().select(content2.getMatrixFont());
        ChoiceBox chbTextGap=new ChoiceBox();
        chbTextGap.setItems(FXCollections.observableArrayList(Content.Gap.values()));
        chbTextGap.getSelectionModel().select(content2.getFontGap());
        ChoiceBox chbTextAlign=new ChoiceBox();
        chbTextAlign.setItems(FXCollections.observableArrayList(Content.Align.values()));
        chbTextAlign.getSelectionModel().select(content2.getTxtAlign());
        ChoiceBox chbTextEffect=new ChoiceBox();
        chbTextEffect.setItems(FXCollections.observableArrayList(Content.Effect.values()));
        chbTextEffect.getSelectionModel().select(content2.getEffect());
        TextField txtTextLapse=new TextField(new Integer(content2.getLapse()).toString());
        txtTextLapse.disableProperty().bind(content2.effectProperty().isEqualTo(Content.Effect.NONE));
        ChoiceBox chbTextPostEffect=new ChoiceBox();
        chbTextPostEffect.setItems(FXCollections.observableArrayList(Content.PostEffect.values()));
        chbTextPostEffect.getSelectionModel().select(content2.getPostEffect());
        TextField txtTextPause=new TextField(new Integer(content2.getPause()).toString());
        txtTextPause.disableProperty().bind(content2.postEffectProperty().isEqualTo(Content.PostEffect.PAUSE).not());
        ChoiceBox chbTextOrder=new ChoiceBox();
        chbTextOrder.setItems(FXCollections.observableArrayList(Content.RotationOrder.values()));
        chbTextOrder.getSelectionModel().select(content2.getOrder());
        CheckBox chkTextClear=new CheckBox();
        chkTextClear.setSelected(content2.getClear());
        
        // content2 - type
        {
            Label lLabel = new Label("Type: ");
            lLabel.setTooltip(new Tooltip("Select the type of content"));
            sGridPane.add(lLabel, new GridPane.C().row(sRowIdx).col(0).halignment(HPos.RIGHT));
            sGridPane.add(chbTextType, new GridPane.C().row(sRowIdx).col(1));
        }
        sRowIdx++;
        
        // content2 text
        {
            Label lLabel = new Label("Content: ");
            lLabel.setTooltip(new Tooltip("Provide text content to be displayed"));
            sGridPane.add(lLabel, new GridPane.C().row(sRowIdx).col(0).halignment(HPos.RIGHT));
            txtTextContent.focusedProperty().addListener((ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) -> {
                if(!t1.booleanValue() && t.booleanValue()){
                    content2.setTxtContent(txtTextContent.getText());
                }
            });
            sGridPane.add(txtTextContent, new GridPane.C().row(sRowIdx).col(1));
        }
        sRowIdx++;
        
        // content2 - leds color
        {
            Label lLabel = new Label("Matrix Color: ");
            lLabel.setTooltip(new Tooltip("Select the color for the Leds"));
            sGridPane.add(lLabel, new GridPane.C().row(sRowIdx).col(0).halignment(HPos.RIGHT));
            chbTextColor.getSelectionModel().selectedItemProperty().addListener((ObservableValue ov, Object t, Object t1) -> {
                content2.setColor((Content.MatrixColor)t1);
            });
            sGridPane.add(chbTextColor, new GridPane.C().row(sRowIdx).col(1));
        }
        sRowIdx++;
        
        // content2 - Origin
        {
            Label lLabel = new Label("Origin ");
            sGridPane.add(lLabel, new GridPane.C().row(sRowIdx).col(0).halignment(HPos.LEFT));
        }
        sRowIdx++;
        
        // content2 - origin X
        {
            Label lLabel = new Label("  X: ");
            lLabel.setTooltip(new Tooltip("Insert the left coordinate of the starting point"));
            sGridPane.add(lLabel, new GridPane.C().row(sRowIdx).col(0).halignment(HPos.RIGHT));
            txtTextOriginX.focusedProperty().addListener((ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) -> {
                if(!t1.booleanValue() && t.booleanValue()){
                    try{
                        double x=Double.parseDouble(txtTextOriginX.getText());
                        double y=Double.parseDouble(txtTextOriginY.getText());
                        content2.setOrigin(new Point2D(x,y));
                    } catch(NumberFormatException nfe){
                        txtTextOriginX.setText(new Double(content2.getOrigin().getX()).toString());
                    }
                }
            });
            sGridPane.add(txtTextOriginX, new GridPane.C().row(sRowIdx).col(1));
        }
        sRowIdx++;
        
        // content2 - originY
        {
            Label rLabel = new Label("  Y: ");
            rLabel.setTooltip(new Tooltip("Insert the upper coordinate of the starting point"));
            sGridPane.add(rLabel, new GridPane.C().row(sRowIdx).col(0).halignment(HPos.RIGHT));
            txtTextOriginY.focusedProperty().addListener((ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) -> {
                if(!t1.booleanValue() && t.booleanValue()){
                    try{
                        double x=Double.parseDouble(txtTextOriginX.getText());
                        double y=Double.parseDouble(txtTextOriginY.getText());
                        content2.setOrigin(new Point2D(x,y));
                    } catch(NumberFormatException nfe){
                        txtTextOriginY.setText(new Double(content2.getOrigin().getY()).toString());
                    }
                }
            });
            sGridPane.add(txtTextOriginY, new GridPane.C().row(sRowIdx).col(1));
        }
        sRowIdx++;

        // content2 - Area
        {
            Label lLabel = new Label("Area ");
            sGridPane.add(lLabel, new GridPane.C().row(sRowIdx).col(0).halignment(HPos.LEFT));
        }
        sRowIdx++;
        
        // content2 - area X1
        {
            Label lLabel = new Label("  X1: ");
            lLabel.setTooltip(new Tooltip("Insert the left coordinate of the area"));
            sGridPane.add(lLabel, new GridPane.C().row(sRowIdx).col(0).halignment(HPos.RIGHT));
            txtTextAreaX1.focusedProperty().addListener((ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) -> {
                if(!t1.booleanValue() && t.booleanValue()){
                    try{
                        double x1=Double.parseDouble(txtTextAreaX1.getText());
                        double y1=Double.parseDouble(txtTextAreaY1.getText());
                        double x2=Double.parseDouble(txtTextAreaX2.getText());
                        double y2=Double.parseDouble(txtTextAreaY2.getText());
                        content2.setArea(new Rectangle(x1,y1,x2,y2));
                    } catch(NumberFormatException nfe){
                        txtTextAreaX1.setText(new Double(content2.getArea().getX()).toString());
                    }
                }
            });
            sGridPane.add(txtTextAreaX1, new GridPane.C().row(sRowIdx).col(1));
        }
        sRowIdx++;
        
        // content2 - area Y1
        {
            Label rLabel = new Label("  Y1: ");
            rLabel.setTooltip(new Tooltip("Insert the upper coordinate of the area"));
            sGridPane.add(rLabel, new GridPane.C().row(sRowIdx).col(0).halignment(HPos.RIGHT));
            txtTextAreaY1.focusedProperty().addListener((ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) -> {
                if(!t1.booleanValue() && t.booleanValue()){
                    try{
                        double x1=Double.parseDouble(txtTextAreaX1.getText());
                        double y1=Double.parseDouble(txtTextAreaY1.getText());
                        double x2=Double.parseDouble(txtTextAreaX2.getText());
                        double y2=Double.parseDouble(txtTextAreaY2.getText());
                        content2.setArea(new Rectangle(x1,y1,x2,y2));
                    } catch(NumberFormatException nfe){
                        txtTextAreaY1.setText(new Double(content2.getArea().getY()).toString());
                    }
                }
            });
            sGridPane.add(txtTextAreaY1, new GridPane.C().row(sRowIdx).col(1));
        }
        sRowIdx++;
        
        // content2 - area X2
        {
            Label lLabel = new Label("  X2: ");
            lLabel.setTooltip(new Tooltip("Insert the right coordinate of the area"));
            sGridPane.add(lLabel, new GridPane.C().row(sRowIdx).col(0).halignment(HPos.RIGHT));
            txtTextAreaX2.focusedProperty().addListener((ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) -> {
                if(!t1.booleanValue() && t.booleanValue()){
                    try{
                        double x1=Double.parseDouble(txtTextAreaX1.getText());
                        double y1=Double.parseDouble(txtTextAreaY1.getText());
                        double x2=Double.parseDouble(txtTextAreaX2.getText());
                        double y2=Double.parseDouble(txtTextAreaY2.getText());
                        content2.setArea(new Rectangle(x1,y1,x2,y2));
                    } catch(NumberFormatException nfe){
                        txtTextAreaX2.setText(new Double(content2.getArea().getWidth()).toString());
                    }
                }
            });
            sGridPane.add(txtTextAreaX2, new GridPane.C().row(sRowIdx).col(1));
        }
        sRowIdx++;
        
        // content2 - area Y1
        {
            Label rLabel = new Label("  Y2: ");
            rLabel.setTooltip(new Tooltip("Insert the lower coordinate of the area"));
            sGridPane.add(rLabel, new GridPane.C().row(sRowIdx).col(0).halignment(HPos.RIGHT));
            txtTextAreaY2.focusedProperty().addListener((ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) -> {
                if(!t1.booleanValue() && t.booleanValue()){
                    try{
                        double x1=Double.parseDouble(txtTextAreaX1.getText());
                        double y1=Double.parseDouble(txtTextAreaY1.getText());
                        double x2=Double.parseDouble(txtTextAreaX2.getText());
                        double y2=Double.parseDouble(txtTextAreaY2.getText());
                        content2.setArea(new Rectangle(x1,y1,x2,y2));
                    } catch(NumberFormatException nfe){
                        txtTextAreaY2.setText(new Double(content2.getArea().getHeight()).toString());
                    }
                }
            });
            sGridPane.add(txtTextAreaY2, new GridPane.C().row(sRowIdx).col(1));
        }
        sRowIdx++;
        
        // Text font
        {
            Label lLabel = new Label("Text Font: ");
            lLabel.setTooltip(new Tooltip("Select the text font"));
            sGridPane.add(lLabel, new GridPane.C().row(sRowIdx).col(0).halignment(HPos.RIGHT));
            chbTextFont.getSelectionModel().selectedItemProperty().addListener((ObservableValue ov, Object t, Object t1) -> {
                content2.setMatrixFont((Content.MatrixFont)t1);
            });
            sGridPane.add(chbTextFont, new GridPane.C().row(sRowIdx).col(1));
        }
        sRowIdx++;

        // Text font-gap
        {
            Label lLabel = new Label("Text Font Gap: ");
            lLabel.setTooltip(new Tooltip("Select the text font gap"));
            sGridPane.add(lLabel, new GridPane.C().row(sRowIdx).col(0).halignment(HPos.RIGHT));
            chbTextGap.getSelectionModel().selectedItemProperty().addListener((ObservableValue ov, Object t, Object t1) -> {
                content2.setFontGap((Content.Gap)t1);
            });
            sGridPane.add(chbTextGap, new GridPane.C().row(sRowIdx).col(1));
        }
        sRowIdx++;

        // Text align
        {
            Label lLabel = new Label("Text Align: ");
            lLabel.setTooltip(new Tooltip("Select the text align"));
            sGridPane.add(lLabel, new GridPane.C().row(sRowIdx).col(0).halignment(HPos.RIGHT));
            chbTextAlign.getSelectionModel().selectedItemProperty().addListener((ObservableValue ov, Object t, Object t1) -> {
                content2.setTxtAlign((Content.Align)t1);
            });
            sGridPane.add(chbTextAlign, new GridPane.C().row(sRowIdx).col(1));
        }
        sRowIdx++;

        sGridPane.add(new Separator(), new GridPane.C().row(sRowIdx).col(0).colSpan(2));
        sRowIdx++;
        
        // Text effect
        {
            Label lLabel = new Label("Text Effect: ");
            lLabel.setTooltip(new Tooltip("Select the text effect"));
            sGridPane.add(lLabel, new GridPane.C().row(sRowIdx).col(0).halignment(HPos.RIGHT));
            chbTextEffect.getSelectionModel().selectedItemProperty().addListener((ObservableValue ov, Object t, Object t1) -> {
                content2.setEffect((Content.Effect)t1);
            });
            sGridPane.add(chbTextEffect, new GridPane.C().row(sRowIdx).col(1));
        }
        sRowIdx++;

        // text lapse 
        {
            Label lLabel = new Label("Time Lapse (ms): ");
            lLabel.setTooltip(new Tooltip("Set the time lapse (ms) to perform the selected effect"));
            sGridPane.add(lLabel, new GridPane.C().row(sRowIdx).col(0).halignment(HPos.RIGHT));
            txtTextLapse.focusedProperty().addListener((ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) -> {
                if(!t1.booleanValue() && t.booleanValue()){
                    try{
                    content2.setLapse(Integer.parseInt(txtTextLapse.getText()));
                    }catch(NumberFormatException nfe){
                        txtTextLapse.setText(new Integer(content2.getLapse()).toString());
                    }
                }
            });
            sGridPane.add(txtTextLapse, new GridPane.C().row(sRowIdx).col(1));
        }
        sRowIdx++;
        
        // text post-effect
        {
            Label lLabel = new Label("Text Posteffect: ");
            lLabel.setTooltip(new Tooltip("Select the action after the effect has been performed"));
            sGridPane.add(lLabel, new GridPane.C().row(sRowIdx).col(0).halignment(HPos.RIGHT));
            chbTextPostEffect.getSelectionModel().selectedItemProperty().addListener((ObservableValue ov, Object t, Object t1) -> {
                content2.setPostEffect((Content.PostEffect)t1);
            });
            sGridPane.add(chbTextPostEffect, new GridPane.C().row(sRowIdx).col(1));
        }
        sRowIdx++;

        // text pause 
        {
            Label lLabel = new Label("Pause (ms): ");
            lLabel.setTooltip(new Tooltip("Set the time pause (ms) after the effect has been performed"));
            sGridPane.add(lLabel, new GridPane.C().row(sRowIdx).col(0).halignment(HPos.RIGHT));
            txtTextPause.focusedProperty().addListener((ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) -> {
                if(!t1.booleanValue() && t.booleanValue()){
                    try{
                    content2.setPause(Integer.parseInt(txtTextPause.getText()));
                    }catch(NumberFormatException nfe){
                        txtTextPause.setText(new Integer(content2.getPause()).toString());
                    }
                }
            });
            sGridPane.add(txtTextPause, new GridPane.C().row(sRowIdx).col(1));
        }
        sRowIdx++;
        
        // text order
        {
            Label lLabel = new Label("Rotation Order: ");
            lLabel.setTooltip(new Tooltip("Select the text order to rotate it with other content in the same area"));
            sGridPane.add(lLabel, new GridPane.C().row(sRowIdx).col(0).halignment(HPos.RIGHT));
            chbTextOrder.getSelectionModel().selectedItemProperty().addListener((ObservableValue ov, Object t, Object t1) -> {
                content2.setOrder((Content.RotationOrder)t1);
            });
            sGridPane.add(chbTextOrder, new GridPane.C().row(sRowIdx).col(1));
        }
        sRowIdx++;

        // text clear
        {
            Label lLabel = new Label("Clear text: ");
            lLabel.setTooltip(new Tooltip("Select if the area is cleaned or not after posteffect"));
            sGridPane.add(lLabel, new GridPane.C().row(sRowIdx).col(0).halignment(HPos.RIGHT));
            chkTextClear.selectedProperty().addListener((ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) -> {
                content2.setClear(t1.booleanValue());
            });
            sGridPane.add(chkTextClear, new GridPane.C().row(sRowIdx).col(1));
        }
        sRowIdx++;
        
        ScrollPane sp=new ScrollPane(sGridPane);
        tpContent2.setContent(sp);
        
        
        accordion.getPanes().addAll(tpFrame,tpContent1,tpContent2);
        accordion.setExpandedPane(tpFrame);
        return accordion;
    }
    
    /**
     *
     * @return
     */
    @Override
    public String getJavaDocURL() {
        return "http://jfxtras.org/doc/8.0/" + MatrixPanel.class.getName().replace(".", "/") + ".html";
    }

    public static void main(String[] args) {
        launch(args);
    }
}
