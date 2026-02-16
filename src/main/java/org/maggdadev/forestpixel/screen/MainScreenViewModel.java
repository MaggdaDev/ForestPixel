package org.maggdadev.forestpixel.screen;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.maggdadev.forestpixel.structure.ProjectModel;
import org.maggdadev.forestpixel.structure.ProjectViewModel;

import java.io.File;
import java.io.FileNotFoundException;

public class MainScreenViewModel {
    private MainScreenModel model;
    private final ObjectProperty<ProjectViewModel> openedProjectViewModel = new SimpleObjectProperty<ProjectViewModel>();

    private final ObjectProperty<File> fileLocation = new SimpleObjectProperty<>();

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
            setFileLocation(file);
        }
    }

    public void save() throws FileNotFoundException {
        if (fileLocation.get() == null) {
            throw new FileNotFoundException("No file location specified for saving the project.");
        }
        saveModelTo(fileLocation.get());
    }

    public void openProject(File file) {
        ProjectModel loadedModel = ProjectViewModel.loadProjectModelFrom(file);
        if (loadedModel != null) {
            System.out.println(loadedModel);
            openedProjectViewModel.set(new ProjectViewModel(loadedModel));
            setFileLocation(file);
        } else {
            System.err.println("Failed to load project from file: " + file.getAbsolutePath());
        }
    }



    public File getFileLocation() {
        return fileLocation.get();
    }

    public ObjectProperty<File> fileLocationProperty() {
        return fileLocation;
    }

    public void setFileLocation(File file) {
        fileLocation.set(file);
    }
}
