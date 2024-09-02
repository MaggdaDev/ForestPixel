package org.maggdadev.forestpixel.canvas.tools.views;

import javafx.scene.shape.Rectangle;
import org.maggdadev.forestpixel.canvas.tools.ToolType;
import org.maggdadev.forestpixel.canvas.tools.viewmodels.SelectViewModel;

public class SelectView extends ToolView{
    private final Rectangle mouseAreaRectangle = new Rectangle();
    public SelectView(SelectViewModel viewModel) {
        super(viewModel, ToolType.SELECT);
        mouseAreaRectangle.xProperty().bind(viewModel.areaStartXProperty());
        mouseAreaRectangle.yProperty().bind(viewModel.areaStartYProperty());
        mouseAreaRectangle.widthProperty().bind(viewModel.widthProperty());
        mouseAreaRectangle.heightProperty().bind(viewModel.heightProperty());
        mouseAreaRectangle.visibleProperty().bind(viewModel.selectStateProperty().isNotEqualTo(SelectViewModel.SelectState.IDLE));
    }

    public Rectangle getMouseAreaRectangle() {
        return mouseAreaRectangle;
    }


}
