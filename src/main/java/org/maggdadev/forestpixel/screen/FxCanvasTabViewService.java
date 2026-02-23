package org.maggdadev.forestpixel.screen;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.HBox;
import org.maggdadev.forestpixel.canvas.CanvasView;
import org.maggdadev.forestpixel.canvas.CanvasViewModel;
import org.maggdadev.forestpixel.structure.ProjectFileViewModel;
import java.util.Objects;

public class FxCanvasTabViewService extends HBox implements CanvasTabViewService {
    private final TabPane tabPane;

    public FxCanvasTabViewService() {
        tabPane = new TabPane();
        getChildren().add(tabPane);
    }

    @Override
    public void addToActiveTab(ProjectFileViewModel projectFileViewModel) {
        String id = projectFileViewModel.getId();
        tabPane.getSelectionModel().select(
                tabPane.getTabs().stream()
                        .filter(tab -> Objects.equals(tab.getId(), id))
                        .findFirst()
                        .orElseGet(() -> createTabForCanvas(projectFileViewModel)));
    }

    private Tab createTabForCanvas(ProjectFileViewModel projectFileViewModel) {
        CanvasViewModel viewModel = new CanvasViewModel(projectFileViewModel.getCanvasModel());
        CanvasView canvasView = new CanvasView(viewModel);
        Tab tab = new Tab(projectFileViewModel.getId(), canvasView);
        tab.setId(projectFileViewModel.getId());
        tabPane.getTabs().add(tab);
        return tab;
    }
}
