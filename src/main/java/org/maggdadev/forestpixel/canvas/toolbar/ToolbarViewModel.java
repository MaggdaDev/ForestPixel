package org.maggdadev.forestpixel.canvas.toolbar;

import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.scene.paint.Color;
import org.maggdadev.forestpixel.canvas.tools.ToolType;
import org.maggdadev.forestpixel.canvas.tools.models.*;
import org.maggdadev.forestpixel.canvas.tools.viewmodels.*;

import java.util.List;

public class ToolbarViewModel {
    private final ObjectProperty<ToolViewModel> activeToolViewModel = new SimpleObjectProperty<>();
    private final ObjectProperty<ToolViewModel> previousActiveToolViewModel = new SimpleObjectProperty<>();
    private final BooleanProperty colorPickingVisible = new SimpleBooleanProperty(false);


    private final ObjectProperty<Color> color = new SimpleObjectProperty<>();

    private final List<ToolViewModel> toolViewModelList;
    public ToolbarViewModel() {
        this.activeToolViewModel.addListener((obs, oldVal, newVal) -> {
            if (newVal == null) {
                colorPickingVisible.set(false);
            } else {
                colorPickingVisible.set(newVal.getToolType().USES_COLOR);
            }
            if(oldVal != null &&  !oldVal.equals(newVal)) {
                previousActiveToolViewModel.set(oldVal);
            }
        });

        toolViewModelList = List.of(
                new FreeHandDrawingToolViewModel(new FreeHandDrawingToolModel((canvasModel, canvasContext) -> canvasContext.getColor()), ToolType.PENCIL), // pencil
                new BucketViewModel(new BucketModel()),
                new PipetViewModel(new PipetModel(), this),
                new FreeHandDrawingToolViewModel(new FreeHandDrawingToolModel((canvasModel, canvasContext) -> Color.WHITE), ToolType.RUBBER),  // rubber
                new MoveViewModel(new MoveModel()),
                new SelectViewModel(new SelectModel()),
                new LineViewModel(new LineModel())
                );

    }

    public void selectPreviousTool() {
        ToolViewModel currModel = activeToolViewModel.get();
        setActiveToolViewModel(previousActiveToolViewModel.get());
        previousActiveToolViewModel.set(currModel);
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

    public List<ToolViewModel> getToolViewModelList() {
        return toolViewModelList;
    }
}
