package org.maggdadev.forestpixel.canvas.layers;

import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import org.maggdadev.forestpixel.canvas.CanvasContext;
import org.maggdadev.forestpixel.canvas.CanvasModel;
import org.maggdadev.forestpixel.canvas.utils.SwappableObservableArrayList;

public class LayersViewModels {
    private final SwappableObservableArrayList<LayerViewModel> layers = new SwappableObservableArrayList<>();

    private final ObservableList<LayerViewModel> layersUnmodifiable = FXCollections.unmodifiableObservableList(layers);

    private final IntegerProperty activeLayerOrder = new SimpleIntegerProperty(-1);
    private final StringProperty activeLayerId = new SimpleStringProperty("-1");

    private final CanvasModel canvasModel;

    private final CanvasContext context;

    public LayersViewModels(CanvasModel canvasModel, CanvasContext context) {
        this.canvasModel = canvasModel;
        this.context = context;
        layers.addListener((ListChangeListener<? super LayerViewModel>) (change) -> {
            while (change.next()) {
                if (change.wasAdded() || change.wasRemoved()) {
                    refreshBindings();
                    if (activeLayerOrder.get() == -1 && !layers.isEmpty()) {
                        layers.getFirst().setActive(true);
                    }
                }
            }
        });
        canvasModel.forEachLayer(this::addExistingLayer);
    }

    private void refreshBindings() {
        activeLayerId.unbind();
        activeLayerOrder.unbind();
        BooleanProperty[] layersSelectedProperties = new BooleanProperty[layers.size()];
        for (int i = 0; i < layers.size(); i++) {
            layersSelectedProperties[i] = layers.get(i).activeProperty();
        }
        activeLayerId.bind(Bindings.createStringBinding(() -> {
            for (LayerViewModel layer : layers) {
                if (layer.isActive()) {
                    return layer.getId();
                }
            }
            return "-1";
        }, layersSelectedProperties));
        activeLayerOrder.bind(Bindings.createIntegerBinding(() -> {
            for (int i = 0; i < layers.size(); i++) {
                if (layers.get(i).getId().equals(activeLayerId.get())) {
                    return i;
                }
            }
            return -1;
        }, activeLayerId));

    }

    /**
     * Creates new LayerModel, adds it to canvas model, and adds a new CanvasLayerViewModel corresponding to it
     */
    public void addNewLayer() {
        addExistingLayer(canvasModel.addLayer());
    }

    public void addExistingLayer(LayerModel existingModel) {
        LayerViewModel newLayer = new LayerViewModel(existingModel);
        newLayer.createBindings(context, Bindings.createIntegerBinding(() -> layers.indexOf(newLayer), layers));
        layers.add(newLayer);
    }

    public void swap(String id1, String id2) {
        layers.swap(indexOf(id1), indexOf(id2));
    }

    public int indexOf(String id) {
        return layers.indexOf(getById(id));
    }

    public LayerViewModel getById(String id) {
        for (LayerViewModel layer : layers) {
            if (layer.getId().equals(id)) {
                return layer;
            }
        }
        return null;
    }

    public ObservableList<LayerViewModel> getLayersUnmodifiable() {
        return layersUnmodifiable;
    }

    public int getActiveLayerOrder() {
        return activeLayerOrder.get();
    }

    public ReadOnlyIntegerProperty activeLayerOrderProperty() {
        return activeLayerOrder;
    }

    public String getActiveLayerId() {
        return activeLayerId.get();
    }

    public ReadOnlyStringProperty activeLayerIdProperty() {
        return activeLayerId;
    }

    public void setActiveLayerId(String activeLayerId) {
        getById(activeLayerId).setActive(true);
    }

    public void remove(String id) {
        if (getById(id).isActive()) {
            getById(id).setActive(false);
        }
        layers.remove(getById(id));
        canvasModel.removeLayer(id);
    }
}
