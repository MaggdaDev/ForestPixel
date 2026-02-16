package org.maggdadev.forestpixel.screen;

import javafx.beans.binding.BooleanBinding;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.maggdadev.forestpixel.io.PreferenceKey;
import org.maggdadev.forestpixel.io.Preferences;
import org.maggdadev.forestpixel.structure.dialogs.CloseProjectConfirmation;

import java.io.File;

public class FxMainScreenDialogService implements MainScreenDialogService{
    private final static FileChooser.ExtensionFilter EXTENSION_FILTER = new FileChooser.ExtensionFilter("Forest Pixel files", "*.fp");
    private final Window stage;
    public FxMainScreenDialogService(Window stage) {
        this.stage = stage;
    }
    @Override
    public CloseProjectResult mayDeleteProjectAlert() {
        return new CloseProjectConfirmation().showAndWait().orElse(CloseProjectResult.CANCEL);
    }

    @Override
    public File saveAsDialog() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(EXTENSION_FILTER);
        fileChooser.setSelectedExtensionFilter(EXTENSION_FILTER);
        fileChooser.setInitialFileName("yourmom.fp");
        fileChooser.setInitialDirectory(new File(Preferences.get(PreferenceKey.FP_FILES_DIRECTORY, System.getProperty("user.home"))));
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            Preferences.set(PreferenceKey.FP_FILES_DIRECTORY, file.getParent());
        }
        return file;
    }

    @Override
    public File openDialog() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(EXTENSION_FILTER);
        fileChooser.setSelectedExtensionFilter(EXTENSION_FILTER);
        fileChooser.setInitialDirectory(new File(Preferences.get(PreferenceKey.FP_FILES_DIRECTORY, System.getProperty("user.home"))));
        File file = fileChooser.showOpenDialog(stage);
        if (file != null && file.exists()) {
            Preferences.set(PreferenceKey.FP_FILES_DIRECTORY, file.getParent());
        }
        return file;
    }

}
