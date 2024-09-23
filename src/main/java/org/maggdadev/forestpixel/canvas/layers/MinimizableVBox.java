package org.maggdadev.forestpixel.canvas.layers;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class MinimizableVBox extends VBox {
    private final Button toggleButton = new Button("-");

    private final VBox content = new VBox();

    private final BooleanProperty isExpanded = new SimpleBooleanProperty(true);

    public MinimizableVBox(String headerText) {
        Label headerLabel = new Label(headerText);
        // Header
        toggleButton.setOnAction(event -> toggleExpanded());
        toggleButton.setPrefSize(25, 25);
        toggleButton.textProperty().bind(isExpanded.map(expanded -> expanded ? "-" : "+"));
        HBox header = new HBox(toggleButton, headerLabel);
        header.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        header.setSpacing(5);
        headerLabel.setStyle("-fx-font-weight: bold");

        getChildren().addAll(header, content);
        content.setSpacing(10);
        content.visibleProperty().bind(isExpanded);
        content.managedProperty().bind(isExpanded);
        content.setPadding(new Insets(0, 0, 0, 30));
    }

    public void addSeparatorAbove() {
        getChildren().addFirst(new Separator());
    }

    public VBox getContent() {
        return content;
    }

    private void toggleExpanded() {
        isExpanded.set(!isExpanded.get());
    }

    public boolean isIsExpanded() {
        return isExpanded.get();
    }

    public BooleanProperty isExpandedProperty() {
        return isExpanded;
    }

    public void setIsExpanded(boolean isExpanded) {
        this.isExpanded.set(isExpanded);
    }
}
