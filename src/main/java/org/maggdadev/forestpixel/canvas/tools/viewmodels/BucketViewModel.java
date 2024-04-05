package org.maggdadev.forestpixel.canvas.tools.viewmodels;

import org.maggdadev.forestpixel.canvas.events.CanvasMouseEvent;
import org.maggdadev.forestpixel.canvas.tools.models.BucketModel;

public class BucketViewModel extends ToolViewModel{

    private final BucketModel model;
    public BucketViewModel(BucketModel model) {
        this.model = model;
    }

    @Override
    protected void onPrimaryButtonTyped(CanvasMouseEvent e) {
        model.applyToCanvas(e.canvasModel(), e.xIdx(), e.yIdx());
    }
}
