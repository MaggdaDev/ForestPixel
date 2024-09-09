package org.maggdadev.forestpixel.canvas.layers;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

public class LayersStackPane extends StackPane {
    private final ObservableMap<String, CanvasLayerView> layers = FXCollections.observableHashMap();

    public LayersStackPane() {
        setMouseTransparent(true);
        getChildren().addListener((ListChangeListener.Change<? extends Node> change) -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    change.getAddedSubList().forEach(node -> {
                        if (node instanceof CanvasLayerView canvasLayerView) {
                            layers.put(canvasLayerView.getLayerId(), canvasLayerView);
                        } else {
                            throw new RuntimeException("Only CanvasLayerView instances are allowed in LayersStackPane");
                        }
                    });
                }
                if (change.wasRemoved()) {
                    for (Node node : change.getRemoved()) {
                        if (node instanceof CanvasLayerView canvasLayerView) {
                            layers.remove(canvasLayerView.getLayerId());
                        } else {
                            throw new RuntimeException("Only CanvasLayerView instances are allowed in LayersStackPane");
                        }
                    }
                }
            }
        });
        layers.addListener((MapChangeListener<? super String, ? super CanvasLayerView>) (change) -> {
            if (change.wasAdded()) {
                if (!getChildren().contains(change.getValueAdded())) {
                    getChildren().add(change.getValueAdded());
                } else {
                    throw new RuntimeException("Layer already added to LayersStackPane");
                }
            }
            if (change.wasRemoved()) {
                if (getChildren().contains(change.getValueRemoved())) {
                    getChildren().remove(change.getValueRemoved());
                } else {
                    throw new RuntimeException("Removed layer not found in LayersStackPane");
                }
            }

        });
    }

    public ObservableMap<String, CanvasLayerView> getLayers() {
        return layers;
    }

    public void add(CanvasLayerView layer) {
        layers.put(layer.getLayerId(), layer);
    }

    public void remove(String layerId) {
        layers.remove(layerId);
    }
}
