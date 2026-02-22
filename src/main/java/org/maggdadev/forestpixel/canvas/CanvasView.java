package org.maggdadev.forestpixel.canvas;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Subscription;
import org.maggdadev.forestpixel.canvas.frames.FramePane;
import org.maggdadev.forestpixel.canvas.frames.FramesBarView;
import org.maggdadev.forestpixel.canvas.layers.LayerView;
import org.maggdadev.forestpixel.canvas.layers.LayersBarView;
import org.maggdadev.forestpixel.canvas.toolbar.ToolbarView;

import java.util.Objects;


public class CanvasView extends BorderPane {
    public final static double CANVAS_WIDTH = 1000;
    public final static double CANVAS_HEIGHT = 300;
    private final CanvasViewModel viewModel;
    private final Pane placeHolderPane;
    private final ScrollPane canvasScrollPane;

    private final FramePane framePane;

    public CanvasView(CanvasViewModel viewModel) {
        this.viewModel = viewModel;
        VBox leftSideBar = new VBox();
        VBox rightSideBar = new VBox();
        HBox bottomBar = new HBox();

        placeHolderPane = new Pane();
        placeHolderPane.setPickOnBounds(true);
        placeHolderPane.setPadding(Insets.EMPTY);
        placeHolderPane.setMouseTransparent(false);
        placeHolderPane.setBorder(new Border(new BorderStroke(Color.GREY, BorderStrokeStyle.SOLID, null, new BorderWidths(1))));
        ToolbarView toolBarView = new ToolbarView(viewModel.getToolBarViewModel());
        leftSideBar.getChildren().add(toolBarView);

        LayersBarView layersBarView = new LayersBarView(viewModel.getLayersBarViewModel());
        rightSideBar.getChildren().add(layersBarView);

        FramesBarView framesBarView = new FramesBarView(viewModel.getFramesBarViewModel());
        bottomBar.getChildren().add(framesBarView);

        placeHolderPane.getChildren().addAll(toolBarView.getAdditionalToolNodesOnCanvas()); // keep children updated according to additional nodes on canvas
        toolBarView.getAdditionalToolNodesOnCanvas().addListener((ListChangeListener<? super Node>) (listChange) -> {
            while (listChange.next()) {
                if (listChange.wasAdded()) {
                    placeHolderPane.getChildren().addAll(listChange.getAddedSubList());
                } else if (listChange.wasRemoved()) {
                    placeHolderPane.getChildren().removeAll(listChange.getRemoved());
                }
            }
        });

        canvasScrollPane = new ScrollPane(placeHolderPane);
        canvasScrollPane.setPrefSize(CANVAS_WIDTH, CANVAS_HEIGHT);
        canvasScrollPane.setPickOnBounds(true);

        framePane = new FramePane(viewModel);

        setLeft(leftSideBar);
        setCenter(new StackPane(canvasScrollPane, framePane));
        setRight(rightSideBar);
        setBottom(bottomBar);

        setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(1))));
        HBox.setHgrow(leftSideBar, Priority.NEVER);

        installEventHandlers();

        createBindings();
        setOnCanvasReady(viewModel::update);

    }

    public void setOnCanvasReady(Runnable onCanvasReady) {
        ChangeListener<Bounds> listener = new ChangeListener<>() {
            @Override
            public void changed(ObservableValue<? extends Bounds> observableValue, Bounds bounds, Bounds newBounds) {
                if (newBounds != null && newBounds.getWidth() > 0 && newBounds.getHeight() > 0) {
                    onCanvasReady.run();
                    observableValue.removeListener(this);   // Execute only once

                }
            }
        };
        canvasScrollPane.viewportBoundsProperty().addListener(listener);
        listener.changed(canvasScrollPane.viewportBoundsProperty(), null, canvasScrollPane.getViewportBounds());    // Execute initially
    }

    private void createBindings() {
        placeHolderPane.prefWidthProperty().bind(Bindings.multiply(viewModel.modelWidthProperty(), viewModel.zoomScaleFactorProperty()));
        placeHolderPane.prefHeightProperty().bind(Bindings.multiply(viewModel.modelHeightProperty(), viewModel.zoomScaleFactorProperty()));

        canvasScrollPane.hvalueProperty().bindBidirectional(viewModel.zoomHValueProperty());
        canvasScrollPane.vvalueProperty().bindBidirectional(viewModel.zoomVValueProperty());
        viewModel.availableViewportHeightProperty().bind(canvasScrollPane.viewportBoundsProperty().map(Bounds::getHeight));
        viewModel.availableViewportWidthProperty().bind(canvasScrollPane.viewportBoundsProperty().map(Bounds::getWidth));

        viewModel.zoomScaleFactorProperty().addListener((obs, oldVal, newVal) -> {
            redrawAll();
            canvasScrollPane.layout();  // Needed such that FIRST ScrollNode-size is adjusted according to new Zoom fact and
            // THEN new hval is set. The other way around, changing hval first and then layouting (=adjusting scroll node width),
            // the change in width would induce ANOTHER corrupt hval change
        });

        viewModel.viewNeedsUpdateProperty().subscribe((newVal) -> {
            if (newVal) {
                redrawAll();
                viewModel.setViewNeedsUpdate(false);
            }
        });


        canvasScrollPane.hvalueProperty().addListener((obs, oldVal, newVal) -> {
            redrawAll();
        });

        canvasScrollPane.vvalueProperty().addListener((obs, oldVal, newVal) -> {
            redrawAll();
        });

        viewModel.pauseRedrawAllDueToInternalChangesOngoingProperty().addListener((obs, oldVal, newShouldPause) -> {
            if (!newShouldPause) {
                redrawAll();
            }
        });
    }

    public void redrawAll() {
        if (framePane.getActiveLayersStackPane() == null) {
            System.out.println("Trying to redraw but active layer stack pane cant be found!");
            return;
        }
        framePane.getActiveLayersStackPane().getChildren().forEach((node) -> {
            if (node instanceof LayerView layerView) {
                layerView.redraw(viewModel.getCanvasContext());
                if (viewModel.getPreviewImage() != null && Objects.equals(viewModel.getCanvasContext().getActiveLayerId(), layerView.getLayerId())) {
                    layerView.drawPreviewImage(viewModel.getPreviewImage());
                }
            } else {
                System.out.println("Node is not a layer view: " + node.toString());
            }
        });
    }


    private void installEventHandlers() {
        placeHolderPane.setOnMouseClicked(viewModel.getOnCanvasMouseClicked());
        placeHolderPane.setOnMousePressed(viewModel.getOnCanvasMousePressed());
        placeHolderPane.setOnMouseReleased(viewModel.getOnCanvasMouseReleased());
        placeHolderPane.setOnMouseDragged(viewModel.getOnCanvasMouseDragged());

        setOnKeyPressed((KeyEvent e) -> {
            if (e.isControlDown()) {
                switch (e.getCode()) {
                    case Z:
                        viewModel.undo();
                        break;
                    case Y:
                        viewModel.redo();
                        break;
                    case V:
                        viewModel.paste();
                        break;
                    case C:
                        viewModel.copy();
                        break;
                    case X:
                        viewModel.cut();
                        break;
                }
            } else {
                viewModel.notifyKeyPressed(e.getCode());
            }
        });

        canvasScrollPane.addEventFilter(ScrollEvent.SCROLL, (ScrollEvent e) -> { // prevent scroll pane usual scrolling when ctrl is down
            if (e.isControlDown()) {
                viewModel.getOnCanvasZoom().handle(e);
                e.consume();
            }
        });

        canvasScrollPane.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent e) -> {
            if (e.getCode() == KeyCode.LEFT || e.getCode() == KeyCode.RIGHT || e.getCode() == KeyCode.UP || e.getCode() == KeyCode.DOWN) {
                viewModel.notifyKeyPressed(e.getCode());
                e.consume();
            }
        });

        canvasScrollPane.setPadding(new Insets(0));
    }
}
