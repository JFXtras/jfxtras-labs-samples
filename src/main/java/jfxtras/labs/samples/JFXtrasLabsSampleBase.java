package jfxtras.labs.samples;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.WeakHashMap;

import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.layout.Priority;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import jfxtras.scene.layout.GridPane;
import jfxtras.scene.layout.VBox;

/**
 */
abstract public class JFXtrasLabsSampleBase extends SampleBase {

	/** {@inheritDoc} */
	@Override public String getProjectName() {
		return "JFXtrasLabs";
	}

	/** {@inheritDoc} */
	@Override public String getProjectVersion() {
		return "8.0"; // TODO read from gradle meta-inf?
	}
}
