package utask.staging.ui;

import java.util.logging.Logger;

import com.jfoenix.controls.JFXListView;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.ui.PersonPanelSelectionChangedEvent;
import seedu.address.commons.util.FxViewUtil;
import seedu.utask.model.task.ReadOnlyTask;

public class TaskListPanel extends StagingUiPart<Region> {
    private final Logger logger = LogsCenter.getLogger(TaskListPanel.class);
    private static final String FXML = "TaskAnchorPane.fxml";

    @FXML
    private JFXListView<ReadOnlyTask> list;

    @FXML
    private Label dueLabel;

    @FXML
    private JFXListView<ReadOnlyTask> dueList;

    @FXML
    private JFXListView<ReadOnlyTask> subList;

    @FXML
    private ScrollPane rootPane;

    @FXML
    private VBox container;

    private Pane parent;

    /**
     * @param placeholder
     *            The AnchorPane where the BrowserPanel must be inserted
     */
    public TaskListPanel(Pane placeholder, ObservableList<ReadOnlyTask> tasks) {
        super(FXML);

        assert (placeholder != null && tasks != null);

        this.parent = placeholder;
        populate();
    }

    private void populate() {
        Platform.runLater(() -> {
            ObservableList<ReadOnlyTask> dueTasks = TypicalTaskBuilder.due();
            double height = 130.0;
            JFXListView<ReadOnlyTask> due = createListControlAndAddToParent("Due", container);
            setConnections(due, dueTasks, null);
            due.setMinHeight(height * dueTasks.size());

            ObservableList<ReadOnlyTask> todayTasks = TypicalTaskBuilder.today();
            JFXListView<ReadOnlyTask> today = createListControlAndAddToParent("Today", container);
            setConnections(today, todayTasks, due);
            today.setMinHeight(height * todayTasks.size());

            FxViewUtil.applyAnchorBoundaryParameters(rootPane, 0.0, 0.0, 0.0, 0.0);
            parent.getChildren().add(rootPane);
        });
    }

    private void setConnections(ListView<ReadOnlyTask> listView,
        ObservableList<ReadOnlyTask> tasks, ListView<ReadOnlyTask> previousListView) {
        listView.setItems(tasks);
        listView.setCellFactory(lw -> new TaskListViewCell(previousListView));
        setEventHandlerForSelectionChangeEvent(listView);
    }

//    private void addToPlaceholder(AnchorPane placeHolderPane) {
//        SplitPane.setResizableWithParent(placeHolderPane, false);
//        FxViewUtil.applyAnchorBoundaryParameters(getRoot(), 0.0, 0.0, 0.0, 0.0);
//        placeHolderPane.getChildren().add(getRoot());
//    }
    private void setEventHandlerForSelectionChangeEvent(ListView<ReadOnlyTask> listView) {
        listView.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        logger.fine("Selection in task list panel changed to : '" + newValue + "'");
                        raise(new PersonPanelSelectionChangedEvent(newValue));
                    }
                });
    }

    public void scrollTo(ListView<ReadOnlyTask> listView, int index) {
        Platform.runLater(() -> {
            listView.scrollTo(index);
            listView.getSelectionModel().clearAndSelect(index);
        });
    }

    private JFXListView<ReadOnlyTask> createListControlAndAddToParent(String name, Pane parent) {
        Label label = new Label(name);
        label.getStyleClass().add("list-label");
        JFXListView<ReadOnlyTask> list = new JFXListView<ReadOnlyTask>();
//        list.getStyleClass().add("jfx-list-view");
//        list.setStyle("-jfx-expanded : true;");
//        list.getStyleClass().add("custom-jfx-list-view1");

        parent.getChildren().add(label);
        parent.getChildren().add(list);

        return list;
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

