package utask.staging.ui;

import java.util.logging.Logger;

import com.jfoenix.controls.JFXListView;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import utask.commons.core.LogsCenter;
import utask.commons.util.FxViewUtil;
import utask.model.task.ReadOnlyTask;
import utask.staging.ui.events.TaskListPanelSelectionChangedEvent;
import utask.staging.ui.helper.TypicalTaskBuilder;

public class UTTaskListPanel extends StagingUiPart<Region> {
    private final Logger logger = LogsCenter.getLogger(UTTaskListPanel.class);
    private static final String FXML = "UTTaskListPanel.fxml";

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
    private static final double CARD_HEIGHT = 150.0;

    /**
     * @param placeholder
     *            The AnchorPane where the BrowserPanel must be inserted
     */
    public UTTaskListPanel(Pane placeholder, ObservableList<ReadOnlyTask> tasks) {
        super(FXML);

        assert (placeholder != null && tasks != null);

        this.parent = placeholder;
        populate();
    }

    private void populate() {
//        Platform.runLater(() -> {
        ObservableList<ReadOnlyTask> dueTasks = TypicalTaskBuilder.due();
        JFXListView<ReadOnlyTask> due = createListControlAndAddToParent("Due", container);
        setConnections(due, dueTasks);
        due.setMinHeight(CARD_HEIGHT * dueTasks.size());

        ObservableList<ReadOnlyTask> todayTasks = TypicalTaskBuilder.today();
        JFXListView<ReadOnlyTask> today = createListControlAndAddToParent("Today", container);
        setConnections(today, todayTasks);
        today.setMinHeight(CARD_HEIGHT * todayTasks.size());

        FxViewUtil.applyAnchorBoundaryParameters(rootPane, 0.0, 0.0, 0.0, 0.0);
        parent.getChildren().add(rootPane);
//        });

        UTListViewHelper.getInstance().updateListViews();

    }

    private void setConnections(ListView<ReadOnlyTask> listView, ObservableList<ReadOnlyTask> tasks) {
        listView.setItems(tasks);
        UTListViewHelper.getInstance().addListView(listView);
        //listView.setCellFactory(lw -> new TaskListViewCell(fake));
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
                        raise(new TaskListPanelSelectionChangedEvent(listView, newValue));
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
        list.getStyleClass().add("jfx-list-view");
        list.getStyleClass().add("custom-jfx-list-view1");

        parent.getChildren().add(label);
        parent.getChildren().add(list);

        return list;
    }
}

