package org.maggdadev.forestpixel.canvas;

import javafx.beans.binding.Bindings;
import javafx.beans.value.WeakChangeListener;
import javafx.collections.ListChangeListener;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.skin.ScrollPaneSkin;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.maggdadev.forestpixel.canvas.toolbar.ToolbarView;
import org.maggdadev.forestpixel.canvas.tools.viewmodels.ToolViewModel;

import java.lang.reflect.Field;


public class CanvasView extends HBox {
    public final static double CANVAS_WIDTH = 1000;
    public final static double CANVAS_HEIGHT = 300;
    private final ToolbarView toolBarView;
    private final VBox sideBar;
    private final Canvas canvas;

    private ToolViewModel activeToolViewModel;
    private final CanvasViewModel viewModel;

    private final Pane placeHolderPane;
    private final ScrollPane canvasScrollPane;

    public CanvasView(CanvasViewModel viewModel) {
        this.viewModel = viewModel;
        sideBar = new VBox();
        Rectangle imageViewBackground = new Rectangle();
        imageViewBackground.setFill(Color.WHITE);

        placeHolderPane = new Pane();
        placeHolderPane.setPickOnBounds(true);
        placeHolderPane.setPadding(Insets.EMPTY);
        placeHolderPane.setMouseTransparent(false);



        canvas = new Canvas();
        canvas.getGraphicsContext2D().setImageSmoothing(false);
        canvas.setPickOnBounds(true);
        canvas.setMouseTransparent(true);
        canvas.widthProperty().bind(viewModel.extendedCanvasPixelWidthProperty());
        canvas.heightProperty().bind(viewModel.extendedCanvasPixelHeightProperty());


        toolBarView = new ToolbarView(viewModel.getToolBarViewModel());


        sideBar.getChildren().add(toolBarView);
        StackPane canvasStack = new StackPane(canvas, placeHolderPane);

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

        canvas.setManaged(false);

        getChildren().addAll(sideBar, canvasScrollPane);
        setAlignment(Pos.TOP_LEFT);
        setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(1))));
        HBox.setHgrow(sideBar, Priority.NEVER);

        installEventHandlers();

        createBindings();

        viewModel.update();
    }


    private void createBindings() {
        placeHolderPane.prefWidthProperty().bind(Bindings.multiply(viewModel.modelWidthProperty(), viewModel.zoomScaleFactorProperty()));
        placeHolderPane.prefHeightProperty().bind(Bindings.multiply(viewModel.modelHeightProperty(), viewModel.zoomScaleFactorProperty()));


        canvasScrollPane.hvalueProperty().bindBidirectional(viewModel.zoomHValueProperty());
        canvasScrollPane.vvalueProperty().bindBidirectional(viewModel.zoomVValueProperty());

        canvas.layoutXProperty().bind(viewModel.quantizedViewportXProperty());
        canvas.layoutYProperty().bind(viewModel.quantizedViewportYProperty());

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
        canvas.getGraphicsContext2D().clearRect(0,0, canvas.getWidth(), canvas.getHeight());
        drawImage(viewModel.imageProperty().get());
        if(viewModel.getPreviewImage() != null) {
            drawImage(viewModel.getPreviewImage());
        }

    }

    private void drawImage(Image image) {
        int leftExtra = viewModel.getSourceStartIndexX() > 0 ? 1 : 0;
        int topExtra = viewModel.getSourceStartIndexY() > 0 ? 1 : 0;
        canvas.getGraphicsContext2D().drawImage(image,
                viewModel.getSourceStartIndexX()-1,
                viewModel.getSourceStartIndexY()-1,
                    viewModel.getExtendedCanvasPixelWidth(),
                    viewModel.getExtendedCanvasPixelHeight(),
                - leftExtra * viewModel.getZoomScaleFactor(),
                - topExtra * viewModel.getZoomScaleFactor(),
                ((double)viewModel.getExtendedCanvasPixelWidth()) * viewModel.getZoomScaleFactor(),
                ((double)viewModel.getExtendedCanvasPixelHeight()) * viewModel.getZoomScaleFactor());
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
