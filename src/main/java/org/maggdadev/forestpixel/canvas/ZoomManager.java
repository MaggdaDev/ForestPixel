package org.maggdadev.forestpixel.canvas;

import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.scene.input.ScrollEvent;

public class ZoomManager {
    private final CanvasViewModel viewModel;
    private final DoubleProperty effectiveWidth = new SimpleDoubleProperty(), effectiveHeight = new SimpleDoubleProperty();
    private final DoubleProperty viewportPosX = new SimpleDoubleProperty(), viewportPosY = new SimpleDoubleProperty();
    private final IntegerProperty sourceStartIndexX = new SimpleIntegerProperty(), sourceStartIndexY = new SimpleIntegerProperty();
    private final DoubleProperty zoomHValue = new SimpleDoubleProperty(0), zoomVValue = new SimpleDoubleProperty(0);
    private final DoubleProperty zoomScaleFactor = new SimpleDoubleProperty(1);
    private final IntegerProperty drawSourceX = new SimpleIntegerProperty(), drawSourceY = new SimpleIntegerProperty();
    private final IntegerProperty drawSourceWidth = new SimpleIntegerProperty(), drawSourceHeight = new SimpleIntegerProperty();
    private final DoubleProperty drawDestinationX = new SimpleDoubleProperty(), drawDestinationY = new SimpleDoubleProperty();
    private final DoubleProperty drawDestinationWidth = new SimpleDoubleProperty(), drawDestinationHeight = new SimpleDoubleProperty();

    private final BooleanProperty xAllVisible = new SimpleBooleanProperty(true), yAllVisible = new SimpleBooleanProperty(true);

    public ZoomManager(CanvasViewModel viewModel) {
        this.viewModel = viewModel;
        effectiveWidth.bind(Bindings.createDoubleBinding(
                () -> calculateEffectiveDimension(viewModel.getModelWidth(), getZoomScaleFactor(), viewModel.getAvailableViewportWidth()),
                viewModel.modelWidthProperty(), viewModel.availableViewportWidthProperty(), zoomScaleFactor));
        effectiveHeight.bind(Bindings.createDoubleBinding(
                () -> calculateEffectiveDimension(viewModel.getModelHeight(), getZoomScaleFactor(), viewModel.getAvailableViewportHeight()),
                viewModel.modelHeightProperty(), viewModel.availableViewportHeightProperty(), zoomScaleFactor));
        viewportPosX.bind(zoomHValue.multiply(effectiveWidth));           // effective width and height
        viewportPosY.bind(zoomVValue.multiply(effectiveHeight));

        drawSourceX.bind(Bindings.createIntegerBinding(() -> sceneToIdx(getViewportPosX()), viewportPosX, zoomScaleFactor));
        drawSourceY.bind(Bindings.createIntegerBinding(() -> sceneToIdx(getViewportPosY()), viewportPosY, zoomScaleFactor));

        drawDestinationX.bind(drawSourceX.multiply(zoomScaleFactor).subtract(viewportPosX));
        drawDestinationY.bind(drawSourceY.multiply(zoomScaleFactor).subtract(viewportPosY));

        drawSourceWidth.bind(Bindings.createIntegerBinding(() ->
                        Math.min(sceneToIdx(viewModel.getAvailableViewportWidth()) + 2, viewModel.getModelWidth()),
                zoomScaleFactor, viewModel.availableViewportWidthProperty()));
        drawSourceHeight.bind(Bindings.createIntegerBinding(() ->
                        Math.min(sceneToIdx(viewModel.getAvailableViewportHeight()) + 2, viewModel.getModelHeight()),
                zoomScaleFactor, viewModel.availableViewportHeightProperty()));

        drawDestinationWidth.bind(drawSourceWidth.multiply(zoomScaleFactor));
        drawDestinationHeight.bind(drawSourceHeight.multiply(zoomScaleFactor));

        xAllVisible.bind(Bindings.createBooleanBinding(
                () -> viewModel.getModelWidth() <= sceneToIdx(viewModel.getAvailableViewportWidth()),
                viewModel.modelWidthProperty(), viewModel.availableViewportWidthProperty(), zoomScaleFactor));
        yAllVisible.bind(Bindings.createBooleanBinding(
                () -> viewModel.getModelHeight() <= sceneToIdx(viewModel.getAvailableViewportHeight()),
                viewModel.modelHeightProperty(), viewModel.availableViewportHeightProperty(), zoomScaleFactor));
    }

