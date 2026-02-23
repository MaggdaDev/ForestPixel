package org.maggdadev.forestpixel.screen;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.MouseEvent;
import org.maggdadev.forestpixel.canvas.CanvasView;
import org.maggdadev.forestpixel.canvas.CanvasViewModel;
import org.maggdadev.forestpixel.structure.ProjectFileViewModel;

import java.util.Objects;

public class FxCanvasTabViewService extends SplitPane implements CanvasTabViewService {
    private final TabPane tabPane;
    private final TabPane tabPane2;
    private final ObjectProperty<TabPane> activeTabPane = new SimpleObjectProperty<>();

    public FxCanvasTabViewService() {
        tabPane = new TabPane();
        tabPane.setId("TabPane1");
        tabPane2 = new TabPane();
        tabPane2.setId("TabPane2");
        addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
            Node target = (Node) event.getTarget();
            Node splitPaneItem = findSplitPaneItem(target);
            if (splitPaneItem != null) {
                activeTabPane.set((TabPane) splitPaneItem);
            }
        });
        getItems().addAll(tabPane, tabPane2);
        activeTabPane.subscribe(pane -> System.out.println("Active TabPane changed: " + (pane != null ? pane.getId() : "null")));
    }

    private Node findSplitPaneItem(Node node) {
        while (node != null) {
            if (getItems().contains(node)) {
                return node;
            }
            node = node.getParent();
        }
        return null;
    }

    @Override
    public void addToActiveTab(ProjectFileViewModel projectFileViewModel) {
        String id = projectFileViewModel.getId();
        TabPane paneToUse = activeTabPane.get() != null ? activeTabPane.get() : (TabPane) getItems().getFirst();
        paneToUse.getSelectionModel().select(
                paneToUse.getTabs().stream()
                        .filter(tab -> Objects.equals(tab.getId(), id))
                        .findFirst()
                        .orElseGet(() -> createTabForCanvas(paneToUse, projectFileViewModel)));
    }

    private static Tab createTabForCanvas(TabPane tabPaneToAdd, ProjectFileViewModel projectFileViewModel) {
        CanvasViewModel viewModel = new CanvasViewModel(projectFileViewModel.getCanvasModel());
        CanvasView canvasView = new CanvasView(viewModel);
        Tab tab = new Tab(projectFileViewModel.getId(), canvasView);
        tab.setId(projectFileViewModel.getId());
        tabPaneToAdd.getTabs().add(tab);
        Runnable contentUpdatedListener = canvasView::redrawAll;
        projectFileViewModel.addOnContentUpdatedListener(contentUpdatedListener);
        tab.setOnClosed(e -> projectFileViewModel.removeOnContentUpdatedListener(contentUpdatedListener));
        viewModel.canvasVersionCounterProperty().addListener((obs, oldVal, newVal) -> {
            projectFileViewModel.notifyContentUpdated();
        });
        return tab;
    }
}
