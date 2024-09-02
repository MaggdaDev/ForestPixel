package org.maggdadev.forestpixel.canvas.events;

import org.maggdadev.forestpixel.canvas.CanvasContext;
import org.maggdadev.forestpixel.canvas.CanvasModel;


public record CanvasMouseEvent(CanvasModel canvasModel, double pixelXPos, double pixelYPos, int xIdx, int yIdx, ActionType actionType, ButtonType buttonType, CanvasContext canvasContext) implements CanvasEvent{
    public static enum ActionType {
        PRESSED,
        CLICKED,
        RELEASED,
        DRAGGED,

        SELECTION_CANCELLED
    }

    public static enum ButtonType {
        PRIMARY,
        SECONDARY
    }
}
