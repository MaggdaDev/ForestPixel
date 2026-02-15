package org.maggdadev.forestpixel.structure;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.VBox;
import org.maggdadev.forestpixel.structure.dialogs.AddFileDialog;
import org.maggdadev.forestpixel.structure.dialogs.AddFolderDialog;
import org.maggdadev.forestpixel.structure.dialogs.AddProjectNodeDialog;
import org.maggdadev.forestpixel.structure.dialogs.DeleteConfirmationDialog;

public class ProjectView extends VBox {
    private final TreeView<ProjectNodeModel> treeView = new TreeView<>();
    private final Button addFileButton = new Button("Add File"),
    addFolderButton = new Button("Add Folder"), deleteButton = new Button("Delete");
    private final VBox buttonBox = new VBox();
    public ProjectView(ProjectViewModel viewModel) {

        treeView.setRoot(viewModel);
        treeView.getSelectionModel().select(viewModel);
        viewModel.setExpanded(true);
        treeView.setMaxHeight(Double.MAX_VALUE);
        treeView.setCellFactory(param -> {
            return new ProjectNodeView(ProjectView.this);
        });

        BooleanBinding disableAddNode = Bindings.createBooleanBinding(() -> {
            TreeItem<ProjectNodeModel> selectedItem = treeView.getSelectionModel().getSelectedItem();
            return selectedItem == null || !selectedItem.getValue().isFolder();
        }, treeView.getSelectionModel().selectedItemProperty());

        addFileButton.disableProperty().bind(disableAddNode);
        addFileButton.setOnAction((e) ->addAndSelectNewNode(new AddFileDialog()));

        addFolderButton.disableProperty().bind(disableAddNode);
        addFolderButton.setOnAction(e -> addAndSelectNewNode(new AddFolderDialog()));

        deleteButton.disableProperty().bind(treeView.getSelectionModel().selectedItemProperty().isNull());
        deleteButton.setOnAction(e -> deleteNode());

        buttonBox.getChildren().addAll(addFileButton, addFolderButton, deleteButton);
        buttonBox.setPadding(new Insets(20));
        buttonBox.setSpacing(10);
        getChildren().addAll(treeView, buttonBox);
        VBox.setVgrow(treeView, javafx.scene.layout.Priority.ALWAYS);
        VBox.setVgrow(buttonBox, javafx.scene.layout.Priority.NEVER);
    }

    private void deleteNode() {
        TreeItem<ProjectNodeModel> focusedItem = treeView.getSelectionModel().getSelectedItem();
        if(focusedItem instanceof ProjectNodeViewModel focusedViewModel) {
            String focusedViewModelType = focusedViewModel.getValue().isFolder() ? "folder" : "file";
            String focusedViewModelName = focusedViewModel.getValue().getName();
            new DeleteConfirmationDialog(focusedViewModelType, focusedViewModelName)
                    .showAndWait().ifPresent((ButtonType type) -> {;
                if (type == ButtonType.OK) {
                    focusedViewModel.delete();
                }
            });
        }
    }

    private void addAndSelectNewNode(AddProjectNodeDialog dialog) {
        TreeItem<ProjectNodeModel> focusedItem = treeView.getSelectionModel().getSelectedItem();
        if(focusedItem instanceof ProjectNodeViewModel focusedViewModel) {
            dialog.showAndWait().ifPresent(newFile -> {
                focusedViewModel.setExpanded(true);
                ProjectNodeViewModel newViewModel = focusedViewModel.addChild(newFile);
                Platform.runLater(() -> {
                    treeView.getSelectionModel().select(newViewModel);
                    int idx = treeView.getSelectionModel().getSelectedIndex();
                    treeView.getFocusModel().focus(idx);
                    treeView.scrollTo(treeView.getSelectionModel().getSelectedIndex());
                });
            });
        }
    }

}
