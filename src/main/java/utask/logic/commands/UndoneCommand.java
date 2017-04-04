package utask.logic.commands;

import utask.commons.core.EventsCenter;
import utask.commons.core.Messages;
import utask.commons.events.ui.ShowTaskOfInterestEvent;
import utask.commons.exceptions.IllegalValueException;
import utask.commons.util.UpdateUtil;
import utask.logic.commands.exceptions.CommandException;
import utask.logic.commands.inteface.ReversibleCommand;
import utask.model.task.ReadOnlyTask;
import utask.model.task.Task;

/**
 * Edits the details of an existing task in the uTask.
 */
// @@author A0138423J
public class UndoneCommand extends Command implements ReversibleCommand {

    public static final String COMMAND_WORD = "undone";
    public static final String COMMAND_FORMAT = "[INDEX (must be a positive integer)]";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Updates the status to uncompleted of the task specified "
            + "by the index number used in the last task listing. \n"
            + "Parameters: " + COMMAND_FORMAT
            + "Example: " + COMMAND_WORD
            + COMMAND_WORD + " ";

    public static final String MESSAGE_UNDONE_TASK_SUCCESS = "Undone task: %1$s";
    public static final String MESSAGE_NOT_DONE = "A number for index must be provided.";
    public static final String MESSAGE_DUPLICATE_STATUS = "This task is already uncompleted in uTask.";
    public static final String MESSAGE_INTERNAL_ERROR = "Error updating Status.";

    private final int filteredTaskListIndex;
    private ReadOnlyTask taskToEdit;
    private Task editedTask;

    // @@author A0138423J
    /**
     * @param filteredTaskListIndex
     *            the index of the task in the filtered task list to edit
     */
    public UndoneCommand(int filteredTaskListIndex) {
        assert filteredTaskListIndex > 0;
        // converts filteredTaskListIndex from one-based to zero-based.
        this.filteredTaskListIndex = filteredTaskListIndex - 1;
    }

    // @@author A0138423J
    @Override
    public CommandResult execute() throws CommandException {
        if (filteredTaskListIndex >= model.getTotalSizeOfLists()) {
            throw new CommandException(
                    Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        }
        // If value already incomplete, inform the user
        if (!taskToEdit.getStatus().isStatusComplete()) {
            throw new CommandException(MESSAGE_DUPLICATE_STATUS);
        }

        // Retrieve task to be edited from save file
        taskToEdit = UpdateUtil.fetchTaskToEdit(filteredTaskListIndex);
        editedTask = null;
        try {
            editedTask = UpdateUtil.createEditedTask(taskToEdit, false);
            model.updateTask(taskToEdit, editedTask);
            model.addUndoCommand(this);
        } catch (IllegalValueException e) {
            throw new CommandException(MESSAGE_INTERNAL_ERROR);
        }
        return new CommandResult(
                String.format(MESSAGE_UNDONE_TASK_SUCCESS, editedTask));
    }

    // @@author A0138423J
    @Override
    public void undo() throws Exception {
        model.updateTask(editedTask, taskToEdit);
    }

    // @@author A0138423J
    @Override
    public void redo() throws Exception {
        model.updateTask(taskToEdit, editedTask);
        EventsCenter.getInstance()
                .post(new ShowTaskOfInterestEvent(editedTask));
    }
}
