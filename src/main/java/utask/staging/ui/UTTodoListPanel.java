package utask.staging.ui;

import java.util.ArrayList;
import java.util.logging.Logger;

import com.jfoenix.controls.JFXListView;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import utask.commons.core.LogsCenter;
import utask.commons.util.FxViewUtil;
import utask.model.task.ReadOnlyTask;
import utask.staging.ui.events.TaskListPanelSelectionChangedEvent;
import utask.ui.PersonListPanel;

public class UTTodoListPanel extends StagingUiPart<Region> {
    private final Logger logger = LogsCenter.getLogger(PersonListPanel.class);
    private static final String FXML = "UTTodoListPanel.fxml";

    @FXML
    private VBox rootPane;

    @FXML
    private JFXListView<ReadOnlyTask> lstTasks;

    public UTTodoListPanel(Pane parent, ObservableList<ReadOnlyTask> tasks, ArrayList<ListView> chain) {
        super(FXML);

        assert(parent != null && tasks != null);

        lstTasks.getStyleClass().add("jfx-list-view");
        lstTasks.getStyleClass().add("custom-jfx-list-view1");
        lstTasks.setStyle("-jfx-expanded : true;");

        setConnections(lstTasks, tasks, chain);

        FxViewUtil.applyAnchorBoundaryParameters(rootPane, 0.0, 0.0, 0.0, 0.0);
        parent.getChildren().add(rootPane);
    }

    private void setConnections(ListView<ReadOnlyTask> listView,
        ObservableList<ReadOnlyTask> tasks, ArrayList<ListView> previousListView) {
        listView.setItems(tasks);
        UTListViewHelper.getInstance().addListView(listView);
//        System.out.println("SETCONN : TODO");
        //listView.setCellFactory(lw -> new TaskListViewCell(3));
        setEventHandlerForSelectionChangeEvent(listView);
    }

//    private void addToPlaceholder(AnchorPane placeHolderPane) {
//        SplitPane.setResizableWithParent(placeHolderPane, false);
//        FxViewUtil.applyAnchorBoundaryParameters(getRoot(), 0.0, 0.0, 0.0, 0.0);
//        placeHolderPane.getChildren().add(getRoot());
//    }

    //TODO: Extract method
    private void setEventHandlerForSelectionChangeEvent(ListView<ReadOnlyTask> listView) {
        listView.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        logger.fine("Selection in task list panel changed to : '" + newValue + "'");
                        raise(new TaskListPanelSelectionChangedEvent(listView, newValue));
                    }
                });
    }

    public void scrollTo(int index) {
        Platform.runLater(() -> {
            lstTasks.scrollTo(index);
            lstTasks.getSelectionModel().clearAndSelect(index);
        });
    }
}
