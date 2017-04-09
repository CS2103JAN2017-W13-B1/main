//@@author A0139996A
package utask.logic.commands;

import utask.commons.core.EventsCenter;
import utask.commons.events.ui.UpdateSortInFindOverlayEvent;
import utask.logic.commands.exceptions.CommandException;

/**
 * Sorts in find overlay
 */
public class SortInFindCommand extends Command {

    //TODO: Very similar with Jiahao general sort
    public static final String COMMAND_WORD = "sort";
    public static final String MESSAGE_SUCCESS = "Tasks list has been sorted";

    public static final String COMMAND_FORMAT = "COLUMN_ALPHABET [ ASC | DSC ]";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Sort tasks. "
            + "Parameters: " + COMMAND_FORMAT + "\n";

    private final String column;
    private final String orderBy;

    public SortInFindCommand(String column, String orderBy) {
        this.column = column;
        this.orderBy = orderBy;
    }

    @Override
    public CommandResult execute() throws CommandException {
        EventsCenter.getInstance().post(new UpdateSortInFindOverlayEvent(column, orderBy));
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
