//@@author A0138493W
package utask.logic.commands;

import java.util.Set;

import utask.commons.core.EventsCenter;
import utask.staging.ui.events.FindRequestEvent;

/**
 * Finds and lists all tasks in uTask who contains any of the argument keywords.
 * Keyword matching is case sensitive.
 */
public class FindCommand extends Command {

    public static final String COMMAND_WORD = "find";
    public static final String COMMAND_FORMAT = "KEYWORD [MORE_KEYWORDS]...";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all tasks that contain any of "
            + "the specified keywords (case-sensitive) and displays them as a list with index numbers.\n"
            + "Parameters: " + COMMAND_FORMAT + "\n"
            + "Example: " + COMMAND_WORD + " Monday"
            + "Press [ESC] to return";

    private final Set<String> keywords;

    public FindCommand(Set<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public CommandResult execute() {
        model.updateFilteredTaskList(keywords);
        EventsCenter.getInstance().post(new FindRequestEvent(""));
        return new CommandResult(getMessageForTaskListShownSummary(model.getFilteredTaskList().size()));
    }

}
