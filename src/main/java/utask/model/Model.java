package utask.model;

import java.util.Set;

import utask.commons.core.UnmodifiableObservableList;
import utask.model.task.ReadOnlyTask;
import utask.model.task.Task;
import utask.model.task.UniqueTaskList;
import utask.model.task.UniqueTaskList.DuplicateTaskException;

/**
 * The API of the Model component.
 */
public interface Model {
    //@@author A0138493W
    public static final String SORT_ORDER_BY_EARLIEST_FIRST = "earliest";
    public static final String SORT_ORDER_BY_LATEST_FIRST = "latest";
    public static final String SORT_ORDER_DEFAULT = SORT_ORDER_BY_EARLIEST_FIRST;
    public static final String SORT_ORDER_ERROR = "Unable to sort due to unrecognized sorting order ";

    /** Clears existing backing model and replaces with the provided new data. */
    void resetData(ReadOnlyUTask newData);

    /** Returns the UTask */
    ReadOnlyUTask getUTask();

    /** Deletes the given task. */
    void deleteTask(ReadOnlyTask target) throws UniqueTaskList.TaskNotFoundException;

    /** Adds the given task */
    void addTask(Task task) throws UniqueTaskList.DuplicateTaskException;

    /**
     * Updates the task located at {@code filteredTaskListIndex} with {@code editedTask}.
     *
     * @throws DuplicateTaskException if updating the task's details causes the task to be equivalent to
     *      another existing task in the list.
     * @throws IndexOutOfBoundsException if {@code filteredTaskListIndex} < 0 or >= the size of the filtered list.
     */
    void updateTask(int filteredTaskListIndex, ReadOnlyTask editedTask)
            throws UniqueTaskList.DuplicateTaskException;

    /** Returns the filtered task list as an {@code UnmodifiableObservableList<ReadOnlyTask>} */
    UnmodifiableObservableList<ReadOnlyTask> getFilteredTaskList();

    /** Updates the filter of the filtered task list to show all tasks */
    void updateFilteredListToShowAll();

    /** Updates the filter of the filtered task list to filter by the given keywords*/
    void updateFilteredTaskList(Set<String> keywords);

    /** Updates the filter of the sorted task list to sort by the given keywords*/
    void sortFilteredTaskList(String keywords);

}
