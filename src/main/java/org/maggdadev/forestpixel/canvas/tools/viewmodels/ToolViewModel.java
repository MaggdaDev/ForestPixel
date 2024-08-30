package org.maggdadev.forestpixel.canvas.tools.viewmodels;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import org.maggdadev.forestpixel.canvas.events.CanvasMouseEvent;
import org.maggdadev.forestpixel.canvas.tools.ToolType;


public abstract class ToolViewModel {
    private final ToolType toolType;
    private final BooleanProperty mouseAreaIndicatorActive = new SimpleBooleanProperty(false);

    ToolViewModel(ToolType toolType) {
        this.toolType = toolType;
    }

    public final void notifyCanvasMouseEvent(CanvasMouseEvent e) {
        switch (e.buttonType()) {
            case PRIMARY:
                switch(e.actionType()) {
                    case PRESSED -> onPrimaryButtonPressed(e);
                    case RELEASED -> onPrimaryButtonReleased(e);
                    case CLICKED -> onPrimaryButtonTyped(e);
                    case DRAGGED -> onPrimaryButtonDragged(e);
                }
                break;
            case SECONDARY:
                switch(e.actionType()) {
                    case PRESSED -> onSecondaryButtonPressed(e);
                    case RELEASED -> onSecondaryButtonReleased(e);
                    case CLICKED -> onSecondaryButtonTyped(e);
                    case DRAGGED -> onSecondaryButtonDragged(e);
                }
                break;
        }
    }

    public ToolType getToolType() {
        return toolType;
    }

    protected void onPrimaryButtonPressed(CanvasMouseEvent e) {
    }

    protected void onPrimaryButtonReleased(CanvasMouseEvent e) {
    }

    protected void onPrimaryButtonTyped(CanvasMouseEvent e) {
    }

    protected void onPrimaryButtonDragged(CanvasMouseEvent e) {
    }

    protected void onSecondaryButtonPressed(CanvasMouseEvent e) {
    }

    protected void onSecondaryButtonReleased(CanvasMouseEvent e) {
    }

    protected void onSecondaryButtonTyped(CanvasMouseEvent e) {
    }

    protected void onSecondaryButtonDragged(CanvasMouseEvent e) {
    }

    public boolean isMouseAreaIndicatorActive() {
        return mouseAreaIndicatorActive.get();
    }

    public BooleanProperty mouseAreaIndicatorActiveProperty() {
        return mouseAreaIndicatorActive;
    }

    public void setMouseAreaIndicatorActive(boolean mouseAreaIndicatorActive) {
        this.mouseAreaIndicatorActive.set(mouseAreaIndicatorActive);
    }
}
