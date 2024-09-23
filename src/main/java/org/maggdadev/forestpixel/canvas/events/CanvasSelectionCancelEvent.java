package org.maggdadev.forestpixel.canvas.events;

import org.maggdadev.forestpixel.canvas.CanvasContext;
import org.maggdadev.forestpixel.canvas.CanvasModel;

public record CanvasSelectionCancelEvent(CanvasContext context, CanvasModel model,
                                         CancelState type) implements CanvasEvent {
    public enum CancelState {
        ON_CANCEL,
        AFTER_CANCEL;
    }
}
