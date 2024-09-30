package org.maggdadev.forestpixel.maggdaui;

import javafx.beans.binding.Bindings;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;
import javafx.util.Subscription;
import org.maggdadev.forestpixel.canvas.utils.Selectable;
import org.maggdadev.forestpixel.canvas.utils.SwappableObservableArrayList;

public class SwappableListView<T extends Selectable> extends ListView<T> {
    private final Callback<T, Node> contentFactory;
    private final SwappableObservableArrayList<T> items;
    public SwappableListView(SwappableObservableArrayList<T> items, Callback<T, Node> contentFactory) {
        super(items);
        this.contentFactory = contentFactory;
        this.items = items;
        getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        setCellFactory(param -> new SwappableListCell());
    }

    private void swapLayers(String id1, String id2) {
        int idx1 = -1, idx2 = -1;
        int counter = 0;
        while (idx1 == -1 || idx2 == -1) {
            if (items.get(counter).getId().equals(id1)) {
                idx1 = counter;
            } else if (items.get(counter).getId().equals(id2)) {
                idx2 = counter;
            }
            counter++;
        }
        items.swap(idx1, idx2);
    }

    private T getItem(String id) {
        return items.stream().filter(item -> item.getId().equals(id)).findFirst().orElse(null);
    }

    private void remove(String id) {
        items.removeIf(item -> item.getId().equals(id));
    }


    private class SwappableListCell extends ListCell<T> {
        private final BorderPane graphic = new BorderPane();
        private final Button deleteButton = new Button("X");
        private final RadioButton radioButton = new RadioButton();

        private Subscription subscriptionOnItemSelected = null;

        public SwappableListCell() {
            radioButton.setMouseTransparent(true);
            orientationProperty().subscribe((newValue) -> {
                graphic.getChildren().clear();
                if (newValue == Orientation.HORIZONTAL) {
                    graphic.setTop(deleteButton);
                    graphic.setBottom(radioButton);
                    BorderPane.setAlignment(deleteButton, Pos.TOP_RIGHT);
                    BorderPane.setAlignment(radioButton, Pos.BOTTOM_CENTER);
                } else {
                    graphic.setRight(deleteButton);
                    graphic.setLeft(radioButton);
                    BorderPane.setAlignment(deleteButton, Pos.CENTER);
                    BorderPane.setAlignment(radioButton, Pos.CENTER);
                }
            });
            deleteButton.visibleProperty().bind(Bindings.size(items).greaterThan(1));
            deleteButton.setOnAction(event -> {
                if (getItem() != null && getItems().size() > 1) {
                    remove(getItem().getId());
                }
            });
            getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null && getItem() != null) {
                    getItem().selectedProperty().set(getItem().getId().equals(newValue.getId()));
                }
            });
        }

        @Override
        public void updateItem(T item, boolean empty) {
            super.updateItem(item, empty);
            if (subscriptionOnItemSelected != null) {
                subscriptionOnItemSelected.unsubscribe();
                subscriptionOnItemSelected = null;
            }
            if (empty) {
                setGraphic(null);
                return;
            }
            radioButton.setUserData(item.getId());
            radioButton.selectedProperty().bind(selectedProperty());
            setGraphic(graphic);
            graphic.setCenter(contentFactory.call(item));

            subscriptionOnItemSelected = item.selectedProperty().subscribe((newValue) -> {
                if (newValue) {
                    getSelectionModel().select(item);
                } else {
                    if (getSelectionModel().getSelectedItem() != null && getSelectionModel().getSelectedItem().getId().equals(item.getId())) {
                        getSelectionModel().clearSelection();
                    }
                }
            });
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
                        swapLayers(getItem().getId(), event.getDragboard().getString());
                    }

                event.consume();
            });
        }
    }
}
