package org.maggdadev.forestpixel.screen;

import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.maggdadev.forestpixel.structure.ProjectTreeView;

public class MainScreenView extends BorderPane {
    private MainScreenViewModel viewModel;

    public MainScreenView(MainScreenViewModel viewModel, Stage stage) {
        this.viewModel = viewModel;
        MenuBar menuBar = new MenuBar(stage);
        ProjectTreeView projectStructureView = new ProjectTreeView();
        setTop(menuBar);
        setLeft(projectStructureView);
    }
}
