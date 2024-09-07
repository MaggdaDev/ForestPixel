package org.maggdadev.forestpixel.canvas.tools.models;

import javafx.scene.paint.Color;
import org.maggdadev.forestpixel.canvas.CanvasContext;
import org.maggdadev.forestpixel.canvas.CanvasModel;
import org.maggdadev.forestpixel.canvas.utils.PixelUtils;
import org.maggdadev.forestpixel.canvas.utils.Point;

import java.util.function.BiFunction;

public class FreeHandDrawingToolModel extends ToolModel {
    private final BiFunction<CanvasModel, CanvasContext, Color> determinePixelColorFunction;
    public FreeHandDrawingToolModel(BiFunction<CanvasModel, CanvasContext, Color> determinePixelColorFunction) {
        this.determinePixelColorFunction = determinePixelColorFunction;
    }

    public void applyToPreview(CanvasModel canvasModel, CanvasContext canvasContext, int startX, int startY, int endX, int endY) {
        super.applyToPreview(canvasModel, canvasContext, endX, endY);
        Iterable<Point> points = PixelUtils.straightLineFromTo(startX, startY, endX, endY, canvasContext.getLineWidth());
        canvasContext.getPreviewImage().setColor(points, determinePixelColorFunction.apply(canvasModel, canvasContext));
    }
}
