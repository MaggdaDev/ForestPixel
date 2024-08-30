package org.maggdadev.forestpixel.canvas.tools.viewmodels;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import org.maggdadev.forestpixel.canvas.events.CanvasMouseEvent;
import org.maggdadev.forestpixel.canvas.tools.ToolType;
import org.maggdadev.forestpixel.canvas.tools.models.SelectModel;

public class SelectViewModel extends ToolViewModel {
    private final SelectModel model;
    private final DoubleProperty gestureStartX = new SimpleDoubleProperty(0),
            gestureStartY = new SimpleDoubleProperty(0),
            width = new SimpleDoubleProperty(0),
            height = new SimpleDoubleProperty(0),
            gestureEndX = new SimpleDoubleProperty(0),
            gestureEndY = new SimpleDoubleProperty(0),
            areaStartX = new SimpleDoubleProperty(0),
            areaStartY = new SimpleDoubleProperty(0);

    private final BooleanProperty mouseAreaIndicatorActive = new SimpleBooleanProperty(false);

    public SelectViewModel(SelectModel model) {
        super(ToolType.SELECT);
        this.model = model;
        width.bind(Bindings.max(gestureEndX.subtract(gestureStartX), gestureStartX.subtract(gestureEndX)));
        height.bind(Bindings.max(gestureEndY.subtract(gestureStartY), gestureStartY.subtract(gestureEndY)));
        areaStartX.bind(Bindings.min(gestureStartX, gestureEndX));
        areaStartY.bind(Bindings.min(gestureStartY, gestureEndY));
    }

    @Override
    protected void onPrimaryButtonPressed(CanvasMouseEvent e) {
        super.onPrimaryButtonPressed(e);
        mouseAreaIndicatorActive.set(true);
        gestureStartX.set(e.pixelXPos());
        gestureStartY.set(e.pixelYPos());
        System.out.println("SelectViewModel.onPrimaryButtonPressed " + e.pixelXPos());
    }

    @Override
    protected void onPrimaryButtonReleased(CanvasMouseEvent e) {
        super.onPrimaryButtonReleased(e);
        mouseAreaIndicatorActive.set(false);
    }

    @Override
    protected void onPrimaryButtonDragged(CanvasMouseEvent e) {
        super.onPrimaryButtonDragged(e);
        gestureEndX.set(e.pixelXPos());
        gestureEndY.set(e.pixelYPos());
    }

    public double getGestureStartX() {
        return gestureStartX.get();
    }

    public DoubleProperty gestureStartXProperty() {
        return gestureStartX;
    }

    public double getGestureStartY() {
        return gestureStartY.get();
    }

    public DoubleProperty gestureStartYProperty() {
        return gestureStartY;
    }

    public double getWidth() {
        return width.get();
    }

    public DoubleProperty widthProperty() {
        return width;
    }

    public double getHeight() {
        return height.get();
    }

    public DoubleProperty heightProperty() {
        return height;
    }

    public boolean isMouseAreaIndicatorActive() {
        return mouseAreaIndicatorActive.get();
    }

    public BooleanProperty mouseAreaIndicatorActiveProperty() {
        return mouseAreaIndicatorActive;
    }

    public double getGestureEndX() {
        return gestureEndX.get();
    }

    public DoubleProperty gestureEndXProperty() {
        return gestureEndX;
    }

    public double getGestureEndY() {
        return gestureEndY.get();
    }

    public DoubleProperty gestureEndYProperty() {
        return gestureEndY;
    }

    public double getAreaStartX() {
        return areaStartX.get();
    }

    public DoubleProperty areaStartXProperty() {
        return areaStartX;
    }

    public double getAreaStartY() {
        return areaStartY.get();
    }

    public DoubleProperty areaStartYProperty() {
        return areaStartY;
    }
}
