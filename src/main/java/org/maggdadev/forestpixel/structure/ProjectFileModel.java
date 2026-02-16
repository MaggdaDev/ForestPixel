package org.maggdadev.forestpixel.structure;

import org.maggdadev.forestpixel.canvas.CanvasModel;

public class ProjectFileModel extends ProjectNodeModel{
    private transient CanvasModel canvasModel;
    public ProjectFileModel(String name, ProjectNodeType type, int width, int height) {
        super(name, type);
        canvasModel = new CanvasModel(width, height);
    }

    public void setCanvasModel(CanvasModel canvasModel) {
        this.canvasModel = canvasModel;
    }


    public CanvasModel getCanvas() {
        return canvasModel;
    }
}
