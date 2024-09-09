package org.maggdadev.forestpixel.canvas;

import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;
import org.maggdadev.forestpixel.canvas.history.HistoryModel;
import org.maggdadev.forestpixel.canvas.history.SingleColorMultiPixelChange;
import org.maggdadev.forestpixel.canvas.layers.CanvasLayerModel;
import org.maggdadev.forestpixel.canvas.utils.Point;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CanvasModel {

    private final HashMap<String, CanvasLayerModel> layers = new HashMap<>();
    private int widthPixels = 0, heightPixels = 0;

    private final HistoryModel historyModel;

    public CanvasModel(int widthPixels, int heightPixels) {
        this.widthPixels = widthPixels;
        this.heightPixels = heightPixels;
        historyModel = new HistoryModel(this);
    }

    public CanvasLayerModel addLayer(String layerId) {
        if (layers.containsKey(layerId)) {
            throw new IllegalArgumentException("Layer with id " + layerId + " already exists!");
        }
        CanvasLayerModel layer = new CanvasLayerModel(widthPixels, heightPixels, layerId);
        layers.put(layerId, layer);
        return layer;
    }

    public void removeLayer(String layerId) {
        if (!layers.containsKey(layerId)) {
            throw new IllegalArgumentException("Layer with id " + layerId + " does not exist!");
        }
        layers.remove(layerId);
    }

    /**
     * @param points Array of 2D-int-tuples representing the indices of the points
     * @param color  Single color for all of the points
     */
    public void setPixelColor(List<Point> points, Color color, String layerId) {
        historyModel.applyNewChange(new SingleColorMultiPixelChange(layers.get(layerId), points, color));
    }

    public void undo() {
        historyModel.undo();
    }

    public void redo() {
        historyModel.redo();
    }

    /**
     * @param x xIdx
     * @param y yIdx
     * @return returns the color at the position or null, if the position is out of range
     */
    public Color getPixelColor(int x, int y, String layerId) {
        if (isOnCanvas(x, y)) {
            return layers.get(layerId).getColorAt(x, y);
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


    public void applyPreviewImage(PreviewImage previewImage, String layerId) {
        if (!layers.isEmpty() && !"-1".equals(layerId)) {
            historyModel.applyNewChange(layers.get(layerId).previewImageToMultiPixelChange(previewImage));
        }
    }


    public void eraseAreaForSelection(int xStart, int yStart, int width, int height, String layerId) {
        List<Point> points = new ArrayList<>();
        for (int i = xStart; i < xStart + width; i++) {
            for (int j = yStart; j < yStart + height; j++) {
                points.add(new Point(i, j));
            }
        }
        historyModel.applyNewChange(new SingleColorMultiPixelChange(layers.get(layerId), points, Color.TRANSPARENT));
    }

    public PixelReader getPixelReaderForLayer(String layerId) {
        return layers.get(layerId).getPixelReader();
    }

    public boolean hasLayers() {
        return !layers.isEmpty();
    }
}
