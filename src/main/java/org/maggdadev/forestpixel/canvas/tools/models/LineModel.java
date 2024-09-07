package org.maggdadev.forestpixel.canvas.tools.models;

import org.maggdadev.forestpixel.canvas.CanvasContext;
import org.maggdadev.forestpixel.canvas.CanvasModel;
import org.maggdadev.forestpixel.canvas.utils.PixelUtils;
import org.maggdadev.forestpixel.canvas.utils.Point;

public class LineModel extends ToolModel {

    private int startX = -1, startY = -1, endX = -1, endY = -1;

    @Override
    public void applyToPreview(CanvasModel canvasModel, CanvasContext canvasContext, int xIdx, int yIdx) {
        super.applyToPreview(canvasModel, canvasContext, xIdx, yIdx);
        if (isInvalid()) {
            return;
        }
        Iterable<Point> linePositions = PixelUtils.straightLineFromTo(startX, startY, endX, endY, canvasContext.getLineWidth());
        canvasContext.getPreviewImage().clear().setColor(linePositions, canvasContext.getColor());
    }

    public void setStartLine(int x, int y) {
        startX = x;
        startY = y;
    }

    public void setEndLine(int x, int y) {
        endX = x;
        endY = y;
    }

    public void resetStartAndEnd() {
        startX = -1;
        startY = -1;
        endX = -1;
        endY = -1;
    }

    private boolean isInvalid() {
        return startX == -1 || startY == -1 || endX == -1 || endY == -1;
    }

    public int getEndX() {
        return endX;
    }

    public int getEndY() {
        return endY;
    }
}
