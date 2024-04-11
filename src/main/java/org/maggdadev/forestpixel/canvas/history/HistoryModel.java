package org.maggdadev.forestpixel.canvas.history;

import org.maggdadev.forestpixel.canvas.CanvasModel;

import java.util.ArrayDeque;

public class HistoryModel {
    private final ArrayDeque<CanvasChange> redoStack = new ArrayDeque<>();
    private final ArrayDeque<CanvasChange> undoStack = new ArrayDeque<>();

    private final CanvasModel canvasModel;

    public HistoryModel(CanvasModel canvasModel) {
        this.canvasModel = canvasModel;
    }

    public void applyNewChange(CanvasChange change) {
        change.applyToImage(canvasModel.getImage());
        undoStack.push(change);;
        redoStack.clear();
    }

    public void undo() {
        if (!undoStack.isEmpty()) {
            System.out.println("Undo");
            CanvasChange change = undoStack.pop();
            change.undoToImage(canvasModel.getImage());
            redoStack.push(change);
        }
    }

    public void redo() {
        if (!redoStack.isEmpty()) {
            CanvasChange change = redoStack.pop();
            change.applyToImage(canvasModel.getImage());
            undoStack.push(change);
        }
    }
}
