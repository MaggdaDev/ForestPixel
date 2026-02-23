package org.maggdadev.forestpixel;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.maggdadev.forestpixel.screen.*;

public class ForestPixelApplication extends Application {
    private final static String[] cssPaths = {
            "/styles/canvas-view.css"
    };
    @Override
    public void start(Stage stage) throws Exception {
        MainScreenDialogService dialogService = new FxMainScreenDialogService(stage);
        FxCanvasTabViewService canvasTabViewService = new FxCanvasTabViewService();

        MainScreenModel mainScreenModel = new MainScreenModel();
        MainScreenViewModel mainScreenViewModel = new MainScreenViewModel(mainScreenModel, dialogService, canvasTabViewService);
        MainScreenView mainScreen = new MainScreenView(mainScreenViewModel, stage, canvasTabViewService);
        Scene scene = new Scene(mainScreen);
        for(String path : cssPaths) {
            try {
                scene.getStylesheets().add(ForestPixelApplication.class.getResource(path).toExternalForm());
            } catch (NullPointerException e) {
                System.err.println("Stylesheet not found: " + path);
            }
        }
        stage.setScene(scene);
        stage.setTitle("Forest Pixel");
        stage.setHeight(600);
        stage.setWidth(800);
        stage.show();
    }
}
