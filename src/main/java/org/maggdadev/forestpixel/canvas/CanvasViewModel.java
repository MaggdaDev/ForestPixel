package org.maggdadev.forestpixel.canvas;

import javafx.beans.property.*;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import org.maggdadev.forestpixel.canvas.events.CanvasMouseEvent;
import org.maggdadev.forestpixel.canvas.events.CanvasSelectionCancelEvent;
import org.maggdadev.forestpixel.canvas.events.CanvasZoomEvent;
import org.maggdadev.forestpixel.canvas.layers.LayerViewModel;
import org.maggdadev.forestpixel.canvas.layers.LayersBarViewModel;
import org.maggdadev.forestpixel.canvas.layers.LayersViewModels;
import org.maggdadev.forestpixel.canvas.toolbar.ToolbarViewModel;
import org.maggdadev.forestpixel.canvas.tools.viewmodels.ToolViewModel;

public class CanvasViewModel {
    private final CanvasModel model;
    private final DoubleProperty availableViewportWidth = new SimpleDoubleProperty(0), availableViewportHeight = new SimpleDoubleProperty(0);
    private final LayersViewModels layerViewModels;
    private final ObjectProperty<PreviewImage> previewImage = new SimpleObjectProperty<>();
    private final CanvasContext canvasContext;
    private final ObjectProperty<ToolViewModel> activeToolViewModel = new SimpleObjectProperty<>();
    private final ToolbarViewModel toolBarViewModel;
    private final LayersBarViewModel layersBarViewModel;
    private final IntegerProperty modelWidth = new SimpleIntegerProperty(0), modelHeight = new SimpleIntegerProperty(0);
    private final BooleanProperty viewNeedsUpdate = new SimpleBooleanProperty(false);
    private final ZoomManager zoomManager;
    private final CopyPasteManager copyPasteManager;

    public CanvasViewModel(CanvasModel model) {
        this.model = model;
        this.modelWidth.set(model.getWidthPixels());
        this.modelHeight.set(model.getHeightPixels());

        zoomManager = new ZoomManager(this);
        canvasContext = new CanvasContext(previewImage, zoomManager.zoomScaleFactorProperty());
        toolBarViewModel = new ToolbarViewModel(canvasContext, zoomManager::moveCanvasBy);
        layerViewModels = new LayersViewModels(model, canvasContext);

        activeToolViewModel.bind(toolBarViewModel.activeToolViewModelProperty());

        // Layers
        layersBarViewModel = new LayersBarViewModel(layerViewModels);
        canvasContext.activeLayerIdProperty().bind(layersBarViewModel.activeLayerIdProperty());
        canvasContext.activeLayerOrderProperty().bind(layerViewModels.activeLayerOrderProperty());
        canvasContext.lowerLayersOpacityProperty().bind(layersBarViewModel.lowerLayersOpacityProperty());
        canvasContext.upperLayersOpacityProperty().bind(layersBarViewModel.upperLayersOpacityProperty());

        // Copy paste
        copyPasteManager = new CopyPasteManager(this, toolBarViewModel.getSelectViewModel());

    }

    private void handleCanvasMouseEvent(CanvasMouseEvent event) {
        if (activeToolViewModel.get() != null &&
                (!getCanvasState().equals(CanvasState.SELECTED) || activeToolViewModel.get().isRequestMouseEventsEvenIfSelected())) {
            activeToolViewModel.get().notifyCanvasEvent(event);

        } else if (event.actionType().equals(CanvasMouseEvent.ActionType.PRESSED) && getCanvasState().equals(CanvasState.SELECTED)) {
            cancelSelection(event.canvasContext(), event.canvasModel());
        }

        update();
    }

    public void cancelSelection(CanvasContext canvasContext, CanvasModel canvasModel) {
        toolBarViewModel.notifyAllToolsCanvasEvent(new CanvasSelectionCancelEvent(canvasContext, canvasModel, CanvasSelectionCancelEvent.CancelState.ON_CANCEL));
        toolBarViewModel.notifyAllToolsCanvasEvent(new CanvasSelectionCancelEvent(canvasContext, canvasModel, CanvasSelectionCancelEvent.CancelState.AFTER_CANCEL));
        canvasContext.setState(CanvasState.IDLE);
    }

    private void handleZoomEvent(CanvasZoomEvent event) {
        toolBarViewModel.notifyAllToolsCanvasEvent(event);
    }

    public void notifyKeyPressed(KeyCode code) {
        toolBarViewModel.notifyKeyPressed(code);
    }


    void update() {/*
        if (model.getImage() == null) {
            image.set(null);
            modelHeight.set(0);
            modelWidth.set(0);
        } else {
            // Ensure there is a nonnull image with same dimensions
            if (image.get() == null || Math.round(image.get().getWidth()) != model.getWidthPixels() || Math.round(image.get().getHeight()) != model.getHeightPixels()) {
                if (image.get() == null) {
                    System.out.println("[CanvasViewModel] Created image");
                } else {
                    System.out.println("[CanvasViewModel] Created new image since dimensions are different");
                }
                image.set(new WritableImage(model.getWidthPixels(), model.getHeightPixels()));
            }

            modelWidth.set(image.get().getWidth());
            modelHeight.set(image.get().getHeight());

            // flush model to viewModel/view property
            image.get().getPixelWriter().setPixels(0, 0, model.getWidthPixels(), model.getHeightPixels(), model.getImage().getPixelReader(), 0, 0);

            setViewNeedsUpdate(true);
        }*/
        setViewNeedsUpdate(true);
    }

