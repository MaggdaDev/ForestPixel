package org.maggdadev.forestpixel.canvas.frames;

import javafx.collections.ListChangeListener;
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
        framesViewModels.getFrames().forEach(this::addLayersStackPane);
        framesViewModels.getFrames().addListener((ListChangeListener<? super FrameViewModel>) (change) -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    change.getAddedSubList().forEach(this::addLayersStackPane);
                }
                if (change.wasRemoved()) {
                    for (FrameViewModel frame : change.getRemoved()) {
                        getChildren().removeIf(node -> {
                            if (node instanceof LayersStackPane layersStackPane) {
                                return layersStackPane.getFrameId().equals(frame.getId());
                            }
                            return false;
                        });
                    }
                }
            }
        });
    }

    public LayersStackPane getActiveLayersStackPane() {
        return (LayersStackPane) getChildren().stream().filter((Node node) -> ((LayersStackPane) node).isVisible()).findFirst().orElse(null);
    }

    private void addLayersStackPane(FrameViewModel frameViewModel) {
        LayersStackPane layersStackPane = new LayersStackPane(canvasViewModel, frameViewModel.getLayersViewModels(), frameViewModel.getId());
        layersStackPane.visibleProperty().bind(framesViewModels.activeFrameIdProperty().isEqualTo(frameViewModel.getId()));
        getChildren().add(layersStackPane);
    }
}
