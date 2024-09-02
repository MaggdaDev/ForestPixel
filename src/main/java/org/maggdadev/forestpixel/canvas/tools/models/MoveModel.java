package org.maggdadev.forestpixel.canvas.tools.models;

import org.maggdadev.forestpixel.canvas.CanvasContext;
import org.maggdadev.forestpixel.canvas.CanvasModel;

public class MoveModel extends ToolModel {
    private double moveStartX, moveStartY, moveEndX, moveEndY;

    private boolean isDirty = false;

    public MoveModel() {
        reset();
    }
    @Override
    public void applyToPreview(CanvasModel canvasModel, CanvasContext canvasContext, int xIdx, int yIdx) {
        super.applyToPreview(canvasModel, canvasContext, xIdx, yIdx);
        isDirty = false;
        canvasContext.getPreviewImage().setXOffset(Math.round ((float)(moveEndX - moveStartX)));
        canvasContext.getPreviewImage().setYOffset(Math.round ((float)(moveEndY - moveStartY)));
    }

    public boolean isDirty() {
        return isDirty;
    }

    public void reset() {
        moveStartX = -1;
        moveStartY = -1;
        moveEndX = -1;
        moveEndY = -1;
    }

    public void setStartMove(double x, double y) {
        moveStartX = x;
        moveStartY = y;
        isDirty = true;
    }

    public void setEndMove(double x, double y) {
        if(Math.round(x - moveStartX) != Math.round( moveEndX - moveStartX) || Math.round(y - moveStartY) != Math.round(moveEndY - moveStartY) ) {
            isDirty = true;
        }
        moveEndX = x;
        moveEndY = y;
    }
}
