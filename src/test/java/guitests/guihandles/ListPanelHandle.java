package guitests.guihandles;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import guitests.working.GuiRobot;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import utask.TestApp;
import utask.model.task.ReadOnlyTask;
import utask.testutil.TestUtil;

/**
 * Provides a handle for to the panel containing listviews of tasks and todos
 */
public class ListPanelHandle extends GuiHandle {

    public static final int NOT_FOUND = -1;
    public static final String CARD_PANE_ID = "#cardPane";

    private static final String DUE_LIST_VIEW_ID = "#lstDue";
    private static final String TODAY_LIST_VIEW_ID = "#lstToday";
    private static final String TOMORROW_LIST_VIEW_ID = "#lstTomorrow";
    private static final String FUTURE_LIST_VIEW_ID = "#lstFuture";
    private static final String TODO_LIST_VIEW_ID = "#lstTodoTasks";

    public ListPanelHandle(GuiRobot guiRobot, Stage primaryStage) {
        super(guiRobot, primaryStage, TestApp.APP_TITLE);
    }

    public ListView<ReadOnlyTask> getDueListView() {
        return getNode(DUE_LIST_VIEW_ID);
    }

    public ListView<ReadOnlyTask> getTodayListView() {
        return getNode(TODAY_LIST_VIEW_ID);
    }

    public ListView<ReadOnlyTask> getTomorrowListView() {
        return getNode(TOMORROW_LIST_VIEW_ID);
    }

    public ListView<ReadOnlyTask> getFutureListView() {
        return getNode(FUTURE_LIST_VIEW_ID);
    }

    public ListView<ReadOnlyTask> getTodoListView() {
        return getNode(TODO_LIST_VIEW_ID);
    }

    public List<ReadOnlyTask> getSelectedTaskOfList(ListView<ReadOnlyTask> list) {
        return list.getSelectionModel().getSelectedItems();
    }

    public List<ReadOnlyTask> getSelectedTaskOfDueList() {
        return getSelectedTaskOfList(getDueListView());
    }

    public List<ReadOnlyTask> getSelectedTaskOfTodayList() {
        return getSelectedTaskOfList(getTodayListView());
    }

    public List<ReadOnlyTask> getSelectedTaskOfTomorrowList() {
        return getSelectedTaskOfList(getTomorrowListView());
    }

    public List<ReadOnlyTask> getSelectedTaskOfFutureList() {
        return getSelectedTaskOfList(getFutureListView());
    }

    public List<ReadOnlyTask> getSelectedTaskOfTodoList() {
        return getSelectedTaskOfList(getTodoListView());
    }

    public boolean hasSelectionInListViews() {
        return !getDueListView().getSelectionModel().isEmpty()       ||
               !getTodayListView().getSelectionModel().isEmpty()     ||
               !getTomorrowListView().getSelectionModel().isEmpty()  ||
               !getFutureListView().getSelectionModel().isEmpty()    ||
               !getTodoListView().getSelectionModel().isEmpty();
    }

    /**
     * Returns true if the list is showing the person details correctly and in correct order.
     * @param tasks A list of person in the correct order.
     */
    public boolean isDueListMatching(ReadOnlyTask... tasks) {
        return this.isListMatching(getDueListView(), 0, tasks);
    }

    public boolean isTodayListMatching(ReadOnlyTask... tasks) {
        return this.isListMatching(getTodayListView(), 0, tasks);
    }

    public boolean isTomorrowListMatching(ReadOnlyTask... tasks) {
        return this.isListMatching(getTomorrowListView(), 0, tasks);
    }

    public boolean isFutureListMatching(ReadOnlyTask... tasks) {
        return this.isListMatching(getFutureListView(), 0, tasks);
    }

    public boolean isTodoListMatching(ReadOnlyTask... tasks) {
        return this.isListMatching(getTodoListView(), 0, tasks);
    }

    /**
     * Returns true if the list is showing the person details correctly and in correct order.
     * @param startPosition The starting position of the sub list.
     * @param tasks A list of person in the correct order.
     */
    public boolean isListMatching(ListView<ReadOnlyTask> listView,
            int startPosition, ReadOnlyTask... tasks) throws IllegalArgumentException {

        if (tasks.length + startPosition != listView.getItems().size()) {
            throw new IllegalArgumentException("List size mismatched\n" +
                    "Expected " + (listView.getItems().size() - 1) + " persons");
        }
        assertTrue(this.containsInOrder(listView.getItems(), startPosition, tasks));
        for (int i = 0; i < tasks.length; i++) {
            final int scrollTo = i + startPosition;
            guiRobot.interact(() -> listView.scrollTo(scrollTo));
            guiRobot.sleep(200);
            if (!TestUtil.compareCardAndTask(getTaskCardHandle(listView, startPosition + i), tasks[i])) {
                return false;
            }
        }
        return true;
    }

