package utask.logic.commands;

import utask.commons.core.EventsCenter;
import utask.commons.core.Messages;
import utask.commons.events.ui.ShowTaskOfInterestInFindTaskOverlayEvent;
import utask.commons.events.ui.ShowTaskOfInterestInMainWindowEvent;
import utask.logic.commands.exceptions.CommandException;
import utask.model.Model;
import utask.model.task.ReadOnlyTask;

/**
 * Represents a command with hidden internal logic and the ability to be executed.
 */
public abstract class Command {
    protected Model model;

    /**
     * Constructs a feedback message to summarise an operation that displayed a listing of persons.
     *
     * @param displaySize used to generate summary
     * @return summary message for persons displayed
     */
    public static String getMessageForTaskListShownSummary(int displaySize) {
        return String.format(Messages.MESSAGE_TASKS_LISTED_OVERVIEW, displaySize);
    }

    /**
     * Executes the command and returns the result message.
     *
     * @return feedback message of the operation result for display
     * @throws CommandException If an error occurs during command execution.
     */
    public abstract CommandResult execute() throws CommandException;

    /**
     * Provides any needed dependencies to the command.
     * Commands making use of any of these should override this method to gain
     * access to the dependencies.
     */
    public void setData(Model model) {
        this.model = model;
    }

    //@@author A0139996A
    /**
     * Provides a way to notify UI to selected task of interest
     */
    protected void notifyUI(ReadOnlyTask task) {
        assert task != null : "Incorrect usage: Task should not be null";

        if (model.isFindOverlayShowing()) {
            EventsCenter.getInstance().post(new ShowTaskOfInterestInFindTaskOverlayEvent(task));
        } else {
            EventsCenter.getInstance().post(new ShowTaskOfInterestInMainWindowEvent(task));
        }
    }
    //@@author
}
