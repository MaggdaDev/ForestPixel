package org.maggdadev.forestpixel.canvas.history;

import org.maggdadev.forestpixel.canvas.layers.CanvasLayerModel;

public abstract class CanvasChange {

    protected final CanvasLayerModel model;

    protected CanvasChange(CanvasLayerModel model) {
        this.model = model;
    }

    public abstract void apply();

    public abstract void undo();

    public abstract boolean isChange();
}
