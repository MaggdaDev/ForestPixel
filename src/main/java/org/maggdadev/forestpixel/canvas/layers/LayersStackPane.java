package org.maggdadev.forestpixel.canvas.layers;

import javafx.collections.ListChangeListener;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import org.maggdadev.forestpixel.canvas.CanvasViewModel;

public class LayersStackPane extends Pane {
    private final String frameId;

    public LayersStackPane(CanvasViewModel canvasViewModel, LayersViewModels layersViewModels, String frameId) {
        this.frameId = frameId;
        setBorder(new Border(new BorderStroke(Color.GREY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        setMouseTransparent(true);
        layersViewModels.getLayersUnmodifiable().forEach(layerViewModel -> {
            LayerView layerView = new LayerView(layerViewModel, canvasViewModel);
            getChildren().add(layerView);
        });
        layersViewModels.getLayersUnmodifiable().addListener((ListChangeListener<? super LayerViewModel>) (change) -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    for (LayerViewModel layerViewModel : change.getAddedSubList()) {
                        LayerView layerView = new LayerView(layerViewModel, canvasViewModel);
                        getChildren().add(layerView);
                    }
                }
                if (change.wasRemoved()) {
                    for (LayerViewModel layerViewModel : change.getRemoved()) {
                        getChildren().removeIf(node -> {
                            if (node instanceof LayerView layerView) {
                                return layerView.getLayerId().equals(layerViewModel.getId());
                            }
                            return false;
                        });
                    }
                }
            }

        });
    }

    public String getFrameId() {
        return frameId;
    }
}
