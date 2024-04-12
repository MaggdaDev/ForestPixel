package org.maggdadev.forestpixel.canvas.tools.viewmodels;

import org.maggdadev.forestpixel.canvas.tools.ToolType;
import org.maggdadev.forestpixel.canvas.tools.models.PipetModel;

public class PipetViewModel extends ToolViewModel {
    private final PipetModel model;

    public PipetViewModel(PipetModel model) {
        super(ToolType.PIPET);
        this.model = model;
    }
}
