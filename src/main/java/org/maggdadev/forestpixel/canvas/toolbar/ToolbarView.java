package org.maggdadev.forestpixel.canvas.toolbar;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Orientation;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.GridPane;
import org.maggdadev.forestpixel.canvas.tools.ToolType;
import org.maggdadev.forestpixel.canvas.tools.ToolView;
import org.maggdadev.forestpixel.canvas.tools.viewmodels.BucketViewModel;
import org.maggdadev.forestpixel.canvas.tools.viewmodels.ToolViewModel;
import org.maggdadev.forestpixel.canvas.tools.models.BucketModel;
import org.maggdadev.forestpixel.canvas.tools.models.PencilModel;
import org.maggdadev.forestpixel.canvas.tools.viewmodels.PencilViewModel;

public class ToolbarView extends ToolBar {
    private final ToolView[] toolViews;
    private final GridPane gridPane;
    private final ToggleGroup toggleGroup;

    private final ToolbarViewModel viewModel;

    public ToolbarView(ToolbarViewModel viewModel) {
        this.viewModel = viewModel;
        toolViews = new ToolView[]{
                new ToolView(new PencilViewModel(new PencilModel()), ToolType.PENCIL),
                new ToolView(new BucketViewModel(new BucketModel()), ToolType.BUCKET)
        };

        gridPane = new GridPane();

        for(int i = 0; i < toolViews.length; i++) {
            gridPane.add(toolViews[i], i % 2, i / 2);
        }

        gridPane.setHgap(2);
        gridPane.setVgap(2);
        widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                System.out.println(t1);
            }
        });
        getItems().addAll(gridPane);
        setPrefWidth(50);
        setWidth(50);
        setOrientation(Orientation.VERTICAL);
        setFocusTraversable(false);

        toggleGroup = new ToggleGroup();
        toggleGroup.getToggles().addAll(toolViews);

        toggleGroup.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
           if(newVal == null) {
               viewModel.setActiveToolViewModel(null);
           } else {
               viewModel.setActiveToolViewModel(((ToolView) newVal).getViewModel());
           }
        });
        if(toggleGroup.getSelectedToggle() != null)
            viewModel.setActiveToolViewModel(((ToolView) toggleGroup.getSelectedToggle()).getViewModel());
    }
}