    // commands

    public void undo() {
        model.undo();
        update();
    }

    public void redo() {
        model.redo();
        update();
    }

    public void copy() {
        copyPasteManager.copy(canvasContext);
    }

    public void paste() {
        copyPasteManager.paste(model, canvasContext);
        update();
    }

    public void cut() {
        copyPasteManager.cut(canvasContext);
        cancelSelection(canvasContext, model);
        update();
    }


    ToolbarViewModel getToolBarViewModel() {
        return toolBarViewModel;
    }

    public EventHandler<MouseEvent> getOnCanvasMouseClicked() {
        return (MouseEvent e) -> {
            switch (e.getButton()) {
                case PRIMARY ->
                        sendFireCanvasEvent(e, CanvasMouseEvent.ActionType.CLICKED, CanvasMouseEvent.ButtonType.PRIMARY);
                case SECONDARY ->
                        sendFireCanvasEvent(e, CanvasMouseEvent.ActionType.CLICKED, CanvasMouseEvent.ButtonType.SECONDARY);
            }
        };
    }

    public EventHandler<MouseEvent> getOnCanvasMousePressed() {
        return (MouseEvent e) -> {
            switch (e.getButton()) {
                case PRIMARY ->
                        sendFireCanvasEvent(e, CanvasMouseEvent.ActionType.PRESSED, CanvasMouseEvent.ButtonType.PRIMARY);
                case SECONDARY ->
                        sendFireCanvasEvent(e, CanvasMouseEvent.ActionType.PRESSED, CanvasMouseEvent.ButtonType.SECONDARY);
            }
        };
    }

    public EventHandler<MouseEvent> getOnCanvasMouseReleased() {
        return (MouseEvent e) -> {
            switch (e.getButton()) {
                case PRIMARY ->
                        sendFireCanvasEvent(e, CanvasMouseEvent.ActionType.RELEASED, CanvasMouseEvent.ButtonType.PRIMARY);
                case SECONDARY ->
                        sendFireCanvasEvent(e, CanvasMouseEvent.ActionType.RELEASED, CanvasMouseEvent.ButtonType.SECONDARY);
            }
        };
    }

    public EventHandler<MouseEvent> getOnCanvasMouseDragged() {
        return (MouseEvent e) -> {
            switch (e.getButton()) {
                case PRIMARY ->
                        sendFireCanvasEvent(e, CanvasMouseEvent.ActionType.DRAGGED, CanvasMouseEvent.ButtonType.PRIMARY);
                case SECONDARY ->
                        sendFireCanvasEvent(e, CanvasMouseEvent.ActionType.DRAGGED, CanvasMouseEvent.ButtonType.SECONDARY);
            }
        };
    }

    private void sendFireCanvasEvent(MouseEvent e, CanvasMouseEvent.ActionType aType, CanvasMouseEvent.ButtonType bType) {
        handleCanvasMouseEvent(new CanvasMouseEvent(model, e.getX(), e.getY(), zoomManager.sceneToIdx(e.getX()), zoomManager.sceneToIdx(e.getY()), aType, bType, canvasContext));
    }

    public EventHandler<ScrollEvent> getOnCanvasZoom() {
        return (ScrollEvent e) -> {
            if (e.isControlDown()) {
                zoomManager.handleZoomEvent(e);
                handleZoomEvent(new CanvasZoomEvent(canvasContext));
                e.consume();
            }
        };
    }

    // start: get/set
    public CanvasState getCanvasState() {
        return canvasContext.getState();
    }

    public DoubleProperty zoomScaleFactorProperty() {
        return zoomManager.zoomScaleFactorProperty();
    }

    public int getModelWidth() {
        return modelWidth.get();
    }

    public IntegerProperty modelWidthProperty() {
        return modelWidth;
    }

    public int getModelHeight() {
        return modelHeight.get();
    }

    public IntegerProperty modelHeightProperty() {
        return modelHeight;
    }

    public double getZoomScaleFactor() {
        return zoomManager.getZoomScaleFactor();
    }

    public BooleanProperty viewNeedsUpdateProperty() {
        return viewNeedsUpdate;
    }

    public void setViewNeedsUpdate(boolean viewNeedsUpdate) {
        this.viewNeedsUpdate.set(viewNeedsUpdate);
    }

    public int getSourceStartIndexX() {
        return zoomManager.getSourceStartIndexX();
    }


    public int getSourceStartIndexY() {
        return zoomManager.getSourceStartIndexY();
    }

    public PreviewImage getPreviewImage() {
        return previewImage.get();
    }

    public LayersBarViewModel getLayersBarViewModel() {
        return layersBarViewModel;
    }

    public CanvasContext getCanvasContext() {
        return canvasContext;
    }

    public ObservableList<LayerViewModel> getLayersUnmodifiable() {
        return layerViewModels.getLayersUnmodifiable();
    }

    public double getAvailableViewportWidth() {
        return availableViewportWidth.get();
    }

    public DoubleProperty availableViewportWidthProperty() {
        return availableViewportWidth;
    }

    public double getAvailableViewportHeight() {
        return availableViewportHeight.get();
    }

    public DoubleProperty availableViewportHeightProperty() {
        return availableViewportHeight;
    }

    public DoubleProperty zoomHValueProperty() {
        return zoomManager.zoomHValueProperty();
    }

    public DoubleProperty zoomVValueProperty() {
        return zoomManager.zoomVValueProperty();
    }

    public ZoomManager getCanvasZoomHandler() {
        return zoomManager;
    }


}