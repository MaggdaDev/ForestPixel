package org.maggdadev.forestpixel.canvas.frames;

public class FrameModel {
    private final String id;

    private static int currentId = 0;

    public FrameModel() {
        id = String.valueOf(currentId++);
    }

    public String getId() {
        return id;
    }
}
