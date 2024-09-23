package org.maggdadev.forestpixel.canvas;

import javafx.scene.image.Image;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import org.maggdadev.forestpixel.canvas.tools.viewmodels.SelectViewModel;

public class CopyPasteManager {
    private final static String CLIPBOARD_IDENTIFIER = "forestpixel";
    private final static DataFormat CLIPBOARD_IDENTIFIER_FORMAT = new DataFormat("text/forestpixelidentifier"), CLIPBOARD_IMAGE_OFFSET_FORMAT = new DataFormat("text/imageoffset");
    private final SelectViewModel selectViewModel;

    private final CanvasViewModel canvasViewModel;
    private final Clipboard clipboard;

    public CopyPasteManager(CanvasViewModel canvasViewModel, SelectViewModel selectViewModel) {
        this.selectViewModel = selectViewModel;
        this.canvasViewModel = canvasViewModel;
        clipboard = Clipboard.getSystemClipboard();

    }

    public void copy(CanvasContext canvasContext) {
        if (_copy(canvasContext)) {
            selectViewModel.notifyCopy();
        }
    }

    public void cut(CanvasContext canvasContext) {
        if (_copy(canvasContext)) {
            selectViewModel.eraseSelection(canvasContext);
            selectViewModel.notifyCut();
        }
    }

    /**
     * @param canvasContext context
     * @return whether the copy was successful
     */
    private boolean _copy(CanvasContext canvasContext) {
        System.out.println("copy");
        if (!canvasContext.getActiveLayerId().equals("-1") && canvasContext.getState().equals(CanvasState.SELECTED)) {
            Image selectedAreaAsImage = selectViewModel.getSelectedAreaAsImage(canvasContext);
            if (selectedAreaAsImage == null) {
                return false; // no selection
            }
            // Add image to clipboard using dataformat
            ClipboardContent content = new ClipboardContent();
            content.put(CLIPBOARD_IDENTIFIER_FORMAT, CLIPBOARD_IDENTIFIER);
            content.put(CLIPBOARD_IMAGE_OFFSET_FORMAT, imageOffsetToString(selectViewModel.getTopLeftIdxXWithOffset(), selectViewModel.getTopLeftIdxYWithOffset()));
            content.putImage(selectedAreaAsImage);

            clipboard.setContent(content);
            return true;
        }
        return false;
    }

    public void paste(CanvasModel canvasModel, CanvasContext canvasContext) {
        System.out.println("paste");
        if (!canvasContext.getActiveLayerId().equals("-1") && clipboard.hasImage() &&
                clipboard.hasContent(CLIPBOARD_IMAGE_OFFSET_FORMAT) &&
                clipboard.hasContent(CLIPBOARD_IDENTIFIER_FORMAT) && CLIPBOARD_IDENTIFIER.equals(clipboard.getContent(CLIPBOARD_IDENTIFIER_FORMAT))) {
            if (!canvasContext.getState().equals(CanvasState.IDLE)) {
                canvasViewModel.cancelSelection(canvasContext, canvasModel);
            }
            if (canvasContext.getPreviewImage() == null) {
                canvasContext.setPreviewImage(new PreviewImage(canvasModel.getWidthPixels(), canvasModel.getHeightPixels(), canvasContext.lineWidthProperty()));
            }
            Image pasteImage = clipboard.getImage();
            int[] offset = stringToImageOffset((String) clipboard.getContent(CLIPBOARD_IMAGE_OFFSET_FORMAT));
            canvasContext.getPreviewImage().setPixels(offset[0], offset[1], (int) pasteImage.getWidth(), (int) pasteImage.getHeight(), pasteImage.getPixelReader(), 0, 0);
            selectViewModel.selectOnPreviewImage(offset[0], offset[1], (int) pasteImage.getWidth(), (int) pasteImage.getHeight(), canvasContext, canvasModel);
            selectViewModel.notifyPaste();
        }
    }

    private String imageOffsetToString(int xIdx, int yIdx) {
        return xIdx + "," + yIdx;
    }

    private int[] stringToImageOffset(String string) {
        String[] split = string.split(",");
        return new int[]{Integer.parseInt(split[0]), Integer.parseInt(split[1])};
    }
}
