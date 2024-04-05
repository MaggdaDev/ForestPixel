package org.maggdadev.forestpixel.canvas.tools;

import org.maggdadev.forestpixel.canvas.events.CanvasEventHandler;
import org.maggdadev.forestpixel.canvas.events.PrimaryButtonEvent;
import org.maggdadev.forestpixel.canvas.tools.models.ToolModel;

import java.util.Objects;


public class ToolViewModel {
    // handlers must not be null! Use CanvasEventHandler.empty()!
    private CanvasEventHandler<PrimaryButtonEvent> onPrimaryButtonEvent = CanvasEventHandler.empty();

    private final ToolModel toolModel;
    public ToolViewModel(ToolModel toolModel) {
        this.toolModel = toolModel;
        setOnPrimaryButtonEvent((PrimaryButtonEvent e) -> {
            return this.toolModel.applyToCanvas(e.canvasModel(), e.xIdx(), e.yIdx());
        });
    }


    public CanvasEventHandler<PrimaryButtonEvent> getOnPrimaryButtonEvent() {
        return onPrimaryButtonEvent;
    }

    public void setOnPrimaryButtonEvent(CanvasEventHandler<PrimaryButtonEvent> onPrimaryButtonEvent) {
        Objects.requireNonNull(onPrimaryButtonEvent);
        this.onPrimaryButtonEvent = onPrimaryButtonEvent;
    }


}
