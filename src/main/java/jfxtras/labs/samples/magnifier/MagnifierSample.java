package jfxtras.labs.samples.magnifier;

import java.io.File;
import java.text.DecimalFormat;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Separator;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jfxtras.labs.samples.JFXtrasSampleBase;
import jfxtras.labs.scene.control.Magnifier;

/**
 * Sample class for checking the Magnifier control behavior.
 *
 * @author SaiPradeepDandem
 */
public class MagnifierSample extends JFXtrasSampleBase {

    private DoubleProperty radius = new SimpleDoubleProperty();
    private DoubleProperty frameWidth = new SimpleDoubleProperty();
    private DoubleProperty scaleFactor = new SimpleDoubleProperty();
    private DoubleProperty scopeLineWidth = new SimpleDoubleProperty();
    private BooleanProperty scopeLinesVisible = new SimpleBooleanProperty();
    private BooleanProperty active = new SimpleBooleanProperty();
    private BooleanProperty scalableOnScroll = new SimpleBooleanProperty();
    private BooleanProperty resizableOnScroll = new SimpleBooleanProperty();

    final DecimalFormat df = new DecimalFormat("##.#");

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public String getSampleName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public String getSampleDescription() {
        return "Basic Magnifier usage to inspect an image or JavaFX nodes";
    }

    @Override
    public String getJavaDocURL() {
        String s = "/" + Magnifier.class.getName().replace(".", File.separator) + ".html";
        System.out.println("!!! " + s);
        s = Magnifier.class.getResource(s).toExternalForm();
        System.out.println("!!! " + s);
        return s;
    }

    @Override
    public Node getPanel(Stage stage) {
        final Label title1 = getLabel("Inspecting image", "subTitleLabel");
        final Label title2 = getLabel("Inspecting nodes", "subTitleLabel");
        final Magnifier imageLayout = buildImageLayout();
        final Magnifier formLayout = buildFormLayout();

        VBox root = new VBox(10);
        root.getStylesheets().add(MagnifierSample.class.getResource("magnifiersample.css").toExternalForm());
        root.setPadding(new Insets(30, 30, 30, 30));
        root.getChildren().addAll(title1, new Separator(), imageLayout, title2, new Separator(), formLayout);
        return root;
    }

    private Magnifier buildImageLayout() {
        final ImageView sample1 = new ImageView(new Image(
                MagnifierSample.class.getResourceAsStream("/jfxtras/labs/samples/magnifier/tech_drawing.jpg")));
        sample1.setFitHeight(280);
        sample1.setFitWidth(320);
        return configureSample(sample1);
    }

    private Magnifier buildFormLayout() {
        Label info = new Label(" * Uncheck the 'active' property to fill the form details.");
        info.getStyleClass().add("form-info");
        GridPane gp = new GridPane();
        gp.setVgap(8);
        gp.setHgap(10);
        gp.add(info, 0, 0, 4, 1);
        gp.addRow(1, new Label("First Name"), new Label(":"), new TextField());
        gp.addRow(2, new Label("Last Name"), new Label(":"), new TextField());
        gp.addRow(3, new Label("Gender"), new Label(":"), new RadioButton("Male"));
        gp.addRow(4, new Label(""), new Label(""), new RadioButton("Female"));
        gp.addRow(5, new Label("Subjects"), new Label(":"), new CheckBox("Maths"), new CheckBox("Social"));
        gp.addRow(6, new Label(""), new Label(""), new CheckBox("Science"), new CheckBox("Biology"));

        HBox formButtonBox = new HBox();
        formButtonBox.setSpacing(10);
        formButtonBox.setPadding(new Insets(15, 0, 0, 0));
        formButtonBox.setAlignment(Pos.CENTER_RIGHT);
        formButtonBox.getChildren().addAll(new Button("Cancel"), new Button("Submit"));

        VBox vb = new VBox();
        vb.setAlignment(Pos.TOP_LEFT);
        vb.setSpacing(10);
        vb.setPadding(new Insets(10));
        vb.getStyleClass().addAll("formBox");
        vb.setMaxHeight(250);
        vb.setMaxWidth(400);
        vb.getChildren().addAll(getLabel("Student Form", "formTitle"), gp, formButtonBox);
        return configureSample(vb);
    }

    private Label getLabel(String txt, String... styleClass) {
        Label lbl = new Label(txt);
        lbl.getStyleClass().addAll(styleClass);
        return lbl;
    }

