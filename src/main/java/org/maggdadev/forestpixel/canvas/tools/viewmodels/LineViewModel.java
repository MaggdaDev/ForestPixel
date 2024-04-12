package org.maggdadev.forestpixel.canvas.tools.viewmodels;

import org.maggdadev.forestpixel.canvas.tools.ToolType;
import org.maggdadev.forestpixel.canvas.tools.models.LineModel;

public class LineViewModel extends ToolViewModel{
    private final LineModel model;

    public LineViewModel(LineModel model) {
        super(ToolType.LINE);
        this.model = model;
    }
}
