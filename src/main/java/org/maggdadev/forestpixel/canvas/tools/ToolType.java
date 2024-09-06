package org.maggdadev.forestpixel.canvas.tools;

public enum ToolType {
    PENCIL(true, true),
    BUCKET(true),
    LINE(true, true),
    MOVE(false),
    PIPET(true),
    RUBBER(false, true),
    SELECT(false);

    public final boolean USES_COLOR, USES_LINE_WIDTH;

    ToolType(boolean usesColor, boolean usesLineWidth) {
        USES_COLOR = usesColor;
        USES_LINE_WIDTH = usesLineWidth;
    }

    ToolType(boolean usesColor) {
        this(usesColor, false);
    }
}
