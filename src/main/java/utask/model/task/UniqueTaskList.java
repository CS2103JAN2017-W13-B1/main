package utask.model.task;

import java.util.Iterator;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utask.commons.core.UnmodifiableObservableList;
import utask.commons.exceptions.DuplicateDataException;
import utask.commons.util.CollectionUtil;
import utask.model.tag.Tag;
import utask.model.tag.UniqueTagList;

/**
 * A list of tasks that enforces uniqueness between its elements and does not
 * allow nulls.
 *
 * Supports a minimal set of list operations.
 *
 * @see Task#equals(Object)
 * @see CollectionUtil#elementsAreUnique(Collection)
 */
public class UniqueTaskList implements Iterable<Task> {

    private final ObservableList<Task> internalList = FXCollections
            .observableArrayList();

    /**
     * Returns true if the list contains an equivalent task as the given
     * argument.
     */
    public boolean contains(ReadOnlyTask toCheck) {
        assert toCheck != null;
        return internalList.contains(toCheck);
    }

    /**
     * Adds a task to the list.
     *
     * @throws DuplicateTaskException
     *             if the task to add is a duplicate of an existing task in the
     *             list.
     */
    public void add(Task toAdd) throws DuplicateTaskException {
        assert toAdd != null;
        if (contains(toAdd)) {
            throw new DuplicateTaskException();
        }

        internalList.add(toAdd);
    }

    public ObservableList<Task> getInternalList() {
        return internalList;
    }

    // @@author A0138423J
    /**
     * Updates the task in the list held by {@code taskToUpdate} with
     * {@code editedTask}.
     *
     * @throws DuplicateTaskException
     *             if updating the task's details causes the task to be
     *             equivalent to another existing task in the list.
     * @throws IndexOutOfBoundsException
     *             if {@code index} < 0 or >= the size of the list.
     */
    public void updateTask(ReadOnlyTask readOnlyTaskToUpdate,
            ReadOnlyTask readOnlyEditedTask) throws DuplicateTaskException {
        assert readOnlyTaskToUpdate != null;
        assert readOnlyEditedTask != null;

        // fetch index of task to update
        int index = internalList.indexOf(readOnlyTaskToUpdate);

        // casting ReadOnlyTask to Task
        Task taskToUpdate = (Task) readOnlyTaskToUpdate;
        Task editedTask = (Task) readOnlyEditedTask;

        // if not same type of task and editedTask already in internalList
        // throw DuplicateTaskException()
        if (!taskToUpdate.equals(editedTask)
                && internalList.contains(editedTask)) {
            throw new DuplicateTaskException();
        }

        // replace original task with modified task
        internalList.set(index, (Task) editedTask);
    }

    public void updateTaskWithUpdatedTag (Tag tagToReplace, Tag updatedTag) {
        for (Task x : internalList) {
            for (Tag t : x.getTags()) {
                if (t.equals(tagToReplace)) {
                    t.setTag(updatedTag);
                }
            }
        }
    }

    public void deleteTaskWithDeletedTag (Tag tagToDelete) {
        for (Task x : internalList) {
            UniqueTagList tempList = x.getTags();
            for (Tag t : x.getTags()) {
                if (t.equals(tagToDelete)) {
                    tempList.delete(t);
                }
            }
            x.setTags(tempList);
        }
    }
    // @@author

    /**
     * Removes the equivalent task from the list.
     *
     * @throws TaskNotFoundException
     *             if no such task could be found in the list.
     */
    public boolean remove(ReadOnlyTask toRemove) throws TaskNotFoundException {
        assert toRemove != null;
        final boolean taskFoundAndDeleted = internalList.remove(toRemove);
        if (!taskFoundAndDeleted) {
            throw new TaskNotFoundException();
        }
        return taskFoundAndDeleted;
    }

    public void setTasks(UniqueTaskList replacement) {
        this.internalList.setAll(replacement.internalList);
    }

    public void setTasks(List<? extends ReadOnlyTask> tasks)
            throws DuplicateTaskException {
        final UniqueTaskList replacement = new UniqueTaskList();
        for (final ReadOnlyTask task : tasks) {
            replacement.add(new EventTask(task));
        }
        setTasks(replacement);
    }

    public UnmodifiableObservableList<Task> asObservableList() {
        return new UnmodifiableObservableList<>(internalList);
    }

    @Override
    public Iterator<Task> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof UniqueTaskList // instanceof handles nulls
                        && this.internalList
                                .equals(((UniqueTaskList) other).internalList));
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }

    /**
     * Signals that an operation would have violated the 'no duplicates'
     * property of the list.
     */
    public static class DuplicateTaskException extends DuplicateDataException {
        protected DuplicateTaskException() {
            super("Operation would result in duplicate tasks");
        }
    }

    /**
     * Signals that an operation targeting a specified task in the list would
     * fail because there is no such matching task in the list.
     */
    public static class TaskNotFoundException extends Exception {
    }

}
