package org.maggdadev.forestpixel.canvas.layers;

import javafx.beans.binding.Bindings;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.maggdadev.forestpixel.maggdaui.MinimizableBox;
import org.maggdadev.forestpixel.maggdaui.SwappableListView;

public class LayersBarView extends MinimizableBox {
    private final SwappableListView<LayerViewModel> layersListView;
    private final LayersBarViewModel viewModel;
    private final LayersOpacityBox aboveLayersOpacityBox;
    private final LayersOpacityBox belowLayersOpacityBox;
    private final MinimizableBox opacityBox;

    public LayersBarView(LayersBarViewModel viewModel) {
        super("Layers");
        this.viewModel = viewModel;
        // ListView
        layersListView = new SwappableListView<LayerViewModel>(null, (LayerViewModel v) -> {
            TextField textField = new TextField();
            textField.textProperty().bindBidirectional(v.nameProperty());
            textField.setOnAction(e -> textField.getParent().requestFocus());
            return textField;
        });
        layersListView.itemsProperty().bind(viewModel.currentLayersUnmodifiableProperty());
        layersListView.setRemoveFunction(viewModel::removeLayer);
        layersListView.setSwapFunction(viewModel::swapLayers);
        layersListView.setFixedCellSize(30);
        layersListView.prefHeightProperty().bind(Bindings.createDoubleBinding(() ->
                layersListView.getFixedCellSize() * viewModel.getCurrentLayersUnmodifiable().size() + 5, viewModel.getCurrentLayersUnmodifiable(), layersListView.fixedCellSizeProperty()));

        // Add layer
        Button addLayerButton = new Button("Add layer");
        addLayerButton.setOnAction(e -> viewModel.addNewLayer());
        VBox addLayerBox = new VBox(addLayerButton);
        addLayerBox.setAlignment(javafx.geometry.Pos.CENTER);
        getContent().getChildren().addAll(layersListView, addLayerBox);

        // Opacity sliders
        aboveLayersOpacityBox = new LayersOpacityBox("Upper layers");
        belowLayersOpacityBox = new LayersOpacityBox("Lower layers");

        opacityBox = new MinimizableBox("Opacity of non-active layers");
        opacityBox.addSeparatorAbove();
        opacityBox.getContent().getChildren().addAll(aboveLayersOpacityBox, belowLayersOpacityBox);
        getContent().getChildren().add(opacityBox);

        createBindings();
    }


    private void createBindings() {
        layersListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                viewModel.setActiveLayer(newValue.getId());
            }
        });

        aboveLayersOpacityBox.opacityValueProperty().bindBidirectional(viewModel.upperLayersOpacityProperty());
        belowLayersOpacityBox.opacityValueProperty().bindBidirectional(viewModel.lowerLayersOpacityProperty());
        opacityBox.visibleProperty().bind(viewModel.moreThanOneLayerProperty());
    }


}