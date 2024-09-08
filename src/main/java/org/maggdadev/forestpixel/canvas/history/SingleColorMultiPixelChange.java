package org.maggdadev.forestpixel.canvas.history;

import javafx.scene.paint.Color;
import org.maggdadev.forestpixel.canvas.layers.CanvasLayerModel;
import org.maggdadev.forestpixel.canvas.utils.Point;

import java.util.ArrayList;
import java.util.List;

public class SingleColorMultiPixelChange extends MultiPixelChange {
    public SingleColorMultiPixelChange(CanvasLayerModel model, List<Point> points, Color color) {
        super(model, points, createColorList(color, points.size()));
    }

    private static List<Color> createColorList(Color color, int listSize) {
        List<Color> colorList = new ArrayList<>();
        for(int i = 0; i < listSize; i++) {
            colorList.add(new Color(color.getRed(), color.getGreen(), color.getBlue(), color.getOpacity()));
        }
        return colorList;
    }
}
