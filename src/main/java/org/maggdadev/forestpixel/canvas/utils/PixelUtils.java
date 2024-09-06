package org.maggdadev.forestpixel.canvas.utils;

import java.util.ArrayList;
import java.util.List;

public class PixelUtils {

    public static List<int[]> straightLineFromTo(int fromX, int fromY, int toX, int toY, int lineWidth) {
        List<int[]> retList = new ArrayList<>();
        int dx = Math.abs(toX - fromX);
        int dy = Math.abs(toY - fromY);
        int sx = fromX < toX ? 1 : -1;
        int sy = fromY < toY ? 1 : -1;
        int err = dx - dy;

        int e2;
        while (true) {
            addPointsWithLineWidth(retList, fromX, fromY, lineWidth);
            if (fromX == toX && fromY == toY) {
                break;
            }
            e2 = 2 * err;
            if (e2 > -dy) {
                err -= dy;
                fromX += sx;
            }
            if (e2 < dx) {
                err += dx;
                fromY += sy;
            }
        }
        return retList;
    }

    private static void addPointsWithLineWidth(List<int[]> retList, int x, int y, int lineWidth) {
        int radius = lineWidth / 2;
        for (int i = -radius; i <= radius; i++) {
            for (int j = -radius; j <= radius; j++) {
                if (i * i + j * j <= radius * radius) {
                    retList.add(new int[]{x + i, y + j});
                }
            }
        }
    }
}
