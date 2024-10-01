package org.maggdadev.forestpixel.canvas.tools.models;

import org.maggdadev.forestpixel.canvas.CanvasContext;
import org.maggdadev.forestpixel.canvas.CanvasModel;

public class PipetModel extends ToolModel {
    @Override
    public void applyToPreview(CanvasModel canvasModel, CanvasContext canvasContext, int xIdx, int yIdx) {
        super.applyToPreview(canvasModel, canvasContext, xIdx, yIdx);
        canvasContext.setColor(canvasModel.getPixelColor(xIdx, yIdx, canvasContext.getActiveFrameId(), canvasContext.getActiveLayerId()));
    }
}
