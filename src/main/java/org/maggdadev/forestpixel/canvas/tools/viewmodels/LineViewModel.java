package org.maggdadev.forestpixel.canvas.tools.viewmodels;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.maggdadev.forestpixel.canvas.events.CanvasMouseEvent;
import org.maggdadev.forestpixel.canvas.tools.ToolType;
import org.maggdadev.forestpixel.canvas.tools.models.LineModel;

public class LineViewModel extends ToolViewModel{
    private final LineModel model;
    private final ObjectProperty<LineState> lineState = new SimpleObjectProperty<>(LineState.IDLE);

    public LineViewModel(LineModel model) {
        super(ToolType.LINE);
        this.model = model;
    }

    @Override
    protected void onPrimaryButtonDragged(CanvasMouseEvent e) {
        super.onPrimaryButtonDragged(e);
        if (lineState.get().equals(LineState.IDLE)) {
            model.setStartLine(e.xIdx(), e.yIdx());
            lineState.set(LineState.DRAWING);
        }
        boolean dirty = e.xIdx() != model.getEndX() || e.yIdx() != model.getEndY();
        model.setEndLine(e.xIdx(), e.yIdx());
        if (dirty) {
            model.applyToPreview(e.canvasModel(), e.canvasContext(), e.xIdx(), e.yIdx());
        }
    }

    @Override
    protected void onPrimaryButtonReleased(CanvasMouseEvent e) {
        super.onPrimaryButtonReleased(e);
        if (lineState.get().equals(LineState.DRAWING)) {
            model.applyToCanvas(e.canvasModel(), e.canvasContext(), e.xIdx(), e.yIdx());
            model.resetStartAndEnd();
            lineState.set(LineState.IDLE);
        }
    }

    private enum LineState {
        IDLE, DRAWING
    }
}
