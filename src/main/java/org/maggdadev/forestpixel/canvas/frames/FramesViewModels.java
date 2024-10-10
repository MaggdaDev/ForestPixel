package org.maggdadev.forestpixel.canvas.frames;

import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.collections.ListChangeListener;
import org.maggdadev.forestpixel.canvas.CanvasContext;
import org.maggdadev.forestpixel.canvas.CanvasModel;
import org.maggdadev.forestpixel.canvas.utils.SwappableObservableArrayList;

/**
 * Class that wraps an observable list and keeps track of changes to install all listeners and bindings
 */
public class FramesViewModels {
    private final SwappableObservableArrayList<FrameViewModel> frames = new SwappableObservableArrayList<>();
    private final CanvasContext context;

    private final IntegerProperty activeLayerOrder = new SimpleIntegerProperty(-1), activeFrameOrder = new SimpleIntegerProperty(-1);

    private final StringProperty activeLayerId = new SimpleStringProperty("-1"), activeFrameId = new SimpleStringProperty("-1");

    private CanvasModel model;

    private ListChangeListener<? super FrameViewModel> listenerOnFrames;

    public FramesViewModels(CanvasContext context) {
        this.context = context;
        activeFrameId.addListener((observable, oldValue, newValue) -> {
            System.out.println("Active frame id changed from " + oldValue + " to " + newValue);
        });

        // Listener for bindings + assert always one frame is selected
        frames.addListener((ListChangeListener<? super FrameViewModel>) (change) -> {
            while (change.next()) {
                if (change.wasAdded() || change.wasRemoved()) {
                    if (activeFrameOrder.get() == -1 && !frames.isEmpty()) {
                        frames.getFirst().selectedProperty().set(true);
                    }
                    refreshBindings();
                }
            }
        });


        activeFrameOrder.bind(Bindings.createIntegerBinding(() -> frames.indexOf(getActiveFrameViewModel()), frames, activeFrameId));
        activeFrameId.subscribe((newValue) -> {
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
            for (FrameViewModel frame : frames) {
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
        if (listenerOnFrames != null) {
            frames.removeListener(listenerOnFrames);
        }
        if (model == null) {
            return;
        }
        model.getFrames().forEach(this::addFrame);
        frames.addListener(listenerOnFrames = (change) -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    change.getAddedSubList().forEach(frameViewModel -> {
                        model.addExistingFrame(frameViewModel.getModel());
                    });
                }
                if (change.wasRemoved()) {
                    change.getRemoved().forEach(frameViewModel -> {
                        model.removeFrame(frameViewModel.getModel().getId());
                    });
                }
                if (change.wasPermutated()) {
                    SwappableObservableArrayList.applyPermutationsToList(model.getFrames(), change);
                }
            }
        });
        refreshBindings();
    }

    public FrameViewModel getActiveFrameViewModel() {
        return frames.stream().filter((frameViewModel -> frameViewModel.getId().equals(getActiveFrameId()))).findFirst().orElse(null);
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
