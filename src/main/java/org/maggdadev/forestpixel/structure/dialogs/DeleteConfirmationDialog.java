package org.maggdadev.forestpixel.structure.dialogs;

import javafx.scene.control.Alert;

import java.util.Optional;

public class DeleteConfirmationDialog extends Alert {
    public DeleteConfirmationDialog(String nodeType, String nodeName) {
        super(AlertType.CONFIRMATION);
        setTitle("Delete Confirmation");
        setHeaderText("Are you sure you want to delete the " + nodeType + " '" + nodeName + "'?");
    }
}
