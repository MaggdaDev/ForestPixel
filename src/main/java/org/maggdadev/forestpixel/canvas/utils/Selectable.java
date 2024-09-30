package org.maggdadev.forestpixel.canvas.utils;

import javafx.beans.property.BooleanProperty;

public interface Selectable {
    String getId();

    BooleanProperty selectedProperty();
}
