package jfxtras.presentation;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class MaxSizeExample extends Application {
	
    public static void main(String[] args) {
        launch(args);       
    }

	@Override
	public void start(Stage stage) {
		
		// create layout
		HBox lHBox = new HBox();
		
		// add textfield
		Button lButton = new Button("Button");
		// JavaFX
//		lHBox.getChildren().add(lButton);
//		HBox.setHgrow(lButton, Priority.ALWAYS);
//		lButton.setMaxWidth(Integer.MAX_VALUE);
//		lButton.setMaxWidth(200);
		// JFXtras
//		lHBox.add(lButton, new HBox.C()
//			.hgrow(Priority.ALWAYS)
//		);
//		lButton.setMaxWidth(200);
		
		// create scene
        Scene scene = new Scene(lHBox, 300, 300);
        
        // create stage
        stage.setTitle(this.getClass().getSimpleName());
        stage.setScene(scene);
        stage.show();
    }

}
