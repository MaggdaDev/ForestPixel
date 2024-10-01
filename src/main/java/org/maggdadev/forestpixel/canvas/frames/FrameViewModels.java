package org.maggdadev.forestpixel.canvas.frames;

import org.maggdadev.forestpixel.canvas.CanvasContext;
import org.maggdadev.forestpixel.canvas.CanvasModel;
import org.maggdadev.forestpixel.canvas.utils.SwappableObservableArrayList;

/**
 * Class that wraps an observable list and keeps track of changes to install all listeners and bindings
 */
public class FrameViewModels {
    private final SwappableObservableArrayList<FrameViewModel> frames = new SwappableObservableArrayList<>();

    public FrameViewModels(CanvasModel model, CanvasContext context) {
        model.getFrames().forEach(this::addFrame);
    }

    public SwappableObservableArrayList<FrameViewModel> getFrames() {
        return frames;
    }

    public void addFrame() {
        FrameModel frameModel = new FrameModel();
        addFrame(frameModel);
    }

    public void addFrame(FrameModel frameModel) {
        frames.add(new FrameViewModel(frameModel));
    }
}
