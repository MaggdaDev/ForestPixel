package org.maggdadev.forestpixel.structure;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.maggdadev.forestpixel.canvas.CanvasView;
import org.maggdadev.forestpixel.structure.dialogs.AddFileDialog;
import org.maggdadev.forestpixel.structure.dialogs.AddFolderDialog;
import org.maggdadev.forestpixel.structure.dialogs.AddProjectNodeDialog;
import org.maggdadev.forestpixel.structure.dialogs.DeleteConfirmationDialog;

import java.util.HashMap;

public class ProjectView extends VBox {
    private final TreeView<ProjectNodeModel> treeView = new TreeView<>();
    private final Button addFileButton = new Button("Add File"),
    addFolderButton = new Button("Add Folder"), deleteButton = new Button("Delete");
    private final VBox buttonBox = new VBox();
    private final ObjectProperty<ProjectNodeViewModel> selectedNodeViewModel = new SimpleObjectProperty<>();
    private final HashMap<String, CanvasView> canvasMap = new HashMap<>();
    public ProjectView(ProjectViewModel viewModel, Pane canvasPane) {

        treeView.setRoot(viewModel);
        treeView.getSelectionModel().select(viewModel);
        viewModel.setExpanded(true);
        treeView.setMaxHeight(Double.MAX_VALUE);
        treeView.setCellFactory(param -> new ProjectNodeView(ProjectView.this));

        BooleanBinding disableAddNode = Bindings.createBooleanBinding(() -> {
            TreeItem<ProjectNodeModel> selectedItem = treeView.getSelectionModel().getSelectedItem();
            return selectedItem == null || !selectedItem.getValue().canHaveChildren();
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

        selectedNodeViewModel.bind(Bindings.createObjectBinding(() -> {
            TreeItem<ProjectNodeModel> selectedItem = treeView.getSelectionModel().getSelectedItem();
            if (selectedItem instanceof ProjectNodeViewModel selectedViewModel) {
                return selectedViewModel;
            }
            return null;
        }, treeView.getSelectionModel().selectedItemProperty()));

        // canvas pane
        selectedNodeViewModel.addListener((obs, oldNode, newNode) -> {
            if (newNode instanceof ProjectFileViewModel fileViewModel) {
                // todo: image is not rendered on first try!
                String id = fileViewModel.getId();
                if (canvasMap.containsKey(id)) {
                    canvasPane.getChildren().setAll(canvasMap.get(id));
                } else {
                    CanvasView canvasView = new CanvasView(fileViewModel.getCanvas());
                    System.out.println("new canvas view created for file: ");
                    canvasMap.put(id, canvasView);
                    canvasPane.getChildren().setAll(canvasView);
                }
            }
        });
    }

    private void deleteNode() {
        TreeItem<ProjectNodeModel> focusedItem = treeView.getSelectionModel().getSelectedItem();
        if(focusedItem instanceof ProjectNodeViewModel focusedViewModel) {
            String focusedViewModelType = focusedViewModel.getValue().canHaveChildren() ? "folder" : "file";
            String focusedViewModelName = focusedViewModel.getValue().getNameWithExtension();
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
                ProjectNodeViewModel newViewModel = switch (newFile.getType()) {
                    case SPRITE_FILE -> new ProjectFileViewModel((ProjectFileModel) newFile);
                    default -> new ProjectNodeViewModel(newFile);
                };

                focusedViewModel.addChild(newViewModel);
                Platform.runLater(() -> {
                    treeView.getSelectionModel().select(newViewModel);
                    int idx = treeView.getSelectionModel().getSelectedIndex();
                    treeView.getFocusModel().focus(idx);
                    treeView.scrollTo(treeView.getSelectionModel().getSelectedIndex());
                });
            });
        }
    }

    public ProjectNodeViewModel getSelectedNodeViewModel() {
        return selectedNodeViewModel.get();
    }

    public ObjectProperty<ProjectNodeViewModel> selectedNodeViewModelProperty() {
        return selectedNodeViewModel;
    }
}
