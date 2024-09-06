package org.maggdadev.forestpixel.canvas.toolbar;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.maggdadev.forestpixel.canvas.tools.viewmodels.SelectViewModel;
import org.maggdadev.forestpixel.canvas.tools.views.SelectView;
import org.maggdadev.forestpixel.canvas.tools.views.ToolView;

public class ToolbarView extends ToolBar {
    private final ToolView[] toolViews;
    private final GridPane gridPane;
    private final ToggleGroup toggleGroup;

    private final ToolbarViewModel viewModel;

    // extra panes


    private final ObservableList<Node> additionalToolNodesOnCanvas = FXCollections.observableArrayList();


    public ToolbarView(ToolbarViewModel viewModel) {
        this.viewModel = viewModel;

        toolViews = new ToolView[viewModel.getToolViewModelList().size()];
        gridPane = new GridPane();
        for (int i = 0; i < toolViews.length; i++) {
            if (viewModel.getToolViewModelList().get(i) instanceof SelectViewModel selectViewModel) {
                toolViews[i] = new SelectView(selectViewModel);
                gridPane.add(toolViews[i], i % 2, i / 2);
                additionalToolNodesOnCanvas.add(((SelectView) toolViews[i]).getMouseAreaRectangle());

            } else {
                toolViews[i] = new ToolView(viewModel.getToolViewModelList().get(i), viewModel.getToolViewModelList().get(i).getToolType());
                gridPane.add(toolViews[i], i % 2, i / 2);
            }
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

        VBox extraPaneVBox = createExtraPaneHBox();
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
        setMinHeight(300);
    }

    public ObservableList<Node> getAdditionalToolNodesOnCanvas() {
        return additionalToolNodesOnCanvas;
    }


    private VBox createExtraPaneHBox() {
        ColorPickerPane colorPickerPane = new ColorPickerPane();
        viewModel.colorProperty().bindBidirectional(colorPickerPane.colorProperty());
        colorPickerPane.visibleProperty().bind(viewModel.colorPickingVisibleProperty());

        LineWidthPickerPane lineWidthPickerPane = new LineWidthPickerPane();
        viewModel.lineWidthProperty().bindBidirectional(lineWidthPickerPane.lineWidthProperty());
        lineWidthPickerPane.visibleProperty().bind(viewModel.lineWidthPickerVisibleProperty());

        VBox extraPaneVBox = new VBox(colorPickerPane, lineWidthPickerPane);

        extraPaneVBox.setSpacing(20);
        extraPaneVBox.setPadding(new Insets(20, 0, 20, 0));
        return extraPaneVBox;
    }
}
