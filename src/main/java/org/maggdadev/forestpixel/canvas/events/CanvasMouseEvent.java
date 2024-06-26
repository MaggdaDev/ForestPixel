package org.maggdadev.forestpixel.canvas.events;

import org.maggdadev.forestpixel.canvas.CanvasContext;
import org.maggdadev.forestpixel.canvas.CanvasModel;


public record CanvasMouseEvent(CanvasModel canvasModel, int xIdx, int yIdx, ActionType actionType, ButtonType buttonType, CanvasContext canvasContext) implements CanvasEvent{
    public static enum ActionType {
        PRESSED,
        CLICKED,
        RELEASED,
        DRAGGED
    }

    public static enum ButtonType {
        PRIMARY,
        SECONDARY
    }
}
