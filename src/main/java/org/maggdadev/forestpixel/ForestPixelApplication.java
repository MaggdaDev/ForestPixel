package org.maggdadev.forestpixel;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.maggdadev.forestpixel.canvas.CanvasModel;
import org.maggdadev.forestpixel.canvas.CanvasView;
import org.maggdadev.forestpixel.canvas.CanvasViewModel;

public class ForestPixelApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        CanvasModel canvasModel = new CanvasModel();
        canvasModel.createNewImage(800, 500);
        CanvasViewModel viewModel = new CanvasViewModel(canvasModel);
        Parent root = new CanvasView(viewModel);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
