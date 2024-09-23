package org.maggdadev.forestpixel.canvas.tools.viewmodels;

import org.maggdadev.forestpixel.canvas.events.CanvasEvent;
import org.maggdadev.forestpixel.canvas.events.CanvasMouseEvent;
import org.maggdadev.forestpixel.canvas.events.CanvasSelectionCancelEvent;
import org.maggdadev.forestpixel.canvas.events.CanvasZoomEvent;
import org.maggdadev.forestpixel.canvas.tools.ToolType;


public abstract class ToolViewModel {
    private final ToolType toolType;
    private final boolean requestMouseEventsEvenIfSelected;


    ToolViewModel(ToolType toolType) {
        this(toolType, false);
    }

    ToolViewModel(ToolType toolType, boolean requestMouseEventsEvenIfSelected) {
        this.toolType = toolType;
        this.requestMouseEventsEvenIfSelected = requestMouseEventsEvenIfSelected;
    }

    public final void notifyCanvasEvent(CanvasEvent e) {
        if (e instanceof CanvasMouseEvent mouseEvent) {
            notifyCanvasMouseEvent(mouseEvent);
        } else if (e instanceof CanvasSelectionCancelEvent cancelEvent) {
            notifyCanvasCancelEvent(cancelEvent);
        } else if (e instanceof CanvasZoomEvent zoomEvent) {
            onZoomEvent(zoomEvent);
        }
    }

    private void notifyCanvasMouseEvent(CanvasMouseEvent e) {
        switch (e.buttonType()) {
            case PRIMARY:
                switch (e.actionType()) {
                    case PRESSED -> onPrimaryButtonPressed(e);
                    case RELEASED -> onPrimaryButtonReleased(e);
                    case CLICKED -> onPrimaryButtonTyped(e);
                    case DRAGGED -> onPrimaryButtonDragged(e);
                }
                break;
            case SECONDARY:
                switch (e.actionType()) {
                    case PRESSED -> onSecondaryButtonPressed(e);
                    case RELEASED -> onSecondaryButtonReleased(e);
                    case CLICKED -> onSecondaryButtonTyped(e);
                    case DRAGGED -> onSecondaryButtonDragged(e);
                }
                break;
        }
    }

    private void notifyCanvasCancelEvent(CanvasSelectionCancelEvent e) {
        switch (e.type()) {
            case ON_CANCEL -> onSelectionCancelled(e);
            case AFTER_CANCEL -> afterSelectionCancelled(e);
        }
    }


    public ToolType getToolType() {
        return toolType;
    }

    public boolean isRequestMouseEventsEvenIfSelected() {
        return requestMouseEventsEvenIfSelected;
    }

    protected void onSelectionCancelled(CanvasSelectionCancelEvent e) {
    }

    protected void afterSelectionCancelled(CanvasSelectionCancelEvent e) {
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

    public void onZoomEvent(CanvasZoomEvent event) {
    }

}
