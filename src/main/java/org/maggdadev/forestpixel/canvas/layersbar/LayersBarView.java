package org.maggdadev.forestpixel.canvas.layersbar;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
    private final ObservableList<LayersBarItemViewModel> layers = FXCollections.observableArrayList();

    public LayersBarView(LayersBarViewModel viewModel) {
        this.viewModel = viewModel;
        // Header
        toggleButton.setOnAction(event -> viewModel.toggleExpanded());
        HBox header = new HBox(toggleButton, headingLabel);
        header.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        header.setSpacing(10);
        headingLabel.setStyle("-fx-font-weight: bold");

        // ListView
        layersListView = new ListView<>(layers);
        layersListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        layersListView.setCellFactory(LayersBarItemView::new);
        layersListView.setFixedCellSize(30);
        layersListView.prefHeightProperty().bind(Bindings.createDoubleBinding(() -> layersListView.getFixedCellSize() * layers.size() + 5, layers, layersListView.fixedCellSizeProperty()));

        // Add layer
        addLayerButton.setOnAction(this::addLayer);
        VBox addLayerBox = new VBox(addLayerButton);
        addLayerBox.setAlignment(javafx.geometry.Pos.CENTER);
        getChildren().addAll(header, layersListView, addLayerBox);

        createBindings();
        layers.addAll(new LayersBarItemViewModel("hand"), new LayersBarItemViewModel("leg"), new LayersBarItemViewModel("head"));
    }

    private void addLayer(ActionEvent e) {
        LayersBarItemViewModel newLayer = new LayersBarItemViewModel("New layer");
        newLayer.setRequestFocusPending(true);
        layers.add(newLayer);
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