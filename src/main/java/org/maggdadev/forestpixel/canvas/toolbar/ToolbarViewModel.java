package org.maggdadev.forestpixel.canvas.toolbar;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.paint.Color;
import org.maggdadev.forestpixel.canvas.tools.viewmodels.ToolViewModel;

public class ToolbarViewModel {
    private final ObjectProperty<ToolViewModel> activeToolViewModel = new SimpleObjectProperty<>();
    private final BooleanProperty colorPickingVisible = new SimpleBooleanProperty(false);

    private final ObjectProperty<Color> color = new SimpleObjectProperty<>();
    public ToolbarViewModel() {
        this.activeToolViewModel.addListener((obs, oldVal, newVal) -> {
            if (newVal == null) {
                colorPickingVisible.set(false);
            } else {
                colorPickingVisible.set(newVal.getToolType().USES_COLOR);
            }
        });
    }

    public ObservableValue<? extends ToolViewModel> activeToolViewModelProperty() {
        return activeToolViewModel;
    }

    public void setActiveToolViewModel(ToolViewModel activeToolViewModel) {
        this.activeToolViewModel.set(activeToolViewModel);
    }

    public BooleanProperty colorPickingVisibleProperty() {
        return colorPickingVisible;
    }

    public Color getColor() {
        return color.get();
    }

    public ObjectProperty<Color> colorProperty() {
        return color;
    }
}
