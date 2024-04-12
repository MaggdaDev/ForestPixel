package org.maggdadev.forestpixel.canvas.tools;

public enum ToolType {
    PENCIL(true),
    BUCKET(true),
    LINE(true),
    MOVE(false),
    PIPET(true),
    RUBBER(false),
    SELECT(false);

    public final boolean USES_COLOR;
    ToolType(boolean usesColor) {
        USES_COLOR = usesColor;
    }
}
