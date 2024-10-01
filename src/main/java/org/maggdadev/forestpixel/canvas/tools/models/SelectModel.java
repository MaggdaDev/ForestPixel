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
        canvasContext.getPreviewImage().setPixels(xStart, yStart, width, height, canvasModel.getPixelReaderForLayer(canvasContext.getActiveFrameId(), canvasContext.getActiveLayerId()), xStart, yStart);
        canvasModel.eraseAreaForSelection(xStart, yStart, width, height, canvasContext.getActiveFrameId(), canvasContext.getActiveLayerId());
    }


    private boolean isSelectionInvalid() {
        return selectionStartIdxX == -1 || selectionStartIdxY == -1 || selectionEndIdxX == -1 || selectionEndIdxY == -1;
    }

    public void eraseAreaFromPreview(CanvasContext context, int topLeftXWithOffset, int topLeftYWithOffset, int width, int height, String activeLayerId) {
        context.getPreviewImage().deleteColors(topLeftXWithOffset, topLeftYWithOffset, width, height);
    }

    // GET/SET

    public int getTopLeftXWithOffset(int offsetX) {
        return Math.min(selectionStartIdxX, selectionEndIdxX) + offsetX;
    }

    public int getTopLeftYWithOffset(int offsetY) {
        return Math.min(selectionStartIdxY, selectionEndIdxY) + offsetY;
    }

    public int getWidth() {
        return Math.abs(selectionStartIdxX - selectionEndIdxX);
    }

    public int getHeight() {
        return Math.abs(selectionStartIdxY - selectionEndIdxY);
    }

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
