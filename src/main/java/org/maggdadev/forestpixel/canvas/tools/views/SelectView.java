package org.maggdadev.forestpixel.canvas.tools.views;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.util.Duration;
import org.maggdadev.forestpixel.canvas.tools.ToolType;
import org.maggdadev.forestpixel.canvas.tools.viewmodels.SelectViewModel;


public class SelectView extends ToolView{
    private final Rectangle mouseAreaRectangle = new Rectangle();

    private final static Color BASE_COLOR = Color.GREY, COPY_COLOR = Color.LIGHTGREEN, PASTE_COLOR = Color.LIGHTBLUE;
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


        Timeline copyPulseTimeline = createColorPulseTimeLine(COPY_COLOR);
        Timeline pastePulseTimeline = createColorPulseTimeLine(PASTE_COLOR);
        viewModel.setOnCopy(copyPulseTimeline::play);
        viewModel.setOnPaste(pastePulseTimeline::play);
    }

    private Timeline createColorPulseTimeLine(Color color) {
        Timeline timeline = new Timeline();
        timeline.getKeyFrames().addAll(
                new KeyFrame(Duration.ZERO, new KeyValue(mouseAreaRectangle.strokeProperty(), color)),
                new KeyFrame(Duration.seconds(0.2), new KeyValue(mouseAreaRectangle.strokeProperty(), BASE_COLOR))
        );
        return timeline;
    }

    public Rectangle getMouseAreaRectangle() {
        return mouseAreaRectangle;
    }


}
