package org.maggdadev.forestpixel.screen;

import javafx.scene.layout.*;
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

        Pane canvasPane = new Pane();   // will be controlled by project view
        setCenter(canvasPane);

        viewModel.openedProjectViewModelProperty().subscribe((newProject) -> {
            if (newProject != null) {
                canvasPane.getChildren().clear();
                ProjectView treeView = new ProjectView(newProject, canvasPane);
                leftContent.getChildren().setAll(treeView);
                VBox.setVgrow(treeView, Priority.ALWAYS);
            } else {
                leftContent.getChildren().clear();
                canvasPane.getChildren().clear();
            }
        });



    }
}
