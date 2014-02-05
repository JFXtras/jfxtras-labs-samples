package jfxtras.labs.samples;

import fxsampler.SampleBase;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Priority;
import jfxtras.labs.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Stage;
import jfxtras.labs.util.NodeUtil;

/**
 */
abstract public class JFXtrasSampleBase extends SampleBase {

	/** {@inheritDoc} */
	@Override public String getProjectName() {
		return "JFXtras";
	}

	/** {@inheritDoc} */
	@Override public String getProjectVersion() {
		return "8.0"; // TBEE read from gradle meta-inf?
	}
	
	
	/**
	 * Create a TextArea that automatically saves its contents as a CSS files and adds that to the stage.
	 * 
	 * @param stage
	 * @return 
	 */
	protected TextArea createTextAreaForCSS(Stage stage) {
		try {
			// clear any existing
			stage.getScene().getStylesheets().clear();
			
			// the CSS file
			File lFile = File.createTempFile(this.getClass().getSimpleName(), ".css");
			lFile.deleteOnExit();

			// text field
			final TextArea lTextArea = new TextArea();			
			lTextArea.focusedProperty().addListener( (observable) -> {
				stage.getScene().getStylesheets().remove(lFile.toURI().toString());
				try ( 
					FileWriter lFileWriter = new FileWriter(lFile, false); 
				){
					lFileWriter.write(lTextArea.getText());
				}
				catch (IOException e) {
					throw new RuntimeException(e);
				}
				stage.getScene().getStylesheets().add(lFile.toURI().toString());
			});
			
			// done
			return lTextArea;
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Create a TextArea that automatically saves its contents as a CSS files and adds that to the stage.
	 * 
	 * @param stage
	 * @return 
	 */
	protected TextArea createTextAreaForCSS(Stage stage, ObservableList<String> examples) {
		// create textfield
		final TextArea lTextArea = createTextAreaForCSS(stage);
		lTextArea.setTooltip(new Tooltip(examples.size() + " example(s) available under double click"));
		
		// bind a popup
		lTextArea.setOnMouseClicked( (evt) -> {
			// only if the right mouse button is pressed
			if (evt.getClickCount() < 2) {
				return;
			}
			
			// in a popup
			final Popup lPopup = new Popup();
			lPopup.setAutoFix(true);
			lPopup.setAutoHide(true);
			lPopup.setHideOnEscape(true);

			// container
			VBox lVBox = new VBox();
			
			// the list to show
			final ListView<String> lListView = new ListView<>(examples);			
			lListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
			lListView.setTooltip(new Tooltip("Double click or multiselect and use apply"));
			lListView.setOnMouseClicked( (eventHandler) -> {
				if (eventHandler.getClickCount() >= 2) {
					applyTextAreaAsCSS(lListView, lTextArea);
					lPopup.hide(); // TODO: does this introduce a memory leak?
				}
			});
			lVBox.add(lListView, new VBox.C().vgrow(Priority.ALWAYS));
			
			// button
			Button lApplyButton = new Button("Apply");
			lVBox.add(lApplyButton, new VBox.C().vgrow(Priority.ALWAYS));
			lApplyButton.setOnAction( (eventHandler) -> {
				applyTextAreaAsCSS(lListView, lTextArea);
				lPopup.hide(); // TODO: does this introduce a memory leak?
			});
			
			// show
			lPopup.getContent().add(lVBox);
			lPopup.show(lTextArea, NodeUtil.screenX(lTextArea), NodeUtil.screenY(lTextArea));
		});
		
		// done
		return lTextArea;
	}
	
	private void applyTextAreaAsCSS(ListView<String> listView, TextArea textArea) {
		String lStyleSheet = "";
		for (String s : listView.getSelectionModel().getSelectedItems()) {
			if (lStyleSheet.length() > 0) {
				lStyleSheet += "\n";
			}
			lStyleSheet += s;
		}
		textArea.setText( textArea.getText() + (textArea.getText().length() == 0 ? "" : "\n") + lStyleSheet );
	}
}
