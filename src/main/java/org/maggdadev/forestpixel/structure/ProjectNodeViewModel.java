package org.maggdadev.forestpixel.structure;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.TreeItem;

import java.util.HashMap;

public class ProjectNodeViewModel extends TreeItem<ProjectNodeModel> {
    private final BooleanProperty isFolder = new SimpleBooleanProperty();
    protected  ProjectNodeModel model;

    public ProjectNodeViewModel(ProjectNodeModel model) {
        setValue(model);
        this.isFolder.set(model.canHaveChildren());
        this.model = model;
        model.getChildren().forEach((child) -> {
            if(child instanceof ProjectFileModel fileChild) {
                 getChildren().add(new ProjectFileViewModel(fileChild));
            } else {
                getChildren().add(new ProjectNodeViewModel(child));
            }
        });
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


    void addAllResourceAgentsToMapRecursive(HashMap<String, ResourceIOAgent> agents) {
        if (this instanceof ResourceIOAgent agent) {
            agents.put(getId(), agent);
        }
        for (TreeItem<ProjectNodeModel> child : getChildren()) {
            if(child instanceof ProjectNodeViewModel childViewModel) {
                childViewModel.addAllResourceAgentsToMapRecursive(agents);
            }
        }
    }

    public void addChild(ProjectNodeViewModel addedChildViewModel) {
        getValue().getChildren().add(addedChildViewModel.getModel());  // add to model
        getChildren().add(addedChildViewModel);
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
