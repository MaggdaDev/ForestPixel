package org.maggdadev.forestpixel.canvas.tools.models;

import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import org.maggdadev.forestpixel.canvas.CanvasModel;

public class PencilModel extends ToolModel {

    @Override
    public boolean applyToCanvas(CanvasModel canvasModel, int xIdx, int yIdx) {
        canvasModel.getImage().getPixelWriter().setColor(xIdx, yIdx, Color.BLACK);
        return true;
    }
}
