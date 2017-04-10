package utask.logic.commands;

import java.util.Map;

import utask.commons.core.EventsCenter;
import utask.commons.events.ui.ShowAliasDialogEvent;
import utask.model.AliasCommandMap;

//@@author A0139996A
/**
 * Shows alias in a dialog in the uTask to the user.
 */
public class ListAliasCommand extends Command {
    public static final String COMMAND_WORD = "listalias";
    public static final String COMMAND_FORMAT = "";
    public static final String MESSAGE_SUCCESS = "Showing all alias";

    @Override
    public CommandResult execute() {
        Map<String, String> map = AliasCommandMap.getInstance().getAliasMap();
        EventsCenter.getInstance().post(new ShowAliasDialogEvent(map));
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
