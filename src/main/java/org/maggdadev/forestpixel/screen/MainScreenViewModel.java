package org.maggdadev.forestpixel.screen;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.maggdadev.forestpixel.structure.ProjectModel;
import org.maggdadev.forestpixel.structure.ProjectViewModel;

public class MainScreenViewModel {
    private MainScreenModel model;
    private final ObjectProperty<ProjectViewModel> openedProjectModel = new SimpleObjectProperty<ProjectViewModel>();

    public MainScreenViewModel(MainScreenModel model) {
        this.model = model;
    }

    public void newProject() {
        ProjectModel newProject = new ProjectModel();
        ProjectViewModel newProjectViewModel = new ProjectViewModel(newProject);
        openedProjectModel.set(newProjectViewModel);
    }

    public ProjectViewModel getOpenedProjectModel() {
        return openedProjectModel.get();
    }

    public ObjectProperty<ProjectViewModel> openedProjectModelProperty() {
        return openedProjectModel;
    }
}
