package org.maggdadev.forestpixel.canvas.layersbar;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

public class LayersBarItemViewModel {
    private static int idCounter = 0;
    private final StringProperty name = new SimpleStringProperty("");

    private final BooleanProperty selected = new SimpleBooleanProperty(false);
    private final String id;

    private boolean requestFocusPending = false;

    public LayersBarItemViewModel(String name) {
        this.name.set(name);
        this.id = String.valueOf(idCounter++);
    }


    public void swapOrderWith(ObservableList<LayersBarItemViewModel> items, String otherID) {
        int thisIndex = items.indexOf(this);
        int otherIndex = items.indexOf(items.stream().filter(item -> item.id.equals(otherID)).findFirst().orElse(null));
        if (thisIndex == -1 || otherIndex == -1) {
            return;
        }
        items.set(thisIndex, items.set(otherIndex, items.get(thisIndex)));
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof LayersBarItemViewModel viewModel) {
            return viewModel.id.equals(this.id);
        }
        return false;
    }

    public boolean isSelected() {
        return selected.get();
    }

    public BooleanProperty selectedProperty() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected.set(selected);
    }

    public boolean isRequestFocusPending() {
        return requestFocusPending;
    }

    public void setRequestFocusPending(boolean requestFocusPending) {
        this.requestFocusPending = requestFocusPending;
    }
}
