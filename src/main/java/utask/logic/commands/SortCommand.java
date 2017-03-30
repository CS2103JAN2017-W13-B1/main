//@@author A0138493W

package utask.logic.commands;

import utask.logic.commands.exceptions.CommandException;

/**
 * Sorts last displayed list from the uTask.
 */
public class SortCommand extends Command {

    public static final String COMMAND_WORD = "sort";
    public static final String COMMAND_FORMAT = "[SORTING_ORDER]";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Sort tasks list with filters. "
            + "Parameters: " + COMMAND_FORMAT + "\n"
            + "Example: " + COMMAND_WORD + " earliest";

    public static final String MESSAGE_SUCCESS = "Tasks list has been sorted";

    private final String keywords;

    public SortCommand(String keywords) {
        this.keywords = keywords;
    }

    @Override
    public CommandResult execute() throws CommandException {
        model.sortFilteredTaskList(keywords);
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
