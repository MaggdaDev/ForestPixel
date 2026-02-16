package org.maggdadev.forestpixel.screen;

import java.io.File;

public interface MainScreenDialogService {

    public enum CloseProjectResult {
        SAVE_AND_CLOSE,
        DISCARD_AND_CLOSE,
        CANCEL
    }
    CloseProjectResult mayDeleteProjectAlert();
    File saveAsDialog();
    File openDialog();
}
