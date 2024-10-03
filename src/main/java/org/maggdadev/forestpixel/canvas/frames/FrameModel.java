package org.maggdadev.forestpixel.canvas.frames;

import org.maggdadev.forestpixel.canvas.layers.LayerModel;

import java.util.ArrayList;
import java.util.List;

public class FrameModel {
    private final String id;
    private final List<LayerModel> layers = new ArrayList<>();
    private static int currentId = 0;

    private final int widthPixels, heightPixels;

    public FrameModel(int widthPixels, int heightPixels) {
        id = String.valueOf(currentId++);
        this.widthPixels = widthPixels;
        this.heightPixels = heightPixels;
        addNewLayer();
    }

    public String getId() {
        return id;
    }

    public LayerModel addNewLayer() {
        LayerModel layer = new LayerModel(widthPixels, heightPixels);
        addNewLayer(layer);
        return layer;
    }

    public List<LayerModel> getLayers() {
        return layers;
    }

    public LayerModel getLayer(String layerId) {
        for (LayerModel layer : layers) {
            if (layer.getId().equals(layerId)) {
                return layer;
            }
        }
        throw new IllegalArgumentException("Layer with id " + layerId + " not found in frame " + id);
    }

    public void removeLayer(String id) {
        layers.removeIf(layer -> layer.getId().equals(id));
    }


    public void addNewLayer(LayerModel layer) {
        addExistingLayer(layer);
    }

    public void addExistingLayer(LayerModel model) {
        layers.add(model);
    }

    public int getWidthPixels() {
        return widthPixels;
    }

    public int getHeightPixels() {
        return heightPixels;
    }
}
