package org.maggdadev.forestpixel.canvas.tools.models;

import org.maggdadev.forestpixel.canvas.CanvasContext;
import org.maggdadev.forestpixel.canvas.CanvasModel;
import org.maggdadev.forestpixel.canvas.PreviewImage;

public abstract class ToolModel {

    public void applyToPreview (CanvasModel canvasModel, CanvasContext canvasContext, int xIdx, int yIdx) {
        createPreviewImageIfNull(canvasModel, canvasContext);
    }

    public void applyToCanvas(CanvasModel canvasModel, CanvasContext canvasContext, int xIdx, int yIdx) {
        canvasModel.applyPreviewImage(canvasContext.getPreviewImageAndDelete());
    }

    protected void createPreviewImageIfNull(CanvasModel canvasModel, CanvasContext canvasContext) {
        if(canvasContext.getPreviewImage() == null) {
            canvasContext.setPreviewImage(new PreviewImage(canvasModel.getWidthPixels(), canvasModel.getHeightPixels(), canvasContext.lineWidthProperty()));
        }
    }
}
