package org.maggdadev.forestpixel.canvas.layersbar;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class LayersBarViewModel {
    private final BooleanProperty isExpanded = new SimpleBooleanProperty(true);
    private final ObservableList<LayersBarItemViewModel> layers = FXCollections.observableArrayList();


    public void addLayer() {
        LayersBarItemViewModel newLayer = new LayersBarItemViewModel("New layer");
        newLayer.setRequestFocusPending(true);
        layers.add(newLayer);
    }


    public void toggleExpanded() {
        isExpanded.set(!isExpanded.get());
    }

    // GET/SET
    public BooleanProperty isExpandedProperty() {
        return isExpanded;
    }

    public ObservableList<LayersBarItemViewModel> getLayers() {
        return layers;
    }
}
