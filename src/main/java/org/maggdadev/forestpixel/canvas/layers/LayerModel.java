package org.maggdadev.forestpixel.canvas.layers;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.*;
import javafx.scene.paint.Color;
import org.maggdadev.forestpixel.canvas.PreviewImage;
import org.maggdadev.forestpixel.canvas.history.MultiPixelChange;
import org.maggdadev.forestpixel.canvas.utils.Point;

import java.awt.image.BufferedImage;
import java.io.Serial;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LayerModel implements Cloneable, Serializable {

    private final int width, height;
    private transient WritableImage image;

    private byte[] bytesForSerialization;
    private String name = "layer";
    private final String id;

    public LayerModel(int width, int height) {
        this(new WritableImage(width, height));
    }

    private LayerModel(WritableImage image) {
        this.width = (int) image.getWidth();
        this.height = (int) image.getHeight();
        this.id = UUID.randomUUID().toString();
        this.image = new WritableImage(image.getPixelReader(), width, height);
    }

    @Serial
    private void readObject(java.io.ObjectInputStream in) throws Exception {
        in.defaultReadObject();
        // https://stackoverflow.com/questions/30970005/bufferedimage-to-javafx-image
//        ByteBuffer byteBuffer = ByteBuffer.wrap(bytesForSerialization);
//        Image image = SwingFXUtils.toFXImage(capture, null)
//        PixelBuffer<ByteBuffer> pixelBuffer = new PixelBuffer<>(width, height, byteBuffer, PixelFormat.getByteBgraPreInstance());
        image = fromBytes(bytesForSerialization, width, height);
    }

    private static WritableImage fromBytes(byte[] bytes, int width, int height) {
        WritableImage image = new WritableImage(width, height);
        PixelWriter writer = image.getPixelWriter();
        writer.setPixels(
                0, 0, width, height,
                PixelFormat.getByteBgraPreInstance(),
                bytes,
                0,
                width * 4
        );
        return image;
    }


    @Serial
    private void writeObject(java.io.ObjectOutputStream out) throws Exception {
        bytesForSerialization = new byte[width * height * 4];
        image.getPixelReader().getPixels(0, 0, width, height, PixelFormat.getByteBgraPreInstance(), bytesForSerialization, 0, width * 4);
        out.defaultWriteObject();
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

    public boolean isTransparentAt(int x, int y) {
        return getColorAt(x, y).equals(Color.TRANSPARENT);
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

    @Override
    public String toString() {
        return "Name: " + name + ",   ID: " + id;
    }
}
