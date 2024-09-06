package org.maggdadev.forestpixel.canvas.tools.views;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
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
        mouseAreaRectangle.visibleProperty().bind(viewModel.mouseAreaIndicatorVisibleProperty());

        mouseAreaRectangle.setFill(Color.TRANSPARENT);
        mouseAreaRectangle.setStroke(Color.GREY);
        mouseAreaRectangle.setStrokeWidth(2);
        mouseAreaRectangle.setStrokeType(StrokeType.OUTSIDE);
        mouseAreaRectangle.getStrokeDashArray().addAll(5d, 10d);

        viewModel.isMouseInSelectAreaProperty().bind(mouseAreaRectangle.hoverProperty().and(mouseAreaRectangle.visibleProperty()));
    }

    public Rectangle getMouseAreaRectangle() {
        return mouseAreaRectangle;
    }


}
