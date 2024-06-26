package org.maggdadev.forestpixel.io;

import javafx.scene.image.Image;

import java.net.URL;

public class Images {
    private static Images instance = null;
    private final static String TOOL_ICON_PATH = "/toolIcons/";


    // images
    private final Image toolIconPencil, toolIconBucket, toolIconLine, toolIconMove, toolIconPipet, toolIconRubber, toolIconSelect;

    private Images() {
        toolIconPencil = createImage(TOOL_ICON_PATH + "pencil.png");
        toolIconBucket = createImage(TOOL_ICON_PATH + "bucket.png");
        toolIconLine = createImage(TOOL_ICON_PATH + "line.png");
        toolIconMove = createImage(TOOL_ICON_PATH + "move.png");
        toolIconPipet = createImage(TOOL_ICON_PATH + "pipet.png");
        toolIconRubber = createImage(TOOL_ICON_PATH + "rubber.png");
        toolIconSelect = createImage(TOOL_ICON_PATH + "select.png");
    }

    /**
     * @param path Path to file WITHOUT /resources/. Must end with a valid file
     * @return an image or null, if the file is not found
     */
    private Image createImage(String path) {
        try {
            URL url = getClass().getResource(path);
            if (url == null) throw new NullPointerException();
            String externalFormPath = url.toExternalForm();
            return new Image(externalFormPath);
        } catch (NullPointerException e) {
            System.err.println("Image not found: " + path);
            e.printStackTrace();
            return null;
        }
    }


    public static Images getInstance() {
        if (instance == null) {
            instance = new Images();
        }
        return instance;
    }

    public Image getToolIconPencil() {
        return toolIconPencil;
    }

    public Image getToolIconBucket() {
        return toolIconBucket;
    }

    public Image getToolIconLine() {
        return toolIconLine;
    }

    public Image getToolIconMove() {
        return toolIconMove;
    }

    public Image getToolIconPipet() {
        return toolIconPipet;
    }

    public Image getToolIconRubber() {
        return toolIconRubber;
    }

    public Image getToolIconSelect() {
        return toolIconSelect;
    }
}
