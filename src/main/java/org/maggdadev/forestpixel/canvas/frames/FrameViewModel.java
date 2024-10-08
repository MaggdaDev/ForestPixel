package org.maggdadev.forestpixel.canvas.frames;

import javafx.beans.property.*;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import org.maggdadev.forestpixel.canvas.CanvasContext;
import org.maggdadev.forestpixel.canvas.layers.LayerViewModel;
import org.maggdadev.forestpixel.canvas.layers.LayersViewModels;
import org.maggdadev.forestpixel.canvas.utils.Selectable;
import org.maggdadev.forestpixel.canvas.utils.SwappableObservableArrayList;

public class FrameViewModel implements Selectable {
    private final FrameModel frameModel;

    private final ObjectProperty<Image> thumbnail = new SimpleObjectProperty<>();
    private final StringProperty name = new SimpleStringProperty();

    private final BooleanProperty selected = new SimpleBooleanProperty(false);

    private final LayersViewModels layersViewModels;

    public FrameViewModel(FrameModel frameModel, CanvasContext context) {
        this.frameModel = frameModel;
        layersViewModels = new LayersViewModels(frameModel, context);
        selectedProperty().subscribe((newValue) -> {
            if (!newValue) {
                redrawThumbnail();
            }
        });
        setName(frameModel.getName());
        nameProperty().subscribe(frameModel::setName);
    }

    private void redrawThumbnail() {
        setThumbnail(exportToImage());
    }

    public Image exportToImage() {
        WritableImage output = new WritableImage(frameModel.getWidthPixels(), frameModel.getHeightPixels());
        Color color;
        for (LayerViewModel layer : layersViewModels.getLayersUnmodifiable()) {
            for (int i = 0; i < frameModel.getWidthPixels(); i++) {
                for (int j = 0; j < frameModel.getHeightPixels(); j++) {
                    color = layer.getDrawableImage().getPixelReader().getColor(i, j);
                    if (color.equals(Color.TRANSPARENT)) {
                        continue;
                    }
                    output.getPixelWriter().setColor(i, j, color);
                }
            }

        }
        return output;
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

    public Image getThumbnail() {
        return thumbnail.get();
    }

    public ObjectProperty<Image> thumbnailProperty() {
        return thumbnail;
    }

    public void setThumbnail(Image thumbnail) {
        this.thumbnail.set(thumbnail);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }


}
