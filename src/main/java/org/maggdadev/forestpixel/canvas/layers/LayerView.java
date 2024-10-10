package org.maggdadev.forestpixel.canvas.layers;

import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import org.maggdadev.forestpixel.canvas.CanvasContext;
import org.maggdadev.forestpixel.canvas.CanvasViewModel;
import org.maggdadev.forestpixel.canvas.PreviewImage;
import org.maggdadev.forestpixel.canvas.ZoomManager;

public class LayerView extends Canvas {

    private final CanvasViewModel canvasViewModel;
    private final LayerViewModel viewModel;

    private WritableImage workingImage;

    public LayerView(LayerViewModel viewModel, CanvasViewModel canvasViewModel) {
        this.canvasViewModel = canvasViewModel;
        this.viewModel = viewModel;
        widthProperty().bind(canvasViewModel.availableViewportWidthProperty());
        heightProperty().bind(canvasViewModel.availableViewportHeightProperty());

        opacityProperty().bind(viewModel.opacityProperty());
        getGraphicsContext2D().setImageSmoothing(false);
        setMouseTransparent(true);
        setManaged(false);
        viewOrderProperty().bind(viewModel.orderProperty().multiply(-1));
    }

    private void drawImage(Image image) {
        ZoomManager zh = canvasViewModel.getCanvasZoomHandler();
        getGraphicsContext2D().drawImage(image,
                zh.getDrawSourceX(),
                zh.getDrawSourceY(),
                zh.getDrawSourceWidth(),
                zh.getDrawSourceHeight(),
                zh.getDrawDestinationX(),
                zh.getDrawDestinationY(),
                zh.getDrawDestinationWidth(),
                zh.getDrawDestinationHeight());
    }

    public void redraw(CanvasContext canvasContext) {
        getGraphicsContext2D().clearRect(0, 0, getWidth(), getHeight());
        if (canvasContext.getPreviewImage() != null
                && canvasContext.getActiveLayerId().equals(getLayerId())
                && canvasContext.getPreviewImage().hasDeletedPoints()) {    // deleted points MUST be included in drawn image to avoid jfx auto smoothing
            workingImage = new WritableImage(viewModel.getDrawableImage().getPixelReader(), (int) viewModel.getDrawableImage().getWidth(), (int) viewModel.getDrawableImage().getHeight());
            canvasContext.getPreviewImage().getDeletedPoints().forEach(point -> {
                if (point.x >= 0 && point.x < workingImage.getWidth() && point.y >= 0 && point.y < workingImage.getHeight()) {
                    workingImage.getPixelWriter().setColor(point.x, point.y, Color.TRANSPARENT);
                }
            });
            drawImage(workingImage);
        } else {
            drawImage(viewModel.getDrawableImage());
        }
        getGraphicsContext2D().setFill(Color.RED);
        getGraphicsContext2D().fillRect(-10, -10, 20, 20);
    }

    public void drawPreviewImage(PreviewImage previewImage) {
        drawImage(previewImage.getDrawableImage());
    }

    private int xIdxToCanvasPos(int x) {
        return (int) canvasViewModel.getZoomScaleFactor() * (x - canvasViewModel.getSourceStartIndexX());
    }

    private int yIdxToCanvasPos(int y) {
        return (int) canvasViewModel.getZoomScaleFactor() * (y - canvasViewModel.getSourceStartIndexY());
    }

    public String getLayerId() {
        return viewModel.getId();
    }


}
