package org.maggdadev.forestpixel.canvas.toolbar;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.maggdadev.forestpixel.canvas.tools.ToolType;
import org.maggdadev.forestpixel.canvas.tools.ToolView;
import org.maggdadev.forestpixel.canvas.tools.models.*;
import org.maggdadev.forestpixel.canvas.tools.viewmodels.*;

import java.nio.channels.Pipe;
import java.util.Arrays;

public class ToolbarView extends ToolBar {
    private final ToolView[] toolViews;
    private final GridPane gridPane;
    private final ToggleGroup toggleGroup;

    private final ToolbarViewModel viewModel;

    // extra panes

    private ColorPickerPane colorPickerPane;


    public ToolbarView(ToolbarViewModel viewModel) {
        this.viewModel = viewModel;

        toolViews = new ToolView[viewModel.getToolViewModelList().size()];
        gridPane = new GridPane();
        for (int i = 0; i < toolViews.length; i++) {
            toolViews[i] = new ToolView(viewModel.getToolViewModelList().get(i), viewModel.getToolViewModelList().get(i).getToolType());
            gridPane.add(toolViews[i], i % 2, i / 2);
        }

        gridPane.setHgap(2);
        gridPane.setVgap(2);
        setOrientation(Orientation.VERTICAL);
        setFocusTraversable(false);

        toggleGroup = new ToggleGroup();
        toggleGroup.getToggles().addAll(toolViews);

        toggleGroup.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null) {
                viewModel.setActiveToolViewModel(null);
            } else {
                viewModel.setActiveToolViewModel(((ToolView) newVal).getViewModel());
            }
        });
        if (toggleGroup.getSelectedToggle() != null)
            viewModel.setActiveToolViewModel(((ToolView) toggleGroup.getSelectedToggle()).getViewModel());

        toggleGroup.selectToggle(toggleGroup.getToggles().getFirst());

        VBox extraPaneVBox = createExtraPaneHVox();
        extraPaneVBox.setSpacing(20);
        extraPaneVBox.setPadding(new Insets(20, 0, 20, 0));
        getItems().addAll(gridPane, extraPaneVBox);

        viewModel.activeToolViewModelProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null) {
                if (toggleGroup.getSelectedToggle() != null) {
                    toggleGroup.getSelectedToggle().setSelected(false);
                }
                return;
            }
            toolViews[viewModel.getToolViewModelList().indexOf(newVal)].setSelected(true);
        });
    }


    private VBox createExtraPaneHVox() {
        colorPickerPane = new ColorPickerPane();
        viewModel.colorProperty().bindBidirectional(colorPickerPane.colorProperty());
        colorPickerPane.visibleProperty().bind(viewModel.colorPickingVisibleProperty());

        return new VBox(colorPickerPane);
    }
}
