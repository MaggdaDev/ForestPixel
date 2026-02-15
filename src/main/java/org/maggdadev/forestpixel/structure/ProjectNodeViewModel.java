package org.maggdadev.forestpixel.structure;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.TreeItem;

public class ProjectNodeViewModel extends TreeItem<ProjectNodeModel> {
    private final BooleanProperty isFolder = new SimpleBooleanProperty();
    public ProjectNodeViewModel(ProjectNodeModel model) {
        setValue(model);
        this.isFolder.set(model.isFolder());
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

    public ProjectNodeViewModel addChild(ProjectNodeModel addedChildModel) {
        getValue().getChildren().add(addedChildModel);  // add to model
        ProjectNodeViewModel newViewModelNode = new ProjectNodeViewModel(addedChildModel);
        getChildren().add(newViewModelNode);
        return newViewModelNode;
    }
}
