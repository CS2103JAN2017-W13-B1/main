package utask.logic;

import javafx.collections.ObservableList;
import utask.logic.commands.CommandResult;
import utask.logic.commands.exceptions.CommandException;
import utask.model.task.ReadOnlyTask;

/**
 * API of the Logic component
 */
public interface Logic {
    /**
     * Executes the command and returns the result.
     * @param commandText The command as entered by the user.
     * @return the result of the command execution.
     * @throws CommandException If an error occurs during command execution.
     */
    CommandResult execute(String commandText) throws CommandException;

    /** Returns the filtered list of tasks */
    ObservableList<ReadOnlyTask> getFilteredTaskList();

    /** Returns the filtered list of due tasks */
    ObservableList<ReadOnlyTask> getDueFilteredTaskList();

    /** Returns the filtered list of today tasks */
    ObservableList<ReadOnlyTask> getTodayFilteredTaskList();

    /** Returns the filtered list of due tasks */
    ObservableList<ReadOnlyTask> getTomorrowFilteredTaskList();

    /** Returns the filtered list of today tasks */
    ObservableList<ReadOnlyTask> getFutureFilteredTaskList();

    /** Returns the filtered list of today tasks */
    ObservableList<ReadOnlyTask> getFloatingFilteredTaskList();

    void setIfFindOverlayShowing(boolean isShowing);
}
