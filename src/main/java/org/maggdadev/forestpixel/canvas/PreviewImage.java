package org.maggdadev.forestpixel.canvas;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.util.List;

public class PreviewImage {
    private final WritableImage image;

    private final PixelWriter pixelWriter;
    private final int sourceWidth, sourceHeight;

    private int minDrawnPosXOnPreviewImage = 0, minDrawnPosYOnPreviewImage = 0, maxDrawnPosXOnPreviewImage = 0, maxDrawnPosYOnPreviewImage = 0;

    private final IntegerProperty xOffset = new SimpleIntegerProperty(0), yOffset = new SimpleIntegerProperty(0);

    private final IntegerProperty lineWidth;

    public PreviewImage(int sourceWidth, int sourceHeight, IntegerProperty lineWidth) {
        this.lineWidth = lineWidth;
        image = new WritableImage(3*sourceWidth, 3*sourceHeight);
        pixelWriter = image.getPixelWriter();
        this.sourceWidth = sourceWidth;
        this.sourceHeight = sourceHeight;
        resetMinMaxPos();
    }

    // Start: DRAWING. Always set minmax drawn positions when drawing

    public PreviewImage setColor(int xIdx, int yIdx, Color color) {
        pixelWriter.setColor(sourceXToPreview(xIdx), sourceYToPreview(yIdx), color);
        updateMinMaxPos(xIdx, yIdx);
        return this;
    }

    public PreviewImage setColor(List<int[]> points, Color color) {
        for (int[] point : points) {
            setColor(point[0], point[1], color);
        }
        return this;
    }


    public PreviewImage setPixels(int xStart, int yStart, int width, int height, PixelReader pixelReader, int xSrc, int ySrc) {
        pixelWriter.setPixels(sourceXToPreview(xStart), sourceYToPreview(yStart), width, height, pixelReader, xSrc, ySrc);
        updateMinMaxPos(xStart, yStart);
        updateMinMaxPos(xStart + width, yStart + height);
        return this;
    }

    public PreviewImage clear() {
        PixelWriter pixelWriter = image.getPixelWriter();
        for (int x = Math.max(0, minDrawnPosXOnPreviewImage - lineWidth.get()); x < Math.min(image.getWidth(), maxDrawnPosXOnPreviewImage + lineWidth.get()); x++) {
            for (int y = Math.max(0, minDrawnPosYOnPreviewImage - lineWidth.get()); y < Math.min(image.getHeight(), maxDrawnPosYOnPreviewImage + lineWidth.get()); y++) {
                pixelWriter.setColor(x, y, Color.TRANSPARENT);
            }
        }
        resetMinMaxPos();
        return this;
    }

    // End: DRAWING



    public Color getColor(int xIdx, int yIdx) {
        return image.getPixelReader().getColor(sourceXToPreview(xIdx), sourceYToPreview(yIdx));
    }


    private void updateMinMaxPos(int xIdx, int yIdx) {
        minDrawnPosXOnPreviewImage = Math.min(minDrawnPosXOnPreviewImage, sourceXToPreview(xIdx));
        minDrawnPosYOnPreviewImage = Math.min(minDrawnPosYOnPreviewImage, sourceYToPreview(yIdx));
        maxDrawnPosXOnPreviewImage = Math.max(maxDrawnPosXOnPreviewImage, sourceXToPreview(xIdx));
        maxDrawnPosYOnPreviewImage = Math.max(maxDrawnPosYOnPreviewImage, sourceYToPreview(yIdx));
    }

    private void resetMinMaxPos() {
        minDrawnPosXOnPreviewImage = (int) image.getWidth();
        minDrawnPosYOnPreviewImage = (int) image.getHeight();
        maxDrawnPosXOnPreviewImage = 0;
        maxDrawnPosYOnPreviewImage = 0;
    }

    public WritableImage getDrawableImage() {
       return new WritableImage(image.getPixelReader(), sourceXToPreview(0), sourceYToPreview(0), sourceWidth, sourceHeight);
    }

    private int sourceXToPreview(int sourceX) {
        return sourceX + sourceWidth - xOffset.get();
    }

    private int sourceYToPreview(int sourceY) {
        return sourceY + sourceHeight - yOffset.get();
    }


    public IntegerProperty xOffsetProperty() {
        return xOffset;
    }


    public IntegerProperty yOffsetProperty() {
        return yOffset;
    }

    public int getSourceWidth() {
        return sourceWidth;
    }

    public int getSourceHeight() {
        return sourceHeight;
    }
}
