package org.maggdadev.forestpixel.canvas.layers;

import javafx.beans.binding.Bindings;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.maggdadev.forestpixel.maggdaui.MinimizableBox;
import org.maggdadev.forestpixel.maggdaui.SwappableListView;

public class LayersBarView extends MinimizableBox {
    private final Button addLayerButton = new Button("Add layer");
    private final SwappableListView<LayerViewModel> layersListView;
    private final LayersBarViewModel viewModel;
    private final LayersOpacityBox aboveLayersOpacityBox;
    private final LayersOpacityBox belowLayersOpacityBox;
    private final MinimizableBox opacityBox;

    public LayersBarView(LayersBarViewModel viewModel) {
        super("Layers");
        this.viewModel = viewModel;
        // ListView
        layersListView = new SwappableListView<>(viewModel.getLayersViewModels().getLayers(), (v) -> {
            TextField textField = new TextField();
            textField.textProperty().bindBidirectional(v.nameProperty());
            textField.setOnAction(e -> textField.getParent().requestFocus());
            return textField;
        });
        layersListView.setFixedCellSize(30);
        layersListView.prefHeightProperty().bind(Bindings.createDoubleBinding(() ->
                layersListView.getFixedCellSize() * viewModel.getLayersViewModels().getLayersUnmodifiable().size() + 5, viewModel.getLayersViewModels().getLayersUnmodifiable(), layersListView.fixedCellSizeProperty()));

        // Add layer
        addLayerButton.setOnAction(e -> viewModel.getLayersViewModels().addNewLayer());
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
        layersListView.visibleProperty().bind(viewModel.isExpandedProperty());
        layersListView.managedProperty().bind(viewModel.isExpandedProperty());
        addLayerButton.visibleProperty().bind(viewModel.isExpandedProperty());
        addLayerButton.managedProperty().bind(viewModel.isExpandedProperty());
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