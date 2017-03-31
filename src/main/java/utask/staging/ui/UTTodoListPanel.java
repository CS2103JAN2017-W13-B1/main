//@@author A0139996A
package utask.staging.ui;

import java.util.logging.Logger;

import com.jfoenix.controls.JFXListView;

import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import utask.commons.core.LogsCenter;
import utask.commons.util.FxViewUtil;
import utask.logic.Logic;
import utask.model.task.ReadOnlyTask;
import utask.staging.ui.events.TaskListPanelSelectionChangedEvent;
import utask.staging.ui.helper.UTListView;
import utask.staging.ui.helper.UTListViewHelper;

public class UTTodoListPanel extends StagingUiPart<Region> {
    private static final String FXML = "UTTodoListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(UTTodoListPanel.class);

    @FXML
    private VBox rootPane;

    @FXML
    private Label lblTodo;

    @FXML
    private UTListView<ReadOnlyTask> lstTodoTasks;

    public UTTodoListPanel(Pane parent, Logic logic) {
        super(FXML);

        assert(parent != null && logic != null);

        setConnections(lstTodoTasks, logic.getFloatingFilteredTaskList());

        addStylingPropertiesToListView();
        addStylingPropertiesToLabel();

        FxViewUtil.applyAnchorBoundaryParameters(rootPane, 0.0, 0.0, 0.0, 0.0);
        parent.getChildren().add(rootPane);
    }

    private void setConnections(UTListView<ReadOnlyTask> listView, ObservableList<ReadOnlyTask> tasks) {
        listView.setItems(tasks);

        //Add listview to helper for chain counting
        UTListViewHelper.getInstance().addList(listView);
        setEventHandlerForSelectionChangeEvent(listView);
    }

    private void addStylingPropertiesToListView() {
        lstTodoTasks.getStyleClass().add("jfx-list-view");
        lstTodoTasks.getStyleClass().add("custom-jfx-list-view1");
        lstTodoTasks.setStyle("-jfx-expanded : true;");
    }

    private void addStylingPropertiesToLabel() {
        lblTodo.visibleProperty().bind(Bindings.size(lstTodoTasks.getItems()).greaterThan(0));
        lblTodo.managedProperty().bind(lblTodo.visibleProperty());
    }

    //@@author
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
