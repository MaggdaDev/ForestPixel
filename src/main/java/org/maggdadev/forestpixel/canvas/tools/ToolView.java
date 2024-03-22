package org.maggdadev.forestpixel.canvas.tools;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import org.maggdadev.forestpixel.io.Images;

import java.io.File;


public class ToolView extends ToggleButton {
    private final ToolType type;
    private final static double BUTTON_SIZE = 32;
    private final ImageView imageView;
    public ToolView (ToolType type){
        this.type = type;
        imageView = new ImageView();

        imageView.setImage(getImageFromType(type));

        imageView.setPreserveRatio(true);
        imageView.setFitWidth(BUTTON_SIZE);

        setGraphic(imageView);
        setPrefSize(BUTTON_SIZE, BUTTON_SIZE);
        setPadding(new Insets(0));
    }

    private Image getImageFromType(ToolType type) {
        Images images = Images.getInstance();
        return switch (type) {
            case PENCIL -> images.getToolIconPencil();
            case BUCKET -> images.getToolIconBucket();
            default ->
                    throw new UnsupportedOperationException("For '" + type.toString() + "' the suitable image icon is not implemented.");
        };
    }



}
