package utask.logic.commands;

import java.util.Set;

import utask.logic.commands.exceptions.CommandException;

public class SortCommand extends Command {

    public static final String COMMAND_WORD = "sort";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": List all tasks/events with filters. "
            + "Parameters: sort SORTING_ORDER\n"
            + "Example: " + COMMAND_WORD
            + " completed tasks, tag \"Important\", sort earliest first";

    private final Set<String> keywords;

    public SortCommand(Set<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public CommandResult execute() throws CommandException {
        model.updateFilteredTaskList(keywords);
        return new CommandResult(getMessageForTaskListShownSummary(model.getFilteredTaskList().size()));
    }
}
