package org.maggdadev.forestpixel.canvas.toolbar;

import javafx.beans.property.ObjectProperty;
import javafx.scene.control.ColorPicker;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import org.maggdadev.forestpixel.canvas.tools.views.ToolView;

public class ColorPickerPane extends HBox {
    private final ColorPicker colorPicker;

    ColorPickerPane() {
        colorPicker = new ColorPicker(Color.BLACK);
        colorPicker.setVisible(false);
        colorPicker.getStyleClass().add("button");

        Rectangle currentColorRectangle = new Rectangle(2 * ToolView.BUTTON_SIZE, 2 * ToolView.BUTTON_SIZE);
        currentColorRectangle.setStroke(Color.GREY);
        currentColorRectangle.setStrokeType(StrokeType.INSIDE);
        currentColorRectangle.setStrokeWidth(1);
        currentColorRectangle.fillProperty().bind(colorProperty());
        currentColorRectangle.setOnMouseClicked((MouseEvent e) -> {
            colorPicker.show();
        });

        currentColorRectangle.setOnMouseEntered((e) -> {
            setEffect(new DropShadow(4, Color.BLACK));
        });
        currentColorRectangle.setOnMouseExited((e) -> {
            setEffect(null);
        });

        colorPicker.setManaged(false);
        colorPicker.setLayoutY(2 * ToolView.BUTTON_SIZE);
        getChildren().addAll(currentColorRectangle, colorPicker);


    }

    ObjectProperty<Color> colorProperty() {
        return colorPicker.valueProperty();
    }


}
