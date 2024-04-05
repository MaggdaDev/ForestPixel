package org.maggdadev.forestpixel.canvas;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;

public class CanvasContext {
    private final ObjectProperty<Color> color = new SimpleObjectProperty<>();

    CanvasContext(ObjectProperty<Color> colorOrigin) {
        color.bind(colorOrigin);
    }
    public Color getColor() {
        return color.get();
    }

    public ReadOnlyObjectProperty<Color> colorProperty() {
        return color;
    }
}
