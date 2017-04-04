package utask.logic.commands;

import utask.commons.core.Messages;
import utask.commons.exceptions.IllegalValueException;
import utask.commons.util.UpdateUtil;
import utask.logic.commands.exceptions.CommandException;
import utask.logic.commands.inteface.ReversibleCommand;
import utask.model.task.ReadOnlyTask;
import utask.model.task.Task;
import utask.model.task.UniqueTaskList.DuplicateTaskException;
import utask.staging.ui.helper.DelayedExecution;;

/**
 * Edits the details of an existing task in the uTask.
 */
// @@author A0138423J
public class DoneCommand extends Command implements ReversibleCommand {

    public static final String COMMAND_WORD = "done";
    public static final String COMMAND_FORMAT = "[INDEX (must be a positive integer)]";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Updates the status to completed of the task specified "
            + "by the index number used in the last task listing. \n"
            + "Parameters: " + COMMAND_FORMAT + "\n" + "Example: "
            + COMMAND_WORD + COMMAND_WORD + " 1";

    public static final String MESSAGE_DONE_TASK_SUCCESS = "Done task: %1$s";
    public static final String MESSAGE_NOT_DONE = "A number for index must be provided.";
    public static final String MESSAGE_DUPLICATE_STATUS = "This task is already completed in uTask.";
    public static final String MESSAGE_INTERNAL_ERROR = "Error updating isCompleted attribute.";

    private final int filteredTaskListIndex;
    private ReadOnlyTask taskToEdit;
    private Task editedTask;

    // @@author A0138423J
    /**
     * @param filteredTaskListIndex
     *            the index of the task in the filtered task list to edit
     */
    public DoneCommand(int filteredTaskListIndex) {
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

        // Retrieve task to be edited from save file
        taskToEdit = UpdateUtil.fetchTaskToEdit(filteredTaskListIndex);
        editedTask = null;
        try {
            editedTask = UpdateUtil.createEditedTask(taskToEdit, true);
            notifyUI(taskToEdit);

            //BAD CODE -- BAD CODE -- BAD CODE
            new DelayedExecution((e)-> {
                try {
                    model.updateTask(taskToEdit, editedTask);
                    model.addUndoCommand(this);
                } catch (DuplicateTaskException e1) {
                    assert false : "Should never happen?";
                }
            }).run();
//            EventsCenter.getInstance().post(new UIClearListSelectionEvent());
        } catch (IllegalValueException e) {
            throw new CommandException(MESSAGE_INTERNAL_ERROR);
        }
        return new CommandResult(
                String.format(MESSAGE_DONE_TASK_SUCCESS, editedTask));
    }

    // @@author A0138423J
    @Override
    public void undo() throws Exception {
        model.updateTask(editedTask, taskToEdit);
        notifyUI(taskToEdit);
    }

    // @@author A0138423J
    @Override
    public void redo() throws Exception {
        model.updateTask(taskToEdit, editedTask);
        notifyUI(editedTask);
    }
}
