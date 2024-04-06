package org.maggdadev.forestpixel.canvas;

import javafx.application.Platform;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.ZoomEvent;

public class CanvasZoomHandler {
    private final static double Y_TO_ZOOM_FACT = 0.01;
    private final CanvasViewModel viewModel;
    public CanvasZoomHandler(CanvasViewModel viewModel) {
        this.viewModel = viewModel;
    }
    public void handleZoomEvent(ScrollEvent e) {
        double oldZoomVal = viewModel.zoomScaleFactorProperty().get();

        double newZoomVal = oldZoomVal * Math.pow(2.0, Math.signum(e.getDeltaY()));//Math.max(oldZoomVal + e.getDeltaY() * Y_TO_ZOOM_FACT * oldZoomVal, 1.0);

        double oldHVal = viewModel.getZoomHValue();
        double oldVVal = viewModel.getZoomVValue();


        double oldEffWidth = viewModel.getModelWidth() * oldZoomVal - CanvasView.CANVAS_WIDTH;
        double oldEffHeight = viewModel.getModelHeight() * oldZoomVal - CanvasView.CANVAS_HEIGHT;

        double newEffWidth = viewModel.getModelWidth() * newZoomVal - CanvasView.CANVAS_WIDTH;
        double newEffHeight = viewModel.getModelHeight() * newZoomVal - CanvasView.CANVAS_HEIGHT;

        double modelEventX = (e.getX() + oldHVal * oldEffWidth) / oldZoomVal;

        double modelEventY = (e.getY() + oldVVal * oldEffHeight) / oldZoomVal;
        viewModel.zoomScaleFactorProperty().set(newZoomVal);

        double deltaZoom = newZoomVal - oldZoomVal;

        double newHVal = (modelEventX * deltaZoom + oldHVal * oldEffWidth) / newEffWidth;
        double newVVal = (modelEventY * deltaZoom + oldVVal * oldEffHeight) / newEffHeight;


        newHVal = Math.max(0, Math.min(1, newHVal));
        newVVal = Math.max(0, Math.min(1, newVVal));

        viewModel.setZoomHValue(newHVal);
        viewModel.setZoomVValue(newVVal);
    }




}
