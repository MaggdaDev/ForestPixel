package org.maggdadev.forestpixel;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.maggdadev.forestpixel.canvas.CanvasScreen;

public class ForestPixelApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = new CanvasScreen();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }
}
