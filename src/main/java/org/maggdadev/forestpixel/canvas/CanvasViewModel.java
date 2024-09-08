package org.maggdadev.forestpixel.canvas;

import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.event.EventHandler;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import org.maggdadev.forestpixel.canvas.events.CanvasEvent;
import org.maggdadev.forestpixel.canvas.events.CanvasMouseEvent;
import org.maggdadev.forestpixel.canvas.events.CanvasZoomEvent;
import org.maggdadev.forestpixel.canvas.layersbar.LayersBarViewModel;
import org.maggdadev.forestpixel.canvas.toolbar.ToolbarViewModel;
import org.maggdadev.forestpixel.canvas.tools.viewmodels.ToolViewModel;

public class CanvasViewModel {

    private final CanvasModel model;
    private final ObjectProperty<WritableImage> image = new SimpleObjectProperty<>();
    private final ObjectProperty<PreviewImage> previewImage = new SimpleObjectProperty<>();

    private final IntegerProperty previewImageViewportStartX = new SimpleIntegerProperty(0), previewImageViewPortStartY = new SimpleIntegerProperty(0);

    private final CanvasContext canvasContext;

    private final ObjectProperty<ToolViewModel> activeToolViewModel = new SimpleObjectProperty<>();

    private final ToolbarViewModel toolBarViewModel;

    private final LayersBarViewModel layersBarViewModel;
    private final CanvasZoomHandler canvasZoomHandler;

    private final DoubleProperty zoomHValue = new SimpleDoubleProperty(0);
    private final DoubleProperty zoomVValue = new SimpleDoubleProperty(0);

    private final DoubleProperty modelWidth = new SimpleDoubleProperty(0);

    private final DoubleProperty modelHeight = new SimpleDoubleProperty(0);
    private final DoubleProperty zoomScaleFactor = new SimpleDoubleProperty(1);
    private final BooleanProperty viewNeedsUpdate = new SimpleBooleanProperty(false);

    private final DoubleProperty viewportPosX = new SimpleDoubleProperty();
    private final DoubleProperty viewportPosY = new SimpleDoubleProperty();

    private final DoubleProperty quantizedViewportX = new SimpleDoubleProperty();
    private final DoubleProperty quantizedViewportY = new SimpleDoubleProperty();
    private final IntegerProperty sourceStartIndexX = new SimpleIntegerProperty();
    private final IntegerProperty sourceStartIndexY = new SimpleIntegerProperty();

    private final IntegerProperty extendedCanvasPixelWidth = new SimpleIntegerProperty();
    private final IntegerProperty extendedCanvasPixelHeight = new SimpleIntegerProperty();


    public CanvasViewModel(CanvasModel model) {
        this.model = model;
        canvasContext = new CanvasContext(previewImage, zoomScaleFactor);
        canvasZoomHandler = new CanvasZoomHandler(this);
        this.toolBarViewModel = new ToolbarViewModel(canvasContext, canvasZoomHandler::moveCanvasBy);

        activeToolViewModel.bind(toolBarViewModel.activeToolViewModelProperty());

        viewportPosX.bind(Bindings.multiply(zoomHValueProperty(), Bindings.subtract(Bindings.multiply(zoomScaleFactorProperty(), modelWidth), CanvasView.CANVAS_WIDTH)));           // effective width and height
        viewportPosY.bind(Bindings.multiply(zoomVValueProperty(), Bindings.subtract(Bindings.multiply(zoomScaleFactorProperty(), modelHeight), CanvasView.CANVAS_HEIGHT)));

        sourceStartIndexX.bind(Bindings.createIntegerBinding(() -> {
            int leftExtra = sceneToIdx(getViewportPosX()) > 0 ? 1 : 0;
            return sceneToIdx(getViewportPosX() - leftExtra);
        }, modelWidth, zoomScaleFactor, viewportPosX));
        sourceStartIndexY.bind(Bindings.createIntegerBinding(() -> {
            int topExtra = sceneToIdx(getViewportPosY()) > 0 ? 1 : 0;
            return sceneToIdx(getViewportPosY() - topExtra);
        }, modelHeight, zoomScaleFactor, viewportPosY));
        quantizedViewportX.bind(Bindings.multiply(zoomScaleFactor, sourceStartIndexX));
        quantizedViewportY.bind(Bindings.multiply(zoomScaleFactor, sourceStartIndexY));

        extendedCanvasPixelWidth.bind(Bindings.createIntegerBinding(() -> {
            int leftExtra = sceneToIdx(getViewportPosX()) > 0 ? 1 : 0;
            int rightExtra = (getSourceStartIndexX() + canvasWidthToIdx()) < getModelWidthPixels() ? 1 : 0;
            return Math.round((float) ((canvasWidthToIdx() + leftExtra + rightExtra) * getZoomScaleFactor()));
        }, zoomScaleFactor, modelWidth, sourceStartIndexX));

        extendedCanvasPixelHeight.bind(Bindings.createIntegerBinding(() -> {
            int topExtra = sceneToIdx(getViewportPosY()) > 0 ? 1 : 0;
            int botExtra = (getSourceStartIndexY() + canvasHeightToIdx()) < getModelHeightPixels() ? 1 : 0;
            return Math.round((float) ((canvasHeightToIdx() + topExtra + botExtra) * getZoomScaleFactor()));
        }, zoomScaleFactor, modelHeight, sourceStartIndexY));

        // Layers
        layersBarViewModel = new LayersBarViewModel();
    }


