package org.maggdadev.forestpixel.canvas;

import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class CanvasModel {
    private WritableImage image;
    private int widthPixels = 0, heightPixels = 0;
    public CanvasModel() {

    }

    public void createNewImage(int widthPixels, int heightPixels) {
        image = new WritableImage(widthPixels,heightPixels);
        this.widthPixels = widthPixels;
        this.heightPixels = heightPixels;

        image.getPixelWriter().setColor(10,10, Color.GREEN);
        image.getPixelWriter().setColor(11,10, Color.GREEN);
        image.getPixelWriter().setColor(12,10, Color.GREEN);
        image.getPixelWriter().setColor(13,10, Color.GREEN);
    }

    public int getWidthPixels() {
        return widthPixels;
    }

    public int getHeightPixels() {
        return heightPixels;
    }

    public WritableImage getImage() {
        return image;
    }

    public void setImage(WritableImage image) {
        this.image = image;
    }
}
