package org.maggdadev.forestpixel.canvas.tools.models;

import javafx.scene.image.WritableImage;
import org.maggdadev.forestpixel.canvas.CanvasContext;
import org.maggdadev.forestpixel.canvas.CanvasModel;
import org.maggdadev.forestpixel.canvas.events.PrimaryButtonEvent;

public abstract class ToolModel {

    public void applyToPreview (CanvasModel canvasModel, CanvasContext canvasContext, int xIdx, int yIdx) {
        createPreviewImageIfNull(canvasModel, canvasContext);
    }

    public void applyToCanvas(CanvasModel canvasModel, CanvasContext canvasContext, int xIdx, int yIdx) {
        canvasModel.applyPreviewImage(canvasContext.getPreviewImageAndDelete());
    }

    protected void createPreviewImageIfNull(CanvasModel canvasModel, CanvasContext canvasContext) {
        if(canvasContext.getPreviewImage() == null) {
            canvasContext.setPreviewImage(new WritableImage(canvasModel.getImage().getPixelReader(), canvasModel.getWidthPixels(), canvasModel.getHeightPixels()));
        }
    }
}
