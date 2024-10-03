package org.maggdadev.forestpixel.canvas.frames;


import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import org.maggdadev.forestpixel.maggdaui.MinimizableBox;
import org.maggdadev.forestpixel.maggdaui.SwappableListView;

public class FramesBarView extends MinimizableBox {
    private final static double CELL_SIZE = 80;

    public FramesBarView(FramesBarViewModel viewModel) {
        super("Frames");
        setOrientation(Orientation.HORIZONTAL);

        SwappableListView<FrameViewModel> frameViewModelListView = new SwappableListView<FrameViewModel>(viewModel.getFrames(), (FrameViewModel frameViewModel) -> {
            ImageView imageView = new ImageView();
            imageView.imageProperty().bind(frameViewModel.thumbnailProperty());
            imageView.setPreserveRatio(true);
            imageView.setFitWidth(CELL_SIZE - 4);
            imageView.setFitHeight(CELL_SIZE - 4);
            frameViewModel.thumbnailProperty().subscribe((newValue) -> {
                if (newValue != null) {
                    if (newValue.getWidth() > newValue.getHeight()) {
                        imageView.setFitWidth(CELL_SIZE - 4);
                    } else {
                        imageView.setFitHeight(CELL_SIZE - 4);
                    }
                }
            });
            imageView.setMouseTransparent(true);
            StackPane imageViewStackPane = new StackPane(imageView);
            imageViewStackPane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1))));
            imageViewStackPane.setPadding(new Insets(0));

            TextField nameTextField = new TextField();
            nameTextField.setStyle("-fx-font-size: 8pt;");
            nameTextField.textProperty().bindBidirectional(frameViewModel.nameProperty());
            nameTextField.setOnAction(e -> nameTextField.getParent().requestFocus());
            VBox content = new VBox(imageViewStackPane, nameTextField);
            content.setPadding(new Insets(0));
            return content;
        });
        frameViewModelListView.setOrientation(Orientation.HORIZONTAL);
        frameViewModelListView.setFixedCellSize(CELL_SIZE);
        frameViewModelListView.prefWidthProperty().bind(Bindings.size(viewModel.getFrames()).multiply(CELL_SIZE).add(5));
        frameViewModelListView.setPrefHeight(CELL_SIZE + 65);

        Button addFrameButton = new Button("Create new frame");
        addFrameButton.setOnAction(e -> viewModel.addFrame());

        Button cloneFrameButton = new Button("Clone selected frame");
        cloneFrameButton.setOnAction(e -> viewModel.cloneSelectedFrame());

        getContent().getChildren().addAll(frameViewModelListView, new VBox(addFrameButton, cloneFrameButton));


    }
}