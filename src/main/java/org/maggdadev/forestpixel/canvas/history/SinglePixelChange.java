package org.maggdadev.forestpixel.canvas.history;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class SinglePixelChange implements CanvasChange{
    private final Color oldColor, newColor;

    private final int[] point;
    public SinglePixelChange(Image image, int[] point, Color color) {
        if(point.length != 2) {
            throw new IllegalArgumentException("Point must be 2D, found: " + point.length);
        }
        newColor = color;
        oldColor = image.getPixelReader().getColor(point[0], point[1]);
        this.point = point;
    }

    @Override
    public void applyToImage(WritableImage image) {
        if(!image.getPixelReader().getColor(point[0], point[1]).equals(oldColor)) {
            throw new RuntimeException("History error: Trying to apply a change, but old colors differ (saved old: " + oldColor.toString() + ",     actual old: " + image.getPixelReader().getColor(point[0], point[1]));
        }
        image.getPixelWriter().setColor(point[0], point[1], newColor);
    }

    @Override
    public void undoToImage(WritableImage image) {
        if(!image.getPixelReader().getColor(point[0], point[1]).equals(newColor)) {
            throw new RuntimeException("History error: Trying to undo a change, but current state was not achieved with this change (change would be to: " + newColor.toString() + ",     actually found: " + image.getPixelReader().getColor(point[0], point[1]));
        }
        image.getPixelWriter().setColor(point[0], point[1], oldColor);
    }
}
