package org.maggdadev.forestpixel.structure;

import javafx.scene.control.TreeItem;

public class ProjectNodeViewModel extends TreeItem<ProjectNodeModel> {

    public ProjectNodeViewModel(ProjectNodeModel model) {
        setValue(model);
    }

    public String getId() {
        return getValue().getId();
    }

}
