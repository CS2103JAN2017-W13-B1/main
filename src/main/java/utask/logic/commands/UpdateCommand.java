package utask.logic.commands;

import java.util.ArrayList;

import utask.commons.core.Messages;
import utask.commons.util.UpdateUtil;
import utask.logic.commands.exceptions.CommandException;
import utask.logic.commands.inteface.ReversibleCommand;
import utask.model.task.Attribute;
import utask.model.task.EditTaskDescriptor;
import utask.model.task.ReadOnlyTask;
import utask.model.task.Task;
import utask.model.task.UniqueTaskList;

/**
 * Edits the details of an existing task in the uTask.
 */

// @@author A0138423J
public class UpdateCommand extends Command implements ReversibleCommand {

    public static final String COMMAND_WORD = "update";

    public static final String COMMAND_FORMAT = "INDEX (must be a positive integer) [/name NAME] [/by DEADLINE]"
            + " [/from START_TIME to END_TIME] [/repeat FREQUENCY] [/tag TAG...][/done YES|NO]...";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Edits the details of the task specified "
            + "by the index number used in the last task listing. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: " + COMMAND_FORMAT + "\n" + "Example: "
            + COMMAND_WORD + " 1 /name do homework";

    public static final String MESSAGE_EDIT_TASK_SUCCESS = "Edited task: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_TASK = "This task already exists in the uTask.";

    private final int filteredTaskListIndex;
    private final EditTaskDescriptor editTaskDescriptor;
    private final ArrayList<Attribute> attributeToRemove;

    private ReadOnlyTask taskToEdit;
    private Task editedTask;

    // @@author A0138423J
    /**
     * @param filteredTaskListIndex
     *            the index of the task in the filtered task list to edit
     * @param editTaskDescriptor
     *            details to edit the task with
     * @param attributeToRemove
     *            list <Attribute> of attributes to be removed
     */
    public UpdateCommand(int filteredTaskListIndex,
            EditTaskDescriptor editTaskDescriptor,
            ArrayList<Attribute> attributeToRemove) {
        assert filteredTaskListIndex > 0;
        assert editTaskDescriptor != null;
        assert attributeToRemove != null;

        // converts filteredTaskListIndex from one-based to zero-based.
        this.filteredTaskListIndex = filteredTaskListIndex - 1;
        this.editTaskDescriptor = new EditTaskDescriptor(editTaskDescriptor);
        this.attributeToRemove = attributeToRemove;
    }

    // @@author A0138423J
    @Override
    public CommandResult execute() throws CommandException {
        if (filteredTaskListIndex >= model.getTotalSizeOfLists()) {
            throw new CommandException(
                    Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        }

        taskToEdit = UpdateUtil.fetchTaskToEdit(filteredTaskListIndex);

        // create modified task from existing task
        editedTask = UpdateUtil.createEditedTask(taskToEdit, editTaskDescriptor,
                attributeToRemove);

        try {
            model.updateTask(taskToEdit, editedTask);
            model.addUndoCommand(this);

            notifyUI(editedTask);
        } catch (UniqueTaskList.DuplicateTaskException dpe) {
            throw new CommandException(MESSAGE_DUPLICATE_TASK);
        }
        return new CommandResult(
                String.format(MESSAGE_EDIT_TASK_SUCCESS, editedTask));
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
