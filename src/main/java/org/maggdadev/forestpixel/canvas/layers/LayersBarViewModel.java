package org.maggdadev.forestpixel.canvas.layers;

import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;

public class LayersBarViewModel {
    private final BooleanProperty isExpanded = new SimpleBooleanProperty(true);
    private final LayersViewModels layersViewModels;

    private final StringProperty activeLayerProperty = new SimpleStringProperty("-1");

    private final DoubleProperty upperLayersOpacity = new SimpleDoubleProperty(1.0), lowerLayersOpacity = new SimpleDoubleProperty(1.0);

    private final BooleanProperty moreThanOneLayer = new SimpleBooleanProperty(false);

    public LayersBarViewModel(LayersViewModels canvasLayerViewModels) {
        this.layersViewModels = canvasLayerViewModels;
        moreThanOneLayer.bind(Bindings.createIntegerBinding(() -> layersViewModels.getLayersUnmodifiable().size(), layersViewModels.getLayersUnmodifiable()).greaterThan(1));
    }
    public void toggleExpanded() {
        isExpanded.set(!isExpanded.get());
    }

    // GET/SET
    public BooleanProperty isExpandedProperty() {
        return isExpanded;
    }

    public LayersViewModels getLayersViewModels() {
        return layersViewModels;
    }

    public ObservableValue<String> activeLayerIdProperty() {
        return activeLayerProperty;
    }

    public void setActiveLayer(String id) {
        activeLayerProperty.set(id);
    }

    public void swapLayers(String id1, String id2) {
        layersViewModels.swap(id1, id2);
    }

    public double getUpperLayersOpacity() {
        return upperLayersOpacity.get();
    }

    public DoubleProperty upperLayersOpacityProperty() {
        return upperLayersOpacity;
    }

    public double getLowerLayersOpacity() {
        return lowerLayersOpacity.get();
    }

    public DoubleProperty lowerLayersOpacityProperty() {
        return lowerLayersOpacity;
    }

    public boolean isMoreThanOneLayer() {
        return moreThanOneLayer.get();
    }

    public BooleanProperty moreThanOneLayerProperty() {
        return moreThanOneLayer;
    }
}
