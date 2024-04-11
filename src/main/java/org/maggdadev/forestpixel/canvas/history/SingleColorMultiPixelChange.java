package org.maggdadev.forestpixel.canvas.history;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class SingleColorMultiPixelChange extends MultiPixelChange {
    public SingleColorMultiPixelChange(Image image, List<int[]> points, Color color) {
        super(image, points, createColorList(color, points.size()));
    }

    private static List<Color> createColorList(Color color, int listSize) {
        List<Color> colorList = new ArrayList<>();
        for(int i = 0; i < listSize; i++) {
            colorList.add(new Color(color.getRed(), color.getGreen(), color.getBlue(), color.getOpacity()));
        }
        return colorList;
    }
}
