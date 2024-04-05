package org.maggdadev.forestpixel.canvas.toolbar;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import org.maggdadev.forestpixel.canvas.tools.viewmodels.ToolViewModel;

public class ToolbarViewModel {
    private final ObjectProperty<ToolViewModel> activeToolViewModel = new SimpleObjectProperty<>();
    public ToolbarViewModel() {

    }

    public ObservableValue<? extends ToolViewModel> activeToolViewModelProperty() {
        return activeToolViewModel;
    }

    public void setActiveToolViewModel(ToolViewModel activeToolViewModel) {
        this.activeToolViewModel.set(activeToolViewModel);
    }
}
