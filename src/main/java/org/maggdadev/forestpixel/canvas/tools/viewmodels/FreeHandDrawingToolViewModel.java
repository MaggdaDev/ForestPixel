package org.maggdadev.forestpixel.canvas.tools.viewmodels;

import org.maggdadev.forestpixel.canvas.events.CanvasMouseEvent;
import org.maggdadev.forestpixel.canvas.tools.ToolType;
import org.maggdadev.forestpixel.canvas.tools.models.FreeHandDrawingToolModel;

/**
 * Viewmodel for pencil and rubber tool
 */
public class FreeHandDrawingToolViewModel extends ToolViewModel {
    private final int[] lastDragPos = new int[]{0, 0};
    private boolean alreadyDrawing = false;

    private final FreeHandDrawingToolModel model;

    public FreeHandDrawingToolViewModel(FreeHandDrawingToolModel model, ToolType toolType) {
        super(toolType);
        this.model = model;
    }

    @Override
    protected void onPrimaryButtonDragged(CanvasMouseEvent e) {
        if (!alreadyDrawing) {
            alreadyDrawing = true;
            lastDragPos[0] = e.xIdx();
            lastDragPos[1] = e.yIdx();
        }
        model.applyToPreview(e.canvasModel(), e.canvasContext(), lastDragPos[0], lastDragPos[1], e.xIdx(), e.yIdx());   // start == last drag pos
        lastDragPos[0] = e.xIdx();
        lastDragPos[1] = e.yIdx();
    }

    @Override
    protected void onPrimaryButtonPressed(CanvasMouseEvent e) {
        alreadyDrawing = true;
        lastDragPos[0] = e.xIdx();
        lastDragPos[1] = e.yIdx();
        model.applyToPreview(e.canvasModel(), e.canvasContext(), e.xIdx(), e.yIdx(), e.xIdx(), e.yIdx());   // start == end
    }

    @Override
    protected void onPrimaryButtonReleased(CanvasMouseEvent e) {
        super.onPrimaryButtonReleased(e);
        model.applyToCanvas(e.canvasModel(), e.canvasContext(), e.xIdx(), e.yIdx());
        alreadyDrawing = false;
    }
}
