package org.maggdadev.forestpixel.maggdaui;

import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Insets;
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

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class SwappableListView<T extends Selectable> extends ListView<T> {
    private final Callback<T, Node> contentFactory;
    private final SwappableObservableArrayList<T> items;

    private BiConsumer<String, String> swapFunction;
    private Consumer<String> removeFunction;

    public SwappableListView(SwappableObservableArrayList<T> items, Callback<T, Node> contentFactory, BiConsumer<String, String> swapFunction, Consumer<String> removeFunction) {
        super(items == null ? null : items.getUnmodifiable());
        if (swapFunction == null) {
            swapFunction = this::defaultSwap;
        }
        if (removeFunction == null) {
            removeFunction = this::defaultRemove;
        }
        this.swapFunction = swapFunction;
        this.removeFunction = removeFunction;
        this.contentFactory = contentFactory;
        this.items = items;
        getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        setCellFactory(param -> new SwappableListCell());
    }

    public SwappableListView(SwappableObservableArrayList<T> items, Callback<T, Node> contentFactory) {
        this(items, contentFactory, null, null);
    }

    private void defaultSwap(String id1, String id2) {
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
        return items.getUnmodifiable().stream().filter(item -> item.getId().equals(id)).findFirst().orElse(null);
    }

    private void defaultRemove(String id) {
        items.removeIf(item -> item.getId().equals(id));
    }


    private class SwappableListCell extends ListCell<T> {
        private final BorderPane graphic = new BorderPane();
        private final Button deleteButton = new Button("X");
        private final RadioButton radioButton = new RadioButton();

        private Subscription subscriptionOnItemSelected = null;

        private final IntegerProperty itemsAmount = new SimpleIntegerProperty(0);

        public SwappableListCell() {
            radioButton.setMouseTransparent(true);
            orientationProperty().subscribe((newValue) -> {
                graphic.getChildren().clear();
                if (newValue == Orientation.HORIZONTAL) {
                    graphic.setTop(radioButton);
                    graphic.setBottom(deleteButton);
                } else {
                    graphic.setRight(deleteButton);
                    graphic.setLeft(radioButton);
                }
                BorderPane.setAlignment(deleteButton, Pos.CENTER);
                BorderPane.setAlignment(radioButton, Pos.CENTER);
            });
            itemsProperty().subscribe((newValue) -> {
                if (itemsAmount.isBound()) {
                    itemsAmount.unbind();
                }
                if (newValue == null) {
                    itemsAmount.set(0);
                } else {
                    itemsAmount.bind(Bindings.size(newValue));
                }

            });
            deleteButton.visibleProperty().bind(itemsAmount.greaterThan(1));
            deleteButton.setOnAction(event -> {
                if (getItem() != null && getItems().size() > 1) {
                    removeFunction.accept(getItem().getId());
                }
            });
            getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null && getItem() != null && getItems().contains(getItem())) {
                    getItem().selectedProperty().set(getItem().getId().equals(newValue.getId()));
                }
            });
            setPadding(new Insets(1));
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
                        swapFunction.accept(getItem().getId(), event.getDragboard().getString());
                    }

                event.consume();
            });
        }
    }

    public void setSwapFunction(BiConsumer<String, String> swapFunction) {
        this.swapFunction = swapFunction;
    }

    public void setRemoveFunction(Consumer<String> removeFunction) {
        this.removeFunction = removeFunction;
    }
}
