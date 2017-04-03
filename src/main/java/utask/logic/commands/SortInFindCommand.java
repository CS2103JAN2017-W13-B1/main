//@@author A0139996A

package utask.logic.commands;

import utask.logic.commands.exceptions.CommandException;

/**
 * Sorts in find overlay
 */
public class SortInFindCommand extends Command {

    //TODO
    //Similar w jiahao
    public static final String COMMAND_WORD = "sort";
    public static final String MESSAGE_SUCCESS = "Tasks list has been sorted";

    public static final String COMMAND_FORMAT = "COLUMN_ALPHABET [ ASC | DSC ]";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Sort tasks. "
            + "Parameters: " + COMMAND_FORMAT + "\n";

    private final String keywords;

    public SortInFindCommand(String keywords) {
        this.keywords = keywords;
    }

    @Override
    public CommandResult execute() throws CommandException {
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
