package org.maggdadev.forestpixel.canvas.tools.viewmodels;

import org.maggdadev.forestpixel.canvas.tools.ToolType;
import org.maggdadev.forestpixel.canvas.tools.models.RubberModel;

public class RubberViewModel extends ToolViewModel {
    private final RubberModel model;

    public RubberViewModel(RubberModel model) {
        super(ToolType.RUBBER);
        this.model = model;
    }
}
