package org.maggdadev.forestpixel.canvas.history;

import javafx.scene.paint.Color;
import org.maggdadev.forestpixel.canvas.layers.CanvasLayerModel;
import org.maggdadev.forestpixel.canvas.utils.Point;

public class SinglePixelChange extends CanvasChange {
    private final Color oldColor, newColor;
    private final boolean isChange;
    private final Point point;

    public SinglePixelChange(CanvasLayerModel model, Point point, Color color) {
        super(model);
        newColor = color;
        oldColor = model.getColorAt(point.x, point.y);
        this.point = point;
        isChange = !newColor.equals(oldColor);
    }

    @Override
    public void apply() {
        if (!model.getColorAt(point.x, point.y).equals(oldColor)) {
            throw new RuntimeException("History error: Trying to apply a change, but old colors differ (saved old: " + oldColor.toString() + ",     actual old: " + model.getColorAt(point.x, point.y));
        }
        model.setColorAt(point.x, point.y, newColor);
    }

    @Override
    public void undo() {
        if (!model.getColorAt(point.x, point.y).equals(newColor)) {
            throw new RuntimeException("History error: Trying to undo a change, but current state was not achieved with this change (change would be to: " + newColor.toString() + ",     actually found: " + model.getColorAt(point.x, point.y));
        }
        model.setColorAt(point.x, point.y, oldColor);
    }

    @Override
    public boolean isChange() {
        return isChange;
    }
}
