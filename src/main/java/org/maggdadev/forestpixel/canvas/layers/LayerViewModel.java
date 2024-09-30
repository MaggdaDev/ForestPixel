package org.maggdadev.forestpixel.canvas.layers;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.*;
import javafx.scene.image.Image;
import org.maggdadev.forestpixel.canvas.CanvasContext;
import org.maggdadev.forestpixel.canvas.utils.Selectable;

public class LayerViewModel implements Selectable {

    private final LayerModel model;
    private final IntegerProperty order = new SimpleIntegerProperty(0);
    private final StringProperty name = new SimpleStringProperty("new layer");
    private final DoubleProperty opacity = new SimpleDoubleProperty(1.0);
    private final BooleanProperty selected = new SimpleBooleanProperty(false);

    public LayerViewModel(LayerModel model) {
        this.model = model;
    }

    public void createBindings(CanvasContext canvasContext, IntegerBinding orderBinding) {
        order.bind(orderBinding);
        order.addListener((observable, oldValue, newValue) -> {
            model.setOrder(newValue.intValue());
        });
        opacity.bind(Bindings.createDoubleBinding(() -> {
            if (order.get() > canvasContext.getActiveLayerOrder()) {
                return canvasContext.getUpperLayersOpacity();
            } else if (order.get() < canvasContext.getActiveLayerOrder()) {
                return canvasContext.getLowerLayersOpacity();
            } else {
                return 1.0;
            }
        }, order, canvasContext.activeLayerOrderProperty(), canvasContext.upperLayersOpacityProperty(), canvasContext.lowerLayersOpacityProperty()));
    }


    public Image getDrawableImage() {
        return model.getImage();
    }

    @Override
    public String getId() {
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

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public boolean getSelected() {
        return selected.get();
    }

    @Override
    public BooleanProperty selectedProperty() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected.set(selected);
    }

    public int getOrder() {
        return order.get();
    }


}
