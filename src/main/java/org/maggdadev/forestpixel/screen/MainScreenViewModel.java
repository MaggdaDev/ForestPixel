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
    private final MainScreenDialogService dialogService;

    public MainScreenViewModel(MainScreenModel model, MainScreenDialogService dialogService) {
        this.model = model;
        this.dialogService = dialogService;
    }

    public void newProject() {
        boolean closeSuccessful = closeProject();
        if(!closeSuccessful) {
            return;
        }
        ProjectModel newProject = new ProjectModel();
        ProjectViewModel newProjectViewModel = new ProjectViewModel(newProject);
        openedProjectViewModel.set(newProjectViewModel);
    }

    /**
     *
     * @return whether the closing was successful (i.e. user confirmed closing the project)
     */
    public boolean closeProject() {
        if (openedProjectViewModel.get() == null) {
            return true;
        }
        switch (dialogService.mayDeleteProjectAlert()) {
            case CANCEL:
                return false;
            case SAVE_AND_CLOSE:
                save();
        }
        openedProjectViewModel.set(null);
        return true;
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

    public void saveAs() {
        File file = dialogService.saveAsDialog();
        if (file != null) {
            saveModelTo(file);
        }
    }

    public void save() {
        try {
            if (fileLocation.get() == null) {
                throw new FileNotFoundException("No file location specified for saving the project.");
            }
            saveModelTo(fileLocation.get());
        } catch(Exception e) {
            saveAs();
        }
    }

    public void openProject() {
        File file = dialogService.openDialog();
        if(file == null) {
            return;
        }
        boolean closeSuccessful = closeProject();
        if (!closeSuccessful) {
            return;
        }
        ProjectModel loadedModel = ProjectViewModel.loadProjectModelFrom(file);
        if (loadedModel != null) {
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
