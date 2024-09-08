package org.maggdadev.forestpixel.canvas.utils;

import java.util.Objects;

public class Point {
    public int x;
    public int y;

    public int layer = -1;   // No layer

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point(int x, int y, int layer) {
        this.x = x;
        this.y = y;
        this.layer = layer;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Point)) {
            return false;
        }
        Point other = (Point) obj;
        return other.x == x && other.y == y && other.layer == layer;

    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, layer);
    }
}
