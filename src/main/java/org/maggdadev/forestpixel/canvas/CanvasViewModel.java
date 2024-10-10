package org.maggdadev.forestpixel.canvas;

import javafx.beans.property.*;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import org.maggdadev.forestpixel.canvas.events.CanvasMouseEvent;
import org.maggdadev.forestpixel.canvas.events.CanvasSelectionCancelEvent;
import org.maggdadev.forestpixel.canvas.events.CanvasZoomEvent;
import org.maggdadev.forestpixel.canvas.frames.FramesBarViewModel;
import org.maggdadev.forestpixel.canvas.frames.FramesViewModels;
import org.maggdadev.forestpixel.canvas.layers.LayersBarViewModel;
import org.maggdadev.forestpixel.canvas.toolbar.ToolbarViewModel;
import org.maggdadev.forestpixel.canvas.tools.viewmodels.ToolViewModel;

import java.io.*;

public class CanvasViewModel {
    private CanvasModel model;
    private final DoubleProperty availableViewportWidth = new SimpleDoubleProperty(0), availableViewportHeight = new SimpleDoubleProperty(0);
    private final ObjectProperty<PreviewImage> previewImage = new SimpleObjectProperty<>();
    private final CanvasContext canvasContext;
    private final ObjectProperty<ToolViewModel> activeToolViewModel = new SimpleObjectProperty<>();
    private final ToolbarViewModel toolBarViewModel;
    private final LayersBarViewModel layersBarViewModel;
    private final FramesBarViewModel framesBarViewModel;
    private final FramesViewModels framesViewModels;
    private final IntegerProperty modelWidth = new SimpleIntegerProperty(0), modelHeight = new SimpleIntegerProperty(0);
    private final BooleanProperty viewNeedsUpdate = new SimpleBooleanProperty(false);
    private final ZoomManager zoomManager;
    private final CopyPasteManager copyPasteManager;

    private final ObjectProperty<File> fileLocation = new SimpleObjectProperty<>();

    public CanvasViewModel(CanvasModel model) {
        this.model = model;
        zoomManager = new ZoomManager(this);
        canvasContext = new CanvasContext(previewImage, zoomManager.zoomScaleFactorProperty());
        toolBarViewModel = new ToolbarViewModel(canvasContext, zoomManager::moveCanvasBy);

        activeToolViewModel.bind(toolBarViewModel.activeToolViewModelProperty());


        // Frames
        framesViewModels = new FramesViewModels(canvasContext);
        framesBarViewModel = new FramesBarViewModel(framesViewModels);

        // Layers
        layersBarViewModel = new LayersBarViewModel(framesViewModels);
        layersBarViewModel.activeFrameIdProperty().bind(canvasContext.activeFrameIdProperty());
        canvasContext.lowerLayersOpacityProperty().bind(layersBarViewModel.lowerLayersOpacityProperty());
        canvasContext.upperLayersOpacityProperty().bind(layersBarViewModel.upperLayersOpacityProperty());

        canvasContext.activeLayerIdProperty().bind(framesViewModels.activeLayerIdProperty());
        canvasContext.activeLayerOrderProperty().bind(framesViewModels.activeLayerOrderProperty());
        canvasContext.activeFrameIdProperty().bind(framesViewModels.activeFrameIdProperty());

        canvasContext.activeLayerIdProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty() && !newValue.equals("-1")) {
                update();
            }
        });

        // Copy paste
        copyPasteManager = new CopyPasteManager(this, toolBarViewModel.getSelectViewModel());

        setModel(model);

    }

    private void setModel(CanvasModel model) {
        this.model = model;
        this.modelWidth.set(model.getWidthPixels());
        this.modelHeight.set(model.getHeightPixels());
        framesViewModels.setModel(model);
        update();
    }

    public void saveModelTo(File file) throws IOException {
        if (file == null) {
            return;
        }
        try (FileOutputStream fileOut = new FileOutputStream(file)) {
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(model);
            setFileLocation(file);
        } catch (IOException e) {
            throw new IOException("Could not save model to file due to error " + e.getMessage(), e);
        }
    }

    public void loadModelFrom(File file) {
        if (file == null) {
            return;
        }
        try (FileInputStream fileIn = new FileInputStream(file)) {
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);
            setModel((CanvasModel) objectIn.readObject());
            setFileLocation(file);
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Could not open model from file due to error " + e.getMessage(), e);
        }
    }

    public void save() {
        if (fileLocation.get() == null) {
            System.err.println("Trying to save but no file location set.");
            return;
        }
        try {
            saveModelTo(fileLocation.get());
        } catch (IOException e) {
            throw new RuntimeException("Could not save model to file due to error " + e.getMessage(), e);
        }
    }

    public void exportTo(File file) {
        WritableImage image = new WritableImage(model.getWidthPixels() * framesViewModels.getFrames().size(), model.getHeightPixels());
        for (int i = 0; i < framesViewModels.getFrames().size(); i++) {
            image.getPixelWriter().setPixels(i * model.getWidthPixels(), 0, model.getWidthPixels(), model.getHeightPixels(), framesViewModels.getFrames().get(i).exportToImage().getPixelReader(), 0, 0);
        }
        try {
            javax.imageio.ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
        } catch (IOException e) {
            throw new RuntimeException("Could not export model to file due to error " + e.getMessage(), e);
        }

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
        framesBarViewModel.notifyKeyPressed(code);
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
        if (canvasContext.noFrameSelected()) {
            return;
        }
        model.undo(canvasContext.getActiveFrameId());
        update();
    }

    public void redo() {
        if (canvasContext.noFrameSelected()) {
            return;
        }
        model.redo(canvasContext.getActiveFrameId());
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

    public FramesBarViewModel getFramesBarViewModel() {
        return framesBarViewModel;
    }

    public CanvasContext getCanvasContext() {
        return canvasContext;
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

    public FramesViewModels getFramesViewModels() {
        return framesViewModels;
    }

    public File getFileLocation() {
        return fileLocation.get();
    }

    public ObjectProperty<File> fileLocationProperty() {
        return fileLocation;
    }

    public void setFileLocation(File fileLocation) {
        this.fileLocation.set(fileLocation);
    }


}