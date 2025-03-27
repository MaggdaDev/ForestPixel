package org.maggdadev.forestpixel.canvas.layers;

import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import org.maggdadev.forestpixel.canvas.frames.FrameViewModel;
import org.maggdadev.forestpixel.canvas.frames.FramesViewModels;

public class LayersBarViewModel {
    private final StringProperty activeLayerProperty = new SimpleStringProperty("-1");

    private final DoubleProperty upperLayersOpacity = new SimpleDoubleProperty(1.0), lowerLayersOpacity = new SimpleDoubleProperty(1.0);

    private final BooleanProperty moreThanOneLayer = new SimpleBooleanProperty(false);

    private final StringProperty activeFrameId = new SimpleStringProperty("-1");

    private final ObjectProperty<LayersViewModels> layersViewModelsOfActiveFrame = new SimpleObjectProperty<>();

    private final ObjectProperty<ObservableList<LayerViewModel>> currentLayersUnmodifiable = new SimpleObjectProperty<>();


    public LayersBarViewModel(FramesViewModels framesViewModels) {
        layersViewModelsOfActiveFrame.subscribe((newValue) -> {
            if (newValue != null) {
                moreThanOneLayer.unbind();
                moreThanOneLayer.bind(Bindings.size(newValue.getLayersUnmodifiable()).greaterThan(1));
            }
        });
        layersViewModelsOfActiveFrame.bind(Bindings.createObjectBinding(() -> { // After change subscription such that it is initially called
            FrameViewModel activeFrame = framesViewModels.getActiveFrameViewModel();
            if (activeFrame != null) {
                return activeFrame.getLayersViewModels();
            }
            return null;
        }, framesViewModels.activeFrameIdProperty()));

        currentLayersUnmodifiable.bind(Bindings.createObjectBinding(() -> {
            if (layersViewModelsOfActiveFrame.get() != null) {
                return layersViewModelsOfActiveFrame.get().getLayers().getUnmodifiable();
            }
            return null;
        }, layersViewModelsOfActiveFrame));
    }

    public void addNewLayer() {
        if (layersViewModelsOfActiveFrame.get() != null) {
            layersViewModelsOfActiveFrame.get().addNewLayer();
        }
    }
    public ObservableValue<String> activeLayerIdProperty() {
        return activeLayerProperty;
    }

    public void setActiveLayer(String id) {
        activeLayerProperty.set(id);
    }

    public void swapLayers(String id1, String id2) {
        if (layersViewModelsOfActiveFrame.get() != null) {
            layersViewModelsOfActiveFrame.get().swap(id1, id2);
        }
    }

    public void removeLayer(String s) {
        if (layersViewModelsOfActiveFrame.get() != null) {
            layersViewModelsOfActiveFrame.get().remove(s);
        }
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

    public Property<String> activeFrameIdProperty() {
        return activeFrameId;
    }

    public ObservableList<LayerViewModel> getCurrentLayersUnmodifiable() {
        return currentLayersUnmodifiable.get();
    }


    public ObjectProperty<ObservableList<LayerViewModel>> currentLayersUnmodifiableProperty() {
        return currentLayersUnmodifiable;
    }
}
