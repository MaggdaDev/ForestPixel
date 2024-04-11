package org.maggdadev.forestpixel.canvas;

import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import org.maggdadev.forestpixel.canvas.history.HistoryModel;
import org.maggdadev.forestpixel.canvas.history.MultiPixelChange;
import org.maggdadev.forestpixel.canvas.history.SingleColorMultiPixelChange;
import org.maggdadev.forestpixel.canvas.history.SinglePixelChange;

import java.util.ArrayList;
import java.util.List;

public class CanvasModel {
    private WritableImage image;
    private int widthPixels = 0, heightPixels = 0;

    private final HistoryModel historyModel;
    public CanvasModel() {
        historyModel = new HistoryModel(this);
    }

    public void createNewImage(int widthPixels, int heightPixels) {
        image = new WritableImage(widthPixels,heightPixels);
        this.widthPixels = widthPixels;
        this.heightPixels = heightPixels;
    }

    public void setPixelColor(int x, int y, Color color) {
        historyModel.applyNewChange(new SinglePixelChange(image, new int[]{x,y}, color));
    }

    /**
     *
     * @param points Array of 2D-int-tuples representing the indices of the points
     * @param color Single color for all of the points
     */
    public void setPixelColor(List<int[]> points, Color color) {
        historyModel.applyNewChange(new SingleColorMultiPixelChange(image, points, color));
    }

    public void undo() {
        historyModel.undo();
    }

    public void redo() {
        historyModel.redo();
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

    public void applyPreviewImage(WritableImage previewImage) {
        if(previewImage == null)
            return;
        PixelReader modelReader = image.getPixelReader();
        PixelReader previewReader = previewImage.getPixelReader();
        List<Color> colors = new ArrayList<>();
        List<int[]> points = new ArrayList<>();
        for(int i = 0; i < image.getWidth(); i ++) {
            for(int j = 0; j < image.getHeight(); j ++) {
                if(!modelReader.getColor(i,j).equals(previewReader.getColor(i,j))) {
                    points.add(new int[]{i,j});
                    colors.add(previewReader.getColor(i,j));
                }
            }
        }
        historyModel.applyNewChange(new MultiPixelChange(image, points, colors));
    }
}
