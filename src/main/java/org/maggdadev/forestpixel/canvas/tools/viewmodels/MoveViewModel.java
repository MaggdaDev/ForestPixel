package org.maggdadev.forestpixel.canvas.tools.viewmodels;

import org.maggdadev.forestpixel.canvas.tools.ToolType;
import org.maggdadev.forestpixel.canvas.tools.models.MoveModel;

public class MoveViewModel extends ToolViewModel{
    private final MoveModel model;

    public MoveViewModel(MoveModel model) {
        super(ToolType.MOVE);
        this.model = model;
    }
}
