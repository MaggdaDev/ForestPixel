package org.maggdadev.forestpixel.canvas;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.WeakChangeListener;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.skin.ScrollPaneSkin;
import javafx.scene.image.ImageView;
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

    private ToolViewModel activeToolViewModel;
    private final CanvasViewModel viewModel;

    private final ImageView imageView;

    private final ScrollPane canvasScrollPane;


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
        imageView.setSmooth(false);
        toolBarView = new ToolbarView(viewModel.getToolBarViewModel());



        sideBar.getChildren().add(toolBarView);
        StackPane canvasStack = new StackPane(imageViewBackground, imageView);
        canvasScrollPane = new ScrollPane(canvasStack);
        scrollPaneSkin = new CustomScrollPaneSkin(canvasScrollPane);
        canvasScrollPane.setSkin(scrollPaneSkin);
        canvasScrollPane.setPrefSize(CANVAS_WIDTH, CANVAS_HEIGHT);

        getChildren().addAll(sideBar, canvasScrollPane);
        setAlignment(Pos.TOP_LEFT);
        setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(1))));
        HBox.setHgrow(sideBar, Priority.NEVER);

        installEventHandlers();

        createBindings();

        viewModel.update();

    }

    private void createBindings() {
        imageView.imageProperty().bind(viewModel.imageProperty());

        imageView.fitWidthProperty().bind(Bindings.multiply(viewModel.modelWidthProperty(), viewModel.zoomScaleFactorProperty()));
        imageView.fitHeightProperty().bind(Bindings.multiply(viewModel.modelHeightProperty(), viewModel.zoomScaleFactorProperty()));

        canvasScrollPane.hvalueProperty().bindBidirectional(viewModel.zoomHValueProperty());
        canvasScrollPane.vvalueProperty().bindBidirectional(viewModel.zoomVValueProperty());

        //scrollPaneSkin.hBarValueProperty().bind(viewModel.zoomHValueProperty());
        //scrollPaneSkin.vBarValueProperty().bind(viewModel.zoomVValueProperty());

    }

    private void installEventHandlers() {
        imageView.setOnMouseClicked(viewModel.getOnCanvasMouseClicked());
        imageView.setOnMousePressed(viewModel.getOnCanvasMousePressed());
        imageView.setOnMouseReleased(viewModel.getOnCanvasMouseReleased());
        imageView.setOnMouseDragged(viewModel.getOnCanvasMouseDragged());

        canvasScrollPane.addEventFilter(ScrollEvent.SCROLL, (ScrollEvent e) -> { // prevent scroll pane usual scrolling when ctrl is down
            if(e.isControlDown()) {
                viewModel.getOnCanvasZoom().handle(e);
                e.consume();
                //scrollPaneSkin.setHBarValue(viewModel.getZoomHValue());
                //scrollPaneSkin.setVBarValue(viewModel.getZoomVValue());
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
