//@@author A0138493W

package utask.logic.commands;

import utask.logic.commands.exceptions.CommandException;

public class SortCommand extends Command {

    public static final String COMMAND_WORD = "sort";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Sort all tasks/events with filters. "
            + "Parameters: SORTING_ORDER\n"
            + "Example: " + COMMAND_WORD + " earliest";

    public static final String MESSAGE_SUCCESS = "Sorted all tasks by ";

    private final String keywords;

    public SortCommand(String keywords) {
        this.keywords = keywords;
    }

    @Override
    public CommandResult execute() throws CommandException {
        model.updateFilteredTaskList(keywords);
        return new CommandResult(MESSAGE_SUCCESS + keywords);
    }
}
