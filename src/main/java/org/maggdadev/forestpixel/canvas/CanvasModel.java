package org.maggdadev.forestpixel.canvas;

import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;
import org.maggdadev.forestpixel.canvas.frames.FrameModel;
import org.maggdadev.forestpixel.canvas.history.HistoryModel;
import org.maggdadev.forestpixel.canvas.history.SingleColorMultiPixelChange;
import org.maggdadev.forestpixel.canvas.layers.LayerModel;
import org.maggdadev.forestpixel.canvas.utils.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class CanvasModel {

    private final List<FrameModel> frames = new ArrayList<>();
    private final List<LayerModel> layers = new ArrayList<>();
    private int widthPixels = 0, heightPixels = 0;

    private final HistoryModel historyModel;

    public CanvasModel(int widthPixels, int heightPixels) {
        this.widthPixels = widthPixels;
        this.heightPixels = heightPixels;
        historyModel = new HistoryModel(this);
        addLayer();
    }

    public LayerModel addLayer() {
        LayerModel layer = new LayerModel(widthPixels, heightPixels);
        layers.add(layer);
        return layer;
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
            return getLayer(layerId).getColorAt(x, y);
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
            historyModel.applyNewChange(getLayer(layerId).previewImageToMultiPixelChange(previewImage));
        }
    }


    public void eraseAreaForSelection(int xStart, int yStart, int width, int height, String layerId) {
        List<Point> points = new ArrayList<>();
        for (int i = xStart; i < xStart + width; i++) {
            for (int j = yStart; j < yStart + height; j++) {
                points.add(new Point(i, j));
            }
        }
        historyModel.applyNewChange(new SingleColorMultiPixelChange(getLayer(layerId), points, Color.TRANSPARENT));
    }

    public PixelReader getPixelReaderForLayer(String layerId) {
        return getLayer(layerId).getPixelReader();
    }

    public boolean hasLayers() {
        return !layers.isEmpty();
    }

    private LayerModel getLayer(String layerId) {
        for (LayerModel layer : layers) {
            if (layer.getLayerId().equals(layerId)) {
                return layer;
            }
        }
        throw new IllegalArgumentException("Layer with id " + layerId + " not found");
    }

    public void removeLayer(String id) {
        layers.removeIf(layer -> layer.getLayerId().equals(id));
    }

    public void forEachLayer(Consumer<LayerModel> consumer) {
        layers.forEach(consumer);
    }

    public List<FrameModel> getFrames() {
        return frames;
    }
}
