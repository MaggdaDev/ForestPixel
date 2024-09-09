package org.maggdadev.forestpixel.canvas;

import javafx.beans.binding.Bindings;
import javafx.beans.value.WeakChangeListener;
import javafx.collections.ListChangeListener;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.skin.ScrollPaneSkin;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import org.maggdadev.forestpixel.canvas.layers.CanvasLayerView;
import org.maggdadev.forestpixel.canvas.layers.LayersStackPane;
import org.maggdadev.forestpixel.canvas.layersbar.LayersBarView;
import org.maggdadev.forestpixel.canvas.toolbar.ToolbarView;
import org.maggdadev.forestpixel.canvas.tools.viewmodels.ToolViewModel;

import java.lang.reflect.Field;


public class CanvasView extends BorderPane {
    public final static double CANVAS_WIDTH = 1000;
    public final static double CANVAS_HEIGHT = 300;
    private final ToolbarView toolBarView;

    private final LayersBarView layersBarView;
    private final VBox leftSideBar, rightSideBar;

    private ToolViewModel activeToolViewModel;
    private final CanvasViewModel viewModel;

    private final Pane placeHolderPane;
    private final ScrollPane canvasScrollPane;

    private final LayersStackPane layersStackPane = new LayersStackPane();

    public CanvasView(CanvasViewModel viewModel) {
        this.viewModel = viewModel;
        leftSideBar = new VBox();
        rightSideBar = new VBox();

        placeHolderPane = new Pane();
        placeHolderPane.setPickOnBounds(true);
        placeHolderPane.setPadding(Insets.EMPTY);
        placeHolderPane.setMouseTransparent(false);

        CanvasLayerView layerView = new CanvasLayerView(viewModel.addLayer(), viewModel);
        layersStackPane.add(layerView);

        toolBarView = new ToolbarView(viewModel.getToolBarViewModel());
        leftSideBar.getChildren().add(toolBarView);

        layersBarView = new LayersBarView(viewModel.getLayersBarViewModel());
        rightSideBar.getChildren().add(layersBarView);

        StackPane canvasStack = new StackPane(layersStackPane, placeHolderPane);

        placeHolderPane.getChildren().addAll(toolBarView.getAdditionalToolNodesOnCanvas()); // keep children updated according to additional nodes on canvas
        toolBarView.getAdditionalToolNodesOnCanvas().addListener((ListChangeListener<? super Node>) (listChange) -> {
            while(listChange.next()) {
                if(listChange.wasAdded()) {
                    placeHolderPane.getChildren().addAll(listChange.getAddedSubList());
                } else if(listChange.wasRemoved()) {
                    placeHolderPane.getChildren().removeAll(listChange.getRemoved());
                }
            }
        });

        canvasScrollPane = new ScrollPane(canvasStack);
        canvasScrollPane.setPrefSize(CANVAS_WIDTH, CANVAS_HEIGHT);
        canvasScrollPane.setPickOnBounds(true);
        placeHolderPane.setOnMousePressed((e) -> {
            System.out.println("Your mom");
        });
        placeHolderPane.widthProperty().subscribe((newVal) -> {
            System.out.println("Width: " + newVal);
        });

        setLeft(leftSideBar);
        setCenter(canvasScrollPane);
        setRight(rightSideBar);
        setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(1))));
        HBox.setHgrow(leftSideBar, Priority.NEVER);

        installEventHandlers();

        createBindings();

        viewModel.update();


    }


    private void createBindings() {
        placeHolderPane.prefWidthProperty().bind(Bindings.multiply(viewModel.modelWidthProperty(), viewModel.zoomScaleFactorProperty()));
        placeHolderPane.prefHeightProperty().bind(Bindings.multiply(viewModel.modelHeightProperty(), viewModel.zoomScaleFactorProperty()));

        canvasScrollPane.hvalueProperty().bindBidirectional(viewModel.zoomHValueProperty());
        canvasScrollPane.vvalueProperty().bindBidirectional(viewModel.zoomVValueProperty());

        viewModel.zoomScaleFactorProperty().addListener((obs, oldVal, newVal) -> {
            redrawAll();
        });

        viewModel.viewNeedsUpdateProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                redrawAll();
                viewModel.setViewNeedsUpdate(false);
            }
        });


        canvasScrollPane.hvalueProperty().addListener( (obs, oldVal, newVal) -> {
            redrawAll();
        });

        canvasScrollPane.vvalueProperty().addListener( (obs, oldVal, newVal) -> {
            redrawAll();
        });

    }

    public void redrawAll() {
        layersStackPane.getLayers().forEach((layer) -> {
            layer.redraw(viewModel.getCanvasContext());
            if (viewModel.getPreviewImage() != null && viewModel.getCanvasContext().getActiveLayerId() == layer.getLayerId()) {
                layer.drawPreviewImage(viewModel.getPreviewImage());
            }
        });
    }


    private void installEventHandlers() {
        placeHolderPane.setOnMouseClicked(viewModel.getOnCanvasMouseClicked());
        placeHolderPane.setOnMousePressed(viewModel.getOnCanvasMousePressed());
        placeHolderPane.setOnMouseReleased(viewModel.getOnCanvasMouseReleased());
        placeHolderPane.setOnMouseDragged(viewModel.getOnCanvasMouseDragged());

        setOnKeyPressed((KeyEvent e) -> {
            if(e.isControlDown()) {
                switch (e.getCode()) {
                    case Z:
                        viewModel.undo();
                        break;
                    case Y:
                        viewModel.redo();
                        break;
                }
            }
        });

        canvasScrollPane.addEventFilter(ScrollEvent.SCROLL, (ScrollEvent e) -> { // prevent scroll pane usual scrolling when ctrl is down
            if (e.isControlDown()) {
                viewModel.getOnCanvasZoom().handle(e);
                e.consume();
            }
        });

        canvasScrollPane.setPadding(new Insets(0));

        // dirty hack to make jfx stop messing with my zoom
        canvasScrollPane.contentProperty().addListener((obs, oldVal, newVal) -> {
            removeBoundsChangeListenerFromScrollPane();
        });
        removeBoundsChangeListenerFromScrollPane();

    }

    private void removeBoundsChangeListenerFromScrollPane() {
        try {
            canvasScrollPane.setSkin(new ScrollPaneSkin(canvasScrollPane));
            Field field = ScrollPaneSkin.class.getDeclaredField("weakBoundsChangeListener");
            field.setAccessible(true);
            WeakChangeListener<Bounds> badListener = (WeakChangeListener<Bounds>) field.get(canvasScrollPane.getSkin());
            canvasScrollPane.getContent().layoutBoundsProperty().removeListener(badListener);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Hack not working.");
        }
    }

}
