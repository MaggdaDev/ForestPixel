package org.maggdadev.forestpixel.canvas.toolbar;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Orientation;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.FlowPane;
import org.maggdadev.forestpixel.canvas.tools.ToolType;
import org.maggdadev.forestpixel.canvas.tools.ToolView;

public class ToolbarView extends ToolBar {
    private final ToolView[] toolViews;
    private final FlowPane flowPane;
    private final ToggleGroup toggleGroup;
    public ToolbarView() {
        toolViews = new ToolView[]{
                new ToolView(ToolType.PENCIL),
                new ToolView(ToolType.BUCKET)
        };

        flowPane = new FlowPane(toolViews);
        flowPane.setPrefWrapLength(68);
        flowPane.setHgap(2);
        flowPane.setVgap(2);
        widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                System.out.println(t1);
            }
        });
        getItems().addAll(flowPane);
        setPrefWidth(50);
        setWidth(50);
        setOrientation(Orientation.VERTICAL);
        setFocusTraversable(false);

        toggleGroup = new ToggleGroup();
        toggleGroup.getToggles().addAll(toolViews);
    }
}
