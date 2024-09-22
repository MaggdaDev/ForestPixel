package org.maggdadev.forestpixel.canvas.layersbar;

import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import org.maggdadev.forestpixel.canvas.utils.SwappableObservableArrayList;

public class LayersBarItemViewModel {
    private static int idCounter = 0;
    private final StringProperty name = new SimpleStringProperty("");

    private final IntegerProperty order = new SimpleIntegerProperty(0);
    private final BooleanProperty selected = new SimpleBooleanProperty(false);
    private final String id;

    private boolean requestFocusPending = false;

    public LayersBarItemViewModel(String name, SwappableObservableArrayList<LayersBarItemViewModel> layers) {
        this.name.set(name);
        this.id = String.valueOf(idCounter++);
        order.bind(Bindings.createIntegerBinding(() -> layers.indexOf(this), layers));
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

    public int getOrder() {
        return order.get();
    }

    public IntegerProperty orderProperty() {
        return order;
    }
}
