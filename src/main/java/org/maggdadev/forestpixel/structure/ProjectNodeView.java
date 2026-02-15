package org.maggdadev.forestpixel.structure;

import javafx.scene.control.cell.TextFieldTreeCell;

public class ProjectNodeView extends TextFieldTreeCell<ProjectNodeModel> {
    public ProjectNodeView(ProjectView projectView) {
        super();
    }

    @Override
    public void updateItem(ProjectNodeModel item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setText(null);
        } else {
            setText(item.getName());
        }
    }

}
