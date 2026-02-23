package org.maggdadev.forestpixel.structure;

import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class ProjectNodeView extends TextFieldTreeCell<ProjectNodeModel> {
    public ProjectNodeView(ProjectView projectView) {
        super();
        addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            if (e.getButton() == MouseButton.PRIMARY && e.getClickCount() == 2 && !isEmpty()) {
                if(getTreeItem() instanceof ProjectNodeViewModel nodeViewModel) {
                    nodeViewModel.open();
                }
                e.consume();       // optional: prevent other handlers
            }
        });
    }

    @Override
    public void updateItem(ProjectNodeModel item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setText(null);
        } else {
            setText(item.getNameWithExtension());
        }
    }

}
