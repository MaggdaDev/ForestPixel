package org.maggdadev.forestpixel.canvas;

import javafx.scene.canvas.Canvas;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.maggdadev.forestpixel.canvas.toolbar.ToolbarView;

public class CanvasScreen extends HBox {

    private final Canvas canvas;
    private final ToolbarView toolBarView;
    private final VBox sideBar;

    public CanvasScreen() {
        canvas = new Canvas();
        sideBar = new VBox();

        toolBarView = new ToolbarView();

        sideBar.getChildren().add(toolBarView);

        getChildren().addAll(sideBar, canvas);
        HBox.setHgrow(sideBar, Priority.NEVER);
        HBox.setHgrow(canvas, Priority.ALWAYS);

    }
}
