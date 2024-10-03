package org.maggdadev.forestpixel.canvas.layers;

import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import org.maggdadev.forestpixel.canvas.frames.FrameViewModel;
import org.maggdadev.forestpixel.canvas.frames.FramesViewModels;
import org.maggdadev.forestpixel.canvas.utils.SwappableObservableArrayList;

import java.util.ArrayList;
import java.util.List;

public class LayersBarViewModel {
    private final StringProperty activeLayerProperty = new SimpleStringProperty("-1");

    private final DoubleProperty upperLayersOpacity = new SimpleDoubleProperty(1.0), lowerLayersOpacity = new SimpleDoubleProperty(1.0);

    private final BooleanProperty moreThanOneLayer = new SimpleBooleanProperty(false);

    private final StringProperty activeFrameId = new SimpleStringProperty("-1");

    private final ObjectProperty<LayersViewModels> layersViewModelsOfActiveFrame = new SimpleObjectProperty<>();

    private final SwappableObservableArrayList<LayerViewModel> currentLayers = new SwappableObservableArrayList<>();

    private ListChangeListener<LayerViewModel> currentListenerToViewModelLayers;


    public LayersBarViewModel(FramesViewModels framesViewModels) {
        layersViewModelsOfActiveFrame.subscribe((newValue) -> {
            if (newValue != null) {
                moreThanOneLayer.unbind();
                moreThanOneLayer.bind(Bindings.size(newValue.getLayersUnmodifiable()).greaterThan(1));
            }
        });

        layersViewModelsOfActiveFrame.subscribe((oldValue, newValue) -> {
            if (oldValue != null) {
                if (currentListenerToViewModelLayers != null) {
                    oldValue.getLayersUnmodifiable().removeListener(currentListenerToViewModelLayers);
                    currentListenerToViewModelLayers = null;
                }
            }
            if (newValue != null) {
                currentLayers.setAll(newValue.getLayers());
                newValue.getLayersUnmodifiable().addListener(currentListenerToViewModelLayers = createListChangeListener(currentLayers));
            } else {
                currentLayers.clear();
            }
        });
        layersViewModelsOfActiveFrame.bind(Bindings.createObjectBinding(() -> { // After change subscription such that it is initially called
            FrameViewModel activeFrame = framesViewModels.getActiveFrameViewModel();
            if (activeFrame != null) {
                return activeFrame.getLayersViewModels();
            }
            return null;
        }, framesViewModels.activeFrameIdProperty()));
    }

    public void addNewLayer() {
        if (layersViewModelsOfActiveFrame.get() != null) {
            layersViewModelsOfActiveFrame.get().addNewLayer();
        }
    }

    private static ListChangeListener<LayerViewModel> createListChangeListener(SwappableObservableArrayList<LayerViewModel> listToUpdate) {
        return (change) -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    listToUpdate.addAll(change.getAddedSubList());
                }
                if (change.wasRemoved()) {
                    for (LayerViewModel layerViewModel : change.getRemoved()) {
                        listToUpdate.remove(layerViewModel);
                    }
                }
                if (change.wasPermutated()) {
                    int[] perm = new int[change.getTo() - change.getFrom()];
                    for (int i = change.getFrom(); i < change.getTo(); i++) {
                        perm[i - change.getFrom()] = change.getPermutation(i);
                    }
                    List<int[]> swaps = getSwaps(perm);
                    for (int[] swap : swaps) {
                        listToUpdate.swap(swap[0], swap[1]);
                    }
                    List<LayerViewModel> temp = new ArrayList<>(listToUpdate);
                    for (int i = change.getFrom(); i < change.getTo(); i++) {
                        listToUpdate.set(i, temp.get(change.getPermutation(i)));
                    }
                }
            }
        };
    }

    // A static method that takes a long permutation consiting of multiple swaps and returns a list of size 2 arrays of integers, where each array represents a swap.
    private static List<int[]> getSwaps(int[] permutation) {
        List<int[]> swaps = new ArrayList<>();
        boolean[] visited = new boolean[permutation.length];
        for (int i = 0; i < permutation.length; i++) {
            if (permutation[i] != i && !visited[i] && !visited[permutation[i]]) {
                swaps.add(new int[]{i, permutation[i]});
                visited[i] = true;
                visited[permutation[i]] = true;
            }
        }
        return swaps;
    }

    public ObservableValue<String> activeLayerIdProperty() {
        return activeLayerProperty;
    }

    public void setActiveLayer(String id) {
        activeLayerProperty.set(id);
    }

    public void swapLayers(String id1, String id2) {
        if (layersViewModelsOfActiveFrame.get() != null) {
            layersViewModelsOfActiveFrame.get().swap(id1, id2);
        }
    }

    public void removeLayer(String s) {
        if (layersViewModelsOfActiveFrame.get() != null) {
            layersViewModelsOfActiveFrame.get().remove(s);
        }
    }

    public double getUpperLayersOpacity() {
        return upperLayersOpacity.get();
    }

    public DoubleProperty upperLayersOpacityProperty() {
        return upperLayersOpacity;
    }

    public double getLowerLayersOpacity() {
        return lowerLayersOpacity.get();
    }

    public DoubleProperty lowerLayersOpacityProperty() {
        return lowerLayersOpacity;
    }

    public boolean isMoreThanOneLayer() {
        return moreThanOneLayer.get();
    }

    public BooleanProperty moreThanOneLayerProperty() {
        return moreThanOneLayer;
    }

    public Property<String> activeFrameIdProperty() {
        return activeFrameId;
    }

    public SwappableObservableArrayList<LayerViewModel> getCurrentLayers() {
        return currentLayers;
    }


}
