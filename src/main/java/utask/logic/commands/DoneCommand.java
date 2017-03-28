package utask.logic.commands;

import java.util.List;

import utask.commons.core.Messages;
import utask.commons.exceptions.IllegalValueException;
import utask.logic.commands.exceptions.CommandException;
import utask.logic.commands.inteface.ReversibleCommand;
import utask.model.task.DeadlineTask;
import utask.model.task.EventTask;
import utask.model.task.FloatingTask;
import utask.model.task.IsCompleted;
import utask.model.task.ReadOnlyTask;
import utask.model.task.Task;

/**
 * Edits the details of an existing task in the uTask.
 */
// @@author A0138423J
public class DoneCommand extends Command implements ReversibleCommand {

    public static final String COMMAND_WORD = "done";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Updates the status to completed of the task specified "
            + "by the index number used in the last task listing. \n"
            + "Parameters: INDEX (must be a positive integer) " + "Example: "
            + COMMAND_WORD + " 1";

    public static final String MESSAGE_DONE_TASK_SUCCESS = "Done task: %1$s";
    public static final String MESSAGE_NOT_DONE = "A number for index must be provided.";
    public static final String MESSAGE_DUPLICATE_STATUS = "This task is already completed in uTask.";
    public static final String MESSAGE_INTERNAL_ERROR = "Error updating isCompleted attribute.";

    private final int filteredTaskListIndex;
    private ReadOnlyTask taskToEdit;

    /**
     * @param filteredTaskListIndex
     *            the index of the task in the filtered task list to edit
     */
    public DoneCommand(int filteredTaskListIndex) {
        assert filteredTaskListIndex > 0;

        // converts filteredTaskListIndex from one-based to zero-based.
        this.filteredTaskListIndex = filteredTaskListIndex - 1;

    }

    @Override
    public CommandResult execute() throws CommandException {
        List<ReadOnlyTask> lastShownList = model.getFilteredTaskList();

        if (filteredTaskListIndex >= lastShownList.size()) {
            throw new CommandException(
                    Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        }

        // Retrieve task to be edited from save file
        taskToEdit = lastShownList.get(filteredTaskListIndex);

        // If value already true, inform the user
        if ("true".equals(taskToEdit.getIsCompleted().toString())) {
            throw new CommandException(MESSAGE_DUPLICATE_STATUS);
        }
        Task temp = null;

        try {
            temp = createEditedTask(taskToEdit, true);
            model.updateTask(filteredTaskListIndex, temp);
            model.addUndoCommand(this);
        } catch (IllegalValueException e) {
            throw new CommandException(MESSAGE_INTERNAL_ERROR);
        }
        model.updateFilteredListToShowAll();
        return new CommandResult(
                String.format(MESSAGE_DONE_TASK_SUCCESS, temp));
    }

    /**
     * Creates and returns a {@code Task} with the details of {@code taskToEdit}
     * edited with {@code editTaskDescriptor}.
     */
    private static Task createEditedTask(ReadOnlyTask taskToEdit, Boolean value)
            throws IllegalValueException {
        assert taskToEdit != null;
        assert value != null;

        // TODO
        Task placeholder = null;
        if (!taskToEdit.getDeadline().isEmpty() && !taskToEdit.getTimestamp().isEmpty()) {
            placeholder = new EventTask(taskToEdit.getName(), taskToEdit.getDeadline(),
                    taskToEdit.getTimestamp(), taskToEdit.getFrequency(), taskToEdit.getTags(),
                    new IsCompleted(value.toString()));
        } else if (!taskToEdit.getDeadline().isEmpty() && taskToEdit.getTimestamp().isEmpty()) {
            placeholder = new DeadlineTask(taskToEdit.getName(), taskToEdit.getDeadline(),
                    taskToEdit.getFrequency(), taskToEdit.getTags(),
                    new IsCompleted(value.toString()));
        } else if (taskToEdit.getDeadline().isEmpty() && taskToEdit.getTimestamp().isEmpty()) {
            placeholder = new FloatingTask(taskToEdit.getName(), taskToEdit.getFrequency(), taskToEdit.getTags(),
                    new IsCompleted(value.toString()));
        }

        return placeholder;
    }

    //@@author A0139996A
    @Override
    public void undo() throws Exception {
        createEditedTask(taskToEdit, false);
    }

    @Override
    public void redo() throws Exception {
        createEditedTask(taskToEdit, true);
    }
}
