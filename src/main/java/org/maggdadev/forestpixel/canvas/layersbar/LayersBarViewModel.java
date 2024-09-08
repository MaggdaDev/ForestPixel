package org.maggdadev.forestpixel.canvas.layersbar;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class LayersBarViewModel {
    private final BooleanProperty isExpanded = new SimpleBooleanProperty(true);

    public void toggleExpanded() {
        isExpanded.set(!isExpanded.get());
    }

    public boolean isIsExpanded() {
        return isExpanded.get();
    }

    public BooleanProperty isExpandedProperty() {
        return isExpanded;
    }
}
