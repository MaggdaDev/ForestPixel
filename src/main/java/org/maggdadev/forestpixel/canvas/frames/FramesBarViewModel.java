package org.maggdadev.forestpixel.canvas.frames;

import org.maggdadev.forestpixel.canvas.utils.SwappableObservableArrayList;

public class FramesBarViewModel {
    private final FrameViewModels frameViewModels;

    public FramesBarViewModel(FrameViewModels frameViewModels) {
        this.frameViewModels = frameViewModels;
    }

    public SwappableObservableArrayList<FrameViewModel> getFrames() {
        return frameViewModels.getFrames();
    }

    public void addFrame() {
        frameViewModels.addFrame();
    }
}
