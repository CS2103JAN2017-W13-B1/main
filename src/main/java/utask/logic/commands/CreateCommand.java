package utask.logic.commands;

import java.util.HashSet;
import java.util.Set;

import utask.commons.exceptions.IllegalValueException;
import utask.logic.commands.exceptions.CommandException;
import utask.logic.commands.inteface.ReversibleCommand;
import utask.model.tag.Tag;
import utask.model.tag.TagColorIndex;
import utask.model.tag.TagName;
import utask.model.task.Frequency;
import utask.model.task.Status;
import utask.model.task.Task;
import utask.model.task.UniqueTaskList;
import utask.model.task.UniqueTaskList.DuplicateTaskException;
import utask.model.task.UniqueTaskList.TaskNotFoundException;
import utask.ui.helper.DelayedExecution;

/**
 * Creates a new task to uTask.
 */
public abstract class CreateCommand extends Command implements ReversibleCommand {

    public static final String COMMAND_WORD = "create";

    public static final String COMMAND_FORMAT = "NAME  [/by DEADLINE] [/from START_TIME to END_TIME]"
            + " [/repeat FREQUENCY] [/tag TAG]...";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Creates a task to uTask. "
            + "Parameters: " + COMMAND_FORMAT + "\n"
            + "Example: " + COMMAND_WORD
            + " read essay /by 200217 /from 1830 to 2030 /repeat Every Monday /tag urgent /tag assignment";

    public static final String MESSAGE_SUCCESS = "New task created: %1$s";
    public static final String MESSAGE_DUPLICATE_TASK = "This task already exists in uTask";

    protected Task toAdd;
    protected final Frequency frequency;
    protected final Set<Tag> tagSet;
    protected final Status status;

    /**
     * Creates an CreateCommand using raw values.
     *
     * @throws IllegalValueException if any of the raw values are invalid
     */
    public CreateCommand(String frequency, Set<String> tags, String status)
            throws IllegalValueException {
        tagSet = new HashSet<>();
        for (String tagName : tags) {
            tagSet.add(new Tag(new TagName(tagName), new TagColorIndex("")));
        }

        if ("".equals(frequency)) {
            this.frequency = Frequency.getEmptyFrequency();
        } else {
            this.frequency = new Frequency(frequency);
        }
        if ("".equals(status)) {
            this.status = Status.getEmptyStatus();
        } else {
            this.status = new Status(status);
        }
    }

    @Override
    public CommandResult execute() throws CommandException {
        assert model != null;
        try {
            model.addTask(toAdd);
            model.addUndoCommand(this);

            notifyUI(toAdd);

            return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
        } catch (UniqueTaskList.DuplicateTaskException e) {
            throw new CommandException(MESSAGE_DUPLICATE_TASK);
        }
    }

    //@@author A0139996A
    public void undo() throws Exception {
        notifyUI(toAdd);

        //Access to deleteTask() must be delayed to prevent race condition with notifyUI
        new DelayedExecution((e)-> {
            try {
                model.deleteTask(toAdd);
            } catch (TaskNotFoundException pnfe) {
                //TODO: Display error using another async channel
                assert false : "The target task cannot be missing";
            }
        }).run();
    }

    public void redo() throws DuplicateTaskException {
        model.addTask(toAdd);
        notifyUI(toAdd);
    }
    //@@author
}
