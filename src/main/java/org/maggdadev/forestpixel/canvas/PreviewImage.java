package org.maggdadev.forestpixel.canvas;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import org.maggdadev.forestpixel.canvas.utils.Point;

import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Stream;

public class PreviewImage {
    private final WritableImage image;

    private final PixelWriter pixelWriter;
    private final int sourceWidth, sourceHeight;

    private final Collection<Point> deletedPoints;

    private int minDrawnPosXOnPreviewImage = 0, minDrawnPosYOnPreviewImage = 0, maxDrawnPosXOnPreviewImage = 0, maxDrawnPosYOnPreviewImage = 0;

    private final IntegerProperty xOffset = new SimpleIntegerProperty(0), yOffset = new SimpleIntegerProperty(0);

    private final IntegerProperty lineWidth;

    public PreviewImage(int sourceWidth, int sourceHeight, IntegerProperty lineWidth) {
        deletedPoints = new HashSet<>();
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

    public PreviewImage deleteColor(int xIdx, int yIdx) {
        setColor(xIdx, yIdx, Color.TRANSPARENT);
        deletedPoints.add(new Point(sourceXToPreview(xIdx), sourceYToPreview(yIdx)));
        return this;
    }

    public PreviewImage deleteColors(Iterable<Point> points) {
        for (Point point : points) {
            deleteColor(point.x, point.y);
        }
        return this;
    }

    public PreviewImage setColor(Iterable<Point> points, Color color) {
        for (Point point : points) {
            setColor(point.x, point.y, color);
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
        deletedPoints.clear();
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


    public Stream<Point> getDeletedPoints() {
        // return deleted points but converted back to source coordinates using previewToSource methods
        return deletedPoints.stream().map(point -> new Point(previewXToSource(point.x), previewYToSource(point.y)));

    }

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

    private int previewXToSource(int previewX) {
        return previewX + xOffset.get() - sourceWidth;
    }

    private int previewYToSource(int sourceY) {
        return sourceY + yOffset.get() - sourceHeight;
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

    public boolean hasDeletedPoints() {
        return !deletedPoints.isEmpty();
    }
}
