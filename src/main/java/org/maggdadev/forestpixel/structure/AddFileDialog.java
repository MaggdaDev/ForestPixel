package org.maggdadev.forestpixel.structure;

import javafx.scene.control.Dialog;
import javafx.scene.control.TextInputDialog;

public class AddFileDialog extends TextInputDialog {
    public AddFileDialog() {
        setTitle("Add File");
        setHeaderText("Enter the name of the new file...");
        setContentText("File name:");
    }

}
