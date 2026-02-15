package org.maggdadev.forestpixel.screen;

import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.maggdadev.forestpixel.structure.ProjectView;

public class MainScreenView extends BorderPane {
    private MainScreenViewModel viewModel;

    public MainScreenView(MainScreenViewModel viewModel, Stage stage) {
        this.viewModel = viewModel;
        MenuBar menuBar = new MenuBar(stage, viewModel);
        setTop(menuBar);

        VBox leftContent = new VBox();
        setLeft(leftContent);

        viewModel.openedProjectModelProperty().subscribe((newProject) -> {
            if (newProject != null) {
                ProjectView treeView = new ProjectView(newProject);
                leftContent.getChildren().setAll(treeView);
                VBox.setVgrow(treeView, Priority.ALWAYS);
            } else {
                leftContent.getChildren().clear();
            }
        });
    }
}
