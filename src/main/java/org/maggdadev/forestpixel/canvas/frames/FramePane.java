package org.maggdadev.forestpixel.canvas.frames;

import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import org.maggdadev.forestpixel.canvas.CanvasViewModel;
import org.maggdadev.forestpixel.canvas.layers.LayersStackPane;

public class FramePane extends StackPane {
    private final FramesViewModels framesViewModels;

    private final CanvasViewModel canvasViewModel;

    public FramePane(CanvasViewModel canvasViewModel) {
        this.framesViewModels = canvasViewModel.getFramesViewModels();
        this.canvasViewModel = canvasViewModel;
        setMouseTransparent(true);
        framesViewModels.getFrames().getUnmodifiable().forEach(this::addLayersStackPane);

        framesViewModels.getFrames().addOnElementAdded(this::addLayersStackPane);
        framesViewModels.getFrames().addOnElementRemoved(frame -> getChildren().removeIf(node -> {
            if (node instanceof LayersStackPane layersStackPane) {
                return layersStackPane.getFrameId().equals(frame.getId());
            }
            return false;
        }));
    }

    public LayersStackPane getActiveLayersStackPane() {//
        return (LayersStackPane) getChildren().stream().filter((Node node) -> ((LayersStackPane) node).isVisible()).findFirst().orElse(null);
    }

    private void addLayersStackPane(FrameViewModel frameViewModel) {
        LayersStackPane layersStackPane = new LayersStackPane(canvasViewModel, frameViewModel.getLayersViewModels(), frameViewModel.getId());
        layersStackPane.visibleProperty().bind(framesViewModels.activeFrameIdProperty().isEqualTo(frameViewModel.getId()));
        getChildren().add(layersStackPane);
    }
}
