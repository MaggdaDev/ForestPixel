package org.maggdadev.forestpixel.canvas.layersbar;

import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import org.maggdadev.forestpixel.canvas.utils.SwappableObservableArrayList;

public class LayersBarViewModel {
    private final BooleanProperty isExpanded = new SimpleBooleanProperty(true);
    private final SwappableObservableArrayList<LayersBarItemViewModel> layers = new SwappableObservableArrayList<>();

    private final StringProperty activeLayerProperty = new SimpleStringProperty("-1");

    private final DoubleProperty upperLayersOpacity = new SimpleDoubleProperty(1.0), lowerLayersOpacity = new SimpleDoubleProperty(1.0);

    private final BooleanProperty moreThanOneLayer = new SimpleBooleanProperty(false);

    public LayersBarViewModel() {
        moreThanOneLayer.bind(Bindings.createIntegerBinding(layers::size, layers).greaterThan(1));
    }


    public void addLayer() {
        LayersBarItemViewModel newLayer = new LayersBarItemViewModel("New layer", layers);
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

    public ObservableValue<String> activeLayerIdProperty() {
        return activeLayerProperty;
    }

    public void setActiveLayer(String id) {
        activeLayerProperty.set(id);
    }

    public void swapLayers(int index1, int index2) {
        layers.swap(index1, index2);
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
