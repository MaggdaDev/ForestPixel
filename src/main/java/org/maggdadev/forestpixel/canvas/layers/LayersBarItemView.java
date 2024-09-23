package org.maggdadev.forestpixel.canvas.layers;

import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.util.StringConverter;
import javafx.util.Subscription;


public class LayersBarItemView extends TextFieldListCell<LayerViewModel> {

    private final RadioButton radioButton = new RadioButton();

    private final Button deleteButton = new Button("X");
    private final TextField textField = new TextField();

    private final BorderPane graphic = new BorderPane();

    private Subscription subscriptionOnViewModelSelected;
    private ChangeListener<Boolean> listenerOnThisSelected;

    private final LayersBarViewModel barViewModel;

    private boolean isRequestFocusPending = false;
    public LayersBarItemView(LayersBarViewModel barViewModel) {
        this.barViewModel = barViewModel;
        radioButton.setMouseTransparent(true);
        HBox content = new HBox(radioButton, textField);
        content.setSpacing(5);
        content.setAlignment(Pos.CENTER_LEFT);
        graphic.setLeft(content);
        graphic.setRight(deleteButton);
        deleteButton.setOnAction(this::delete);
        deleteButton.visibleProperty().bind(barViewModel.moreThanOneLayerProperty());

        setConverter(new StringConverter<>() {
            @Override
            public String toString(LayerViewModel object) {
                return "";
            }

            @Override
            public LayerViewModel fromString(String string) {
                return null;
            }
        });
    }

    @Override
    public void updateItem(LayerViewModel viewModel, boolean empty) {
        if (getItem() != null) {
            clearAll(getItem());
        }
        super.updateItem(viewModel, empty);
        if (empty || viewModel == null) {
            return;
        }
        refreshSelectedBinding(viewModel);
        textField.textProperty().bindBidirectional(viewModel.nameProperty());
        setGraphic(graphic);

        setOnDragDetected(event -> {
            if (isEmpty()) {
                return;
            }
            Dragboard dragboard = startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putString(getItem().getId());
            dragboard.setContent(content);
            event.consume();

        });
        setOnDragOver(event -> {
            if (event.getDragboard().hasString())
                if (getItem().getId().equals(event.getDragboard().getString())) {
                    event.acceptTransferModes(TransferMode.ANY);
                } else {
                    event.acceptTransferModes(TransferMode.MOVE);
                    barViewModel.swapLayers(getItem().getId(), event.getDragboard().getString());
                }

            event.consume();
        });

        if (isRequestFocusPending) {
            textField.requestFocus();
            textField.selectAll();
            isRequestFocusPending = false;
        }

        textField.setOnAction(event -> {
            requestFocus();
        });

    }

    private void delete(ActionEvent e) {
        barViewModel.getLayersViewModels().remove(getItem().getId());
    }

    private void clearAll(LayerViewModel viewModel) {
        textField.textProperty().unbindBidirectional(viewModel.nameProperty());
        radioButton.selectedProperty().unbind();
        setGraphic(null);
        setOnDragDetected(null);
        setOnDragOver(null);
        setOnDragDone(null);
        textField.setOnAction(null);
        if (subscriptionOnViewModelSelected != null) {
            subscriptionOnViewModelSelected.unsubscribe();
            subscriptionOnViewModelSelected = null;
        }
        if (listenerOnThisSelected != null) {
            selectedProperty().removeListener(listenerOnThisSelected);
        }
    }

    private void refreshSelectedBinding(LayerViewModel viewModel) {
        subscriptionOnViewModelSelected = viewModel.activeProperty().subscribe(
                (newValue) -> {
                    if (newValue) {
                        getListView().getSelectionModel().select(viewModel);
                    } else {
                        if (getListView().getSelectionModel().getSelectedItem() == viewModel) {
                            getListView().getSelectionModel().clearSelection();
                        }
                    }
                });
        selectedProperty().addListener(listenerOnThisSelected =
                (observable, oldValue, newValue) -> viewModel.setActive(newValue));


        radioButton.selectedProperty().bind(viewModel.activeProperty());
    }


}
