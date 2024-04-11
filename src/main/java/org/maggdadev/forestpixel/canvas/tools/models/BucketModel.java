package org.maggdadev.forestpixel.canvas.tools.models;

import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import org.maggdadev.forestpixel.canvas.CanvasContext;
import org.maggdadev.forestpixel.canvas.CanvasModel;

import java.util.Stack;

public class BucketModel extends ToolModel {
    @Override
    public void applyToPreview(CanvasModel canvasModel, CanvasContext canvasContext, int xIdx, int yIdx) {
        super.applyToPreview(canvasModel, canvasContext, xIdx, yIdx);

        Color startColor = canvasModel.getPixelColor(xIdx, yIdx);
        Color fillColor = canvasContext.getColor();
        if ((!canvasModel.isOnCanvas(xIdx, yIdx)) || startColor.equals(fillColor))
            return;

        Stack<int[]> stack = new Stack<>();
        stack.add(new int[]{xIdx, xIdx, yIdx, 1});
        stack.add(new int[]{xIdx, xIdx, yIdx - 1, -1});
        int[] currArr;
        int x, y, x1, x2, dy;
        while (!stack.isEmpty()) {
            currArr = stack.pop();
            x1 = currArr[0];
            x2 = currArr[1];
            y = currArr[2];
            dy = currArr[3];
            x = x1;
            if (isToBeFilled(x, y, startColor, canvasModel, canvasContext.getPreviewImage())) {
                while (isToBeFilled(x - 1, y, startColor, canvasModel, canvasContext.getPreviewImage())) {
                    canvasContext.getPreviewImage().getPixelWriter().setColor(x - 1, y, fillColor);
                    x--;
                }
                if (x < x1) {
                    stack.add(new int[]{x, x1 - 1, y - dy, -dy});
                }
            }

            while (x1 <= x2) {
                while (isToBeFilled(x1, y, startColor, canvasModel, canvasContext.getPreviewImage())) {
                    canvasContext.getPreviewImage().getPixelWriter().setColor(x1, y, fillColor);
                    x1++;
                }
                if (x1 > x) {
                    stack.add(new int[]{x, x1 - 1, y + dy, dy});
                }
                if (x1 - 1 > x2) {
                    stack.add(new int[]{x2 + 1, x1 - 1, y - dy, -dy});
                }
                x1++;
                while (x1 < x2 && (!isToBeFilled(x1, y, startColor, canvasModel, canvasContext.getPreviewImage()))) {
                    x1++;
                }
                x = x1;
            }
        }
    }

    private boolean isToBeFilled(int x, int y, Color startColor, CanvasModel canvasModel, WritableImage writtenOnImage) {
        return canvasModel.isOnCanvas(x, y) && startColor.equals(writtenOnImage.getPixelReader().getColor(x, y));
    }


}
