package org.maggdadev.forestpixel.structure;

import java.util.Optional;

public class ProjectViewModel extends ProjectNodeViewModel {
    private final ProjectModel model;

    public ProjectViewModel(ProjectModel model) {
        super(model);
        this.model = model;

    }

    public Optional<ProjectNodeViewModel> findNodeById(String id) {
        ProjectNodeViewModel result = super.findNodeByIdRecursive(id);
        if(result == null) {
            return Optional.empty();
        } else {
            return Optional.of(result);
        }
    }

}
