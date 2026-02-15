package org.maggdadev.forestpixel.structure;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.VBox;
import javafx.util.Subscription;

public class ProjectView extends VBox {
    private final TreeView<ProjectNodeModel> treeView = new TreeView<>();
    private final Button addFileButton = new Button("Add File");
    private final VBox buttonBox = new VBox();
    public ProjectView(ProjectViewModel viewModel) {

        treeView.setRoot(viewModel);
        viewModel.getChildren().addAll(new ProjectNodeViewModel(new ProjectNodeModel()), new ProjectNodeViewModel(new ProjectNodeModel())); // todo temp
        treeView.setMaxHeight(Double.MAX_VALUE);
        treeView.setCellFactory(param -> {
            return new ProjectNodeView(ProjectView.this);
        });

        addFileButton.disableProperty().bind(treeView.getSelectionModel().selectedItemProperty().isNull());
        addFileButton.setOnAction((e) -> {
            TreeItem<ProjectNodeModel> focusedItem = treeView.getSelectionModel().getSelectedItem();
            if(focusedItem instanceof ProjectNodeViewModel focusedViewModel) {
                new AddFileDialog().showAndWait().ifPresent(newFile -> {
                    focusedViewModel.setExpanded(true);
                    ProjectNodeViewModel newViewModel = focusedViewModel.addFile(newFile);
                    Platform.runLater(() -> {
                        treeView.getSelectionModel().select(newViewModel);
                        int idx = treeView.getSelectionModel().getSelectedIndex();
                        treeView.getFocusModel().focus(idx);
                        treeView.scrollTo(treeView.getSelectionModel().getSelectedIndex());
                    });
                });
            }
            
        });

        buttonBox.getChildren().addAll(addFileButton);
        buttonBox.setPadding(new Insets(20));
        getChildren().addAll(treeView, buttonBox);
        VBox.setVgrow(treeView, javafx.scene.layout.Priority.ALWAYS);
        VBox.setVgrow(buttonBox, javafx.scene.layout.Priority.NEVER);
    }

}
