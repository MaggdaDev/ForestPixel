package org.maggdadev.forestpixel.canvas.tools.models;

import javafx.scene.image.WritableImage;
import org.maggdadev.forestpixel.canvas.CanvasModel;
import org.maggdadev.forestpixel.canvas.events.PrimaryButtonEvent;

public abstract class ToolModel {

    public abstract void applyToCanvas(CanvasModel canvasModel, int xIdx, int yIdx);
}
