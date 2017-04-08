package guitests.working;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import guitests.guihandles.TaskCardHandle;
import utask.commons.core.Messages;
import utask.testutil.TestTask;
import utask.testutil.TestUtil;

public class AddCommandTest extends UTaskGuiTest {

    @Test
    public void add() {
        //add one person
        //TestTask[] currentList = td.getTypicalPersons();
        TestTask[] currentList = new TestTask[0];
        TestTask taskToAdd = td.todo;
        assertAddToTodoListSuccess(taskToAdd, currentList);
        currentList = TestUtil.addTasksToList(currentList, taskToAdd);

        //add another person
//        taskToAdd = td.i;
//        assertAddSuccess(taskToAdd, currentList);
//        currentList = TestUtil.addTasksToList(currentList, taskToAdd);

        //add duplicate person
//        commandBox.runCommand(td.h.getAddCommand());
//        assertResultMessage(CreateCommand.MESSAGE_DUPLICATE_TASK);
//        assertTrue(todoListPanel.isListMatching(currentList));

        //add to empty list
        commandBox.runCommand("clear");
        assertAddToTodoListSuccess(td.todo);

        //invalid command
        commandBox.runCommand("adds Johnny");
        assertResultMessage(Messages.MESSAGE_UNKNOWN_COMMAND);
    }

    @Test
    public void addDueTasks() {
        TestTask[] currentList = new TestTask[0];
        TestTask taskToAdd = td.due;
        assertAddToDueListSuccess(taskToAdd);
    }

    public void addTodayTasks() {
        TestTask[] currentList = new TestTask[0];
        TestTask taskToAdd = td.due;
        assertAddToDueListSuccess(taskToAdd, currentList);
    }

    private void assertAddToTodoListSuccess(TestTask task, TestTask... currentList) {
        commandBox.runCommand(task.getAddCommand());

        //confirm the new card contains the right data
        TaskCardHandle addedCard = todoListPanel.navigateToTask(task.getName().fullName);
        assertMatching(task, addedCard);

        //confirm the list now contains all previous persons plus the new person
        TestTask[] expectedList = TestUtil.addTasksToList(currentList, task);
        assertTrue(todoListPanel.isListMatching(expectedList));
    }

    private void assertAddToDueListSuccess(TestTask task, TestTask... currentList) {
        commandBox.runCommand(task.getAddCommand());
        //confirm the new card contains the right data
        TaskCardHandle addedCard = taskListPanel.navigateToDueTask(task.getName().fullName);
        assertMatching(task, addedCard);

        //confirm the list now contains all previous persons plus the new person
        TestTask[] expectedList = TestUtil.addTasksToList(currentList, task);
        assertTrue(taskListPanel.isListMatching(expectedList));
    }
}
