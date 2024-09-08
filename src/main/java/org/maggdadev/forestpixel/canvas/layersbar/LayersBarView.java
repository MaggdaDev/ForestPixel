package org.maggdadev.forestpixel.canvas.layersbar;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class LayersBarView extends VBox {
    private final Button toggleButton = new Button("-");

    private final Label headingLabel = new Label("Layers");
    private final ListView<LayersBarItemViewModel> layersListView;
    private final LayersBarViewModel viewModel;
    private final ObservableList<LayersBarItemViewModel> layers = FXCollections.observableArrayList();

    public LayersBarView(LayersBarViewModel viewModel) {
        this.viewModel = viewModel;
        // Initialize components
        layersListView = new ListView<>(layers);
        layersListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        layersListView.setCellFactory(LayersBarItemView::new);

        HBox header = new HBox(toggleButton, headingLabel);
        header.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        header.setSpacing(10);
        headingLabel.setStyle("-fx-font-weight: bold");
        getChildren().addAll(header, layersListView);

        // Set up toggle button action
        toggleButton.setOnAction(event -> viewModel.toggleExpanded());
        createBindings();

        layers.addAll(new LayersBarItemViewModel("hand"), new LayersBarItemViewModel("leg"), new LayersBarItemViewModel("head"));
    }

    private void createBindings() {
        layersListView.visibleProperty().bind(viewModel.isExpandedProperty());
        layersListView.managedProperty().bind(viewModel.isExpandedProperty());
        headingLabel.visibleProperty().bind(viewModel.isExpandedProperty());
        headingLabel.managedProperty().bind(viewModel.isExpandedProperty());
        toggleButton.textProperty().bind(viewModel.isExpandedProperty().map(expanded -> expanded ? "-" : "+"));
    }


}