package org.maggdadev.forestpixel.canvas.tools.models;

public class SelectModel extends ToolModel {
    private int selectionStartIdxX, selectionStartIdxY, selectionEndIdxX, selectionEndIdxY;

    public void resetSelection() {
        selectionStartIdxX = -1;
        selectionStartIdxY = -1;
        selectionEndIdxX = -1;
        selectionEndIdxY = -1;
    }


    // GET/SET


    public int getSelectionStartIdxX() {
        return selectionStartIdxX;
    }

    public void setSelectionStartIdxX(int selectionStartIdxX) {
        this.selectionStartIdxX = selectionStartIdxX;
    }

    public int getSelectionStartIdxY() {
        return selectionStartIdxY;
    }

    public void setSelectionStartIdxY(int selectionStartIdxY) {
        this.selectionStartIdxY = selectionStartIdxY;
    }

    public int getSelectionEndIdxX() {
        return selectionEndIdxX;
    }

    public void setSelectionEndIdxX(int selectionEndIdxX) {
        this.selectionEndIdxX = selectionEndIdxX;
    }

    public int getSelectionEndIdxY() {
        return selectionEndIdxY;
    }

    public void setSelectionEndIdxY(int selectionEndIdxY) {
        this.selectionEndIdxY = selectionEndIdxY;
    }
}
