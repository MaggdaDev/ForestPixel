package org.maggdadev.forestpixel.canvas.history;

import java.util.ArrayDeque;

public class HistoryModel {
    private final ArrayDeque<CanvasChange> redoStack = new ArrayDeque<>();
    private final ArrayDeque<CanvasChange> undoStack = new ArrayDeque<>();

    public void applyNewChange(CanvasChange change) {
        if (change.isChange()) {
            change.apply();
            undoStack.push(change);
            redoStack.clear();
        }
    }

    public void undo() {
        if (!undoStack.isEmpty()) {
            System.out.println("Undo");
            CanvasChange change = undoStack.pop();
            change.undo();
            redoStack.push(change);
        }
    }

    public void redo() {
        if (!redoStack.isEmpty()) {
            CanvasChange change = redoStack.pop();
            change.apply();
            undoStack.push(change);
        }
    }
}
