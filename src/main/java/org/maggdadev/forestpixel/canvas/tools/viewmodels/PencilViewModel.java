package org.maggdadev.forestpixel.canvas.tools.viewmodels;

import javafx.geometry.Point2D;
import org.maggdadev.forestpixel.canvas.events.CanvasMouseEvent;
import org.maggdadev.forestpixel.canvas.tools.ToolType;
import org.maggdadev.forestpixel.canvas.tools.models.PencilModel;
import org.maggdadev.forestpixel.canvas.tools.models.ToolModel;
import org.maggdadev.forestpixel.canvas.utils.PixelUtils;

import java.util.List;

public class PencilViewModel extends ToolViewModel {
    private boolean alreadyPressed = false, alreadyDragging = false;
    private final int[] lastDragPos = new int[]{0,0};

    private final PencilModel model;

    public PencilViewModel(PencilModel model) {
        super(ToolType.PENCIL);
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
