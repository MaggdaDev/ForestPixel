package org.maggdadev.forestpixel.structure.dialogs;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.maggdadev.forestpixel.structure.ProjectNodeModel;

import java.util.function.Function;

public abstract class AddProjectNodeDialog extends Dialog<ProjectNodeModel> {
    protected VBox content;
    protected final TextField textField;
    public AddProjectNodeDialog(String nodeName) {
        setTitle("Add " + nodeName);
        setHeaderText("Enter the name of the new " + nodeName + "...");
        Label label = new Label(nodeName.substring(0,1).toUpperCase() + nodeName.substring(1) + " name:");
        textField = new TextField();
        HBox inputBox = new HBox(label, textField);
        content = new VBox(inputBox);
        content.setSpacing(10);
        content.setAlignment(Pos.CENTER);
        inputBox.setSpacing(10);
        inputBox.setPadding(new Insets(10));
        inputBox.setAlignment(Pos.CENTER);
        getDialogPane().setContent(content);
        getDialogPane().getButtonTypes().addAll(javafx.scene.control.ButtonType.OK, javafx.scene.control.ButtonType.CANCEL);
        setOnShown(event -> {
            Platform.runLater(() -> {
                textField.requestFocus();
                System.out.println("Shown!");
            });
        });
       getDialogPane().lookupButton(ButtonType.OK).disableProperty().bind(Bindings.createBooleanBinding(() -> textField.getText().trim().isEmpty(), textField.textProperty()));
    }

    protected void setupResultConverter(Function<String, ProjectNodeModel> resultConverter) {
        setResultConverter(dialogButton -> {
            if (dialogButton == javafx.scene.control.ButtonType.OK) {
                String folderName = textField.getText().trim();
                if (!folderName.isEmpty()) {
                    return resultConverter.apply(folderName);
                }
            }
            return null;
        });
    }
}
