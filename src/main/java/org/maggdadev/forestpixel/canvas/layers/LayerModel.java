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

public class LayerModel implements Cloneable {

    private static int currentId = 0;
    private final int width, height;
    private final WritableImage image;

    private String name = "layer";
    private final String id;

    public LayerModel(int width, int height) {
        this(new WritableImage(width, height));
    }

    private LayerModel(WritableImage image) {
        this.width = (int) image.getWidth();
        this.height = (int) image.getHeight();
        this.id = String.valueOf(currentId++);
        this.image = image;
    }

    public MultiPixelChange previewImageToMultiPixelChange(PreviewImage previewImage) {
        if (previewImage == null)
            return null;
        PixelReader previewReader = previewImage.getDrawableImage().getPixelReader();
        List<Color> colors = new ArrayList<>();
        List<Point> points = new ArrayList<>();
        Color addColor;
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                if (!getColorAt(i, j).equals(previewReader.getColor(i, j)) && !previewReader.getColor(i, j).equals(Color.TRANSPARENT)) {
                    points.add(new Point(i, j));
                    addColor = previewImage.getColor(i, j);
                    if (!addColor.equals(Color.TRANSPARENT) && addColor.getOpacity() == 0) {
                        addColor = new Color(addColor.getRed(), addColor.getGreen(), addColor.getBlue(), 1);
                    }
                    colors.add(addColor);
                }
            }
        }
        previewImage.getDeletedPoints().forEach(point -> {
            points.add(point);
            colors.add(Color.TRANSPARENT);
        });
        return new MultiPixelChange(this, points, colors);
    }

    @Override
    public LayerModel clone() {
        LayerModel clone = new LayerModel(this.image);
        clone.name = this.name;
        return clone;
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

    // GET/SET

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

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
