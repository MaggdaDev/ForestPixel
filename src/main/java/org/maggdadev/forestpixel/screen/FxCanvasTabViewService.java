package org.maggdadev.forestpixel.screen;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventTarget;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import org.maggdadev.forestpixel.canvas.CanvasView;
import org.maggdadev.forestpixel.canvas.CanvasViewModel;
import org.maggdadev.forestpixel.structure.ProjectFileViewModel;

import java.util.Objects;
import java.util.Optional;

public class FxCanvasTabViewService extends SplitPane implements CanvasTabViewService {
    private final TabPane tabPane;
    private final TabPane tabPane2;
    private final ObjectProperty<TabPane> activeTabPane = new SimpleObjectProperty<>();
    private Tab currentlyDraggedTab = null;

    public FxCanvasTabViewService() {
        tabPane = makeNewTabPane();
        tabPane2 = makeNewTabPane();
        addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
            Node target = (Node) event.getTarget();
            Node splitPaneItem = findSplitPaneItem(target);
            if (splitPaneItem != null) {
                activeTabPane.set((TabPane) splitPaneItem);
            }
        });
        getItems().addAll(tabPane, tabPane2);
    }

    private Optional<Tab> findDraggedTab(EventTarget target) {
        if (target instanceof Node targetNode) {
            Node currentNode = targetNode;
            while (currentNode != null) {
                if (currentNode.getStyleClass().contains("tab")) {
                    Tab tab = (Tab) currentNode.getProperties().get(Tab.class);
                    if (tab != null) {
                        return Optional.of(tab);
                    }
                }
                currentNode = currentNode.getParent();
            }
        }
        return Optional.empty();
    }

    private TabPane makeNewTabPane() {
        TabPane newTabPane = new TabPane();
        newTabPane.setOnDragDetected(event -> {
            try {
                System.out.println("Detect");
                findDraggedTab(event.getTarget()).ifPresent(tab -> {
                    currentlyDraggedTab = tab;
                    Dragboard db = newTabPane.startDragAndDrop(TransferMode.MOVE);
                    ClipboardContent content = new ClipboardContent();
                    content.putString("TAB_MOVE");
                    db.setContent(content);
                });
                event.consume();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        newTabPane.setOnDragOver(event -> {
            try {
                if (currentlyDraggedTab != null && currentlyDraggedTab.getTabPane() != newTabPane && event.getDragboard().hasString() && "TAB_MOVE".equals(event.getDragboard().getString())) {
                    event.acceptTransferModes(TransferMode.MOVE);
                }
                event.consume();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        newTabPane.setOnDragDropped(event -> {
            System.out.println("Dropped");
            if (currentlyDraggedTab != null && currentlyDraggedTab.getTabPane() != newTabPane && event.getDragboard().hasString() && "TAB_MOVE".equals(event.getDragboard().getString())) {
                TabPane sourceTabPane = currentlyDraggedTab.getTabPane();
                sourceTabPane.getTabs().remove(currentlyDraggedTab);
                newTabPane.getTabs().stream()
                        .filter(tab -> Objects.equals(tab.getId(), currentlyDraggedTab.getId()))
                        .findFirst().ifPresentOrElse(
                                tab -> newTabPane.getSelectionModel().select(tab),
                                () -> {
                                    newTabPane.getTabs().add(currentlyDraggedTab);
                                    newTabPane.getSelectionModel().select(currentlyDraggedTab);
                                }
                        );
                currentlyDraggedTab = null;
                event.setDropCompleted(true);
            }
            event.consume();
        });
        return newTabPane;
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
