package org.maggdadev.forestpixel.canvas.frames;

import org.maggdadev.forestpixel.canvas.utils.SwappableObservableArrayList;

public class FramesBarViewModel {
    private final FramesViewModels framesViewModels;

    public FramesBarViewModel(FramesViewModels framesViewModels) {
        this.framesViewModels = framesViewModels;
    }

    public SwappableObservableArrayList<FrameViewModel> getFrames() {
        return framesViewModels.getFrames();
    }

    public void addFrame() {
        framesViewModels.addFrame();
    }
}
