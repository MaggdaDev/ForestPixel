package org.maggdadev.forestpixel.canvas.tools.models;

import org.maggdadev.forestpixel.canvas.CanvasContext;
import org.maggdadev.forestpixel.canvas.CanvasModel;
import org.maggdadev.forestpixel.canvas.utils.PixelUtils;
import org.maggdadev.forestpixel.canvas.utils.Point;

public class FreeHandDrawingToolModel extends ToolModel {
    private final boolean isEraser;

    public FreeHandDrawingToolModel(boolean isEraser) {
        this.isEraser = isEraser;
    }

    public void applyToPreview(CanvasModel canvasModel, CanvasContext canvasContext, int startX, int startY, int endX, int endY) {
        super.applyToPreview(canvasModel, canvasContext, endX, endY);
        Iterable<Point> points = PixelUtils.straightLineFromTo(startX, startY, endX, endY, canvasContext.getLineWidth());
        if (isEraser) {
            canvasContext.getPreviewImage().deleteColors(points);
        } else {
            canvasContext.getPreviewImage().setColor(points, canvasContext.getColor());
        }
    }
}