    public void handleZoomEvent(ScrollEvent e) {
        double oldZoomVal = getZoomScaleFactor();

        double newZoomVal = Math.max(1, oldZoomVal * Math.pow(2.0, Math.signum(e.getDeltaY())));//Math.max(oldZoomVal + e.getDeltaY() * Y_TO_ZOOM_FACT * oldZoomVal, 1.0);

        double oldHVal = zoomHValue.get();
        double oldVVal = zoomVValue.get();

        double oldEffWidth = effectiveWidth.get();
        double oldEffHeight = effectiveHeight.get();
        double modelEventX = (e.getX() + oldHVal * oldEffWidth) / oldZoomVal;
        double modelEventY = (e.getY() + oldVVal * oldEffHeight) / oldZoomVal;

        viewModel.setPauseRedrawAllDueToInternalChangesOngoing(true);
        setZoomScaleFactor(newZoomVal);
        double newEffWidth = effectiveWidth.get();
        double newEffHeight = effectiveHeight.get();

        double deltaZoom = newZoomVal - oldZoomVal;

        double newHVal = (modelEventX * deltaZoom + oldHVal * oldEffWidth) / newEffWidth;
        double newVVal = (modelEventY * deltaZoom + oldVVal * oldEffHeight) / newEffHeight;

        zoomHValue.set(xAllVisible.get() ? 0 : restrictZoomValue(newHVal));
        zoomVValue.set(yAllVisible.get() ? 0 : restrictZoomValue(newVVal));
        viewModel.setPauseRedrawAllDueToInternalChangesOngoing(false);  // Redraw only once per zoom!
    }

    private double calculateEffectiveDimension(double modelDimension, double zoomFactor, double canvasDimension) {
        return modelDimension * zoomFactor - canvasDimension;
    }

    public void moveCanvasBy(double deltaX, double deltaY) {
        if (!xAllVisible.get()) {
            zoomHValue.set(restrictZoomValue(zoomHValue.get() - getZoomScaleFactor() * deltaX / calculateEffectiveDimension(viewModel.getModelWidth(), getZoomScaleFactor(), CanvasView.CANVAS_WIDTH)));
        }
        if (!yAllVisible.get()) {
            zoomVValue.set(restrictZoomValue(zoomVValue.get() - getZoomScaleFactor() * deltaY / calculateEffectiveDimension(viewModel.getModelHeight(), getZoomScaleFactor(), CanvasView.CANVAS_HEIGHT)));
        }
    }

    private double restrictZoomValue(double zoomValue) {
        return Math.max(0.0, Math.min(zoomValue, 1.0));
    }

    public int sceneToIdx(double s) {
        return (int) (s / getZoomScaleFactor());
    }

// END: CALCULATIONS/conversions

    public double getViewportPosX() {
        return viewportPosX.get();
    }

    public double getViewportPosY() {
        return viewportPosY.get();
    }

    public int getSourceStartIndexX() {
        return sourceStartIndexX.get();
    }

    public int getSourceStartIndexY() {
        return sourceStartIndexY.get();
    }

    public DoubleProperty zoomHValueProperty() {
        return zoomHValue;
    }

    public DoubleProperty zoomVValueProperty() {
        return zoomVValue;
    }

    public double getZoomScaleFactor() {
        return zoomScaleFactor.get();
    }

    public DoubleProperty zoomScaleFactorProperty() {
        return zoomScaleFactor;
    }

    public void setZoomScaleFactor(double zoomScaleFactor) {
        this.zoomScaleFactor.set(zoomScaleFactor);
    }

    public int getDrawSourceX() {
        return drawSourceX.get();
    }

    public int getDrawSourceY() {
        return drawSourceY.get();
    }

    public int getDrawSourceWidth() {
        return drawSourceWidth.get();
    }

    public int getDrawSourceHeight() {
        return drawSourceHeight.get();
    }

    public double getDrawDestinationX() {
        return drawDestinationX.get();
    }

    public double getDrawDestinationY() {
        return drawDestinationY.get();
    }

    public double getDrawDestinationWidth() {
        return drawDestinationWidth.get();
    }

    public double getDrawDestinationHeight() {
        return drawDestinationHeight.get();
    }
}