    /**
     * Clicks on the ListView.
     */
    public void clickOnListView(ListView<ReadOnlyTask> listView) {
        Point2D point = TestUtil.getScreenMidPoint(listView);
        guiRobot.clickOn(point.getX(), point.getY());
    }

    public void clickOnDueListView() {
        clickOnListView(getDueListView());
    }

    public void clickOnTodayListView() {
        clickOnListView(getTodayListView());
    }

    public void clickOnTomorrowListView() {
        clickOnListView(getTomorrowListView());
    }

    public void clickOnFutureListView() {
        clickOnListView(getFutureListView());
    }

    public void clickOnTodoListView() {
        clickOnListView(getTodoListView());
    }

    /**
     * Returns true if the {@code persons} appear as the sub list (in that order) at position {@code startPosition}.
     */
    public boolean containsInOrder(List<ReadOnlyTask> tasksInList, int startPosition, ReadOnlyTask... tasks) {
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

    public boolean containsInOrderInDueTask(int startPosition, ReadOnlyTask... tasks) {
        return containsInOrder(getDueListView().getItems(), startPosition, tasks);
    }

    public boolean containsInOrderInTodayTask(int startPosition, ReadOnlyTask... tasks) {
        return containsInOrder(getTodayListView().getItems(), startPosition, tasks);
    }

    public boolean containsInOrderInTomorrowTask(int startPosition, ReadOnlyTask... tasks) {
        return containsInOrder(getTomorrowListView().getItems(), startPosition, tasks);
    }

    public boolean containsInOrderInFutureTask(int startPosition, ReadOnlyTask... tasks) {
        return containsInOrder(getFutureListView().getItems(), startPosition, tasks);
    }

    public boolean containsInOrderInTodoTask(int startPosition, ReadOnlyTask... tasks) {
        return containsInOrder(getTodoListView().getItems(), startPosition, tasks);
    }

    public TaskCardHandle navigateToTaskInList(ListView<ReadOnlyTask> list, String name) {
        guiRobot.sleep(500); //Allow a bit of time for the list to be updated
        final Optional<ReadOnlyTask> task = list.getItems().stream()
                                                    .filter(p -> p.getName().fullName.equals(name))
                                                    .findAny();
        if (!task.isPresent()) {
            throw new IllegalStateException("Name not found: " + name);
        }

        return navigateToTask(task.get());
    }

    public TaskCardHandle navigateToTaskInDueList(String name) {
        return navigateToTaskInList(getDueListView(), name);
    }

    public TaskCardHandle navigateToTaskInTodayList(String name) {
        return navigateToTaskInList(getTodayListView(), name);
    }

    public TaskCardHandle navigateToTaskInTomorrowList(String name) {
        return navigateToTaskInList(getTomorrowListView(), name);
    }

    public TaskCardHandle navigateToTaskInFutureList(String name) {
        return navigateToTaskInList(getFutureListView(), name);
    }

    public TaskCardHandle navigateToTaskInTodoList(String name) {
        return navigateToTaskInList(getTodoListView(), name);
    }

    /**
     * Navigates the listview to display and select the task.
     */
    public TaskCardHandle navigateToTask(ReadOnlyTask task) {
        int index = getIndexOfListWithTask(task);
        ListView<ReadOnlyTask> list = getListViewWithTask(task);

        if (list != null) {
            guiRobot.interact(() -> {
                list.scrollTo(index);
                guiRobot.sleep(150);
                list.getSelectionModel().select(index);
            });
            guiRobot.sleep(100);
        }

        return getTaskCardHandle(task);
    }


    /**
     * Returns the position of the person given, {@code NOT_FOUND} if not found in the list.
     */
    public int getIndexOfListWithTask(ReadOnlyTask targetTask) {
        List<ReadOnlyTask> list = null;

        if (getDueListView().getItems().contains(targetTask)) {
            list = getDueListView().getItems();
        } else if (getTodoListView().getItems().contains(targetTask)) {
            list = getTodoListView().getItems();
        } else if (getTomorrowListView().getItems().contains(targetTask)) {
            list = getTomorrowListView().getItems();
        } else if (getFutureListView().getItems().contains(targetTask)) {
            list = getFutureListView().getItems();
        } else if (getTodoListView().getItems().contains(targetTask)) {
            list = getTodoListView().getItems();
        } else {
            list = new ArrayList<>();
        }

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getName().equals(targetTask.getName())) {
                return i;
            }
        }

