package org.maggdadev.forestpixel.canvas.layers;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.Image;

public class CanvasLayerViewModel {

    private final CanvasLayerModel model;
    private final IntegerProperty order = new SimpleIntegerProperty(0);

    private final DoubleProperty opacity = new SimpleDoubleProperty(1.0);

    public CanvasLayerViewModel(CanvasLayerModel model, IntegerProperty orderProperty) {
        this.model = model;
        this.order.bind(orderProperty);
        this.order.addListener((observable, oldValue, newValue) -> {
            model.setOrder(newValue.intValue());
        });
    }

    public void bindOpacity(IntegerProperty activeLayerOrder, DoubleProperty upperLayersOpacity, DoubleProperty lowerLayersOpacity) {
        opacity.bind(Bindings.createDoubleBinding(() -> {
            if (order.get() > activeLayerOrder.get()) {
                return upperLayersOpacity.get();
            } else if (order.get() < activeLayerOrder.get()) {
                return lowerLayersOpacity.get();
            } else {
                return 1.0;
            }
        }, order, activeLayerOrder, upperLayersOpacity, lowerLayersOpacity));
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

    public double getOpacity() {
        return opacity.get();
    }

    public DoubleProperty opacityProperty() {
        return opacity;
    }
}
