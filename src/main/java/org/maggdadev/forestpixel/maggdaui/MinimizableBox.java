package org.maggdadev.forestpixel.maggdaui;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class MinimizableBox extends BorderPane {
    private final Button toggleButton = new Button("-");

    private Pane content = new VBox();

    private final BooleanProperty isExpanded = new SimpleBooleanProperty(true);

    private final ObjectProperty<Orientation> orientation = new SimpleObjectProperty<>(Orientation.VERTICAL);

    public MinimizableBox(String headerText) {

        Label headerLabel = new Label(headerText);
        headerLabel.setStyle("-fx-font-weight: bold");
        // Header
        toggleButton.setOnAction(event -> toggleExpanded());
        toggleButton.setPrefSize(25, 25);
        toggleButton.textProperty().bind(isExpanded.map(expanded -> expanded ? "-" : "+"));
        orientation.subscribe((newValue) -> {
            if (content != null) {
                content.visibleProperty().unbind();
                content.managedProperty().unbind();
            }
            setTop(null);
            if (newValue == Orientation.HORIZONTAL) {
                // header
                BorderPane header = new BorderPane();
                header.setRight(toggleButton);
                header.setCenter(headerLabel);
                setTop(header);

                // content
                HBox hbox = new HBox(content == null ? null : content.getChildrenUnmodifiable().toArray(new Node[0]));
                hbox.setSpacing(10);
                content = hbox;

            } else {
                // header
                HBox header = new HBox(toggleButton, headerLabel);
                header.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
                header.setSpacing(5);
                setTop(header);

                // content
                VBox vbox = new VBox(content == null ? null : content.getChildrenUnmodifiable().toArray(new Node[0]));
                vbox.setSpacing(10);
                vbox.setPadding(new Insets(0, 0, 0, 30));
                content = vbox;

                //VBox.setVgrow(content, javafx.scene.layout.Priority.ALWAYS);
            }
            setCenter(content);
            content.visibleProperty().bind(isExpanded);
            content.managedProperty().bind(isExpanded);
        });
    }

    public void addSeparatorAbove() {
        getChildren().addFirst(new Separator());
    }

    public Pane getContent() {
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

    public void setOrientation(Orientation orientation) {
        this.orientation.set(orientation);
    }
}
