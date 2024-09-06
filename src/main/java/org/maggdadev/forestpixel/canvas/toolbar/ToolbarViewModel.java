package org.maggdadev.forestpixel.canvas.toolbar;

import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.scene.paint.Color;
import org.maggdadev.forestpixel.canvas.CanvasContext;
import org.maggdadev.forestpixel.canvas.events.CanvasMouseEvent;
import org.maggdadev.forestpixel.canvas.events.CanvasZoomEvent;
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

        toolViewModelList = List.of(
                new FreeHandDrawingToolViewModel(new FreeHandDrawingToolModel((canvasModel, canvasContext) -> canvasContext.getColor()), ToolType.PENCIL), // pencil
                new BucketViewModel(new BucketModel()),
                new PipetViewModel(new PipetModel(), this),
                new FreeHandDrawingToolViewModel(new FreeHandDrawingToolModel((canvasModel, canvasContext) -> canvasModel.getTransparentColor()), ToolType.RUBBER),  // rubber
                new MoveViewModel(new MoveModel(), context.mouseInSelectAreaProperty(), moveCanvasFunction),
                new SelectViewModel(new SelectModel(), context.previewOffsetXProperty(), context.previewOffsetYProperty(), context.zoomFactorProperty(), context.mouseInSelectAreaProperty()),
                new LineViewModel(new LineModel())
                );

    }

    public void notifyAllToolsSelectionCancelled(CanvasMouseEvent event) {
        CanvasMouseEvent cancelEvent = new CanvasMouseEvent(event.canvasModel(), event.pixelXPos(), event.pixelYPos(),  event.xIdx(), event.yIdx(), CanvasMouseEvent.ActionType.SELECTION_CANCELLED, event.buttonType(), event.canvasContext());
        for(ToolViewModel toolViewModel : toolViewModelList) {
            toolViewModel.notifyCanvasMouseEvent(cancelEvent);
        }
        CanvasMouseEvent afterCancelEvent = new CanvasMouseEvent(event.canvasModel(), event.pixelXPos(), event.pixelYPos(),  event.xIdx(), event.yIdx(), CanvasMouseEvent.ActionType.AFTER_SELECTION_CANCELLED, event.buttonType(), event.canvasContext());
        for(ToolViewModel toolViewModel : toolViewModelList) {
            toolViewModel.notifyCanvasMouseEvent(afterCancelEvent);
        }
    }

    public void notifyAllToolsZoom(CanvasZoomEvent event) {
        for(ToolViewModel toolViewModel : toolViewModelList) {
            toolViewModel.onZoomEvent(event);
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
}
