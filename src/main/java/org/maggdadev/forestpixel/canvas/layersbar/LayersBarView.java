package org.maggdadev.forestpixel.canvas.layersbar;

import javafx.beans.binding.Bindings;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class LayersBarView extends VBox {
    private final Button toggleButton = new Button("-");
    private final Button addLayerButton = new Button("Add layer");
    private final Label headingLabel = new Label("Layers");
    private final ListView<LayersBarItemViewModel> layersListView;
    private final LayersBarViewModel viewModel;

    public LayersBarView(LayersBarViewModel viewModel) {
        this.viewModel = viewModel;
        // Header
        toggleButton.setOnAction(event -> viewModel.toggleExpanded());
        HBox header = new HBox(toggleButton, headingLabel);
        header.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        header.setSpacing(10);
        headingLabel.setStyle("-fx-font-weight: bold");

        // ListView
        layersListView = new ListView<>(viewModel.getLayers());
        layersListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        layersListView.setCellFactory(LayersBarItemView::new);
        layersListView.setFixedCellSize(30);
        layersListView.prefHeightProperty().bind(Bindings.createDoubleBinding(() -> layersListView.getFixedCellSize() * viewModel.getLayers().size() + 5, viewModel.getLayers(), layersListView.fixedCellSizeProperty()));

        // Add layer
        addLayerButton.setOnAction(e -> viewModel.addLayer());
        VBox addLayerBox = new VBox(addLayerButton);
        addLayerBox.setAlignment(javafx.geometry.Pos.CENTER);
        getChildren().addAll(header, layersListView, addLayerBox);

        createBindings();
    }


    private void createBindings() {
        layersListView.visibleProperty().bind(viewModel.isExpandedProperty());
        layersListView.managedProperty().bind(viewModel.isExpandedProperty());
        headingLabel.visibleProperty().bind(viewModel.isExpandedProperty());
        headingLabel.managedProperty().bind(viewModel.isExpandedProperty());
        addLayerButton.visibleProperty().bind(viewModel.isExpandedProperty());
        addLayerButton.managedProperty().bind(viewModel.isExpandedProperty());
        toggleButton.textProperty().bind(viewModel.isExpandedProperty().map(expanded -> expanded ? "-" : "+"));
    }


}