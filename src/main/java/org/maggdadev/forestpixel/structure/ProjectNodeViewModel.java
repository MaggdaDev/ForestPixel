package org.maggdadev.forestpixel.structure;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.TreeItem;

public class ProjectNodeViewModel extends TreeItem<ProjectNodeModel> {
    private final BooleanProperty isFolder = new SimpleBooleanProperty();
    protected  ProjectNodeModel model;

    public ProjectNodeViewModel(ProjectNodeModel model) {
        setValue(model);
        this.isFolder.set(model.isFolder());
        this.model = model;
        model.getChildren().forEach((child) -> getChildren().add(new ProjectNodeViewModel(child)));
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

    public void delete() {
        if (getParent() == null) {
            throw new UnsupportedOperationException("Trying to delete root node");
        }
        getParent().getValue().getChildren().remove(getValue()); // remove from model
        Platform.runLater(() -> {
            getParent().getChildren().remove(this); // remove from viewmodel
        });
    }

    public ProjectNodeModel getModel() {
        return model;
    }
}
