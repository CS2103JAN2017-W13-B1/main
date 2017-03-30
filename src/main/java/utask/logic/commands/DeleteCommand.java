//@@author A0138493W

package utask.logic.commands;

import java.util.List;

import utask.commons.core.Messages;
import utask.logic.commands.exceptions.CommandException;
import utask.logic.commands.inteface.ReversibleCommand;
import utask.model.task.ReadOnlyTask;
import utask.model.task.Task;
import utask.model.task.UniqueTaskList.TaskNotFoundException;
import utask.staging.ui.helper.UTFliterListHelper;
import utask.staging.ui.helper.UTListViewHelper;

/**
 * Deletes a task identified using it's last displayed index from the uTask.
 */
public class DeleteCommand extends Command implements ReversibleCommand {

    public static final String COMMAND_WORD = "delete";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the task identified by the index number used in the last task listing.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_DELETE_TASK_SUCCESS = "Tasks have been deleted";

    ReadOnlyTask taskToDelete;
    public final List<Integer> targetList;


    public DeleteCommand(List<Integer> targetList) {
        this.targetList = targetList;
    }

    //TODO: Cleanup
    //@@author A0139996A
    @Override
    public CommandResult execute() throws CommandException {
        //TODO: Jiahao coded this >>>>>>>>
        for (int targetIndex : targetList) {
            if (model.getTotalSizeOfLists() < targetIndex) {
                throw new CommandException(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
            }

            //- 1 as helper method is using zero-based indexing
            List<ReadOnlyTask> lastShownList =
                    UTFliterListHelper.getInstance().getUnderlyingListOfListViewByIndex(targetIndex - 1);

            int actualInt = UTFliterListHelper.getInstance().getActualIndexFromDisplayIndex(targetIndex - 1);
            taskToDelete = lastShownList.get(actualInt);

            //EventsCenter.getInstance().post(new ShowTaskOfInterestEvent(taskToDelete));

            //TODO: Find better a elegant solution
            //Needed to prevent TaskListPaneSelectionChangedEvent from triggering, which can go into a loop
            UTListViewHelper.getInstance().clearSelectionOfAllListViews();

            try {
                model.deleteTask(taskToDelete);
                model.addUndoCommand(this);
            } catch (TaskNotFoundException pnfe) {
                assert false : "The target task cannot be missing";
            }
        }
        return new CommandResult(String.format(MESSAGE_DELETE_TASK_SUCCESS));
    }

    @Override
    public void undo() throws Exception {
        model.addTask((Task) taskToDelete);
    }

    @Override
    public void redo() throws Exception {
        model.deleteTask((Task) taskToDelete);
    }

}