    @Override
    public Node getControlPanel() {
        final CheckBox activateCB = new CheckBox();
        activateCB.setSelected(true);
        active.bind(activateCB.selectedProperty());

        final Slider rSlider = new Slider(50, 150, 86);
        rSlider.disableProperty().bind(activateCB.selectedProperty().not());
        radius.bind(rSlider.valueProperty());
        Label rL = new Label();
        rL.textProperty().bind(new StringBinding() {
            {
                bind(rSlider.valueProperty());
            }

            @Override
            protected String computeValue() {
                return df.format(rSlider.getValue()) + "px";
            }
        });

        final Slider fmSlider = new Slider(3, 10, 5.5);
        fmSlider.disableProperty().bind(activateCB.selectedProperty().not());
        frameWidth.bind(fmSlider.valueProperty());
        Label fmL = new Label();
        fmL.textProperty().bind(new StringBinding() {
            {
                bind(fmSlider.valueProperty());
            }

            @Override
            protected String computeValue() {
                return df.format(fmSlider.getValue()) + "px";
            }
        });

        final Slider sfSlider = new Slider(1, 8, 3);
        sfSlider.disableProperty().bind(activateCB.selectedProperty().not());
        scaleFactor.bind(sfSlider.valueProperty());
        Label sfL = new Label();
        sfL.textProperty().bind(new StringBinding() {
            {
                bind(sfSlider.valueProperty());
            }

            @Override
            protected String computeValue() {
                return df.format(sfSlider.getValue()) + "";
            }
        });

        final CheckBox slVisibleCB = new CheckBox();
        slVisibleCB.disableProperty().bind(activateCB.selectedProperty().not());
        scopeLinesVisible.bind(slVisibleCB.selectedProperty());

        final Slider sllider = new Slider(1, 4, 1.5);
        sllider.disableProperty().bind(new BooleanBinding() {
            {
                bind(activateCB.selectedProperty(), slVisibleCB.selectedProperty());
            }

            @Override
            protected boolean computeValue() {
                if (!activateCB.isSelected() || !slVisibleCB.isSelected()) {
                    return true;
                }
                return false;
            }
        });
        scopeLineWidth.bind(sllider.valueProperty());
        Label slL = new Label();
        slL.textProperty().bind(new StringBinding() {
            {
                bind(sllider.valueProperty());
            }

            @Override
            protected String computeValue() {
                return df.format(sllider.getValue()) + "px";
            }
        });

        CheckBox scaleOnScrollCB = new CheckBox(" [ Ctrl + mouse scroll ]");
        scaleOnScrollCB.disableProperty().bind(activateCB.selectedProperty().not());
        scalableOnScroll.bind(scaleOnScrollCB.selectedProperty());

        CheckBox resizeOnScrollCB = new CheckBox(" [ Alt + mouse scroll ]");
        resizeOnScrollCB.disableProperty().bind(activateCB.selectedProperty().not());
        resizableOnScroll.bind(resizeOnScrollCB.selectedProperty());

        final Label secHeading = getLabel("Configurable Properties :", "subTitleLabel");
        secHeading.setPrefHeight(25);
        secHeading.setAlignment(Pos.TOP_CENTER);

        GridPane gp = new GridPane();
        gp.setVgap(18);
        gp.setHgap(10);

        gp.add(secHeading, 0, 0, 4, 1);
        gp.addRow(1, getSep("Active"), new Label(":"), activateCB, getSpacer());
        gp.addRow(2, getSep("Radius"), new Label(":"), rSlider, rL);
        gp.addRow(3, getSep("Frame Width"), new Label(":"), fmSlider, fmL);
        gp.addRow(4, getSep("Scale Factor"), new Label(":"), sfSlider, sfL);
        gp.addRow(5, getSep("Scope Lines Visible"), new Label(":"), slVisibleCB, getSpacer());
        gp.addRow(6, getSep("Scope Line Width"), new Label(":"), sllider, slL);

        gp.add(getSep("Scalable On Scroll"), 0, 7);
        gp.add(new Label(":"), 1, 7);
        gp.add(scaleOnScrollCB, 2, 7, 2, 1);

        gp.add(getSep("Resizable On Scroll"), 0, 8);
        gp.add(new Label(":"), 1, 8);
        gp.add(resizeOnScrollCB, 2, 8, 2, 1);

        StackPane sp = new StackPane();
        sp.setPadding(new Insets(25));
        sp.getChildren().add(gp);
        sp.getStylesheets().add(MagnifierSample.class.getResource("magnifiersample.css").toExternalForm());
        return sp;
    }

    private Node getSpacer() {
        StackPane spacer = new StackPane();
        spacer.setMinWidth(50);
        spacer.getChildren().add(new Label(""));
        return spacer;
    }

    private Label getSep(String txt) {
        Label label = new Label(txt);
        label.getStyleClass().add("property-label");
        return label;
    }

    private Magnifier configureSample(Node sample) {
        Magnifier p = new Magnifier();
        p.radiusProperty().bindBidirectional(radius);
        p.frameWidthProperty().bind(frameWidth);
        p.scaleFactorProperty().bind(scaleFactor);
        p.scopeLineWidthProperty().bind(scopeLineWidth);
        p.scopeLinesVisibleProperty().bind(scopeLinesVisible);
        p.activeProperty().bind(active);
        p.scalableOnScrollProperty().bind(scalableOnScroll);
        p.resizableOnScrollProperty().bind(resizableOnScroll);
        p.setContent(sample);
        return p;
    }
}
