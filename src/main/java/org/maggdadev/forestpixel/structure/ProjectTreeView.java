package org.maggdadev.forestpixel.structure;

import javafx.scene.control.TreeView;

public class ProjectTreeView extends TreeView<ProjectNodeModel> {
    public ProjectTreeView(ProjectViewModel viewModel) {

        setRoot(viewModel);
        viewModel.getChildren().addAll(new ProjectNodeViewModel(new ProjectNodeModel()), new ProjectNodeViewModel(new ProjectNodeModel())); // todo temp
        setMaxHeight(Double.MAX_VALUE);


        setCellFactory(param -> {
            return new ProjectNodeView();
        });
    }
}
