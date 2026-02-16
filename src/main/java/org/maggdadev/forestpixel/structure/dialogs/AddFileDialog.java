package org.maggdadev.forestpixel.structure.dialogs;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.StringConverter;
import org.maggdadev.forestpixel.structure.ProjectFileModel;
import org.maggdadev.forestpixel.structure.ProjectNodeModel;

import java.util.Objects;

public class AddFileDialog extends AddProjectNodeDialog {

    private final ComboBox<ProjectNodeModel.ProjectNodeType> typeComboBox;
    private final BooleanProperty isWidthInputValid = new SimpleBooleanProperty(false),
            isHeightInputValid = new SimpleBooleanProperty(false);
    public AddFileDialog() {
        super("file");

        typeComboBox = new ComboBox<>(FXCollections.observableArrayList(ProjectNodeModel.ProjectNodeType.SPRITE_FILE));
        typeComboBox.getSelectionModel().selectFirst();
        typeComboBox.setConverter(new StringConverter<ProjectNodeModel.ProjectNodeType>() {
            @Override
            public String toString(ProjectNodeModel.ProjectNodeType projectNodeType) {
                return projectNodeType.extension;
            }

            @Override
            public ProjectNodeModel.ProjectNodeType fromString(String s) {
                return null;
            }
        });
        ((HBox) content.getChildren().getFirst()).getChildren().add(typeComboBox);

        Label sizeLabel = new Label("Size:");
        TextField widthTextField = new TextField();
        widthTextField.setPromptText("Width");
        Label timesLabel = new Label("x");
        TextField heightTextField = new TextField();
        heightTextField.setPromptText("Height");
        HBox sizeBox = new HBox(sizeLabel, widthTextField, timesLabel, heightTextField);
        sizeBox.setSpacing(10);
        sizeBox.setAlignment(Pos.CENTER);
        content.getChildren().addFirst(sizeBox);


        setupResultConverter(name -> {
            if (Objects.requireNonNull(typeComboBox.getValue()) == ProjectNodeModel.ProjectNodeType.SPRITE_FILE) {
                return new ProjectFileModel(name, ProjectNodeModel.ProjectNodeType.SPRITE_FILE, Integer.parseInt(widthTextField.getText()), Integer.parseInt(heightTextField.getText()));
            } else {
                return null;
            }
        });
        bindValidity(widthTextField, isWidthInputValid);
        bindValidity(heightTextField, isHeightInputValid);
        Button okButton = (Button) getDialogPane().lookupButton(ButtonType.OK);
        okButton.disableProperty().unbind();
        okButton.disableProperty().bind(Bindings.createBooleanBinding(() ->
                        !isWidthInputValid.get() || !isHeightInputValid.get() || textField.getText().trim().isEmpty(),
                    textField.textProperty(), isWidthInputValid, isHeightInputValid));



    }

    private void bindValidity(TextField textField, BooleanProperty validityProperty) {
        validityProperty.bind(Bindings.createBooleanBinding(() -> {
            try {
                int value = Integer.parseInt(textField.getText());
                return value > 0;
            } catch (NumberFormatException e) {
                return false;
            }
        }, textField.textProperty()));
        textField.borderProperty().bind(Bindings.createObjectBinding(() -> {
            if (validityProperty.get()) {
                return null;
            } else {
                return new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, new CornerRadii(5), new BorderWidths(2)));
            }
        }, validityProperty));
    }
}
