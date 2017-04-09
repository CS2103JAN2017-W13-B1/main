package guitests.working;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import guitests.guihandles.TaskCardHandle;
import javafx.scene.control.ListView;
import utask.commons.core.Messages;
import utask.logic.commands.CreateCommand;
import utask.model.task.ReadOnlyTask;
import utask.testutil.TestTask;
import utask.testutil.TestUtil;

public class AddCommandTest extends UTaskGuiTest {

    @Before
    public void clear() {
        commandBox.runCommand("clear");
    }

    @Test
    public void add() {
        //add one person
        //TestTask[] currentList = td.getTypicalPersons();
//        TestTask[] currentList = new TestTask[0];
//        TestTask taskToAdd = td.todo;
//        assertAddToTodoListSuccess(taskToAdd, currentList);
//        currentList = TestUtil.addTasksToList(currentList, taskToAdd);

//        TestTask todoTaskToAdd = td.todoTask;
//        assertAddToTodoListSuccess(todoTaskToAdd);


        //add another person
//        taskToAdd = td.i;
//        assertAddSuccess(taskToAdd, currentList);
//        currentList = TestUtil.addTasksToList(currentList, taskToAdd);

        //add to empty list
        commandBox.runCommand("clear");
        assertAddToTodoListSuccess(td.todoTask);
    }

    @Test
    public void addDueTasksToEmptyList() {
        TestTask taskToAdd = td.dueTask;
        assertAddToDueListSuccess(taskToAdd);
    }

    @Test
    public void addTodayTasksToEmptyList() {
        TestTask taskToAdd = td.todayTask;
        assertAddToTodayListSuccess(taskToAdd);
    }

    @Test
    public void addTomorrowTasksToEmptyList() {
        TestTask taskToAdd = td.tomorrowTask;
        assertAddToTomorrowListSuccess(taskToAdd);
    }

    @Test
    public void addFutureTasksToEmptyList() {
        TestTask taskToAdd = td.futureTask;
        assertAddToFutureListSuccess(taskToAdd);
    }

    @Test
    public void addDuplicateTaskError() {
        commandBox.runCommand(td.h.getAddCommand());
        //add duplicate person
        commandBox.runCommand(td.h.getAddCommand());
        assertResultMessage(CreateCommand.MESSAGE_DUPLICATE_TASK);
    }

    @Test
    public void invalidCommand() {
        commandBox.runCommand("creates task");
        assertResultMessage(Messages.MESSAGE_UNKNOWN_COMMAND);
    }

    @Test
    public void addTodoTasksToEmptyList() {
        TestTask taskToAdd = td.todoTask;
        assertAddToTodoListSuccess(taskToAdd);
    }

    private void assertAddToListSuccess(ListView<ReadOnlyTask> listView, TestTask task, TestTask... currentList) {
        commandBox.runCommand(task.getAddCommand());

        //confirm the new card contains the right data
        TaskCardHandle addedCard = listPanel.navigateToTaskInList(listView, task.getName().fullName);
        assertMatching(task, addedCard);

        //confirm the list now contains all previous persons plus the new person
        TestTask[] expectedList = TestUtil.addTasksToList(currentList, task);
        assertTrue(listPanel.isListMatching(listView, 0, expectedList));
    }

    private void assertAddToDueListSuccess(TestTask task, TestTask... currentList) {
        assertAddToListSuccess(listPanel.getDueListView(), task, currentList);
    }

    private void assertAddToTodayListSuccess(TestTask task, TestTask... currentList) {
        assertAddToListSuccess(listPanel.getTodayListView(), task, currentList);
    }

    private void assertAddToTomorrowListSuccess(TestTask task, TestTask... currentList) {
        assertAddToListSuccess(listPanel.getTomorrowListView(), task, currentList);
    }

    private void assertAddToFutureListSuccess(TestTask task, TestTask... currentList) {
        assertAddToListSuccess(listPanel.getFutureListView(), task, currentList);
    }

    private void assertAddToTodoListSuccess(TestTask task, TestTask... currentList) {
        assertAddToListSuccess(listPanel.getTodoListView(), task, currentList);
    }
}
