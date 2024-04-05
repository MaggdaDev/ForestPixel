package org.maggdadev.forestpixel.canvas.tools.models;

import javafx.geometry.Point2D;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import org.maggdadev.forestpixel.canvas.CanvasModel;

public class PencilModel extends ToolModel {

    @Override
    public void applyToCanvas(CanvasModel canvasModel, int xIdx, int yIdx) {
        if (xIdx >= 0 && xIdx < canvasModel.getWidthPixels() && yIdx >= 0 && yIdx < canvasModel.getHeightPixels()) {
            canvasModel.setPixelColor(xIdx, yIdx, Color.BLACK);
        }
    }

    public void applyToCanvas(CanvasModel canvasModel, Iterable<Point2D> points) {
        for(Point2D point: points) {
            applyToCanvas(canvasModel, Math.round((float)point.getX()), Math.round((float)point.getY()));
        }
    }
}
