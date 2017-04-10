package utask.ui;

import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import utask.commons.util.FxViewUtil;
import utask.logic.Logic;
import utask.model.task.ReadOnlyTask;
import utask.ui.helper.ListViewHelper;
import utask.ui.helper.UTListView;

//@@author A0139996A
/*
 * TodoListPanel handles the UI Logic of how TodoListPanel appears.
 * It also work with ListViewHelper to ensure the list are properly synced.
 * */
public class TodoListPanel extends UiPart<Region> {
    private static final String LISTVIEW_EXPAND_CSS = "-jfx-expanded : true;";
    private static final String EXTENDED_JFXLISTVIEW_CSS = "custom-jfx-list-view1";
    private static final String JFXLISTVIEW_CSS = "jfx-list-view";
    private static final String FXML = "TodoListPanel.fxml";

    @FXML
    private VBox rootPane;

    @FXML
    private Label lblTodo;

    @FXML
    private UTListView<ReadOnlyTask> lstTodoTasks;

    public TodoListPanel(Pane parent, Logic logic) {
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
        ListViewHelper.getInstance().addList(listView);
    }

    private void addStylingPropertiesToListView() {
        lstTodoTasks.getStyleClass().add(JFXLISTVIEW_CSS);
        lstTodoTasks.getStyleClass().add(EXTENDED_JFXLISTVIEW_CSS);
        lstTodoTasks.setStyle(LISTVIEW_EXPAND_CSS);
    }

    private void addStylingPropertiesToLabel() {
        lblTodo.visibleProperty().bind(Bindings.size(lstTodoTasks.getItems()).greaterThan(0));
        lblTodo.managedProperty().bind(lblTodo.visibleProperty());
    }
}
