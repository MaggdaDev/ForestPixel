package org.maggdadev.forestpixel.structure.dialogs;

import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import org.maggdadev.forestpixel.structure.ProjectNodeModel;

public class AddFolderDialog extends AddProjectNodeDialog {

    public AddFolderDialog() {
        super("folder");
        setupResultConverter(name -> new ProjectNodeModel(name, ProjectNodeModel.ProjectNodeType.FOLDER));
    }
}
