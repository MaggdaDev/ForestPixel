package org.maggdadev.forestpixel.canvas.tools.models;

import javafx.scene.paint.Color;
import org.maggdadev.forestpixel.canvas.CanvasContext;
import org.maggdadev.forestpixel.canvas.CanvasModel;

import java.util.function.BiFunction;

public class FreeHandDrawingToolModel extends ToolModel {
    private final BiFunction<CanvasModel, CanvasContext, Color> determinePixelColorFunction;
    public FreeHandDrawingToolModel(BiFunction<CanvasModel, CanvasContext, Color> determinePixelColorFunction) {
        this.determinePixelColorFunction = determinePixelColorFunction;
    }

    @Override
    public void applyToPreview(CanvasModel canvasModel, CanvasContext canvasContext, int xIdx, int yIdx) {
        super.applyToPreview(canvasModel, canvasContext, xIdx, yIdx);
        if (xIdx >= 0 && xIdx < canvasModel.getWidthPixels() && yIdx >= 0 && yIdx < canvasModel.getHeightPixels()) {
            canvasContext.getPreviewImage().setColor(xIdx, yIdx, determinePixelColorFunction.apply(canvasModel, canvasContext));
        }
    }

    public void applyToPreview(CanvasModel canvasModel, CanvasContext canvasContext, Iterable<int[]> points) {
        for(int[] point: points) {
            applyToPreview(canvasModel, canvasContext, point[0],point[1]);
        }
    }
}
