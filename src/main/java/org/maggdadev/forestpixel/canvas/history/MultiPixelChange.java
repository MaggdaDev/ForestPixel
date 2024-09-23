package org.maggdadev.forestpixel.canvas.history;

import javafx.scene.paint.Color;
import org.maggdadev.forestpixel.canvas.layers.LayerModel;
import org.maggdadev.forestpixel.canvas.utils.Point;

import java.util.List;

public class MultiPixelChange extends CanvasChange {
    /**
     *
     * @param CanvasLayerModel layer to be edited
     * @param points list of points, where each paint is an 2D-index-tuple
     * @param colors list of colors for the points. Must be of same length as points
     */
    private final SinglePixelChange[] subChanges;
    private boolean isChange = false;

    public MultiPixelChange(LayerModel model, List<Point> points, List<Color> colors) {
        super(model);
        if(colors.size() != points.size()) {
            throw new IllegalArgumentException("Points (" + points.size() + ") must be of same length as colors (" + colors.size() + ")!");
        }
        subChanges = new SinglePixelChange[colors.size()];
        for(int i = 0; i < points.size(); i++) {
            subChanges[i] = new SinglePixelChange(model, points.get(i), colors.get(i));
            if(subChanges[i].isChange()) {
                isChange = true;
            }
        }
    }


    @Override
    public void apply() {
        for(SinglePixelChange singlePixelChange: subChanges) {
            singlePixelChange.apply();
        }
    }

    @Override
    public void undo() {
        for(SinglePixelChange singlePixelChange: subChanges) {
            singlePixelChange.undo();
        }
    }

    @Override
    public boolean isChange() {
        return isChange;
    }
}
