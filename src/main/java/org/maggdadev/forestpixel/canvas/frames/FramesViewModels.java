package org.maggdadev.forestpixel.canvas.frames;

import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import org.maggdadev.forestpixel.canvas.CanvasContext;
import org.maggdadev.forestpixel.canvas.CanvasModel;
import org.maggdadev.forestpixel.canvas.utils.SwappableObservableArrayList;

import java.util.Collections;
import java.util.function.Consumer;

/**
 * Class that wraps an observable list and keeps track of changes to install all listeners and bindings
 */
public class FramesViewModels {
    private final SwappableObservableArrayList<FrameViewModel> frames = new SwappableObservableArrayList<>();
    private final CanvasContext context;

    private final IntegerProperty activeLayerOrder = new SimpleIntegerProperty(-1), activeFrameOrder = new SimpleIntegerProperty(-1);

    private final StringProperty activeLayerId = new SimpleStringProperty("-1"), activeFrameId = new SimpleStringProperty("-1");

    private CanvasModel model;

    private Consumer<FrameViewModel> frameAddedListener, frameRemovedListener;
    private Consumer<SwappableObservableArrayList.Swap> frameSwappedListener;

    public FramesViewModels(CanvasContext context) {
        this.context = context;
        activeFrameId.addListener((observable, oldValue, newValue) -> {
            System.out.println("Active frame id changed from " + oldValue + " to " + newValue);
        });


        // Listener for bindings + assert always one frame is selected
        Consumer<FrameViewModel> selectFirstIfNoneSelectedAndRefreshBindings = (_) -> {
            if (activeFrameOrder.get() == -1 && !frames.isEmpty()) {
                frames.getFirst().selectedProperty().set(true);
            }
            refreshBindings();
        };
        frames.addOnElementAdded(selectFirstIfNoneSelectedAndRefreshBindings);
        frames.addOnElementRemoved(selectFirstIfNoneSelectedAndRefreshBindings);


        activeFrameOrder.bind(Bindings.createIntegerBinding(() -> frames.indexOf(getActiveFrameViewModel()), frames.getUnmodifiable(), activeFrameId));
        activeFrameId.subscribe((_) -> {
            activeLayerId.unbind();
            activeLayerOrder.unbind();
            if (getActiveFrameViewModel() != null) {
                activeLayerId.bind(getActiveFrameViewModel().activeLayerIdProperty());
                activeLayerOrder.bind(getActiveFrameViewModel().activeLayerOrderProperty());
            }
        });
    }

    private void refreshBindings() {
        activeFrameId.unbind();
        BooleanProperty[] framesSelectedProperties = new BooleanProperty[frames.size()];
        for (int i = 0; i < frames.size(); i++) {
            framesSelectedProperties[i] = frames.get(i).selectedProperty();
        }
        activeFrameId.bind(Bindings.createStringBinding(() -> {
            for (FrameViewModel frame : frames.getUnmodifiable()) {
                if (frame.selectedProperty().get()) {
                    return frame.getId();
                }
            }
            return "-1";
        }, framesSelectedProperties));
    }

    public void setModel(CanvasModel model) {
        this.model = model;
        frames.clear();
        if (frameAddedListener != null) {
            frames.removeOnElementAdded(frameAddedListener);
        }
        if (frameRemovedListener != null) {
            frames.removeOnElementRemoved(frameRemovedListener);
        }
        if (frameSwappedListener != null) {
            frames.removeOnSwap(frameSwappedListener);
        }
        if (model == null) {
            return;
        }
        model.getFrames().forEach(this::addFrame);
        frames.addOnElementAdded(frameAddedListener = (frame) -> {
            model.addExistingFrame(frame.getModel());
        });
        frames.addOnElementRemoved(frameRemovedListener = (frame) -> {
            model.removeFrame(frame.getId());
        });
        frames.addOnSwap(frameSwappedListener = (swap) -> {
            Collections.swap(model.getFrames(), swap.index1(), swap.index2());
        });
        refreshBindings();
    }

    public FrameViewModel getActiveFrameViewModel() {
        return frames.getUnmodifiable().stream().filter((frameViewModel -> frameViewModel.getId().equals(getActiveFrameId()))).findFirst().orElse(null);
    }



    public SwappableObservableArrayList<FrameViewModel> getFrames() {
        return frames;
    }

    public void addFrame() {
        FrameModel frameModel = new FrameModel(model.getWidthPixels(), model.getHeightPixels());
        addFrame(frameModel);
    }

    public void addFrame(FrameModel frameModel) {
        frames.add(new FrameViewModel(frameModel, context));
    }

    public void cloneSelectedFrame() {
        if (isFrameSelected()) {
            FrameModel clonedFrame = getActiveFrameViewModel().getModel().clone();
            addFrame(clonedFrame);
        }
    }

    // GET/SET

    public String getActiveFrameId() {
        return activeFrameId.get();
    }

    public StringProperty activeFrameIdProperty() {
        return activeFrameId;
    }

    public int getActiveLayerOrder() {
        return activeLayerOrder.get();
    }

    public IntegerProperty activeLayerOrderProperty() {
        return activeLayerOrder;
    }

    public String getActiveLayerId() {
        return activeLayerId.get();
    }

    public StringProperty activeLayerIdProperty() {
        return activeLayerId;
    }

    public int getActiveFrameOrder() {
        return activeFrameOrder.get();
    }

    public boolean isFrameSelected() {
        return !getActiveFrameId().equals("-1");
    }


}
