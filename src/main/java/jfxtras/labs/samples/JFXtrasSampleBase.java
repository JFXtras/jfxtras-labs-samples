package jfxtras.labs.samples;

import fxsampler.SampleBase;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseButton;
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
		lTextArea.setTooltip(new Tooltip(examples.size() + " examples are available under the MIDDLE mousebutton"));
		
		// bind a popup
		lTextArea.setOnMouseClicked( (evt) -> {
			// only if the right mouse button is pressed
			if (evt.getButton() != MouseButton.MIDDLE) {
				return;
			}
			
			// in a popup
			final Popup lPopup = new Popup();
			lPopup.setAutoFix(true);
			lPopup.setAutoHide(true);
			lPopup.setHideOnEscape(true);

			// the list to show
			final ListView<String> lListView = new ListView<>(examples);			
			lListView.getSelectionModel().selectedItemProperty().addListener( (observable) -> {
				lTextArea.setText( lListView.getSelectionModel().getSelectedItem() );
				lPopup.hide(); // TODO: does this introduce a memory leak?
			});
			lPopup.getContent().add(lListView);
			
			// show
			lPopup.show(lTextArea, NodeUtil.screenX(lTextArea), NodeUtil.screenY(lTextArea));
		});
		
		// done
		return lTextArea;
	}
}
