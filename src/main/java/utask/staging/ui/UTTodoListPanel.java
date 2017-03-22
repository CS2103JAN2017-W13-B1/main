//@@author A0139996A
package utask.staging.ui;

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
    private static final String FXML = "UTTodoListPanel.fxml";

    private final Logger logger = LogsCenter.getLogger(UTTodoListPanel.class);

    @FXML
    private VBox rootPane;

    @FXML
    private JFXListView<ReadOnlyTask> lstTodoTasks;

    public UTTodoListPanel(Pane parent, ObservableList<ReadOnlyTask> tasks) {
        super(FXML);

        assert(parent != null && tasks != null);

        addStyleSheetsToControls();
        setConnections(lstTodoTasks, tasks);

        FxViewUtil.applyAnchorBoundaryParameters(rootPane, 0.0, 0.0, 0.0, 0.0);
        parent.getChildren().add(rootPane);
    }

    private void addStyleSheetsToControls() {
        lstTodoTasks.getStyleClass().add("jfx-list-view");
        lstTodoTasks.getStyleClass().add("custom-jfx-list-view1");
        lstTodoTasks.setStyle("-jfx-expanded : true;");
    }

    private void setConnections(ListView<ReadOnlyTask> listView, ObservableList<ReadOnlyTask> tasks) {
        listView.setItems(tasks);

        //Add current listview to a helper for chain counting
        UTListViewHelper.getInstance().addListView(listView);
        setEventHandlerForSelectionChangeEvent(listView);
    }

    private void setEventHandlerForSelectionChangeEvent(ListView<ReadOnlyTask> listView) {
        listView.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        logger.fine("Selection in to-dos list panel changed to : '" + newValue + "'");
                        raise(new TaskListPanelSelectionChangedEvent(listView, newValue));
                    }
                });
    }
}
