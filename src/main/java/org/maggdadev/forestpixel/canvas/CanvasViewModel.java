package org.maggdadev.forestpixel.canvas;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventHandler;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import org.maggdadev.forestpixel.canvas.events.CanvasEvent;
import org.maggdadev.forestpixel.canvas.events.CanvasMouseEvent;
import org.maggdadev.forestpixel.canvas.toolbar.ToolbarViewModel;
import org.maggdadev.forestpixel.canvas.tools.viewmodels.ToolViewModel;

public class CanvasViewModel {

    private CanvasModel model;
    private boolean isDirty = false;
    private final ObjectProperty<WritableImage> image = new SimpleObjectProperty<>();

    private ObjectProperty<ToolViewModel> activeToolViewModel = new SimpleObjectProperty<>();

    private ToolbarViewModel toolBarViewModel;

    public ObjectProperty<WritableImage> imageProperty() {
        return image;
    }

    public CanvasViewModel(CanvasModel model) {
        this.model = model;
        this.toolBarViewModel = new ToolbarViewModel();
        activeToolViewModel.bind(toolBarViewModel.activeToolViewModelProperty());
    }

    void handleCanvasEvent(CanvasEvent event) {
        if(activeToolViewModel.get() != null) { // all the events handled by active tool
            if(event instanceof CanvasMouseEvent) {
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

            // flush model to viewmodel/view property
            image.get().getPixelWriter().setPixels(0, 0, model.getWidthPixels(), model.getHeightPixels(), model.getImage().getPixelReader(), 0, 0);
        }
    }

    CanvasModel getModel() {
        return model;
    }

    ToolbarViewModel getToolBarViewModel() {
        return toolBarViewModel;
    }

    public EventHandler<MouseEvent> getOnCanvasMouseClicked() {
        return (MouseEvent e) -> {
            switch (e.getButton()) {
                case PRIMARY -> sendFireCanvasEvent(e, CanvasMouseEvent.ActionType.CLICKED, CanvasMouseEvent.ButtonType.PRIMARY);
                case SECONDARY -> sendFireCanvasEvent(e, CanvasMouseEvent.ActionType.CLICKED, CanvasMouseEvent.ButtonType.SECONDARY);
            }
        };
    }

    public EventHandler<MouseEvent> getOnCanvasMousePressed() {
        return (MouseEvent e) -> {
            switch (e.getButton()) {
                case PRIMARY -> sendFireCanvasEvent(e, CanvasMouseEvent.ActionType.PRESSED, CanvasMouseEvent.ButtonType.PRIMARY);
                case SECONDARY -> sendFireCanvasEvent(e, CanvasMouseEvent.ActionType.PRESSED, CanvasMouseEvent.ButtonType.SECONDARY);
            }
        };
    }

    public EventHandler<MouseEvent> getOnCanvasMouseReleased() {
        return (MouseEvent e) -> {
            switch (e.getButton()) {
                case PRIMARY -> sendFireCanvasEvent(e, CanvasMouseEvent.ActionType.RELEASED, CanvasMouseEvent.ButtonType.PRIMARY);
                case SECONDARY -> sendFireCanvasEvent(e, CanvasMouseEvent.ActionType.RELEASED, CanvasMouseEvent.ButtonType.SECONDARY);
            }
        };
    }

    public EventHandler<MouseEvent> getOnCanvasMouseDragged() {
        return (MouseEvent e) -> {
            switch (e.getButton()) {
                case PRIMARY -> sendFireCanvasEvent(e, CanvasMouseEvent.ActionType.DRAGGED, CanvasMouseEvent.ButtonType.PRIMARY);
                case SECONDARY -> sendFireCanvasEvent(e, CanvasMouseEvent.ActionType.DRAGGED, CanvasMouseEvent.ButtonType.SECONDARY);
            }
        };
    }

    private void sendFireCanvasEvent(MouseEvent e, CanvasMouseEvent.ActionType aType, CanvasMouseEvent.ButtonType bType) {
            handleCanvasEvent(new CanvasMouseEvent(model, Math.round((float)e.getX()), Math.round((float)e.getY()), aType, bType));
    }
}
