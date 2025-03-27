package org.maggdadev.forestpixel.canvas.layers;

import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.collections.ObservableList;
import org.maggdadev.forestpixel.canvas.CanvasContext;
import org.maggdadev.forestpixel.canvas.frames.FrameModel;
import org.maggdadev.forestpixel.canvas.utils.SwappableObservableArrayList;

import java.util.Collections;
import java.util.function.Consumer;

public class LayersViewModels {
    private final SwappableObservableArrayList<LayerViewModel> layers = new SwappableObservableArrayList<>();

    private final ObservableList<LayerViewModel> layersUnmodifiable = layers.getUnmodifiable();

    private final IntegerProperty activeLayerOrder = new SimpleIntegerProperty(-1);
    private final StringProperty activeLayerId = new SimpleStringProperty("-1");

    private final FrameModel frameModel;

    private final CanvasContext context;

    public LayersViewModels(FrameModel frameModel, CanvasContext context) {
        this.frameModel = frameModel;
        this.context = context;


        Consumer<LayerViewModel> selectFirstIfNoneSelectedAndRefreshBindings = (a) -> {
            if (activeLayerOrder.get() == -1 && !layers.isEmpty()) {
                layers.getFirst().setSelected(true);
            }
            refreshBindings();
        };
        layers.addOnElementAdded((layer) -> {
            selectFirstIfNoneSelectedAndRefreshBindings.accept(layer);
            layer.createBindings(context, Bindings.createIntegerBinding(() -> layers.indexOf(layer), layers.getUnmodifiable()));
        });
        layers.addOnElementRemoved((layer) -> {
            selectFirstIfNoneSelectedAndRefreshBindings.accept(layer);
            cleanup(layer.getId());
        });

        // Load from model
        frameModel.getLayers().forEach(this::addExistingLayer);

        layers.addOnElementAdded((layer) -> {
            frameModel.addExistingLayer(layer.getModel());
        });
        layers.addOnElementRemoved((layer) -> {
            frameModel.removeLayer(layer.getId());
        });
        layers.addOnSwap((swap) -> {
            Collections.swap(frameModel.getLayers(), swap.index1(), swap.index2());
        });
    }

    private void refreshBindings() {
        activeLayerId.unbind();
        activeLayerOrder.unbind();
        BooleanProperty[] layersSelectedProperties = new BooleanProperty[layers.size()];
        for (int i = 0; i < layers.size(); i++) {
            layersSelectedProperties[i] = layers.get(i).selectedProperty();
        }
        activeLayerId.bind(Bindings.createStringBinding(() -> {
            for (LayerViewModel layer : layers.getUnmodifiable()) {
                if (layer.getSelected()) {
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
        layers.add(new LayerViewModel(new LayerModel(frameModel.getWidthPixels(), frameModel.getHeightPixels())));
    }

    public void addExistingLayer(LayerModel existingModel) {
        LayerViewModel newLayer = new LayerViewModel(existingModel);
        layers.add(newLayer);
    }

    public void swap(String id1, String id2) {
        layers.swap(indexOf(id1), indexOf(id2));
    }

    public int indexOf(String id) {
        return layers.indexOf(getById(id));
    }

    public LayerViewModel getById(String id) {
        for (LayerViewModel layer : layers.getUnmodifiable()) {
            if (layer.getId().equals(id)) {
                return layer;
            }
        }
        return null;
    }

    public ObservableList<LayerViewModel> getLayersUnmodifiable() {
        return layersUnmodifiable;
    }

    public SwappableObservableArrayList<LayerViewModel> getLayers() {
        return layers;
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
        getById(activeLayerId).setSelected(true);
    }

    public void remove(String id) {
        layers.remove(getById(id));
    }

    private void cleanup(String id) {
        if (getById(id) == null) {
            return;
        }
        if (getById(id).getSelected()) {
            getById(id).setSelected(false);
        }
        ;
        frameModel.removeLayer(id);
    }
}
