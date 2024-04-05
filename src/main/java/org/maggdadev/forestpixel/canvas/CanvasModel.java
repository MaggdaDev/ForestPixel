package org.maggdadev.forestpixel.canvas;

import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class CanvasModel {
    private WritableImage image;
    private int widthPixels = 0, heightPixels = 0;
    public CanvasModel() {

    }

    public void createNewImage(int widthPixels, int heightPixels) {
        image = new WritableImage(widthPixels,heightPixels);
        this.widthPixels = widthPixels;
        this.heightPixels = heightPixels;
    }

    public void setPixelColor(int x, int y, Color color) {
        image.getPixelWriter().setColor(x,y,color);
    }

    /**
     *
     * @param x xIdx
     * @param y yIdx
     * @return returns the color at the position or null, if the position is out of range
     */
    public Color getPixelColor(int x, int y) {
        if(isOnCanvas(x, y)) {
            return image.getPixelReader().getColor(x,y);
        } else {
            return null;
        }
    }

    public boolean isOnCanvas(int x, int y) {
        return x >= 0 && y >= 0 && x < widthPixels && y < heightPixels;
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
