//@@author A0139996A
package utask.staging.ui;

import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;
import com.jfoenix.controls.JFXListView;

import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import utask.commons.core.EventsCenter;
import utask.commons.core.LogsCenter;
import utask.commons.events.ui.JumpToTaskListRequestEvent;
import utask.commons.util.FxViewUtil;
import utask.logic.Logic;
import utask.model.task.ReadOnlyTask;
import utask.staging.ui.helper.UTListView;
import utask.staging.ui.helper.UTListViewHelper;

public class UTTaskListPanel extends StagingUiPart<Region> {
    private static final String FXML = "UTTaskListPanel.fxml";
    private static final double CARD_HEIGHT = UTTaskListCard.CARD_HEIGHT;
    private final Logger logger = LogsCenter.getLogger(UTTaskListPanel.class);

    @FXML
    private ScrollPane rootPane;

    @FXML
    private VBox container;

    public UTTaskListPanel(Pane parent, Logic logic) {
        super(FXML);

        assert (parent != null && logic != null);

        addControlsToParent(parent, logic);
        EventsCenter.getInstance().registerHandler(this);
    }

    private void addControlsToParent(Pane parent, Logic logic) {
        //TODO: Use proper logic methods to populate
        createLabelledListViewControl(container, logic.getDueFilteredTaskList(), "Due");
        createLabelledListViewControl(container, logic.getTodayFilteredTaskList(), "Today");
        createLabelledListViewControl(container, logic.getTomorrowFilteredTaskList(), "Tomorrow");
        createLabelledListViewControl(container, logic.getFutureFilteredTaskList(), "Future");

        FxViewUtil.applyAnchorBoundaryParameters(rootPane, 0.0, 0.0, 0.0, 0.0);
        parent.getChildren().add(rootPane);

        //TODO: This is not the right place to call!
        UTListViewHelper.getInstance().updateListViews();
    }

    private void createLabelledListViewControl(Pane parent, ObservableList<ReadOnlyTask> tasks, String labelName) {

        assert(tasks != null);

        Label label = createLabel(labelName);
        UTListView<ReadOnlyTask> listView = createListView();
        listView.setId("lst" + labelName);

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

    private UTListView<ReadOnlyTask> createListView() {
        UTListView<ReadOnlyTask> list = new UTListView<ReadOnlyTask>();
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
        listView.visibleProperty().bind(Bindings.size(listView.getItems()).greaterThan(0));
        listView.managedProperty().bind(listView.visibleProperty());
    }

    /**
     * If listview has no items, it will not be visible.
     * Use managedProperty to invoke redrawing on UI
     */
    private void addStylingPropertiesToLabelBasedOnListViewSize(JFXListView<ReadOnlyTask> listView, Label label) {
        label.visibleProperty().bind(Bindings.size(listView.getItems()).greaterThan(0));
        label.managedProperty().bind(label.visibleProperty());
    }

    private void setConnections(UTListView<ReadOnlyTask> listView, ObservableList<ReadOnlyTask> tasks) {
        listView.setItems(tasks);
        UTListViewHelper.getInstance().addList(listView);
    }

    private void ensureVisible(ScrollPane pane, Node listView, int numberOfCards) {
        Bounds viewport = pane.getViewportBounds();
        double contentHeight = pane.getContent().getBoundsInLocal().getHeight();
        double nodeMinY = listView.getBoundsInParent().getMinY();
        double cardHeight = UTTaskListCard.CARD_HEIGHT;

        double topOfList = (nodeMinY + (numberOfCards * cardHeight)) / (contentHeight - viewport.getHeight());
        pane.setVvalue(topOfList);
    }

    private void scrollTo(Node node, int index) {
        if (container.getChildren().contains(node)) {
            ensureVisible(rootPane, node, index);
        }
    }

    @Subscribe
    public void handleToTaskListRequestEvent(JumpToTaskListRequestEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        scrollTo(event.targetNode, event.index);
    }
}

