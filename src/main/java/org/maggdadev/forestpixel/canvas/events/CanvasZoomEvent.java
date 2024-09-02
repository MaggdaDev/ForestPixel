package org.maggdadev.forestpixel.canvas.events;

import org.maggdadev.forestpixel.canvas.CanvasContext;

public record CanvasZoomEvent(CanvasContext canvasContext) implements CanvasEvent {
}
