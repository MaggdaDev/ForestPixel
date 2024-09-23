package org.maggdadev.forestpixel.canvas.history;

import org.maggdadev.forestpixel.canvas.layers.LayerModel;

public abstract class CanvasChange {

    protected final LayerModel model;

    protected CanvasChange(LayerModel model) {
        this.model = model;
    }

    public abstract void apply();

    public abstract void undo();

    public abstract boolean isChange();
}
