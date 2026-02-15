package org.maggdadev.forestpixel.structure.dialogs;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import org.maggdadev.forestpixel.structure.ProjectNodeModel;

import java.util.function.Function;

public abstract class AddProjectNodeDialog extends Dialog<ProjectNodeModel> {
    public AddProjectNodeDialog(String nodeName, Function<String, ProjectNodeModel> resultConverter) {
        setTitle("Add " + nodeName);
        setHeaderText("Enter the name of the new " + nodeName + "...");
        Label label = new Label(nodeName.substring(0,1).toUpperCase() + nodeName.substring(1) + " name:");
        TextField textField = new TextField();
        HBox content = new HBox(label, textField);
        content.setSpacing(10);
        content.setPadding(new Insets(10));
        content.setAlignment(Pos.CENTER);
        getDialogPane().setContent(content);
        getDialogPane().getButtonTypes().addAll(javafx.scene.control.ButtonType.OK, javafx.scene.control.ButtonType.CANCEL);
        setResultConverter(dialogButton -> {
            if (dialogButton == javafx.scene.control.ButtonType.OK) {
                String folderName = textField.getText().trim();
                if (!folderName.isEmpty()) {
                    return resultConverter.apply(folderName);
                }
            }
            return null;
        });
        setOnShown(event -> {
            Platform.runLater(() -> {
                textField.requestFocus();
                System.out.println("Shown!");
            });
        });
    }
}
