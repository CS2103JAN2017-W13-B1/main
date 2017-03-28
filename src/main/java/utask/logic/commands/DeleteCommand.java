package utask.logic.commands;

import java.util.List;

import utask.commons.core.Messages;
import utask.logic.commands.exceptions.CommandException;
import utask.logic.commands.inteface.ReversibleCommand;
import utask.model.task.ReadOnlyTask;
import utask.model.task.Task;
import utask.model.task.UniqueTaskList.TaskNotFoundException;
import utask.staging.ui.UTListHelper;
import utask.staging.ui.UTListViewHelper;

/**
 * Deletes a task identified using it's last displayed index from the uTask.
 */
public class DeleteCommand extends Command implements ReversibleCommand {

    public static final String COMMAND_WORD = "delete";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the task identified by the index number used in the last task listing.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_DELETE_TASK_SUCCESS = "Deleted Task: %1$s";

    public final int targetIndex;
    ReadOnlyTask taskToDelete;

    public DeleteCommand(int targetIndex) {
        this.targetIndex = targetIndex;
    }

    //TODO: Cleanup
    //@@author A0139996A
    @Override
    public CommandResult execute() throws CommandException {

//        UnmodifiableObservableList<ReadOnlyTask> lastShownList = model.getFilteredTaskList();
//
//        if (lastShownList.size() < targetIndex) {
//            throw new CommandException(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
//        }

        if (model.getTotalSizeOfLists() < targetIndex) {
            throw new CommandException(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        }

        //- 1 as helper method is using zero-based indexing
        List<ReadOnlyTask> lastShownList =
                UTListHelper.getInstance().getUnderlyingListOfListViewByIndex(targetIndex - 1);

        int actualInt = UTListHelper.getInstance().getActualIndexFromDisplayIndex(targetIndex - 1);
        taskToDelete = lastShownList.get(actualInt);

        //TODO: Find better a elegant solution
        //Needed to prevent TaskListPaneSelectionChangedEvent from triggering, which can go into a loop
        UTListViewHelper.getInstance().clearSelectionOfAllListViews();

        try {
            model.deleteTask(taskToDelete);
            model.addUndoCommand(this);
        } catch (TaskNotFoundException pnfe) {
            assert false : "The target task cannot be missing";
        }

        return new CommandResult(String.format(MESSAGE_DELETE_TASK_SUCCESS, taskToDelete));
    }

    @Override
    public void undo() throws Exception {
        model.addTask((Task) taskToDelete);
    }

    @Override
    public void redo() throws Exception {
        model.deleteTask(taskToDelete);
    }

}
