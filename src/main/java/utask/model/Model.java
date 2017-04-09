package utask.model;

import java.util.List;
import java.util.Set;

import utask.commons.core.UnmodifiableObservableList;
import utask.logic.commands.inteface.ReversibleCommand;
import utask.model.tag.Tag;
import utask.model.tag.UniqueTagList;
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
    public static final String SORT_ORDER_BY_A_TO_Z = "az";
    public static final String SORT_ORDER_BY_Z_TO_A = "za";
    public static final String SORT_ORDER_BY_TAG = "tag";
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

    //@@author A0138423J
    /**
     * Updates the task {@code taskToEdit} with {@code editedTask}.
     *
     * @throws DuplicateTaskException if updating the task's details causes the task to be equivalent to
     *      another existing task in the list.
     * @throws IndexOutOfBoundsException if {@code filteredTaskListIndex} < 0 or >= the size of the filtered list.
     */
    void updateTask(ReadOnlyTask taskToEdit, ReadOnlyTask editedTask)
            throws UniqueTaskList.DuplicateTaskException;

    void addTag(Tag tag) throws UniqueTagList.DuplicateTagException;

    void deleteTag(Tag tag);

    void updateTag(Tag tagToReplace, Tag updatedTag) throws UniqueTagList.DuplicateTagException;

    List<Tag> getTags();

    //@@author

    /** Returns the filtered task list as an {@code UnmodifiableObservableList<ReadOnlyTask>} */
    UnmodifiableObservableList<ReadOnlyTask> getFilteredTaskList();

    /** Returns the filtered list of due tasks */
    UnmodifiableObservableList<ReadOnlyTask> getDueFilteredTaskList();

    /** Returns the filtered list of today tasks */
    UnmodifiableObservableList<ReadOnlyTask> getTodayFilteredTaskList();

    /** Returns the filtered list of tomorrow tasks */
    UnmodifiableObservableList<ReadOnlyTask> getTomorrowFilteredTaskList();

    /** Returns the filtered list of future tasks */
    UnmodifiableObservableList<ReadOnlyTask> getFutureFilteredTaskList();

    /** Returns the filtered list of floating tasks */
    UnmodifiableObservableList<ReadOnlyTask> getFloatingFilteredTaskList();

    /** Updates the filter of the filtered task list to show all tasks */
    void updateFilteredListToShowAll();

    /** Updates the filter of the filtered task list to filter by the given keywords*/
    void updateFilteredTaskList(Set<String> keywords);

    /** Updates the filter of the filtered task list to filter by the given keywords*/
    void updateFilteredTaskListByKeywords(String keywords);

    /** Updates the filter of the sorted task list to sort by the given keywords*/
    void sortFilteredTaskList(String keywords);

    /** Gets total size of tasks in underlying lists of listviews*/
    int getTotalSizeOfLists();

    /** Adds last executed command to reversible stack */
    void addUndoCommand(ReversibleCommand undoCommand);

    /** Adds last executed command to reversible stack */
    ReversibleCommand getUndoCommand();

    /** Gets size of reversible commands in reversible stack*/
    int getUndoCommandCount();

    /** Adds last executed command to reversible stack */
    void addRedoCommand(ReversibleCommand redoCommand);

    /** Adds last executed command to reversible stack */
    ReversibleCommand getRedoCommand();

    /** Gets size of reversible commands in reversible stack*/
    int getRedoCommandCount();

    /** Gets list of interest from display index */
    List<ReadOnlyTask> getUnderlyingListByIndex(int displayIndex);

    /** Gets index with respect to list of interest from display index */
    int getActualIndexFromDisplayIndex(int displayIndex);

    /* Exposes isfindOverlay to Command through Model*/
    void setIfFindOverlayShowing(boolean isShowing);

    /* Exposes isfindOverlay to Command through Model*/
    boolean isFindOverlayShowing();

    /* Exposes UserPrefs through Model*/
    UserPrefs getUserPrefs();

}
