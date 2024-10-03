package org.maggdadev.forestpixel.canvas.frames;

import javafx.scene.input.KeyCode;
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

    public void cloneSelectedFrame() {
        framesViewModels.cloneSelectedFrame();
    }

    public void notifyKeyPressed(KeyCode code) {
        int selectedOrder = framesViewModels.getActiveFrameOrder();
        switch (code) {
            case LEFT:
                if (selectedOrder <= 0) {
                    return;
                }
                selectedOrder--;
                break;
            case RIGHT:
                if (selectedOrder >= framesViewModels.getFrames().size() - 1) {
                    return;
                }
                selectedOrder++;
                break;
            default:
                return;
        }
        framesViewModels.getActiveFrameViewModel().selectedProperty().set(false);
        framesViewModels.getFrames().get(selectedOrder).selectedProperty().set(true);
    }


}
