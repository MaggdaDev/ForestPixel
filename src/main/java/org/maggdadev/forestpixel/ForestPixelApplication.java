package org.maggdadev.forestpixel;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.maggdadev.forestpixel.canvas.CanvasModel;
import org.maggdadev.forestpixel.canvas.CanvasView;
import org.maggdadev.forestpixel.canvas.CanvasViewModel;

import java.io.File;

public class ForestPixelApplication extends Application {
    private final static String FP_FILES_DIRECTORY_PREFERENCE = "initialDirectory";
    private final static String EXPORT_FILES_DIRECTORY_PREFERENCE = "exportDirectory";
    private final static FileChooser.ExtensionFilter EXTENSION_FILTER = new FileChooser.ExtensionFilter("Forest Pixel files", "*.fp");

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
                fileChooser.getExtensionFilters().add(EXTENSION_FILTER);
                fileChooser.setSelectedExtensionFilter(EXTENSION_FILTER);
                fileChooser.setInitialFileName("yourmom.fp");
                fileChooser.setInitialDirectory(new File(FPPreferences.get(FP_FILES_DIRECTORY_PREFERENCE, System.getProperty("user.home"))));
                File file = fileChooser.showSaveDialog(stage);
                if (file == null) {
                    return;
                }
                FPPreferences.set(FP_FILES_DIRECTORY_PREFERENCE, file.getParent());
                viewModel.saveModelTo(file);
            } catch (Exception ex) {
                new Alert(Alert.AlertType.ERROR, ex.getMessage()).show();
            }
        });
        MenuItem openMenuItem = new MenuItem("Open...");
        openMenuItem.setOnAction(e -> {
            try {
                FileChooser fileChooser = new FileChooser();
                fileChooser.getExtensionFilters().add(EXTENSION_FILTER);
                fileChooser.setSelectedExtensionFilter(EXTENSION_FILTER);
                fileChooser.setInitialDirectory(new File(FPPreferences.get(FP_FILES_DIRECTORY_PREFERENCE, System.getProperty("user.home"))));
                File file = fileChooser.showOpenDialog(stage);
                if (file == null || !file.exists()) {
                    return;
                }
                FPPreferences.set(FP_FILES_DIRECTORY_PREFERENCE, file.getParent());
                viewModel.loadModelFrom(file);
            } catch (Exception ex) {
                new Alert(Alert.AlertType.ERROR, ex.getMessage()).show();
            }
        });

        MenuItem saveMenuItem = new MenuItem("Save");
        saveMenuItem.setOnAction(e -> {
            try {
                if (viewModel.getFileLocation() == null) {
                    saveAsMenuItem.fire();
                } else {
                    viewModel.save();
                }
            } catch (Exception ex) {
                new Alert(Alert.AlertType.ERROR, ex.getMessage()).show();
            }
        });

        MenuItem exportMenuItem = new MenuItem("Export...");
        exportMenuItem.setOnAction(e -> {
            try {
                FileChooser fileChooser = new FileChooser();
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG files", "*.png"));
                fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("PNG files", "*.png"));
                fileChooser.setInitialFileName("yourmom.png");
                fileChooser.setInitialDirectory(new File(FPPreferences.get(EXPORT_FILES_DIRECTORY_PREFERENCE, System.getProperty("user.home"))));
                fileChooser.setTitle("Export");
                File file = fileChooser.showSaveDialog(stage);
                if (file == null) {
                    return;
                }
                FPPreferences.set(EXPORT_FILES_DIRECTORY_PREFERENCE, file.getParent());
                viewModel.exportTo(file);
            } catch (Exception ex) {
                new Alert(Alert.AlertType.ERROR, ex.getMessage()).show();
            }
        });

        Menu fileMenu = new Menu("File", null, saveAsMenuItem, saveMenuItem, openMenuItem, new SeparatorMenuItem(), exportMenuItem);
        return new MenuBar(fileMenu);
    }
}
