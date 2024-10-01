package org.maggdadev.forestpixel.canvas.frames;


import javafx.beans.binding.Bindings;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.maggdadev.forestpixel.maggdaui.MinimizableBox;
import org.maggdadev.forestpixel.maggdaui.SwappableListView;

public class FramesBarView extends MinimizableBox {
    private final static double CELL_SIZE = 50;

    public FramesBarView(FramesBarViewModel viewModel) {
        super("Frames");
        setOrientation(Orientation.HORIZONTAL);

        SwappableListView<FrameViewModel> frameViewModelListView = new SwappableListView<FrameViewModel>(viewModel.getFrames(), (FrameViewModel frameViewModel) -> {
            return new Label("Frame");
        });
        frameViewModelListView.setOrientation(Orientation.HORIZONTAL);
        frameViewModelListView.setFixedCellSize(CELL_SIZE);
        frameViewModelListView.prefWidthProperty().bind(Bindings.size(viewModel.getFrames()).multiply(CELL_SIZE).add(5));
        frameViewModelListView.setPrefHeight(CELL_SIZE + 50);

        Button addFrameButton = new Button("Add Frame");
        addFrameButton.setOnAction(e -> viewModel.addFrame());
        getContent().getChildren().addAll(frameViewModelListView, addFrameButton);


    }
}