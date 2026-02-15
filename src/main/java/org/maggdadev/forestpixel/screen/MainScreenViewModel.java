package org.maggdadev.forestpixel.screen;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.maggdadev.forestpixel.structure.ProjectModel;
import org.maggdadev.forestpixel.structure.ProjectViewModel;

import java.io.File;

public class MainScreenViewModel {
    private MainScreenModel model;
    private final ObjectProperty<ProjectViewModel> openedProjectViewModel = new SimpleObjectProperty<ProjectViewModel>();

    public MainScreenViewModel(MainScreenModel model) {
        this.model = model;
    }

    public void newProject() {
        ProjectModel newProject = new ProjectModel();
        ProjectViewModel newProjectViewModel = new ProjectViewModel(newProject);
        openedProjectViewModel.set(newProjectViewModel);
    }

    public ProjectViewModel getOpenedProjectViewModel() {
        return openedProjectViewModel.get();
    }

    public ObjectProperty<ProjectViewModel> openedProjectViewModelProperty() {
        return openedProjectViewModel;
    }

    public void saveModelTo(File file) {
        if (openedProjectViewModel.get() != null) {
            ProjectViewModel.saveModelTo(file, openedProjectViewModel.get().getModel());
        }
    }

    public void openProject(File file) {
        ProjectModel loadedModel = ProjectViewModel.loadProjectModelFrom(file);
        if (loadedModel != null) {
            System.out.println(loadedModel);
            openedProjectViewModel.set(new ProjectViewModel(loadedModel));
        } else {
            System.err.println("Failed to load project from file: " + file.getAbsolutePath());
        }
    }

}
