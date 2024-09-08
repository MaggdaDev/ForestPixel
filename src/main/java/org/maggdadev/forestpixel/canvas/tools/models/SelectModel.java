package org.maggdadev.forestpixel.canvas.tools.models;

import org.maggdadev.forestpixel.canvas.CanvasContext;
import org.maggdadev.forestpixel.canvas.CanvasModel;

public class SelectModel extends ToolModel {
    private int selectionStartIdxX, selectionStartIdxY, selectionEndIdxX, selectionEndIdxY;

    public void resetSelection() {
        selectionStartIdxX = -1;
        selectionStartIdxY = -1;
        selectionEndIdxX = -1;
        selectionEndIdxY = -1;
    }

    @Override
    public void applyToPreview(CanvasModel canvasModel, CanvasContext canvasContext, int xIdx, int yIdx) {
        if(isSelectionInvalid()) {
            return;
        }
        super.applyToPreview(canvasModel, canvasContext, xIdx, yIdx);
        int xStart = Math.min(selectionStartIdxX, selectionEndIdxX);
        int xEnd = Math.max(selectionStartIdxX, selectionEndIdxX);
        int yStart = Math.min(selectionStartIdxY, selectionEndIdxY);
        int yEnd = Math.max(selectionStartIdxY, selectionEndIdxY);
        int width = xEnd - xStart;
        int height = yEnd - yStart;
        canvasContext.getPreviewImage().setPixels(xStart, yStart, width, height, canvasModel.getPixelReaderForLayer(canvasContext.getActiveLayerId()), xStart, yStart);
        canvasModel.eraseAreaForSelection(xStart, yStart, width, height, canvasContext.getActiveLayerId());
    }


    @Override
    public void applyToCanvas(CanvasModel canvasModel, CanvasContext canvasContext, int xIdx, int yIdx) {
        super.applyToCanvas(canvasModel, canvasContext, xIdx, yIdx);
        if (canvasContext.getPreviewImage() != null) {
            canvasModel.applyPreviewImage(canvasContext.getPreviewImageAndDelete(), canvasContext.getActiveLayerId());
        }
    }

    private boolean isSelectionInvalid() {
        return selectionStartIdxX == -1 || selectionStartIdxY == -1 || selectionEndIdxX == -1 || selectionEndIdxY == -1;
    }

    // GET/SET


    public int getSelectionStartIdxX() {
        return selectionStartIdxX;
    }

    public void setSelectionStartIdxX(int selectionStartIdxX) {
        this.selectionStartIdxX = selectionStartIdxX;
    }

    public int getSelectionStartIdxY() {
        return selectionStartIdxY;
    }

    public void setSelectionStartIdxY(int selectionStartIdxY) {
        this.selectionStartIdxY = selectionStartIdxY;
    }

    public int getSelectionEndIdxX() {
        return selectionEndIdxX;
    }

    public void setSelectionEndIdxX(int selectionEndIdxX) {
        this.selectionEndIdxX = selectionEndIdxX;
    }

    public int getSelectionEndIdxY() {
        return selectionEndIdxY;
    }

    public void setSelectionEndIdxY(int selectionEndIdxY) {
        this.selectionEndIdxY = selectionEndIdxY;
    }
}
