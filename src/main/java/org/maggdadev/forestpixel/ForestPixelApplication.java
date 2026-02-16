package org.maggdadev.forestpixel;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.maggdadev.forestpixel.screen.*;

public class ForestPixelApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        MainScreenDialogService dialogService = new FxMainScreenDialogService(stage);

        MainScreenModel mainScreenModel = new MainScreenModel();
        MainScreenViewModel mainScreenViewModel = new MainScreenViewModel(mainScreenModel, dialogService);
        MainScreenView mainScreen = new MainScreenView(mainScreenViewModel, stage);
        Scene scene = new Scene(mainScreen);
        stage.setScene(scene);
        stage.setTitle("Forest Pixel");
        stage.setHeight(600);
        stage.setWidth(800);
        stage.show();
    }
}
