package org.maggdadev.forestpixel.canvas.frames;

import org.maggdadev.forestpixel.canvas.history.CanvasChange;
import org.maggdadev.forestpixel.canvas.history.HistoryModel;
import org.maggdadev.forestpixel.canvas.layers.LayerModel;

import java.util.ArrayList;
import java.util.List;

public class FrameModel {
    private final String id;

    private String name = "";
    private final List<LayerModel> layers = new ArrayList<>();
    private static int currentId = 0;

    private final int widthPixels, heightPixels;

    private final HistoryModel historyModel;

    public FrameModel(int widthPixels, int heightPixels) {
        id = String.valueOf(currentId++);
        historyModel = new HistoryModel();
        this.widthPixels = widthPixels;
        this.heightPixels = heightPixels;
        addNewLayer();
    }

    public void applyNewChange(CanvasChange change) {
        historyModel.applyNewChange(change);
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public void undo() {
        historyModel.undo();
    }

    public void redo() {
        historyModel.redo();
    }


}
