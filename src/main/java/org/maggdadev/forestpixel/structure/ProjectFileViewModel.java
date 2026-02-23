package org.maggdadev.forestpixel.structure;

import org.maggdadev.forestpixel.canvas.CanvasModel;
import org.maggdadev.forestpixel.canvas.CanvasViewModel;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ProjectFileViewModel extends ProjectNodeViewModel implements ResourceIOAgent {
    private final ProjectFileModel model;
    public ProjectFileViewModel(ProjectFileModel model, ProjectViewModel rootViewModel) {
        super(model, rootViewModel);
        this.model = model;
    }

    public void saveTo(OutputStream out) throws IOException {
        model.getCanvas().saveTo(out);
    }

    public void loadFrom(InputStream in) throws IOException {
        model.setCanvasModel(CanvasModel.loadFrom(in));
    }

    public CanvasModel getCanvasModel() {
        return model.getCanvas();
    }

    @Override
    public void open() {
        rootViewModel.openFile(this);
    }
}
