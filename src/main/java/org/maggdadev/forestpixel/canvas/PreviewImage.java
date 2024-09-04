package org.maggdadev.forestpixel.canvas;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class PreviewImage {
    private final WritableImage image;
    private final int sourceWidth, sourceHeight;

    private final IntegerProperty xOffset = new SimpleIntegerProperty(0), yOffset = new SimpleIntegerProperty(0);

    public PreviewImage(int sourceWidth, int sourceHeight) {
        image = new WritableImage(3*sourceWidth, 3*sourceHeight);
        this.sourceWidth = sourceWidth;
        this.sourceHeight = sourceHeight;
    }

    public void setColor(int xIdx, int yIdx, Color color) {
        image.getPixelWriter().setColor(sourceXToPreview(xIdx), sourceYToPreview(yIdx), color);
    }

    public Color getColor(int xIdx, int yIdx) {
        return image.getPixelReader().getColor(sourceXToPreview(xIdx), sourceYToPreview(yIdx));
    }



    public void setPixels(int xStart, int yStart, int width, int height, PixelReader pixelReader, int xSrc, int ySrc) {
        image.getPixelWriter().setPixels(sourceXToPreview(xStart), sourceYToPreview(yStart), width, height, pixelReader, xSrc, ySrc);
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
}
