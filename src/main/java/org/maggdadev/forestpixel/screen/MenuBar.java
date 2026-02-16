package org.maggdadev.forestpixel.screen;

import javafx.beans.binding.BooleanBinding;
import javafx.scene.control.Alert;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.maggdadev.forestpixel.canvas.CanvasViewModel;
import org.maggdadev.forestpixel.io.PreferenceKey;
import org.maggdadev.forestpixel.io.Preferences;

import java.io.File;
import java.io.FileNotFoundException;

public class MenuBar extends javafx.scene.control.MenuBar {
    private final static FileChooser.ExtensionFilter EXTENSION_FILTER = new FileChooser.ExtensionFilter("Forest Pixel files", "*.fp");
    private MainScreenViewModel mainScreenViewModel;
    public MenuBar(Stage stage, MainScreenViewModel viewModel) {/* todo
        MenuItem saveAsMenuItem = createSaveAsMenuItem(stage, viewModel);
        MenuItem saveMenuItem = createSaveMenuItem(viewModel, saveAsMenuItem);
        MenuItem openMenuItem = createOpenMenuItem(stage, viewModel);
        MenuItem exportMenuItem = createExportMenuItem(stage, viewModel);
*/
        BooleanBinding disableSaveButtons = viewModel.openedProjectViewModelProperty().isNull();
        MenuItem saveAsMenuItem = createSaveAsMenuItem(stage, viewModel, disableSaveButtons);
        MenuItem saveMenuItem = createSaveMenuItem(viewModel, saveAsMenuItem, disableSaveButtons);
        MenuItem openMenuItem = createOpenMenuItem(stage, viewModel);
        this.mainScreenViewModel = viewModel;
        MenuItem newProjectMenuItem = createNewProjectMenuItem(viewModel);
        Menu fileMenu = new Menu("File", null, newProjectMenuItem,openMenuItem, saveMenuItem, saveAsMenuItem);/*, null, saveAsMenuItem, saveMenuItem, openMenuItem, new SeparatorMenuItem(), exportMenuItem);*/
        getMenus().add(fileMenu);
    }

    private static MenuItem createNewProjectMenuItem(MainScreenViewModel viewModel) {
        MenuItem newProjectMenuItem = new MenuItem("New project");
        newProjectMenuItem.setOnAction(e -> {
            try {
                viewModel.newProject();
            } catch (Exception ex) {
                new Alert(Alert.AlertType.ERROR, ex.getMessage()).show();
            }
        });
        return newProjectMenuItem;
    }

    private static MenuItem createSaveMenuItem(MainScreenViewModel viewModel, MenuItem saveAsMenuItem, BooleanBinding disable) {
        MenuItem saveMenuItem = new MenuItem("Save");
        saveMenuItem.disableProperty().bind(disable);
        saveMenuItem.setOnAction(e -> {
            try {
                try {
                    viewModel.save();
                } catch (FileNotFoundException ex) {
                    saveAsMenuItem.fire();
                }
            } catch (Exception ex) {
                new Alert(Alert.AlertType.ERROR, ex.getMessage()).show();
            }
        });
        return saveMenuItem;
    }

    private static MenuItem createSaveAsMenuItem(Stage stage, MainScreenViewModel mainScreenViewModel, BooleanBinding disable) {
        MenuItem saveAsMenuItem = new MenuItem("Save as...");
        saveAsMenuItem.disableProperty().bind(disable);
        saveAsMenuItem.setOnAction(e -> {
            try {
                FileChooser fileChooser = new FileChooser();
                fileChooser.getExtensionFilters().add(EXTENSION_FILTER);
                fileChooser.setSelectedExtensionFilter(EXTENSION_FILTER);
                fileChooser.setInitialFileName("yourmom.fp");
                fileChooser.setInitialDirectory(new File(Preferences.get(PreferenceKey.FP_FILES_DIRECTORY, System.getProperty("user.home"))));
                File file = fileChooser.showSaveDialog(stage);
                if (file == null) {
                    return;
                }
                Preferences.set(PreferenceKey.FP_FILES_DIRECTORY, file.getParent());
                mainScreenViewModel.saveModelTo(file);
            } catch (Exception ex) {
                new Alert(Alert.AlertType.ERROR, ex.getMessage()).show();
            }
        });
        return saveAsMenuItem;
    }

    private static MenuItem createExportMenuItem(Stage stage, CanvasViewModel viewModel) {
        MenuItem exportMenuItem = new MenuItem("Export...");
        exportMenuItem.setOnAction(e -> {
            try {
                FileChooser fileChooser = new FileChooser();
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG files", "*.png"));
                fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("PNG files", "*.png"));
                fileChooser.setInitialFileName("yourmom.png");
                fileChooser.setInitialDirectory(new File(Preferences.get(PreferenceKey.EXPORT_FILES_DIRECTORY, System.getProperty("user.home"))));
                fileChooser.setTitle("Export");
                File file = fileChooser.showSaveDialog(stage);
                if (file == null) {
                    return;
                }
                Preferences.set(PreferenceKey.EXPORT_FILES_DIRECTORY, file.getParent());
                viewModel.exportTo(file);
            } catch (Exception ex) {
                new Alert(Alert.AlertType.ERROR, ex.getMessage()).show();
            }
        });
        return exportMenuItem;
    }

    private static MenuItem createOpenMenuItem(Stage stage, MainScreenViewModel viewModel) {
        MenuItem openMenuItem = new MenuItem("Open...");
        openMenuItem.setOnAction(e -> {
            try {
                FileChooser fileChooser = new FileChooser();
                fileChooser.getExtensionFilters().add(EXTENSION_FILTER);
                fileChooser.setSelectedExtensionFilter(EXTENSION_FILTER);
                fileChooser.setInitialDirectory(new File(Preferences.get(PreferenceKey.FP_FILES_DIRECTORY, System.getProperty("user.home"))));
                File file = fileChooser.showOpenDialog(stage);
                if (file == null || !file.exists()) {
                    return;
                }
                Preferences.set(PreferenceKey.FP_FILES_DIRECTORY, file.getParent());
                viewModel.openProject(file);
            } catch (Exception ex) {
                new Alert(Alert.AlertType.ERROR, ex.getMessage()).show();
            }
        });
        return openMenuItem;
    }


}
