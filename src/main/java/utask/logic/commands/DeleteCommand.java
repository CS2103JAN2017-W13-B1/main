//@@author A0138493W
package utask.logic.commands;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import utask.commons.core.Messages;
import utask.logic.commands.exceptions.CommandException;
import utask.logic.commands.inteface.ReversibleCommand;
import utask.model.task.ReadOnlyTask;
import utask.model.task.Task;
import utask.model.task.UniqueTaskList.TaskNotFoundException;
import utask.ui.helper.DelayedExecution;

/**
 * Deletes a task identified using it's last displayed index from the uTask.
 */
public class DeleteCommand extends Command implements ReversibleCommand {

    public static final String COMMAND_WORD = "delete";
    public static final String COMMAND_FORMAT = "INDEX (must be a positive integer)";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the task identified by the index number used in the last task listing.\n"
            + "Parameters: " + COMMAND_FORMAT + "\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_DELETE_TASK_SUCCESS = "Tasks have been deleted";

    ReadOnlyTask taskToDelete;
    List<ReadOnlyTask> deleteTasks;
    public final List<Integer> targetList;


    public DeleteCommand(List<Integer> targetList) {
        this.targetList = targetList;
        deleteTasks = new LinkedList<ReadOnlyTask>();
    }

    @Override
    public CommandResult execute() throws CommandException {

        for (int targetIndex : targetList) {
            checkIfGivenIndexIsValid(targetIndex);
            taskToDelete = getTaskToDelete(targetIndex);
            deleteTasks.add(taskToDelete);

            //Do selection effect for 1 task only. It will cause race condition when doing multiple effect in a loop
            if (targetList.size() == 1) {
                notifyUI(taskToDelete);
                new DelayedExecution((e)-> {
                    deleteTask(taskToDelete);
                }).run();
            } else {
                deleteTask(taskToDelete);
            }
        }

        model.addUndoCommand(this);

        return new CommandResult(String.format(MESSAGE_DELETE_TASK_SUCCESS));
    }

    private void checkIfGivenIndexIsValid(int targetIndex) throws CommandException {
        if (model.getTotalSizeOfLists() < targetIndex) {
            throw new CommandException(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        }
    }

    private void deleteTask(ReadOnlyTask taskToDelete) {
        try {
            model.deleteTask(taskToDelete);
        } catch (TaskNotFoundException pnfe) {
            assert false : "The target task cannot be missing";
        }
    }

    private ReadOnlyTask getTaskToDelete(int targetIndex) {
        //- 1 as helper method is using zero-based indexing
        List<ReadOnlyTask> list = model.getUnderlyingListByIndex(targetIndex - 1);

        int actualInt = model.getActualIndexFromDisplayIndex(targetIndex - 1);
        return list.get(actualInt);
    }

    @Override
    public void undo() throws Exception {
        Collections.reverse(deleteTasks); //Ensures that items are restored in previous state.
        for (int i = 0; i < deleteTasks.size(); i++) {
            model.addTask((Task) deleteTasks.get(i));
        }

        notifyUI(taskToDelete);
    }

    @Override
    public void redo() throws Exception {
        notifyUI(taskToDelete);
        Collections.reverse(deleteTasks); //Ensures that items are restored in previous state.
        //Access to deleteTask() must be delayed to prevent race condition with notifyUI
        new DelayedExecution((e)-> {
            try {
                for (int i = 0; i < deleteTasks.size(); i++) {
                    model.deleteTask((Task) deleteTasks.get(i));
                }
            } catch (TaskNotFoundException pnfe) {
                //This exception is handled as it is not possible to hit this exception with normal usage
                assert false : "The target task cannot be missing";
            }
        }).run();
    }

}