    private void handleCanvasMouseEvent(CanvasMouseEvent event) {
        if (activeToolViewModel.get() != null &&
                (!getCanvasState().equals(CanvasState.SELECTED) || activeToolViewModel.get().isRequestMouseEventsEvenIfSelected())) {
            activeToolViewModel.get().notifyCanvasMouseEvent(event);

        } else if (event.actionType().equals(CanvasMouseEvent.ActionType.PRESSED) && getCanvasState().equals(CanvasState.SELECTED)) {
            toolBarViewModel.notifyAllToolsSelectionCancelled(event);
            handleClickWhileSelected(event);
        }

        update();
    }

    private void handleZoomEvent(CanvasZoomEvent event) {
        toolBarViewModel.notifyAllToolsZoom(event);
    }


    private void handleClickWhileSelected(CanvasEvent event) {
        if (event instanceof CanvasMouseEvent mouseEvent) {
            if (mouseEvent.actionType().equals(CanvasMouseEvent.ActionType.PRESSED) && mouseEvent.buttonType().equals(CanvasMouseEvent.ButtonType.PRIMARY)) {
                canvasContext.setState(CanvasState.IDLE);
            }
        }
    }

    void update() {
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
        }
    }

    public void undo() {
        model.undo();
        update();
    }

    public void redo() {
        model.redo();
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
        handleCanvasMouseEvent(new CanvasMouseEvent(model, e.getX(), e.getY(), xPosToIdx(e.getX()), yPosToIdx(e.getY()), aType, bType, canvasContext));
    }

    public EventHandler<ScrollEvent> getOnCanvasZoom() {
        return (ScrollEvent e) -> {
            if (e.isControlDown()) {
                canvasZoomHandler.handleZoomEvent(e);
                handleZoomEvent(new CanvasZoomEvent(canvasContext));
                e.consume();
            }
        };
    }


// START: Calculations/conversions

    public int xPosToIdx(double xPos) {
        return (int) (xPos / getZoomScaleFactor());
    }

    public int yPosToIdx(double yPos) {
        return (int) (yPos / getZoomScaleFactor());
    }

    public int sceneToIdx(double s) {
        return (int) (s / getZoomScaleFactor());
    }

    private int canvasWidthToIdx() {
        return Math.round((float) (CanvasView.CANVAS_WIDTH / getZoomScaleFactor()));
    }

    private int canvasHeightToIdx() {
        return Math.round((float) (CanvasView.CANVAS_HEIGHT / getZoomScaleFactor()));
    }

    public int getModelWidthPixels() {
        return Math.round((float) (getModelWidth() / getZoomScaleFactor()));
    }

    public int getModelHeightPixels() {
        return Math.round((float) (getModelHeight() / getZoomScaleFactor()));
    }

// END: CALCULATIONS/conversions


// start: get/set

    public ObjectProperty<CanvasState> canvasStateProperty() {
        return canvasContext.stateProperty();
    }

    public CanvasState getCanvasState() {
        return canvasContext.getState();
    }

    public DoubleProperty zoomHValueProperty() {
        return zoomHValue;
    }

    public DoubleProperty zoomVValueProperty() {
        return zoomVValue;
    }

    public DoubleProperty zoomScaleFactorProperty() {
        return zoomScaleFactor;
    }

    public double getModelWidth() {
        return modelWidth.get();
    }

    public DoubleProperty modelWidthProperty() {
        return modelWidth;
    }

    public double getModelHeight() {
        return modelHeight.get();
    }

    public DoubleProperty modelHeightProperty() {
        return modelHeight;
    }

    public double getZoomScaleFactor() {
        return zoomScaleFactor.get();
    }

    public double getZoomHValue() {
        return zoomHValue.get();
    }

    public double getZoomVValue() {
        return zoomVValue.get();
    }

    public void setZoomHValue(double zoomHValue) {
        this.zoomHValue.set(zoomHValue);
    }

    public void setZoomVValue(double zoomVValue) {
        this.zoomVValue.set(zoomVValue);
    }

    public BooleanProperty viewNeedsUpdateProperty() {
        return viewNeedsUpdate;
    }

    public void setViewNeedsUpdate(boolean viewNeedsUpdate) {
        this.viewNeedsUpdate.set(viewNeedsUpdate);
    }

    public double getViewportPosX() {
        return viewportPosX.get();
    }

    public double getViewportPosY() {
        return viewportPosY.get();
    }

    public DoubleProperty quantizedViewportXProperty() {
        return quantizedViewportX;
    }

    public DoubleProperty quantizedViewportYProperty() {
        return quantizedViewportY;
    }

    public int getSourceStartIndexX() {
        return sourceStartIndexX.get();
    }


    public int getSourceStartIndexY() {
        return sourceStartIndexY.get();
    }

    public int getExtendedCanvasPixelWidth() {
        return extendedCanvasPixelWidth.get();
    }

    public IntegerProperty extendedCanvasPixelWidthProperty() {
        return extendedCanvasPixelWidth;
    }

    public int getExtendedCanvasPixelHeight() {
        return extendedCanvasPixelHeight.get();
    }

    public IntegerProperty extendedCanvasPixelHeightProperty() {
        return extendedCanvasPixelHeight;
    }


    public ObjectProperty<WritableImage> imageProperty() {
        return image;
    }

    public PreviewImage getPreviewImage() {
        return previewImage.get();
    }

    public ObjectProperty<PreviewImage> previewImageProperty() {
        return previewImage;
    }


    public LayersBarViewModel getLayersBarViewModel() {
        return layersBarViewModel;
    }
}