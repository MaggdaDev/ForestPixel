package org.maggdadev.forestpixel.structure;

import javafx.scene.control.TreeItem;

public class ProjectNodeViewModel extends TreeItem<ProjectNodeModel> {

    public ProjectNodeViewModel(ProjectNodeModel model) {
        setValue(model);

    }

    public String getId() {
        return getValue().getId();
    }

    public ProjectNodeViewModel findNodeByIdRecursive(String id) {
        if (getId().equals(id)) {
            return this;
        }
        for (TreeItem<ProjectNodeModel> child : getChildren()) {
            ProjectNodeViewModel innerResult = ((ProjectNodeViewModel) child).findNodeByIdRecursive(id);
            if (innerResult != null) {
                return innerResult;
            }
        }
        return null;
    }

    public ProjectNodeViewModel addFile(String fileName) {
        ProjectNodeModel newNode = new ProjectNodeModel();
        newNode.setName(fileName);
        getValue().getChildren().add(newNode);  // add to model
        ProjectNodeViewModel newViewModelNode = new ProjectNodeViewModel(newNode);
        getChildren().add(newViewModelNode);
        return newViewModelNode;
    }
}
