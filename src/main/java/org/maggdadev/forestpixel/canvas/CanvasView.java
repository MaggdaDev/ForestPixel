package org.maggdadev.forestpixel.canvas;

import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.maggdadev.forestpixel.canvas.events.PrimaryButtonEvent;
import org.maggdadev.forestpixel.canvas.toolbar.ToolbarView;
import org.maggdadev.forestpixel.canvas.toolbar.ToolbarViewModel;
import org.maggdadev.forestpixel.canvas.tools.ToolViewModel;


public class CanvasView extends HBox {
    private final ToolbarView toolBarView;
    private final VBox sideBar;

    private ToolViewModel activeToolViewModel;
    private final CanvasViewModel viewModel;

    private final ImageView imageView;


    public CanvasView(CanvasViewModel viewModel) {
        this.viewModel = viewModel;
        sideBar = new VBox();
        Rectangle imageViewBackground = new Rectangle();
        imageViewBackground.setFill(Color.WHITE);
        imageView = new ImageView();

        imageViewBackground.widthProperty().bind(imageView.fitWidthProperty());
        imageViewBackground.heightProperty().bind(imageView.fitHeightProperty());
        imageViewBackground.setMouseTransparent(true);

        imageView.setPickOnBounds(true);
        imageView.setFitWidth(800);
        imageView.setFitHeight(500);
        imageView.imageProperty().bind(viewModel.imageProperty());
        toolBarView = new ToolbarView(viewModel.getToolBarViewModel());



        sideBar.getChildren().add(toolBarView);
        StackPane canvasStack = new StackPane(imageViewBackground, imageView);
        Group group = new Group(canvasStack);
        getChildren().addAll(sideBar, group);
        setAlignment(Pos.TOP_LEFT);
        setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(1))));
        HBox.setHgrow(sideBar, Priority.NEVER);

        installEventHandlers();

        viewModel.update();

    }

    private void installEventHandlers() {
        imageView.setOnMouseClicked((MouseEvent e) -> {
            System.out.println("Mouse clicked on image view");
            viewModel.handleCanvasEvent(new PrimaryButtonEvent(viewModel.getModel(), Math.round((float)e.getX()), Math.round((float)e.getY())));
        });
    }
}
