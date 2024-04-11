package org.maggdadev.forestpixel.canvas.history;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.Collection;
import java.util.List;

public class MultiPixelChange implements CanvasChange {
    /**
     *
     * @param image image to be edited
     * @param points list of points, where each paint is an 2D-index-tuple
     * @param colors list of colors for the points. Must be of same length as points
     */
    private final SinglePixelChange[] subChanges;
    public MultiPixelChange(Image image, List<int[]> points, List<Color> colors) {
        if(colors.size() != points.size()) {
            throw new IllegalArgumentException("Points (" + points.size() + ") must be of same length as colors (" + colors.size() + ")!");
        }
        subChanges = new SinglePixelChange[colors.size()];
        for(int i = 0; i < points.size(); i++) {
            subChanges[i] = new SinglePixelChange(image, points.get(i), colors.get(i));
        }
    }


    @Override
    public void applyToImage(WritableImage image) {
        for(SinglePixelChange singlePixelChange: subChanges) {
            singlePixelChange.applyToImage(image);
        }
    }

    @Override
    public void undoToImage(WritableImage image) {
        for(SinglePixelChange singlePixelChange: subChanges) {
            singlePixelChange.undoToImage(image);
        }
    }
}
