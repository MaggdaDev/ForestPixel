package org.maggdadev.forestpixel.canvas.layers;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.Image;

public class CanvasLayerViewModel {

    private final CanvasLayerModel model;
    private final IntegerProperty order = new SimpleIntegerProperty(0);

    public CanvasLayerViewModel(CanvasLayerModel model, IntegerProperty orderProperty) {
        this.model = model;
        this.order.bind(orderProperty);
        this.order.addListener((observable, oldValue, newValue) -> {
            model.setOrder(newValue.intValue());
        });
    }

    public Image getDrawableImage() {
        return model.getImage();
    }

    public String getLayerId() {
        return model.getLayerId();
    }

    public IntegerProperty orderProperty() {
        return order;
    }

}
