package org.maggdadev.forestpixel.canvas.tools.viewmodels;

import org.maggdadev.forestpixel.canvas.events.CanvasMouseEvent;
import org.maggdadev.forestpixel.canvas.tools.ToolType;
import org.maggdadev.forestpixel.canvas.tools.models.FreeHandDrawingToolModel;
import org.maggdadev.forestpixel.canvas.utils.PixelUtils;

import java.util.List;

/**
 * Viewmodel for pencil and rubber tool
 */
public class FreeHandDrawingToolViewModel extends ToolViewModel {
    private boolean alreadyPressed = false, alreadyDragging = false;
    private final int[] lastDragPos = new int[]{0,0};

    private final FreeHandDrawingToolModel model;

    public FreeHandDrawingToolViewModel(FreeHandDrawingToolModel model, ToolType toolType) {
        super(toolType);
        this.model = model;
    }

    @Override
    protected void onPrimaryButtonDragged(CanvasMouseEvent e) {
        if(!alreadyDragging) {
            model.applyToPreview(e.canvasModel(), e.canvasContext(), e.xIdx(), e.yIdx());
        } else {
            List<int[]> points = PixelUtils.straightLineFromTo(lastDragPos[0], lastDragPos[1], e.xIdx(), e.yIdx());
            model.applyToPreview(e.canvasModel(), e.canvasContext(), points);
        }
        lastDragPos[0] = e.xIdx();
        lastDragPos[1] = e.yIdx();
        alreadyDragging = true;
    }

    @Override
    protected  void onPrimaryButtonPressed(CanvasMouseEvent e) {
        alreadyPressed = true;
        model.applyToPreview(e.canvasModel(), e.canvasContext(), e.xIdx(), e.yIdx());
    }

    @Override
    protected void onPrimaryButtonReleased(CanvasMouseEvent e) {
        alreadyPressed = false;
        alreadyDragging = false;
        model.applyToCanvas(e.canvasModel(), e.canvasContext(), e.xIdx(), e.yIdx());
    }

}
