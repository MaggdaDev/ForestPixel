package org.maggdadev.forestpixel.canvas.tools.models;

import org.maggdadev.forestpixel.canvas.CanvasContext;
import org.maggdadev.forestpixel.canvas.CanvasModel;

public class MoveModel extends ToolModel {
    private double moveStartX, moveStartY, moveEndX, moveEndY;
    private double previousSubMovementsX = 0, previousSubMovementsY = 0;

    private boolean isDirty = false;

    @Override
    public void applyToPreview(CanvasModel canvasModel, CanvasContext canvasContext, int xIdx, int yIdx) {
        super.applyToPreview(canvasModel, canvasContext, xIdx, yIdx);
        isDirty = false;
        canvasContext.setPreviewOffsetX(Math.round ((float)(previousSubMovementsX + moveEndX - moveStartX)));
        canvasContext.setPreviewOffsetY(Math.round ((float)(previousSubMovementsY + moveEndY - moveStartY)));
    }

    public void terminateSubMovement() {
        previousSubMovementsX += moveEndX - moveStartX;
        previousSubMovementsY += moveEndY - moveStartY;
        resetStartAndEnd();
    }

    public boolean isDirty() {
        return isDirty;
    }

    public void resetStartAndEnd() {
        moveStartX = -1;
        moveStartY = -1;
        moveEndX = -1;
        moveEndY = -1;
    }

    public void resetAll(CanvasContext context) {
        resetStartAndEnd();
        previousSubMovementsX = 0;
        previousSubMovementsY = 0;
        context.setPreviewOffsetX(0);
        context.setPreviewOffsetY(0);
    }

    public void setStartMove(double x, double y) {
        moveStartX = x;
        moveStartY = y;
        isDirty = true;
    }

    public void setEndMove(double x, double y, double canvasWidth, double canvasHeight) {
        if(Math.round(x - moveStartX) != Math.round( moveEndX - moveStartX) || Math.round(y - moveStartY) != Math.round(moveEndY - moveStartY) ) {
            isDirty = true;
        }
        if(x - moveStartX > 0) {
            moveEndX = Math.min(x, canvasWidth - previousSubMovementsX + moveStartX);
        } else {
            moveEndX = Math.max(x, - canvasWidth + moveStartX -previousSubMovementsX);
        }
        if(y - moveStartY > 0) {
            moveEndY = Math.min(y, canvasHeight - previousSubMovementsY + moveStartY);
        } else {
            moveEndY = Math.max(y, - canvasHeight + moveStartY -previousSubMovementsY);
        }
    }


}
