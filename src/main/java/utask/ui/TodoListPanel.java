
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
public class TodoListPanel extends UiPart<Region> {
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
        lstTodoTasks.getStyleClass().add("jfx-list-view");
        lstTodoTasks.getStyleClass().add("custom-jfx-list-view1");
        lstTodoTasks.setStyle("-jfx-expanded : true;");
    }

    private void addStylingPropertiesToLabel() {
        lblTodo.visibleProperty().bind(Bindings.size(lstTodoTasks.getItems()).greaterThan(0));
        lblTodo.managedProperty().bind(lblTodo.visibleProperty());
    }
}
