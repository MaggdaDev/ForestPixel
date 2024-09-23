package org.maggdadev.forestpixel.canvas.tools.viewmodels;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.maggdadev.forestpixel.canvas.events.CanvasMouseEvent;
import org.maggdadev.forestpixel.canvas.events.CanvasSelectionCancelEvent;
import org.maggdadev.forestpixel.canvas.tools.ToolType;
import org.maggdadev.forestpixel.canvas.tools.models.MoveModel;

import java.util.function.BiConsumer;

public class MoveViewModel extends ToolViewModel {
    private final MoveModel model;
    private final ObjectProperty<MoveState> moveState = new SimpleObjectProperty<>(MoveState.IDLE);

    private final BooleanProperty isMouseInSelectArea;

    private final BiConsumer<Double, Double> moveCanvasFunction;

    private double moveCanvasStartX = -1, moveCanvasStartY = -1;

    public MoveViewModel(MoveModel model, BooleanProperty isMouseInSelectArea, BiConsumer<Double, Double> moveCanvasFunction) {
        super(ToolType.MOVE, true);
        this.model = model;
        this.isMouseInSelectArea = isMouseInSelectArea;
        this.moveCanvasFunction = moveCanvasFunction;
    }

    @Override
    protected void onPrimaryButtonPressed(CanvasMouseEvent e) {
        super.onPrimaryButtonPressed(e);
        if (moveState.get().equals(MoveState.IDLE)) {
            model.setStartMove(e.pixelXPos() / e.canvasContext().getZoomFactor(), e.pixelYPos() / e.canvasContext().getZoomFactor());
            if (isMouseInSelectArea.get()) {
                moveState.set(MoveState.MOVING_PREVIEW);
            } else {
                moveCanvasStartX = e.pixelXPos() / e.canvasContext().getZoomFactor();
                moveCanvasStartY = e.pixelYPos() / e.canvasContext().getZoomFactor();
                moveState.set(MoveState.MOVING_CANVAS);
            }
        }
    }

    @Override
    protected void onPrimaryButtonDragged(CanvasMouseEvent e) {
        super.onPrimaryButtonDragged(e);
        model.setEndMove(e.pixelXPos() / e.canvasContext().getZoomFactor(), e.pixelYPos() / e.canvasContext().getZoomFactor(),
                e.canvasModel().getWidthPixels(), e.canvasModel().getHeightPixels());
        if (moveState.get().equals(MoveState.MOVING_PREVIEW)) {
            if (model.isDirty()) {
                model.applyToPreview(e.canvasModel(), e.canvasContext(), e.xIdx(), e.yIdx());
            }
        } else {
            moveCanvasFunction.accept(e.pixelXPos() / e.canvasContext().getZoomFactor() - moveCanvasStartX, e.pixelYPos() / e.canvasContext().getZoomFactor() - moveCanvasStartY);
        }


    }

    @Override
    protected void onPrimaryButtonReleased(CanvasMouseEvent e) {
        super.onPrimaryButtonReleased(e);
        if (moveState.get().equals(MoveState.MOVING_PREVIEW)) {
            model.applyToPreview(e.canvasModel(), e.canvasContext(), e.xIdx(), e.yIdx());
            model.terminateSubMovement();
        } else {
            moveCanvasStartX = -1;
            moveCanvasStartY = -1;
        }
        moveState.set(MoveState.IDLE);
    }

    @Override
    protected void afterSelectionCancelled(CanvasSelectionCancelEvent e) {
        super.onSelectionCancelled(e);
        model.resetAll(e.context());
    }


    public enum MoveState {
        IDLE,
        MOVING_PREVIEW,
        MOVING_CANVAS
    }
}
