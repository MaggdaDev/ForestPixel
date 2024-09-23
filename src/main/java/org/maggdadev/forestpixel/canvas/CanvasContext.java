package org.maggdadev.forestpixel.canvas;

import javafx.beans.property.*;
import javafx.scene.paint.Color;

public class CanvasContext {
    private final ObjectProperty<Color> color = new SimpleObjectProperty<>();
    private final IntegerProperty lineWidth = new SimpleIntegerProperty(10);

    private final ObjectProperty<PreviewImage> previewImage;

    private final ObjectProperty<CanvasState> state = new SimpleObjectProperty<>(CanvasState.IDLE);

    private final DoubleProperty zoomFactor;

    private final IntegerProperty previewOffsetX = new SimpleIntegerProperty(0), previewOffsetY = new SimpleIntegerProperty(0);

    private final BooleanProperty mouseInSelectArea = new SimpleBooleanProperty(false);

    private final StringProperty activeLayerId = new SimpleStringProperty("-1");

    /**
     * @param previewImage can be Null!
     */
    CanvasContext(ObjectProperty<PreviewImage> previewImage, DoubleProperty zoomFactor) {
        this.previewImage = previewImage;
        this.zoomFactor = zoomFactor;

    }

    public Color getColor() {
        return color.get();
    }

    public ObjectProperty<Color> colorProperty() {
        return color;
    }

    public PreviewImage getPreviewImage() {
        return previewImage.get();
    }

    public ObjectProperty<PreviewImage> previewImageProperty() {
        return previewImage;
    }

    public void setPreviewImage(PreviewImage previewImage) {
        this.previewImage.set(previewImage);
        previewImage.xOffsetProperty().bindBidirectional(previewOffsetX);
        previewImage.yOffsetProperty().bindBidirectional(previewOffsetY);
    }

    public PreviewImage getPreviewImageAndDelete() {
        PreviewImage ret = previewImage.get();
        previewImage.set(null);
        return ret;
    }

    public void setColor(Color color) {
        this.color.set(color);
    }

    public CanvasState getState() {
        return state.get();
    }

    public ObjectProperty<CanvasState> stateProperty() {
        return state;
    }

    public void setState(CanvasState state) {
        this.state.set(state);
    }

    public double getZoomFactor() {
        return zoomFactor.get();
    }

    public DoubleProperty zoomFactorProperty() {
        return zoomFactor;
    }

    public int getPreviewOffsetX() {
        return previewOffsetX.get();
    }

    public IntegerProperty previewOffsetXProperty() {
        return previewOffsetX;
    }

    public int getPreviewOffsetY() {
        return previewOffsetY.get();
    }

    public IntegerProperty previewOffsetYProperty() {
        return previewOffsetY;
    }

    public void setPreviewOffsetX(int previewOffsetX) {
        this.previewOffsetX.set(previewOffsetX);
    }

    public void setPreviewOffsetY(int previewOffsetY) {
        this.previewOffsetY.set(previewOffsetY);
    }

    public int getLineWidth() {
        return lineWidth.get();
    }

    public IntegerProperty lineWidthProperty() {
        return lineWidth;
    }

    public BooleanProperty mouseInSelectAreaProperty() {
        return mouseInSelectArea;
    }

    public void setMouseInSelectArea(boolean mouseInSelectArea) {
        this.mouseInSelectArea.set(mouseInSelectArea);
    }

    public boolean isMouseInSelectArea() {
        return mouseInSelectArea.get();
    }

    public String getActiveLayerId() {
        return activeLayerId.get();
    }

    public StringProperty activeLayerIdProperty() {
        return activeLayerId;
    }

    public void setActiveLayerId(String activeLayerId) {
        this.activeLayerId.set(activeLayerId);
    }

    public void moveCanvasBy(int moveX, int moveY) {


    }

}
