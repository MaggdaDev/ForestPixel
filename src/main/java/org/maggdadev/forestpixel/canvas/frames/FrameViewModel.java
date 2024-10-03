package org.maggdadev.forestpixel.canvas.frames;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleBooleanProperty;
import org.maggdadev.forestpixel.canvas.CanvasContext;
import org.maggdadev.forestpixel.canvas.layers.LayerViewModel;
import org.maggdadev.forestpixel.canvas.layers.LayersViewModels;
import org.maggdadev.forestpixel.canvas.utils.Selectable;
import org.maggdadev.forestpixel.canvas.utils.SwappableObservableArrayList;

public class FrameViewModel implements Selectable {
    private final FrameModel frameModel;

    private final BooleanProperty selected = new SimpleBooleanProperty(false);

    private final LayersViewModels layersViewModels;

    public FrameViewModel(FrameModel frameModel, CanvasContext context) {
        this.frameModel = frameModel;
        layersViewModels = new LayersViewModels(frameModel, context);
    }


    @Override
    public String getId() {
        return frameModel.getId();
    }

    @Override
    public BooleanProperty selectedProperty() {
        return selected;
    }

    public SwappableObservableArrayList<LayerViewModel> getLayers() {
        return layersViewModels.getLayers();
    }

    public ReadOnlyStringProperty activeLayerIdProperty() {
        return layersViewModels.activeLayerIdProperty();
    }

    public ReadOnlyIntegerProperty activeLayerOrderProperty() {
        return layersViewModels.activeLayerOrderProperty();
    }

    public LayersViewModels getLayersViewModels() {
        return layersViewModels;
    }

    public FrameModel getModel() {
        return frameModel;
    }
}
