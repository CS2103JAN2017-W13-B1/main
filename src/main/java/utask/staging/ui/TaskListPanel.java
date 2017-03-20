package utask.staging.ui;

import java.util.ArrayList;
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
import utask.commons.core.LogsCenter;
import utask.commons.events.ui.PersonPanelSelectionChangedEvent;
import utask.commons.util.FxViewUtil;
import utask.model.task.ReadOnlyTask;
import utask.staging.ui.TaskListPanel.TaskListViewCell;

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
    public TaskListPanel(Pane placeholder, ObservableList<ReadOnlyTask> tasks, ArrayList<ListView> chain) {
        super(FXML);

        assert (placeholder != null && tasks != null);

        this.parent = placeholder;
        populate(chain);
    }

    private void populate(ArrayList<ListView> chain) {
//        Platform.runLater(() -> {
        ObservableList<ReadOnlyTask> dueTasks = TypicalTaskBuilder.due();
        double height = 130.0;
        JFXListView<ReadOnlyTask> due = createListControlAndAddToParent("Due", container);
        setConnections(due, dueTasks, null);
        chain.add(due);
        due.setMinHeight(height * dueTasks.size());

        ObservableList<ReadOnlyTask> todayTasks = TypicalTaskBuilder.today();
        JFXListView<ReadOnlyTask> today = createListControlAndAddToParent("Today", container);
        setConnections(today, todayTasks, chain);
        chain.add(today);
        today.setMinHeight(height * todayTasks.size());

        FxViewUtil.applyAnchorBoundaryParameters(rootPane, 0.0, 0.0, 0.0, 0.0);
        parent.getChildren().add(rootPane);
//        });
    }

    private void setConnections(ListView<ReadOnlyTask> listView,
        ObservableList<ReadOnlyTask> tasks, ArrayList<ListView> previousListView) {
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

    //TODO: EXTRACT THIS
    class TaskListViewCell extends ListCell<ReadOnlyTask> {

        private ArrayList<ListView> previousLists;

        public TaskListViewCell(ArrayList<ListView> previousLists) {
            //Can be null if it is the first list
            this.previousLists = previousLists;
        }

        @Override
        protected void updateItem(ReadOnlyTask task, boolean empty) {
            super.updateItem(task, empty);

            if (empty || task == null) {
                setGraphic(null);
                setText(null);
            } else {

                int offset = 1;

                //TODO:
                if (previousLists != null) {
                    System.out.println("TASK >> " + previousLists.size());

                    for (ListView lw : previousLists) {
                        offset += lw.getItems().size();
                    }
                }

                setGraphic(new TaskListCard(task, getIndex() + offset).getRoot());
            }
        }
    }
}

