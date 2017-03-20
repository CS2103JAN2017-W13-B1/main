//@@author A0138493W

package utask.logic.commands;

import utask.commons.core.UnmodifiableObservableList;
import utask.logic.commands.exceptions.CommandException;
import utask.model.task.ReadOnlyTask;

/**
 * Sorts last displayed list from the uTask.
 */
public class SortCommand extends Command {

    public static final String COMMAND_WORD = "sort";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Sort last displayed tasks list with filters. "
            + "Parameters: [SORTING_ORDER]\n"
            + "Example: " + COMMAND_WORD + " earliest";

    public static final String MESSAGE_SUCCESS = "Last displayed tasks list has been sorted";

    private final String keywords;

    public SortCommand(String keywords) {
        this.keywords = keywords;
    }

    @Override
    public CommandResult execute() throws CommandException {
        UnmodifiableObservableList<ReadOnlyTask> lastShownList = model.getFilteredTaskList();

        model.sortFilteredTaskList(lastShownList, keywords);
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
