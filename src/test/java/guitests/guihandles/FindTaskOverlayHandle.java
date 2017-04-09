package guitests.guihandles;


import java.util.List;

import guitests.working.GuiRobot;
import javafx.geometry.Point2D;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import utask.TestApp;
import utask.model.task.ReadOnlyTask;
import utask.testutil.TestUtil;

/**
 * Provides a handle for the panel containing the todo task.
 */
public class FindTaskOverlayHandle extends GuiHandle {

    public static final int NOT_FOUND = -1;
    private static final String SEARCH_TABLE_VIEW_ID = "#searchTable";

    public FindTaskOverlayHandle(GuiRobot guiRobot, Stage primaryStage) {
        super(guiRobot, primaryStage, TestApp.APP_TITLE);
    }

    public List<ReadOnlyTask> getSelectedTask() {
        TableView<ReadOnlyTask> tableView = getTableView();
        return tableView.getSelectionModel().getSelectedItems();
    }

    public TableView<ReadOnlyTask> getTableView() {
        return getNode(SEARCH_TABLE_VIEW_ID);
    }

    /**
     * Clicks on the ListView.
     */
    public void clickOnListView() {
        Point2D point = TestUtil.getScreenMidPoint(getTableView());
        guiRobot.clickOn(point.getX(), point.getY());
    }

    /**
     * Returns true if the {@code persons} appear as the sub list (in that order) at position {@code startPosition}.
     */
    public boolean containsInOrder(int startPosition, ReadOnlyTask... tasks) {
        List<ReadOnlyTask> tasksInList = getTableView().getItems();

        // Return false if the list in panel is too short to contain the given list
        if (startPosition + tasks.length > tasksInList.size()) {
            return false;
        }

        // Return false if any of the tasks doesn't match
        for (int i = 0; i < tasks.length; i++) {
            if (!tasksInList.get(startPosition + i).getName().fullName.equals(tasks[i].getName().fullName)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns the position of the person given, {@code NOT_FOUND} if not found in the list.
     */
    public int getTaskIndex(ReadOnlyTask targetTask) {
        List<ReadOnlyTask> tasks = getTableView().getItems();
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getName().equals(targetTask.getName())) {
                return i;
            }
        }
        return NOT_FOUND;
    }

    /**
     * Gets a task from the list by index
     */
    public ReadOnlyTask getTask(int index) {
        return getTableView().getItems().get(index);
    }

    public int getResultsSize() {
        return getTableView().getItems().size();
    }
}
