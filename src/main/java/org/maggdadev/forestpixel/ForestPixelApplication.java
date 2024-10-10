package org.maggdadev.forestpixel;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.maggdadev.forestpixel.canvas.CanvasModel;
import org.maggdadev.forestpixel.canvas.CanvasView;
import org.maggdadev.forestpixel.canvas.CanvasViewModel;

import java.io.File;

public class ForestPixelApplication extends Application {
    private final static String INITIAL_DIRECTORY_PREFERENCE = "initialDirectory";

    @Override
    public void start(Stage stage) throws Exception {
        CanvasModel canvasModel = new CanvasModel(800, 500);
        CanvasViewModel viewModel = new CanvasViewModel(canvasModel);
        CanvasView canvasView = new CanvasView(viewModel);


        MenuBar menuBar = createMenuBar(stage, viewModel);


        BorderPane root = new BorderPane();
        root.setCenter(canvasView);
        root.setTop(menuBar);

        Scene scene = new Scene(root);


        stage.setScene(scene);
        stage.setTitle("Forest Pixel");
        stage.show();
    }

    private static MenuBar createMenuBar(Stage stage, CanvasViewModel viewModel) {
        MenuItem saveAsMenuItem = new MenuItem("Save as...");
        saveAsMenuItem.setOnAction(e -> {
            try {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("Forest Pixel files", "*.fp"));
                fileChooser.setInitialFileName("yourmom.fp");
                fileChooser.setInitialDirectory(new File(FPPreferences.get(INITIAL_DIRECTORY_PREFERENCE, System.getProperty("user.home"))));
                File file = fileChooser.showSaveDialog(stage);
                if (file == null || !file.exists()) {
                    return;
                }
                FPPreferences.set(INITIAL_DIRECTORY_PREFERENCE, file.getParent());
                viewModel.saveModelTo(file);
            } catch (Exception ex) {
                new Alert(Alert.AlertType.ERROR, ex.getMessage()).show();
            }
        });
        MenuItem openMenuItem = new MenuItem("Open...");
        openMenuItem.setOnAction(e -> {
            try {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("Forest Pixel files", "*.fp"));
                fileChooser.setInitialDirectory(new File(FPPreferences.get(INITIAL_DIRECTORY_PREFERENCE, System.getProperty("user.home"))));
                File file = fileChooser.showOpenDialog(stage);
                if (file == null || !file.exists()) {
                    return;
                }
                FPPreferences.set(INITIAL_DIRECTORY_PREFERENCE, file.getParent());
                viewModel.loadModelFrom(file);
            } catch (Exception ex) {
                new Alert(Alert.AlertType.ERROR, ex.getMessage()).show();
            }
        });
        Menu fileMenu = new Menu("File", null, saveAsMenuItem, openMenuItem);
        return new MenuBar(fileMenu);
    }
}
