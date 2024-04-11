package org.maggdadev.forestpixel.canvas.history;

import javafx.scene.image.WritableImage;

public interface CanvasChange {

    public void applyToImage(WritableImage image);
    public void undoToImage(WritableImage image);

    public boolean isChange();
}
