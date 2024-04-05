package org.maggdadev.forestpixel.canvas.tools;

public enum ToolType {
    PENCIL(true),
    BUCKET(true);

    public final boolean USES_COLOR;
    ToolType(boolean usesColor) {
        USES_COLOR = usesColor;
    }
}
