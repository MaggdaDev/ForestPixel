package org.maggdadev.forestpixel.canvas.layersbar;

import javafx.beans.property.DoubleProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

public class LayersOpacityBox extends VBox {
    private final Slider opacitySlider = new Slider(0, 1, 1);

    public LayersOpacityBox(String text) {
        setAlignment(Pos.CENTER);
        setPadding(new Insets(5));

        Label opacityLabel = new Label(text);

        opacitySlider.setShowTickLabels(true);
        opacitySlider.setShowTickMarks(true);
        opacitySlider.setMajorTickUnit(50);
        opacitySlider.setMinorTickCount(5);
        opacitySlider.setBlockIncrement(10);
        opacitySlider.setLabelFormatter(new StringConverter<Double>() {
            @Override
            public String toString(Double object) {
                return String.format("%.0f%%", object * 100);
            }

            @Override
            public Double fromString(String string) {
                return null;
            }
        });

        getChildren().addAll(opacityLabel, opacitySlider);
    }

    public DoubleProperty opacityValueProperty() {
        return opacitySlider.valueProperty();
    }
}
