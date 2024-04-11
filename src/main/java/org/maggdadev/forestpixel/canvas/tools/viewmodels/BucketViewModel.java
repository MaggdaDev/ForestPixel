package org.maggdadev.forestpixel.canvas.tools.viewmodels;

import org.maggdadev.forestpixel.canvas.events.CanvasMouseEvent;
import org.maggdadev.forestpixel.canvas.tools.ToolType;
import org.maggdadev.forestpixel.canvas.tools.models.BucketModel;

public class BucketViewModel extends ToolViewModel{

    private final BucketModel model;
    public BucketViewModel(BucketModel model) {
        super(ToolType.BUCKET);
        this.model = model;
    }

    @Override
    protected void onPrimaryButtonPressed(CanvasMouseEvent e) {
        model.applyToPreview(e.canvasModel(), e.canvasContext(),  e.xIdx(), e.yIdx());
    }

    @Override
    protected void onPrimaryButtonReleased(CanvasMouseEvent e) {
        model.applyToCanvas(e.canvasModel(), e.canvasContext(),  e.xIdx(), e.yIdx());
    }
}
