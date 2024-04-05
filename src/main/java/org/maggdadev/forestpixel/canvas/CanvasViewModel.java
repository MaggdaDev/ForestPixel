package org.maggdadev.forestpixel.canvas;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventHandler;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import org.maggdadev.forestpixel.canvas.events.CanvasEvent;
import org.maggdadev.forestpixel.canvas.events.CanvasMouseEvent;
import org.maggdadev.forestpixel.canvas.toolbar.ToolbarViewModel;
import org.maggdadev.forestpixel.canvas.tools.viewmodels.ToolViewModel;

public class CanvasViewModel {

    private final CanvasModel model;
    private final ObjectProperty<WritableImage> image = new SimpleObjectProperty<>();

    private final CanvasContext canvasContext;

    private final ObjectProperty<ToolViewModel> activeToolViewModel = new SimpleObjectProperty<>();

    private final ToolbarViewModel toolBarViewModel;
    private final CanvasZoomHandler canvasZoomHandler;

    private final DoubleProperty zoomHValue = new SimpleDoubleProperty(0);
    private final DoubleProperty zoomVValue = new SimpleDoubleProperty(0);

    private final DoubleProperty modelWidth = new SimpleDoubleProperty(0);

    private final DoubleProperty modelHeight = new SimpleDoubleProperty(0);
    private final DoubleProperty zoomScaleFactor = new SimpleDoubleProperty(1);

    public ObjectProperty<WritableImage> imageProperty() {
        return image;
    }

    public CanvasViewModel(CanvasModel model) {
        this.model = model;
        this.toolBarViewModel = new ToolbarViewModel();
        canvasContext = new CanvasContext(toolBarViewModel.colorProperty());
        canvasZoomHandler = new CanvasZoomHandler(this);

        activeToolViewModel.bind(toolBarViewModel.activeToolViewModelProperty());
    }


    void handleCanvasEvent(CanvasEvent event) {
        if (activeToolViewModel.get() != null) { // all the events handled by active tool
            if (event instanceof CanvasMouseEvent) {
                activeToolViewModel.get().notifyCanvasMouseEvent((CanvasMouseEvent) event);
            } else
                throw new UnsupportedOperationException("The following canvas event has not yet been implemented: " + event.getClass().getName());
        } else {    // all the events not handled by active tool
            //throw new UnsupportedOperationException("The following canvas event has not yet been implemented: " + event.getClass().getName());
        }

        update();
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
        }
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
        handleCanvasEvent(new CanvasMouseEvent(model, canvasZoomHandler.xPosToIdx(e.getX()), canvasZoomHandler.yPosToIdx(e.getY()), aType, bType, canvasContext));
    }

    public EventHandler<ScrollEvent> getOnCanvasZoom() {
        return (ScrollEvent e) -> {
            if (e.isControlDown()) {
                canvasZoomHandler.handleZoomEvent(e);
                e.consume();
            }
        };
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
}
