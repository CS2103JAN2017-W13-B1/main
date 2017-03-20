package utask.staging.ui.stubs;

import seedu.address.commons.core.EventsCenter;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import utask.staging.ui.events.SearchRequestEvent;

public class SearchCommand extends Command {

    public static final String COMMAND_WORD = "search";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Search a task to uTask.";

    public static final String MESSAGE_SUCCESS = "Task(s) found";

    private final String searchKeywords;

    /**
     * Creates an SearchCommand using raw values.
     */
    public SearchCommand(String searchKeywords) {
        this.searchKeywords = searchKeywords;
    }

    @Override
    public CommandResult execute() throws CommandException {
        EventsCenter.getInstance().post(new SearchRequestEvent(searchKeywords));
        return new CommandResult(MESSAGE_SUCCESS);
    }

}
