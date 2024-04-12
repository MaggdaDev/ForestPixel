package org.maggdadev.forestpixel.canvas.tools.viewmodels;

import org.maggdadev.forestpixel.canvas.events.CanvasMouseEvent;
import org.maggdadev.forestpixel.canvas.toolbar.ToolbarViewModel;
import org.maggdadev.forestpixel.canvas.tools.ToolType;
import org.maggdadev.forestpixel.canvas.tools.models.PipetModel;

public class PipetViewModel extends ToolViewModel {
    private final PipetModel model;
    private final ToolbarViewModel toolbarViewModel;

    public PipetViewModel(PipetModel model, ToolbarViewModel toolBarViewModel) {
        super(ToolType.PIPET);
        this.model = model;
        this.toolbarViewModel = toolBarViewModel;
    }

    @Override
    protected void onPrimaryButtonPressed(CanvasMouseEvent e) {
        model.applyToPreview(e.canvasModel(), e.canvasContext(), e.xIdx(), e.yIdx());
    }

    @Override
    protected void onPrimaryButtonReleased(CanvasMouseEvent e) {
        super.onPrimaryButtonReleased(e);
        toolbarViewModel.selectPreviousTool();
    }
}
