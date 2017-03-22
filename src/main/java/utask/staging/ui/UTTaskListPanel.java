//@@author A0139996A
package utask.staging.ui;

import java.util.logging.Logger;

import com.jfoenix.controls.JFXListView;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
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
    private static final String FXML = "UTTaskListPanel.fxml";
    private static final double CARD_HEIGHT = UTTaskListCard.CARD_HEIGHT;
    private final Logger logger = LogsCenter.getLogger(UTTaskListPanel.class);

    @FXML
    private ScrollPane rootPane;

    @FXML
    private VBox container;

    public UTTaskListPanel(Pane parent, ObservableList<ReadOnlyTask> tasks) {
        super(FXML);

        assert (parent != null && tasks != null);

        addControlsToParent(parent);
    }

    private void addControlsToParent(Pane parent) {
        createLabelledListViewControl(container, TypicalTaskBuilder.due(), "Due");
        createLabelledListViewControl(container, TypicalTaskBuilder.today(), "Today");
        createLabelledListViewControl(container, FXCollections.observableArrayList(), "Tomorrow");
        createLabelledListViewControl(container, FXCollections.observableArrayList(), "Future");

        FxViewUtil.applyAnchorBoundaryParameters(rootPane, 0.0, 0.0, 0.0, 0.0);
        parent.getChildren().add(rootPane);

        UTListViewHelper.getInstance().updateListViews();
    }

    private void createLabelledListViewControl(Pane parent, ObservableList<ReadOnlyTask> tasks, String labelName) {

        assert(tasks != null);

        Label label = createLabel(labelName);
        JFXListView<ReadOnlyTask> listView = createListView();

        addLabelledListViewToParent(parent, label, listView);

        setConnections(listView, tasks);
        addStylingPropertiesToLabelBasedOnListViewSize(listView, label);
        addStylingPropertiesToListView(listView);
    }

    private Label createLabel(String name) {
        Label label = new Label(name);
        label.getStyleClass().add("list-label");
        return label;
    }

    private JFXListView<ReadOnlyTask> createListView() {
        JFXListView<ReadOnlyTask> list = new JFXListView<ReadOnlyTask>();
        list.getStyleClass().add("jfx-list-view");
        list.getStyleClass().add("custom-jfx-list-view1");
        return list;
    }

    private void addLabelledListViewToParent(Pane parent, Label label, JFXListView<ReadOnlyTask> list) {
        parent.getChildren().add(label);
        parent.getChildren().add(list);
    }

    private void addStylingPropertiesToListView(JFXListView<ReadOnlyTask> listView) {
        listView.minHeightProperty().bind(Bindings.size(listView.getItems()).multiply(CARD_HEIGHT));
    }

    /**
     * If listview has no items, it will not be visible.
     * Use managedProperty to invoke redrawing on UI
     */
    private void addStylingPropertiesToLabelBasedOnListViewSize(JFXListView<ReadOnlyTask> listView, Label label) {
        label.visibleProperty().bind(Bindings.size(listView.getItems()).greaterThan(0));
        label.managedProperty().bind(label.visibleProperty());
    }

    private void setConnections(ListView<ReadOnlyTask> listView, ObservableList<ReadOnlyTask> tasks) {
        listView.setItems(tasks);
        UTListViewHelper.getInstance().addListView(listView);
        setEventHandlerForSelectionChangeEvent(listView);
    }

    //@@author
    private void setEventHandlerForSelectionChangeEvent(ListView<ReadOnlyTask> listView) {
        listView.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        logger.fine("Selection in task list panel changed to : '" + newValue + "'");
                        raise(new TaskListPanelSelectionChangedEvent(listView, newValue));
                    }
                });
    }
}

