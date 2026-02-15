package org.maggdadev.forestpixel;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.maggdadev.forestpixel.screen.MainScreenModel;
import org.maggdadev.forestpixel.screen.MainScreenView;
import org.maggdadev.forestpixel.screen.MainScreenViewModel;

public class ForestPixelApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        MainScreenModel mainScreenModel = new MainScreenModel();
        MainScreenViewModel mainScreenViewModel = new MainScreenViewModel(mainScreenModel);
        MainScreenView mainScreen = new MainScreenView(mainScreenViewModel, stage);
        Scene scene = new Scene(mainScreen);
        stage.setScene(scene);
        stage.setTitle("Forest Pixel");
        stage.setHeight(600);
        stage.setWidth(800);
        stage.show();
    }
}
