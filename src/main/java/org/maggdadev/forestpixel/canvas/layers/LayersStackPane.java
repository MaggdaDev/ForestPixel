package org.maggdadev.forestpixel.canvas.layers;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

public class LayersStackPane extends StackPane {
    private final ObservableList<CanvasLayerView> layers = FXCollections.observableArrayList();

    public LayersStackPane() {
        setMouseTransparent(true);
        getChildren().addListener((ListChangeListener.Change<? extends Node> change) -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    change.getAddedSubList().forEach(node -> {
                        if (node instanceof CanvasLayerView) {
                            layers.add((CanvasLayerView) node);
                        } else {
                            throw new IllegalArgumentException("Only CanvasLayerView instances are allowed in LayersStackPane");
                        }
                    });
                }
                if (change.wasRemoved()) {
                    layers.removeAll(change.getRemoved());
                }
            }
        });
        layers.addListener((ListChangeListener.Change<? extends CanvasLayerView> change) -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    change.getAddedSubList().forEach(layer -> {
                        if (!getChildren().contains(layer)) {
                            getChildren().add(layer);
                        }
                    });
                }
                if (change.wasRemoved()) {
                    getChildren().removeAll(change.getRemoved());
                }
            }
        });
    }

    public ObservableList<CanvasLayerView> getLayers() {
        return layers;
    }

    public void add(CanvasLayerView layer) {
        layers.add(layer);
    }

    public void remove(CanvasLayerView layer) {
        layers.remove(layer);
    }
}
