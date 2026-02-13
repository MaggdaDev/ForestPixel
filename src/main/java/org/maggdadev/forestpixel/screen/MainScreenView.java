package org.maggdadev.forestpixel.screen;

import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.maggdadev.forestpixel.structure.ProjectTreeView;

public class MainScreenView extends BorderPane {
    private MainScreenViewModel viewModel;

    public MainScreenView(MainScreenViewModel viewModel, Stage stage) {
        this.viewModel = viewModel;
        MenuBar menuBar = new MenuBar(stage, viewModel);
        setTop(menuBar);

        VBox leftContent = new VBox();
        // make left content take all vertical space available, even if it has no children
        leftContent.setBackground(new Background(new BackgroundFill(Color.RED, null, null)));

        setLeft(leftContent);

        viewModel.openedProjectModelProperty().subscribe((newProject) -> {
            if (newProject != null) {
                ProjectTreeView treeView = new ProjectTreeView(newProject);
                leftContent.getChildren().setAll(treeView);
                VBox.setVgrow(treeView, Priority.ALWAYS);
            } else {
                leftContent.getChildren().clear();
            }
        });
    }
}