        return NOT_FOUND;
    }

    public ListView<ReadOnlyTask> getListViewWithTask(ReadOnlyTask targetTask) {
        if (getDueListView().getItems().contains(targetTask)) {
            return getDueListView();
        } else if (getTodoListView().getItems().contains(targetTask)) {
            return getTodoListView();
        } else if (getTomorrowListView().getItems().contains(targetTask)) {
            return getTomorrowListView();
        } else if (getFutureListView().getItems().contains(targetTask)) {
            return getFutureListView();
        } else if (getTodoListView().getItems().contains(targetTask)) {
            return getTodoListView();
        }

        return null;
    }

    /**
     * Gets a person from the list by index
     */
    public ReadOnlyTask getTaskInDueList(int index) {
        return getTask(getDueListView(), index);
    }

    public ReadOnlyTask getTaskInTodayList(int index) {
        return getTask(getTodayListView(), index);
    }

    public ReadOnlyTask getTaskInTomorrowList(int index) {
        return getTask(getTomorrowListView(), index);
    }

    public ReadOnlyTask getTaskInFutureList(int index) {
        return getTask(getFutureListView(), index);
    }

    public ReadOnlyTask getTaskInTodoList(int index) {
        return getTask(getTodoListView(), index);
    }

    public ReadOnlyTask getTask(ListView<ReadOnlyTask> listView, int index) {
        return listView.getItems().get(index);
    }

    public TaskCardHandle getTaskCardHandle(ReadOnlyTask person) {
        Set<Node> nodes = getAllCardNodes();
        Optional<Node> taskCardNode = nodes.stream()
                .filter(n -> new TaskCardHandle(guiRobot, primaryStage, n).isSameCard(person))
                .findFirst();
        if (taskCardNode.isPresent()) {
            return new TaskCardHandle(guiRobot, primaryStage, taskCardNode.get());
        } else {
            return null;
        }
    }

    public TaskCardHandle getTaskCardHandle(ListView<ReadOnlyTask> listView, int index) {
        //return getTaskCardHandle(new EventTask(listView.getItems().get(index)));
        return getTaskCardHandle(listView.getItems().get(index));
    }

    public TaskCardHandle getTaskCardHandleInDueList(int index) {
        return getTaskCardHandle(getDueListView(), index);
    }

    public TaskCardHandle getTaskCardHandleInTodayList(int index) {
        return getTaskCardHandle(getTodayListView(), index);
    }

    public TaskCardHandle getTaskCardHandleInTomorrowList(int index) {
        return getTaskCardHandle(getTomorrowListView(), index);
    }

    public TaskCardHandle getTaskCardHandleInFutureList(int index) {
        return getTaskCardHandle(getFutureListView(), index);
    }

    public TaskCardHandle getTaskCardHandleTodoList(int index) {
        return getTaskCardHandle(getTodoListView(), index);
    }

    protected Set<Node> getAllCardNodes() {
        return guiRobot.lookup(CARD_PANE_ID).queryAll();
    }

    public int getTotalNumberOfTask() {
        return getNumberOfTaskInDueList() + getTotalNumberOfTaskInTodayList() + getTotalNumberOfTaskInTomorrowList() +
                getTotalNumberOfTaskInFutureList() + getTotalNumberOfTaskInTodoList();
    }

    public int getNumberOfTaskInDueList() {
        return getDueListView().getItems().size();
    }

    public int getTotalNumberOfTaskInTodayList() {
        return getTodayListView().getItems().size();
    }

    public int getTotalNumberOfTaskInTomorrowList() {
        return getTomorrowListView().getItems().size();
    }

    public int getTotalNumberOfTaskInFutureList() {
        return getFutureListView().getItems().size();
    }

    public int getTotalNumberOfTaskInTodoList() {
        return getTodoListView().getItems().size();
    }
}
