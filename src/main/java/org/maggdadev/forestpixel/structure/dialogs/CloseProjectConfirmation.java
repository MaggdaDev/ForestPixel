package org.maggdadev.forestpixel.structure.dialogs;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import org.maggdadev.forestpixel.screen.MainScreenDialogService;

public class CloseProjectConfirmation extends Dialog<MainScreenDialogService.CloseProjectResult> {
    public static final ButtonType SAVE_AND_CLOSE = new ButtonType("Save and Close"),
            CLOSE_WITHOUT_SAVING = new ButtonType("Close without Saving"),
            CANCEL = new ButtonType("Cancel");
    public CloseProjectConfirmation() {
        setTitle("Close Project Confirmation");
        setHeaderText("Are you sure you want to close the current project?");
        getDialogPane().getButtonTypes().setAll(SAVE_AND_CLOSE, CLOSE_WITHOUT_SAVING, CANCEL);
        setResultConverter(type -> {
            if (type == SAVE_AND_CLOSE) {
                return MainScreenDialogService.CloseProjectResult.SAVE_AND_CLOSE;
            }
            if (type == CLOSE_WITHOUT_SAVING) {
                return MainScreenDialogService.CloseProjectResult.DISCARD_AND_CLOSE;
            }
            return MainScreenDialogService.CloseProjectResult.CANCEL;
        });
    }
}
