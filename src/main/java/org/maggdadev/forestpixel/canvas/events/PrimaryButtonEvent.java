package org.maggdadev.forestpixel.canvas.events;

import org.maggdadev.forestpixel.canvas.CanvasModel;

public record PrimaryButtonEvent (CanvasModel canvasModel, int xIdx, int yIdx) implements CanvasEvent {};
