package org.maggdadev.forestpixel.canvas.tools.views;

import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import org.maggdadev.forestpixel.canvas.tools.ToolType;
import org.maggdadev.forestpixel.canvas.tools.viewmodels.ToolViewModel;
import org.maggdadev.forestpixel.io.Images;


public class ToolView extends ToggleButton {
    private final ToolType type;
    public final static double BUTTON_SIZE = 32;
    private final ImageView imageView;

    private final ToolViewModel viewModel;



    public ToolView (ToolViewModel viewModel, ToolType type){
        this.type = type;
        this.viewModel = viewModel;
        imageView = new ImageView();

        imageView.setImage(getImageFromType(type));

        imageView.setPreserveRatio(true);
        imageView.setFitWidth(BUTTON_SIZE);

        setTooltip(new Tooltip(null));
        getTooltip().textProperty().bind(Bindings.createStringBinding(() -> "Shortcut: " + viewModel.getShortcutKey().getName(), viewModel.shortcutKeyProperty()));
        getTooltip().setShowDelay(Duration.seconds(0.3));

        setGraphic(imageView);
        setPrefSize(BUTTON_SIZE, BUTTON_SIZE);
        setPadding(new Insets(0));
    }

    private Image getImageFromType(ToolType type) {
        Images images = Images.getInstance();
        return switch (type) {
            case PENCIL -> images.getToolIconPencil();
            case BUCKET -> images.getToolIconBucket();
            case PIPET -> images.getToolIconPipet();
            case LINE -> images.getToolIconLine();
            case MOVE -> images.getToolIconMove();
            case RUBBER -> images.getToolIconRubber();
            case SELECT -> images.getToolIconSelect();
            default ->
                    throw new UnsupportedOperationException("For '" + type.toString() + "' the suitable image icon is not implemented.");
        };
    }
    public ToolViewModel getViewModel() {
        return viewModel;
    }


}
