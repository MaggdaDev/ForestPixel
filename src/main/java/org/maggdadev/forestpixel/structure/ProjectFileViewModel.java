package org.maggdadev.forestpixel.structure;

import org.maggdadev.forestpixel.canvas.CanvasViewModel;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ProjectFileViewModel extends ProjectNodeViewModel implements ResourceIOAgent {
    private final ProjectFileModel model;
    private CanvasViewModel canvasViewModel;
    private boolean isLoaded = false;
    public ProjectFileViewModel(ProjectFileModel model) {
        super(model);
        this.model = model;
        canvasViewModel = new CanvasViewModel(model.getCanvas());
        isLoaded = true;
    }

    public void saveTo(OutputStream out) throws IOException {
        canvasViewModel.saveModelTo(out);
    }

    public void loadFrom(InputStream in) throws IOException {
        canvasViewModel.loadModelFrom(in);
        model.setCanvasModel(canvasViewModel.getModel());
    }

    public CanvasViewModel getCanvas() {
        return canvasViewModel;
    }
}
