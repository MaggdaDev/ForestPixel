package org.maggdadev.forestpixel.canvas.tools.models;

import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
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
        super.applyToPreview(canvasModel, canvasContext, xIdx, yIdx);
        int xStart = Math.min(selectionStartIdxX, selectionEndIdxX);
        int xEnd = Math.max(selectionStartIdxX, selectionEndIdxX);
        int yStart = Math.min(selectionStartIdxY, selectionEndIdxY);
        int yEnd = Math.max(selectionStartIdxY, selectionEndIdxY);
        int width = xEnd - xStart;
        int height = yEnd - yStart;
        eraseAreaFromPreview(canvasContext.getPreviewImage(), xStart, yStart, width, height, canvasModel.getTransparentColor());
        canvasContext.getPreviewImage().getPixelWriter().setPixels(xStart, yStart, width, height, canvasModel.getImage().getPixelReader(), xStart, yStart);
    }

    private void eraseAreaFromPreview(WritableImage previewImage, int x, int y, int width, int height, Color transparentColor) {
        PixelWriter writer = previewImage.getPixelWriter();
        for (int i = x; i < x + width; i++) {
            for (int j = y; j < y + height; j++) {
                writer.setColor(i, j, transparentColor);
            }
        }
    }

    @Override
    public void applyToCanvas(CanvasModel canvasModel, CanvasContext canvasContext, int xIdx, int yIdx) {
        super.applyToCanvas(canvasModel, canvasContext, xIdx, yIdx);
        canvasModel.applyPreviewImage(canvasContext.getPreviewImageAndDelete());
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
