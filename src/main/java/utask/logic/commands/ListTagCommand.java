package utask.logic.commands;

import utask.commons.core.EventsCenter;
import utask.commons.events.ui.ShowTagColorDialogEvent;

/**
 * Shows tag in a dialog in the uTask to the user.
 */
public class ListTagCommand extends Command {
    public static final String COMMAND_WORD = "listtag";
    public static final String COMMAND_FORMAT = "";
    public static final String MESSAGE_SUCCESS = "Showing all tags";

    @Override
    public CommandResult execute() {
        EventsCenter.getInstance().post(new ShowTagColorDialogEvent(model.getTags()));
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
