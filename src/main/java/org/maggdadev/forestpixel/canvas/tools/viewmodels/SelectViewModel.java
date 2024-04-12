package org.maggdadev.forestpixel.canvas.tools.viewmodels;

import org.maggdadev.forestpixel.canvas.tools.ToolType;
import org.maggdadev.forestpixel.canvas.tools.models.SelectModel;

public class SelectViewModel extends ToolViewModel {
    private final SelectModel model;

    public SelectViewModel(SelectModel model) {
        super(ToolType.SELECT);
        this.model = model;
    }
}
