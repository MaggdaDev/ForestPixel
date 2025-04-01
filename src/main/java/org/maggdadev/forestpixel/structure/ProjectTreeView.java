package org.maggdadev.forestpixel.structure;

import javafx.scene.control.TreeView;

public class ProjectTreeView extends TreeView<ProjectNodeModel> {
    public ProjectTreeView() {

        ProjectRootModel projectRootModel = new ProjectRootModel();
        ProjectRootViewModel projectRootViewModel = new ProjectRootViewModel(projectRootModel);
        setRoot(projectRootViewModel);
        projectRootViewModel.getChildren().addAll(new ProjectNodeViewModel(new ProjectNodeModel()), new ProjectNodeViewModel(new ProjectNodeModel()));


        setCellFactory(param -> {
            return new ProjectNodeView();
        });
    }
}
