package org.maggdadev.forestpixel.canvas.layers;

import javafx.scene.image.Image;

public class CanvasLayerViewModel {

    private final CanvasLayerModel model;

    public CanvasLayerViewModel(CanvasLayerModel model) {
        this.model = model;
    }

    public Image getDrawableImage() {
        return model.getImage();
    }

    public String getLayerId() {
        return model.getLayerId();
    }
}
