//@@author A0139996A
package utask.staging.ui.stubs;

import utask.commons.core.EventsCenter;
import utask.logic.commands.Command;
import utask.logic.commands.CommandResult;
import utask.logic.commands.exceptions.CommandException;
import utask.staging.ui.events.FindRequestEvent;

public class SearchCommand extends Command {

    public static final String COMMAND_WORD = "search";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Search a task to uTask.";

    public static final String MESSAGE_SUCCESS = "Searching for %s\n"
                                                + "Press [ESC] to return";

    private final String searchKeywords;

    /**
     * Creates an SearchCommand using raw values.
     */
    public SearchCommand(String searchKeywords) {
        this.searchKeywords = searchKeywords;
    }

    @Override
    public CommandResult execute() throws CommandException {
        EventsCenter.getInstance().post(new FindRequestEvent(searchKeywords));
        return new CommandResult(String.format(MESSAGE_SUCCESS, searchKeywords));
    }

}
