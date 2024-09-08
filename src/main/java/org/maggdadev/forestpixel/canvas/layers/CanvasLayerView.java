package org.maggdadev.forestpixel.canvas.layers;

import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import org.maggdadev.forestpixel.canvas.CanvasViewModel;
import org.maggdadev.forestpixel.canvas.PreviewImage;

public class CanvasLayerView extends Canvas {

    private final CanvasViewModel canvasViewModel;
    private final CanvasLayerViewModel viewModel;

    public CanvasLayerView(CanvasLayerViewModel viewModel, CanvasViewModel canvasViewModel) {
        this.canvasViewModel = canvasViewModel;
        this.viewModel = viewModel;
        widthProperty().bind(canvasViewModel.extendedCanvasPixelWidthProperty());
        heightProperty().bind(canvasViewModel.extendedCanvasPixelHeightProperty());
        layoutXProperty().bind(canvasViewModel.quantizedViewportXProperty());
        layoutYProperty().bind(canvasViewModel.quantizedViewportYProperty());
        getGraphicsContext2D().setImageSmoothing(false);
        setMouseTransparent(true);
        setManaged(false);
    }

    private void drawImage(Image image) {
        int leftExtra = canvasViewModel.getSourceStartIndexX() > 0 ? 1 : 0;
        int topExtra = canvasViewModel.getSourceStartIndexY() > 0 ? 1 : 0;
        getGraphicsContext2D().drawImage(image,
                canvasViewModel.getSourceStartIndexX() - 1,
                canvasViewModel.getSourceStartIndexY() - 1,
                canvasViewModel.getExtendedCanvasPixelWidth(),
                canvasViewModel.getExtendedCanvasPixelHeight(),
                -leftExtra * canvasViewModel.getZoomScaleFactor(),
                -topExtra * canvasViewModel.getZoomScaleFactor(),
                ((double) canvasViewModel.getExtendedCanvasPixelWidth()) * canvasViewModel.getZoomScaleFactor(),
                ((double) canvasViewModel.getExtendedCanvasPixelHeight()) * canvasViewModel.getZoomScaleFactor());
    }

    public void redraw() {
        getGraphicsContext2D().clearRect(0, 0, getWidth(), getHeight());
        drawImage(viewModel.getDrawableImage());
    }

    public void drawPreviewImage(PreviewImage previewImage) {
        drawImage(previewImage.getDrawableImage());
    }

    public int getLayerId() {
        return viewModel.getLayerId();
    }


}
