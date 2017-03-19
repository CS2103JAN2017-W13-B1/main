package utask.staging.ui;

import java.util.logging.Logger;

import com.jfoenix.controls.JFXListView;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.ui.PersonPanelSelectionChangedEvent;
import seedu.address.commons.util.FxViewUtil;
import seedu.address.ui.PersonListPanel;
import seedu.utask.model.task.ReadOnlyTask;

public class TodoListPanel extends StagingUiPart<Region> {
    private final Logger logger = LogsCenter.getLogger(PersonListPanel.class);
    private static final String FXML = "TodoListPanel.fxml";

    @FXML
    private VBox rootPane;

    @FXML
    private JFXListView<ReadOnlyTask> lstTasks;

    public TodoListPanel(Pane parent, ObservableList<ReadOnlyTask> tasks) {
        super(FXML);

        assert(parent != null && tasks != null);
        setConnections(lstTasks, tasks, null);

        FxViewUtil.applyAnchorBoundaryParameters(rootPane, 0.0, 0.0, 0.0, 0.0);
        parent.getChildren().add(rootPane);
    }

    private void setConnections(ListView<ReadOnlyTask> listView,
        ObservableList<ReadOnlyTask> tasks, ListView<ReadOnlyTask> prevListView) {
        listView.setItems(tasks);
        listView.setCellFactory(lw -> new TaskListViewCell(prevListView));
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
                        raise(new PersonPanelSelectionChangedEvent(newValue));
                    }
                });
    }

    //TODO: Extract method
    class TaskListViewCell extends ListCell<ReadOnlyTask> {

        private ListView<ReadOnlyTask> previousList;

        public TaskListViewCell(ListView<ReadOnlyTask> previousList) {
            //Can be null if it is the first list
            this.previousList = previousList;
        }

        @Override
        protected void updateItem(ReadOnlyTask task, boolean empty) {
            super.updateItem(task, empty);

            if (empty || task == null) {
                setGraphic(null);
                setText(null);
            } else {

                int offset = 1;
                if (previousList != null) {
                    offset = previousList.getItems().size() + 1;
                }

                setGraphic(new TaskListCard(task, getIndex() + offset).getRoot());
            }
        }
    }
}
