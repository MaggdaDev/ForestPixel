package org.maggdadev.forestpixel.canvas.tools;

import javafx.scene.input.KeyCode;

public enum ToolType {
    PENCIL(KeyCode.D, true, true),
    BUCKET(KeyCode.F, true),
    LINE(KeyCode.L, true, true),
    MOVE(KeyCode.M, false),
    PIPET(KeyCode.P, true),
    RUBBER(KeyCode.R, false, true),
    SELECT(KeyCode.S, false);

    public final boolean USES_COLOR, USES_LINE_WIDTH;

    public final KeyCode DEFAULT_SHORTCUT;

    ToolType(KeyCode defaultShortCut, boolean usesColor, boolean usesLineWidth) {
        DEFAULT_SHORTCUT = defaultShortCut;
        USES_COLOR = usesColor;
        USES_LINE_WIDTH = usesLineWidth;
    }

    ToolType(KeyCode defaultShortCut, boolean usesColor) {
        this(defaultShortCut, usesColor, false);
    }
}
