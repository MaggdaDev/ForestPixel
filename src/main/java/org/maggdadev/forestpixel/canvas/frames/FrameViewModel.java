package org.maggdadev.forestpixel.canvas.frames;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import org.maggdadev.forestpixel.canvas.utils.Selectable;

public class FrameViewModel implements Selectable {
    private final FrameModel frameModel;

    private final BooleanProperty selected = new SimpleBooleanProperty(false);

    public FrameViewModel(FrameModel frameModel) {
        this.frameModel = frameModel;
    }


    @Override
    public String getId() {
        return frameModel.getId();
    }

    @Override
    public BooleanProperty selectedProperty() {
        return selected;
    }
}
