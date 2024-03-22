package org.maggdadev.forestpixel;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ForestPixelApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        VBox root = new VBox(new Button("Hello"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }
}
