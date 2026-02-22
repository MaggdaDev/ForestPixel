package org.maggdadev.forestpixel.canvas.frames;

import org.maggdadev.forestpixel.canvas.history.CanvasChange;
import org.maggdadev.forestpixel.canvas.history.HistoryModel;
import org.maggdadev.forestpixel.canvas.layers.LayerModel;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FrameModel implements Cloneable, Serializable {
    private final String id;

    private String name;
    private final List<LayerModel> layers = new ArrayList<>();

    private final int widthPixels, heightPixels;

    private transient HistoryModel historyModel;

    public FrameModel(int widthPixels, int heightPixels) {
        this(widthPixels, heightPixels, new ArrayList<>());
        addNewLayer();
    }

    private FrameModel(int widthPixels, int heightPixels, List<LayerModel> layersToClone) {
        id = UUID.randomUUID().toString();
        name = "frame " + id;
        historyModel = new HistoryModel();
        this.widthPixels = widthPixels;
        this.heightPixels = heightPixels;
        for (LayerModel layer : layersToClone) {
            addExistingLayer(layer.clone());
        }
    }

    @Serial
    private void readObject(java.io.ObjectInputStream in) throws Exception {
        in.defaultReadObject();
        historyModel = new HistoryModel();
    }

    public void applyNewChange(CanvasChange change) {
        historyModel.applyNewChange(change);
    }

    public String getId() {
        return id;
    }

    public LayerModel addNewLayer() {
        LayerModel layer = new LayerModel(widthPixels, heightPixels);
        addExistingLayer(layer);
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

    public void addExistingLayer(LayerModel model) {
        layers.add(model);
    }

    @Override
    public FrameModel clone() {
        FrameModel clone = new FrameModel(widthPixels, heightPixels, layers);
        clone.name = name + "(2)";
        return clone;
    }

    // GET/SET

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
