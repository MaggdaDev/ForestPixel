package org.maggdadev.forestpixel.canvas.tools.viewmodels;

import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import org.maggdadev.forestpixel.canvas.CanvasContext;
import org.maggdadev.forestpixel.canvas.CanvasState;
import org.maggdadev.forestpixel.canvas.events.CanvasMouseEvent;
import org.maggdadev.forestpixel.canvas.events.CanvasZoomEvent;
import org.maggdadev.forestpixel.canvas.tools.ToolType;
import org.maggdadev.forestpixel.canvas.tools.models.SelectModel;

public class SelectViewModel extends ToolViewModel {
    private final SelectModel model;
    private final DoubleProperty gestureStartX = new SimpleDoubleProperty(-1),
            gestureStartY = new SimpleDoubleProperty(-1),
            width = new SimpleDoubleProperty(0),
            height = new SimpleDoubleProperty(0),
            gestureEndX = new SimpleDoubleProperty(-1),
            gestureEndY = new SimpleDoubleProperty(-1),
            areaStartX = new SimpleDoubleProperty(0),
            areaStartY = new SimpleDoubleProperty(0);
    private final IntegerProperty offsetX = new SimpleIntegerProperty(0), offsetY = new SimpleIntegerProperty(0);

    private final ObjectProperty<SelectState> selectState = new SimpleObjectProperty<>(SelectState.IDLE);
    private final BooleanProperty mouseAreaIndicatorVisible = new SimpleBooleanProperty(false);

    public SelectViewModel(SelectModel model, IntegerProperty offsetX, IntegerProperty offsetY, ReadOnlyDoubleProperty zoomFactor) {
        super(ToolType.SELECT);
        this.model = model;
        this.offsetX.bind(offsetX);
        this.offsetY.bind(offsetY);
        width.bind(Bindings.max(gestureEndX.subtract(gestureStartX), gestureStartX.subtract(gestureEndX)));
        height.bind(Bindings.max(gestureEndY.subtract(gestureStartY), gestureStartY.subtract(gestureEndY)));
        areaStartX.bind(Bindings.min(gestureStartX, gestureEndX).add(offsetX.multiply(zoomFactor)));
        areaStartY.bind(Bindings.min(gestureStartY, gestureEndY).add(offsetY.multiply(zoomFactor)));

        mouseAreaIndicatorVisible.bind(Bindings.createBooleanBinding(() ->
                        ((!selectState.get().equals(SelectState.IDLE)) && Math.min(Math.min(getGestureStartX(), getGestureStartY()), Math.min(getGestureEndX(), getGestureEndY())) >= 0),
                selectState, gestureStartX, gestureStartY, gestureEndX, gestureEndY));
    }

    @Override
    protected void onPrimaryButtonDragged(CanvasMouseEvent e) {
        super.onPrimaryButtonDragged(e);
        if (selectState.get().equals(SelectState.IDLE)) {
            startSelection(e);
        }
        writeEndIdxToModel(e.pixelXPos(), e.pixelYPos(), e.canvasContext());
        loadFromModel(e.canvasContext());
    }

    @Override
    protected void onPrimaryButtonReleased(CanvasMouseEvent e) {
        super.onPrimaryButtonReleased(e);
        if (selectState.get().equals(SelectState.SELECTING)) {
            selectState.set(SelectState.SELECTED);
            e.canvasContext().setState(CanvasState.SELECTED);
        }
        model.applyToPreview(e.canvasModel(), e.canvasContext(), e.xIdx(), e.yIdx());
    }

    @Override
    protected void onSelectionCancelled(CanvasMouseEvent e) {
        super.onSelectionCancelled(e);
        model.applyToCanvas(e.canvasModel(), e.canvasContext(), e.xIdx(), e.yIdx());
        resetSelection(e.canvasContext());
    }

    @Override
    public void onZoomEvent(CanvasZoomEvent event) {
        super.onZoomEvent(event);
        loadFromModel(event.canvasContext());
    }

    private void startSelection(CanvasMouseEvent e) {
        resetSelection(e.canvasContext());
        writeStartIdxToModel(e.pixelXPos(), e.pixelYPos(), e.canvasContext());
        selectState.set(SelectState.SELECTING);
    }

    private void resetSelection(CanvasContext context) {
        selectState.set(SelectState.IDLE);
        model.resetSelection();
        loadFromModel(context);
    }

    private void loadFromModel(CanvasContext context) {
        gestureStartX.set(idxToX(model.getSelectionStartIdxX(), context));
        gestureStartY.set(idxToY(model.getSelectionStartIdxY(), context));
        gestureEndX.set(idxToX(model.getSelectionEndIdxX(), context));
        gestureEndY.set(idxToY(model.getSelectionEndIdxY(), context));
    }

    private void writeStartIdxToModel(double x, double y, CanvasContext context) {
        model.setSelectionStartIdxX(xToIdx(x, context));
        model.setSelectionStartIdxY(yToIdx(y, context));
    }

    private void writeEndIdxToModel(double x, double y, CanvasContext context) {
        model.setSelectionEndIdxX(xToIdx(x, context));
        model.setSelectionEndIdxY(yToIdx(y, context));
    }

    public IntegerProperty offsetXProperty() {
        return offsetX;
    }

    public IntegerProperty offsetYProperty() {
        return offsetY;
    }

    private int xToIdx(double x, CanvasContext context) {
        return Math.round((float) (x / context.getZoomFactor()));
    }

    private int yToIdx(double y, CanvasContext context) {
        return Math.round((float) (y / context.getZoomFactor()));
    }

    private double idxToX(int xIdx, CanvasContext context) {
        return xIdx * context.getZoomFactor();
    }

    private double idxToY(int yIdx, CanvasContext context) {
        return yIdx * context.getZoomFactor();
    }

    public enum SelectState {
        IDLE,
        SELECTING,
        SELECTED
    }

    public double getGestureStartX() {
        return gestureStartX.get();
    }

    public DoubleProperty gestureStartXProperty() {
        return gestureStartX;
    }

    public double getGestureStartY() {
        return gestureStartY.get();
    }

    public DoubleProperty gestureStartYProperty() {
        return gestureStartY;
    }

    public double getWidth() {
        return width.get();
    }

    public DoubleProperty widthProperty() {
        return width;
    }

    public double getHeight() {
        return height.get();
    }

    public DoubleProperty heightProperty() {
        return height;
    }

    public SelectState getSelectState() {
        return selectState.get();
    }

    public ObjectProperty<SelectState> selectStateProperty() {
        return selectState;
    }

    public double getGestureEndX() {
        return gestureEndX.get();
    }

    public DoubleProperty gestureEndXProperty() {
        return gestureEndX;
    }

    public double getGestureEndY() {
        return gestureEndY.get();
    }

    public DoubleProperty gestureEndYProperty() {
        return gestureEndY;
    }

    public double getAreaStartX() {
        return areaStartX.get();
    }

    public DoubleProperty areaStartXProperty() {
        return areaStartX;
    }

    public double getAreaStartY() {
        return areaStartY.get();
    }

    public DoubleProperty areaStartYProperty() {
        return areaStartY;
    }

    public boolean isMouseAreaIndicatorVisible() {
        return mouseAreaIndicatorVisible.get();
    }

    public BooleanProperty mouseAreaIndicatorVisibleProperty() {
        return mouseAreaIndicatorVisible;
    }
}
