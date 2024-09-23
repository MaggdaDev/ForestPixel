package org.maggdadev.forestpixel.canvas.tools.viewmodels;

import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.scene.image.Image;
import org.maggdadev.forestpixel.canvas.CanvasContext;
import org.maggdadev.forestpixel.canvas.CanvasModel;
import org.maggdadev.forestpixel.canvas.CanvasState;
import org.maggdadev.forestpixel.canvas.events.CanvasMouseEvent;
import org.maggdadev.forestpixel.canvas.events.CanvasSelectionCancelEvent;
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

    private final BooleanProperty isMouseInSelectArea;

    private final ObjectProperty<Runnable> onCopy = new SimpleObjectProperty<>(), onPaste = new SimpleObjectProperty<>();

    public SelectViewModel(SelectModel model, IntegerProperty offsetX, IntegerProperty offsetY, ReadOnlyDoubleProperty zoomFactor, BooleanProperty isMouseInSelectArea) {
        super(ToolType.SELECT);
        this.model = model;
        this.offsetX.bind(offsetX);
        this.offsetY.bind(offsetY);
        this.isMouseInSelectArea = isMouseInSelectArea;
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
    protected void onSelectionCancelled(CanvasSelectionCancelEvent e) {
        super.onSelectionCancelled(e);
        model.applyToCanvas(e.model(), e.context(), 0, 0);
        resetSelection(e.context());
    }

    @Override
    public void onZoomEvent(CanvasZoomEvent event) {
        super.onZoomEvent(event);
        loadFromModel(event.canvasContext());
    }


    public void selectOnPreviewImage(int xIdx, int yIdx, int width, int height, CanvasContext canvasContext, CanvasModel canvasModel) {
        if (!selectState.get().equals(SelectState.IDLE)) {
            throw new RuntimeException("SelectViewModel: selectOnPreviewImage called while not IDLE");
        }
        startSelection(canvasContext, idxToX(xIdx, canvasContext), idxToY(yIdx, canvasContext));
        writeEndIdxToModel(idxToX(xIdx + width, canvasContext), idxToY(yIdx + height, canvasContext), canvasContext);
        selectState.set(SelectState.SELECTED);
        canvasContext.setState(CanvasState.SELECTED);
        loadFromModel(canvasContext);

    }

    public Image getSelectedAreaAsImage(CanvasContext canvasContext) {
        if (!selectState.get().equals(SelectState.SELECTED)) {
            return null;
        }
        return canvasContext.getPreviewImage().getAreaAsImage(model.getTopLeftXWithOffset(offsetX.get()), model.getTopLeftYWithOffset(offsetY.get()), model.getWidth(), model.getHeight());
    }

    private void startSelection(CanvasMouseEvent e) {
        startSelection(e.canvasContext(), e.pixelXPos(), e.pixelYPos());
    }

    private void startSelection(CanvasContext canvasContext, double pixelXPos, double pixelYPos) {
        resetSelection(canvasContext);
        writeStartIdxToModel(pixelXPos, pixelYPos, canvasContext);
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

    public int getTopLeftIdxXWithOffset() {
        return model.getTopLeftXWithOffset(offsetX.get());
    }

    public int getTopLeftIdxYWithOffset() {
        return model.getTopLeftYWithOffset(offsetY.get());
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

    public void setIsMouseInSelectArea(boolean isMouseInSelectArea) {
        this.isMouseInSelectArea.set(isMouseInSelectArea);
    }

    public boolean isIsMouseInSelectArea() {
        return isMouseInSelectArea.get();
    }

    public BooleanProperty isMouseInSelectAreaProperty() {
        return isMouseInSelectArea;
    }

    public void setOnCopy(Runnable onCopy) {
        this.onCopy.set(onCopy);
    }

    public void setOnPaste(Runnable onPaste) {
        this.onPaste.set(onPaste);
    }

    public void notifyCopy() {
        onCopy.get().run();
    }

    public void notifyPaste() {
        onPaste.get().run();
    }
}
