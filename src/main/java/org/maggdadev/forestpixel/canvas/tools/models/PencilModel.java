package org.maggdadev.forestpixel.canvas.tools.models;

import javafx.geometry.Point2D;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import org.maggdadev.forestpixel.canvas.CanvasContext;
import org.maggdadev.forestpixel.canvas.CanvasModel;

public class PencilModel extends ToolModel {

    @Override
    public void applyToPreview(CanvasModel canvasModel, CanvasContext canvasContext, int xIdx, int yIdx) {
        super.applyToPreview(canvasModel, canvasContext, xIdx, yIdx);
        if (xIdx >= 0 && xIdx < canvasModel.getWidthPixels() && yIdx >= 0 && yIdx < canvasModel.getHeightPixels()) {
            canvasContext.getPreviewImage().getPixelWriter().setColor(xIdx, yIdx, canvasContext.getColor());
        }
    }

    public void applyToPreview(CanvasModel canvasModel, CanvasContext canvasContext, Iterable<int[]> points) {
        for(int[] point: points) {
            applyToPreview(canvasModel, canvasContext, point[0],point[1]);
        }
    }
}
