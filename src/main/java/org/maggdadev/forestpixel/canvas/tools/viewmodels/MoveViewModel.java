package org.maggdadev.forestpixel.canvas.tools.viewmodels;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.maggdadev.forestpixel.canvas.events.CanvasMouseEvent;
import org.maggdadev.forestpixel.canvas.tools.ToolType;
import org.maggdadev.forestpixel.canvas.tools.models.MoveModel;

public class MoveViewModel extends ToolViewModel{
    private final MoveModel model;
    private final ObjectProperty<MoveState> moveState = new SimpleObjectProperty<>(MoveState.IDLE);
    public MoveViewModel(MoveModel model) {
        super(ToolType.MOVE, true);
        this.model = model;
    }

    @Override
    protected void onPrimaryButtonDragged(CanvasMouseEvent e) {
        super.onPrimaryButtonDragged(e);
        if(moveState.get().equals(MoveState.IDLE)) {
            startMove(e);
            model.setEndMove(e.pixelXPos(), e.pixelYPos());
        }
        model.setEndMove(e.pixelXPos(), e.pixelYPos());
        if(model.isDirty()) {
            model.applyToPreview(e.canvasModel(), e.canvasContext(), e.xIdx(), e.yIdx());
        }

    }

    @Override
    protected void onSelectionCancelled(CanvasMouseEvent e) {
        super.onSelectionCancelled(e);
        model.reset();
    }

    private void startMove(CanvasMouseEvent e) {
        model.setStartMove(e.pixelXPos(), e.pixelYPos());
        moveState.set(MoveState.MOVING);
    }

    public enum MoveState {
        IDLE,
        MOVING
    }
}
