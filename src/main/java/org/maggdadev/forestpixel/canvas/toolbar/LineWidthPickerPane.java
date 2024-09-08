package org.maggdadev.forestpixel.canvas.toolbar;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.util.converter.NumberStringConverter;

import java.text.NumberFormat;

public class LineWidthPickerPane extends HBox {
    private final TextField lineWidthTextField = new TextField();
    private final IntegerProperty lineWidth = new SimpleIntegerProperty(1);

    LineWidthPickerPane() {
        lineWidthTextField.textProperty().bindBidirectional(lineWidth, new NumberStringConverter(NumberFormat.getNumberInstance()));
        // add a formatter to the text field to only allow positive integers
        lineWidthTextField.setTextFormatter(new javafx.scene.control.TextFormatter<>(change -> {
            if (change.getControlNewText().length() > 2) {
                return null;
            }
            if (!change.getControlNewText().matches("\\d*")) {
                return null;
            }
            return change;
        }));

        // Make line width text field fit 2 characters, but grow if needed
        lineWidthTextField.setPrefColumnCount(2);

        Label label = new Label("px");

        setAlignment(javafx.geometry.Pos.CENTER);
        setSpacing(5);
        getChildren().addAll(lineWidthTextField, label);

        lineWidthTextField.setOnAction(event -> {
            getParent().requestFocus();
        });
        lineWidthTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                if (lineWidth.get() < 1) {
                    lineWidth.set(1);
                }
            }
        });
    }

    public IntegerProperty lineWidthProperty() {
        return lineWidth;
    }
}
