package org.maggdadev.forestpixel.canvas.toolbar;

import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.scene.paint.Color;
import org.maggdadev.forestpixel.canvas.CanvasContext;
import org.maggdadev.forestpixel.canvas.events.CanvasEvent;
import org.maggdadev.forestpixel.canvas.tools.ToolType;
import org.maggdadev.forestpixel.canvas.tools.models.*;
import org.maggdadev.forestpixel.canvas.tools.viewmodels.*;

import java.util.List;
import java.util.function.BiConsumer;

public class ToolbarViewModel {
    private final ObjectProperty<ToolViewModel> activeToolViewModel = new SimpleObjectProperty<>();
    private final ObjectProperty<ToolViewModel> previousActiveToolViewModel = new SimpleObjectProperty<>();
    private final BooleanProperty colorPickingVisible = new SimpleBooleanProperty(false), lineWidthPickerVisible = new SimpleBooleanProperty(false);

    private final ObjectProperty<Color> color = new SimpleObjectProperty<>();
    private final IntegerProperty lineWidth = new SimpleIntegerProperty(1);

    private final List<ToolViewModel> toolViewModelList;
    private final SelectViewModel selectViewModel;

    public ToolbarViewModel(CanvasContext context, BiConsumer<Double, Double> moveCanvasFunction) {
        color.bindBidirectional(context.colorProperty());
        lineWidth.bindBidirectional(context.lineWidthProperty());
        this.activeToolViewModel.addListener((obs, oldVal, newVal) -> {
            if (newVal == null) {
                colorPickingVisible.set(false);
                lineWidthPickerVisible.set(false);
            } else {
                colorPickingVisible.set(newVal.getToolType().USES_COLOR);
                lineWidthPickerVisible.set(newVal.getToolType().USES_LINE_WIDTH);
            }
            if(oldVal != null &&  !oldVal.equals(newVal)) {
                previousActiveToolViewModel.set(oldVal);
            }
        });

        selectViewModel = new SelectViewModel(new SelectModel(), context.previewOffsetXProperty(), context.previewOffsetYProperty(), context.zoomFactorProperty(), context.mouseInSelectAreaProperty());
        toolViewModelList = List.of(
                new FreeHandDrawingToolViewModel(new FreeHandDrawingToolModel(false), ToolType.PENCIL), // pencil
                new BucketViewModel(new BucketModel()),
                new PipetViewModel(new PipetModel(), this),
                new FreeHandDrawingToolViewModel(new FreeHandDrawingToolModel(true), ToolType.RUBBER),  // rubber
                new MoveViewModel(new MoveModel(), context.mouseInSelectAreaProperty(), moveCanvasFunction),
                selectViewModel,
                new LineViewModel(new LineModel())
                );

    }

    public void notifyAllToolsCanvasEvent(CanvasEvent e) {
        for(ToolViewModel toolViewModel : toolViewModelList) {
            toolViewModel.notifyCanvasEvent(e);
        }
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

    public int getLineWidth() {
        return lineWidth.get();
    }

    public IntegerProperty lineWidthProperty() {
        return lineWidth;
    }

    public boolean isLineWidthPickerVisible() {
        return lineWidthPickerVisible.get();
    }

    public BooleanProperty lineWidthPickerVisibleProperty() {
        return lineWidthPickerVisible;
    }

    public SelectViewModel getSelectViewModel() {
        return selectViewModel;
    }
}
