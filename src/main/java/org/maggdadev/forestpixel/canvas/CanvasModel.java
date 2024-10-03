package org.maggdadev.forestpixel.canvas;

import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;
import org.maggdadev.forestpixel.canvas.frames.FrameModel;
import org.maggdadev.forestpixel.canvas.history.SingleColorMultiPixelChange;
import org.maggdadev.forestpixel.canvas.layers.LayerModel;
import org.maggdadev.forestpixel.canvas.utils.Point;

import java.util.ArrayList;
import java.util.List;

public class CanvasModel {

    private final List<FrameModel> frames = new ArrayList<>();

    private final int widthPixels, heightPixels;

    public CanvasModel(int widthPixels, int heightPixels) {
        this.widthPixels = widthPixels;
        this.heightPixels = heightPixels;
        addNewFrame();
    }

    public FrameModel addNewFrame() {
        FrameModel newModel = new FrameModel(widthPixels, heightPixels);
        addExistingFrame(newModel);
        return newModel;
    }

    public void addExistingFrame(FrameModel frameModel) {
        frames.add(frameModel);
    }


    public void undo(String frameId) {
        getFrame(frameId).undo();
    }

    public void redo(String frameId) {
        getFrame(frameId).redo();
    }

    /**
     * @param x xIdx
     * @param y yIdx
     * @return returns the color at the position or null, if the position is out of range
     */
    public Color getPixelColor(int x, int y, String frameId, String layerId) {
        if (isOnCanvas(x, y)) {
            return getFrame(frameId).getLayer(layerId).getColorAt(x, y);
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

    public void applyPreviewImage(PreviewImage previewImage, String frameId, String layerId) {
        if (!getFrame(frameId).getLayers().isEmpty() && !"-1".equals(layerId)) {
            getFrame(frameId).applyNewChange(findLayer(frameId, layerId).previewImageToMultiPixelChange(previewImage));
        }
    }

    private LayerModel findLayer(String frameId, String layerId) {
        return getFrame(frameId).getLayer(layerId);
    }

    public void eraseAreaForSelection(int xStart, int yStart, int width, int height, String frameId, String layerId) {
        List<Point> points = new ArrayList<>();
        for (int i = xStart; i < xStart + width; i++) {
            for (int j = yStart; j < yStart + height; j++) {
                points.add(new Point(i, j));
            }
        }
        getFrame(frameId).applyNewChange(new SingleColorMultiPixelChange(getFrame(frameId).getLayer(layerId), points, Color.TRANSPARENT));
    }

    public FrameModel getFrame(String frameId) {
        return frames.stream()
                .filter(frame -> frame.getId().equals(frameId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Frame with id " + frameId + " not found"));
    }

    public PixelReader getPixelReaderForLayer(String frameId, String layerId) {
        return findLayer(frameId, layerId).getPixelReader();
    }

    public boolean layerExists(String frameId, String layerId) {
        if (frames.stream().noneMatch(frame -> frame.getId().equals(frameId))) {
            return false;
        }
        return getFrame(frameId).getLayers().stream().anyMatch(layer -> layer.getId().equals(layerId));
    }


    public List<FrameModel> getFrames() {
        return frames;
    }
}
