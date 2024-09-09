package org.maggdadev.forestpixel.canvas.layers;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import org.maggdadev.forestpixel.canvas.PreviewImage;
import org.maggdadev.forestpixel.canvas.history.MultiPixelChange;
import org.maggdadev.forestpixel.canvas.utils.Point;

import java.util.ArrayList;
import java.util.List;

public class CanvasLayerModel {
    private final int width, height;
    private final WritableImage image;

    private final int layerId;

    public CanvasLayerModel(int width, int height, int layerId) {
        this.width = width;
        this.height = height;
        this.layerId = layerId;
        image = new WritableImage(width, height);
    }

    public MultiPixelChange previewImageToMultiPixelChange(PreviewImage previewImage) {
        if (previewImage == null)
            return null;
        PixelReader previewReader = previewImage.getDrawableImage().getPixelReader();
        List<Color> colors = new ArrayList<>();
        List<Point> points = new ArrayList<>();
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                if (!getColorAt(i, j).equals(previewReader.getColor(i, j)) && !previewReader.getColor(i, j).equals(Color.TRANSPARENT)) {
                    points.add(new Point(i, j));
                    colors.add(previewReader.getColor(i, j));
                }
            }
        }
        previewImage.getDeletedPoints().forEach(point -> {
            points.add(point);
            colors.add(Color.TRANSPARENT);
        });
        return new MultiPixelChange(this, points, colors);
    }

    public Color getColorAt(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return Color.TRANSPARENT;
        }
        return image.getPixelReader().getColor(x, y);
    }

    public void setColorAt(int x, int y, Color color) {
        image.getPixelWriter().setColor(x, y, color);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }


    public Image getImage() {
        return image;
    }

    public PixelReader getPixelReader() {
        return image.getPixelReader();
    }

    public int getLayerId() {
        return layerId;
    }
}
