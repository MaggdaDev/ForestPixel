package org.maggdadev.forestpixel.canvas;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class CanvasContext {
    private final ObjectProperty<Color> color = new SimpleObjectProperty<>();
    private final ObjectProperty<WritableImage> previewImage ;

    private final ObjectProperty<CanvasState> state = new SimpleObjectProperty<>(CanvasState.IDLE);

    /**
     *
     * @param colorOrigin
     * @param previewImage can be Null!
     */
    CanvasContext(ObjectProperty<Color> colorOrigin, ObjectProperty<WritableImage> previewImage) {
        color.bindBidirectional(colorOrigin);
        this.previewImage = previewImage;
    }
    public Color getColor() {
        return color.get();
    }

    public ReadOnlyObjectProperty<Color> colorProperty() {
        return color;
    }

    public WritableImage getPreviewImage() {
        return previewImage.get();
    }

    public ObjectProperty<WritableImage> previewImageProperty() {
        return previewImage;
    }

    public void setPreviewImage(WritableImage previewImage) {
        this.previewImage.set(previewImage);
    }

    public WritableImage getPreviewImageAndDelete() {
        WritableImage ret = previewImage.get();
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
}
