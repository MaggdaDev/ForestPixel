package org.maggdadev.forestpixel.canvas;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.WeakChangeListener;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.skin.ScrollPaneSkin;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.maggdadev.forestpixel.canvas.toolbar.ToolbarView;
import org.maggdadev.forestpixel.canvas.tools.viewmodels.ToolViewModel;

import java.lang.reflect.Field;


public class CanvasView extends HBox {
    public final static double CANVAS_WIDTH = 800;
    public final static double CANVAS_HEIGHT = 500;
    private final ToolbarView toolBarView;
    private final VBox sideBar;
    private final CustomScrollPaneSkin scrollPaneSkin;
    private final Canvas canvas;

    private ToolViewModel activeToolViewModel;
    private final CanvasViewModel viewModel;

    private final Pane placeHolderPane;
    private final ScrollPane canvasScrollPane;

    private final double SCROLL_LOCK_EPSILON = 0.01;


    public CanvasView(CanvasViewModel viewModel) {
        this.viewModel = viewModel;
        sideBar = new VBox();
        Rectangle imageViewBackground = new Rectangle();
        imageViewBackground.setFill(Color.WHITE);

        placeHolderPane = new Pane();
        placeHolderPane.setPickOnBounds(true);
        placeHolderPane.setPadding(Insets.EMPTY);

        canvas = new Canvas();
        canvas.getGraphicsContext2D().setImageSmoothing(false);
        canvas.setPickOnBounds(true);
        canvas.setMouseTransparent(true);
        canvas.setWidth(CANVAS_WIDTH);
        canvas.setHeight(CANVAS_HEIGHT);


        toolBarView = new ToolbarView(viewModel.getToolBarViewModel());


        sideBar.getChildren().add(toolBarView);
        StackPane canvasStack = new StackPane(canvas, placeHolderPane);
        canvasScrollPane = new ScrollPane(canvasStack);
        scrollPaneSkin = new CustomScrollPaneSkin(canvasScrollPane);
        canvasScrollPane.setSkin(scrollPaneSkin);
        canvasScrollPane.setPrefSize(CANVAS_WIDTH, CANVAS_HEIGHT);

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

        canvas.layoutXProperty().bind(viewModel.viewportPosXProperty());
        canvas.layoutYProperty().bind(viewModel.viewportPosYProperty());

        viewModel.zoomScaleFactorProperty().addListener((obs, oldVal, newVal) -> {
            redrawImage();
        });

        viewModel.viewNeedsUpdateProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                redrawImage();
                viewModel.setViewNeedsUpdate(false);
            }
        });


        canvasScrollPane.hvalueProperty().addListener( (obs, oldVal, newVal) -> {
            redrawImage();
        });

        canvasScrollPane.vvalueProperty().addListener( (obs, oldVal, newVal) -> {
            redrawImage();
        });

    }

    public void redrawImage() {
        int sourceStartX = viewModel.xPosToIdx(viewModel.getViewportPosX());//viewModel.getXIdxFromRelativeToPlaceholderPane(viewModel.getViewportPosX());
        int sourceStartY = viewModel.yPosToIdx(viewModel.getViewportPosY());//viewModel.getXIdxFromRelativeToPlaceholderPane(viewModel.getViewportPosY());
        int sourceWidth = Math.round((float) (viewModel.getModelWidth() / viewModel.getZoomScaleFactor()));
        int sourceHeight = Math.round((float) (viewModel.getModelHeight() / viewModel.getZoomScaleFactor()));
        canvas.getGraphicsContext2D().clearRect(0,0, canvas.getWidth(), canvas.getHeight());
        canvas.getGraphicsContext2D().drawImage(viewModel.imageProperty().get(), sourceStartX, sourceStartY, sourceWidth, sourceHeight, 0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
    }

    private void installEventHandlers() {
        placeHolderPane.setOnMouseClicked(viewModel.getOnCanvasMouseClicked());
        placeHolderPane.setOnMousePressed(viewModel.getOnCanvasMousePressed());
        placeHolderPane.setOnMouseReleased(viewModel.getOnCanvasMouseReleased());
        placeHolderPane.setOnMouseDragged(viewModel.getOnCanvasMouseDragged());

        placeHolderPane.setOnMouseMoved((MouseEvent e)-> {
            System.out.println(e.getY() - viewModel.getViewportPosY());
        });
        canvasScrollPane.addEventFilter(ScrollEvent.SCROLL, (ScrollEvent e) -> { // prevent scroll pane usual scrolling when ctrl is down
            if (e.isControlDown()) {
                viewModel.getOnCanvasZoom().handle(e);
                e.consume();
            }
        });

        // dirty hack to make jfx stop messing with my zoom
        canvasScrollPane.contentProperty().addListener((obs, oldVal, newVal) -> {
            removeBoundsChangeListenerFromScrollPane();
        });
        removeBoundsChangeListenerFromScrollPane();

    }

    private void removeBoundsChangeListenerFromScrollPane() {
        try {
            Field field = ScrollPaneSkin.class.getDeclaredField("weakBoundsChangeListener");
            field.setAccessible(true);
            WeakChangeListener<Bounds> badListener = (WeakChangeListener<Bounds>) field.get(scrollPaneSkin);
            canvasScrollPane.getContent().layoutBoundsProperty().removeListener(badListener);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Hack not working.");
        }
    }

    static class CustomScrollPaneSkin extends ScrollPaneSkin {
        CustomScrollPaneSkin(ScrollPane skinnable) {
            super(skinnable);
        }

        public void setHBarValue(double val) {
            getHorizontalScrollBar().setValue(Math.max(0, Math.min(1, val)));
        }

        public void setVBarValue(double val) {
            getVerticalScrollBar().setValue(Math.max(0, Math.min(1, val)));
        }

        public DoubleProperty hBarValueProperty() {
            return getHorizontalScrollBar().valueProperty();
        }

        public DoubleProperty vBarValueProperty() {
            return getVerticalScrollBar().valueProperty();
        }


    }
}
