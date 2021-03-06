package utask.logic.commands;

import utask.commons.core.EventsCenter;
import utask.commons.core.Messages;
import utask.commons.events.ui.JumpToListInFindTaskOverlayEvent;
import utask.commons.events.ui.JumpToListRequestEvent;
import utask.logic.commands.exceptions.CommandException;

/**
 * Selects a task identified using it's last displayed index from the address book.
 */
public class SelectCommand extends Command {

    public final int targetIndex;

    public static final String COMMAND_WORD = "select";
    public static final String COMMAND_FORMAT = "INDEX (must be a positive integer)";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Selects the task identified by the index number used in the last task listing.\n"
            + "Parameters: " + COMMAND_FORMAT + "\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_SELECT_TASK_SUCCESS = "Selected Task: %1$s";

    public SelectCommand(int targetIndex) {
        this.targetIndex = targetIndex;
    }

    //@@author A0139996A
    @Override
    public CommandResult execute() throws CommandException {
        if (model.getTotalSizeOfLists() < targetIndex) {
            throw new CommandException(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        }

        if (model.isFindOverlayShowing()) {
            EventsCenter.getInstance().post(new JumpToListInFindTaskOverlayEvent(targetIndex - 1));
        } else {
            EventsCenter.getInstance().post(new JumpToListRequestEvent(targetIndex - 1));
        }

        return new CommandResult(String.format(MESSAGE_SELECT_TASK_SUCCESS, targetIndex));
    }

}
