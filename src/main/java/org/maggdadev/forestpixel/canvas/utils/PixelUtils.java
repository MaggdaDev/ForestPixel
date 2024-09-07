package org.maggdadev.forestpixel.canvas.utils;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class PixelUtils {

    public static Iterable<Point> straightLineFromTo(int fromX, int fromY, int toX, int toY, int lineWidth) {
        long timeNow = System.currentTimeMillis();
        Set<Point> set = new HashSet<>();
        int dx = Math.abs(toX - fromX);
        int dy = Math.abs(toY - fromY);
        int sx = fromX < toX ? 1 : -1;
        int sy = fromY < toY ? 1 : -1;
        int err = dx - dy;

        int e2;
        while (true) {
            addPointsWithLineWidth(set, fromX, fromY, lineWidth);
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
        System.out.println("Time: " + (System.currentTimeMillis() - timeNow));
        return set;
    }

    private static void addPointsWithLineWidth(Collection<Point> retSet, int x, int y, int lineWidth) {
        int radius = lineWidth / 2;
        for (int i = -radius; i <= radius; i++) {
            for (int j = -radius; j <= radius; j++) {
                if (i * i + j * j <= radius * radius) {
                    retSet.add(new Point(x + i, y + j));
                }
            }
        }
    }
}
