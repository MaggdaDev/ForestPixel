package org.maggdadev.forestpixel.canvas.tools.models;

import javafx.geometry.Point2D;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import org.maggdadev.forestpixel.canvas.CanvasContext;
import org.maggdadev.forestpixel.canvas.CanvasModel;

public class PencilModel extends ToolModel {

    @Override
    public void applyToCanvas(CanvasModel canvasModel, CanvasContext canvasContext, int xIdx, int yIdx) {
        if (xIdx >= 0 && xIdx < canvasModel.getWidthPixels() && yIdx >= 0 && yIdx < canvasModel.getHeightPixels()) {
            canvasModel.setPixelColor(xIdx, yIdx, canvasContext.getColor());
        }
    }

    public void applyToCanvas(CanvasModel canvasModel, CanvasContext canvasContext, Iterable<Point2D> points) {
        for(Point2D point: points) {
            applyToCanvas(canvasModel, canvasContext, (int)((float)point.getX()), (int)((float)point.getY()));
        }
    }
}
