package org.maggdadev.forestpixel.structure.dialogs;

import org.maggdadev.forestpixel.structure.ProjectNodeModel;

public class AddFileDialog extends AddProjectNodeDialog {

    public AddFileDialog() {
        super("file", fileName -> new ProjectNodeModel(fileName, false));
    }
}
