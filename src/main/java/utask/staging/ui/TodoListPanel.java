package utask.staging.ui;

import com.jfoenix.controls.JFXListView;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import seedu.address.commons.util.FxViewUtil;
import seedu.utask.model.task.ReadOnlyTask;

public class TodoListPanel extends StagingUiPart<Region> {

    private static final String FXML = "TodoListPanel.fxml";

    @FXML
    private VBox rootPane;

    @FXML
    private JFXListView<ReadOnlyTask> lstTasks;

    public TodoListPanel(Pane parent) {
        super(FXML);

        assert(parent != null);
        populate();

//        label.prefWidthProperty().bind(lstTasks.widthProperty());

        FxViewUtil.applyAnchorBoundaryParameters(rootPane, 0.0, 0.0, 0.0, 0.0);
        parent.getChildren().add(rootPane);
    }

    private void populate() {
        ObservableList<ReadOnlyTask> todos = TypicalTaskBuilder.todos();

        setConnections(lstTasks, todos, null);

//        lstTasks.getItems().add(new TaskListCard().getRoot());
//        lstTasks.getItems().add(new TaskListCard().getRoot());
//        lstTasks.getItems().add(new TaskListCard().getRoot());
//        lstTasks.getItems().add(new TaskListCard().getRoot());
//        lstTasks.getItems().add(new TaskListCard().getRoot());
//        lstTasks.getItems().add(new TaskListCard().getRoot());
    }

    private void setConnections(ListView<ReadOnlyTask> listView,
            ObservableList<ReadOnlyTask> tasks, ListView<ReadOnlyTask> prevListView) {
        listView.setItems(tasks);
        listView.setCellFactory(lw -> new TaskListViewCell(prevListView));
        //setEventHandlerForSelectionChangeEvent(listView);
    }

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
