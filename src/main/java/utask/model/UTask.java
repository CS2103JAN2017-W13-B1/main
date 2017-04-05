package utask.model;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utask.commons.core.EventsCenter;
import utask.commons.core.UnmodifiableObservableList;
import utask.commons.events.ui.UIShowTagColorDialogEvent;
import utask.model.tag.Tag;
import utask.model.tag.UniqueTagList;
import utask.model.task.ReadOnlyTask;
import utask.model.task.Task;
import utask.model.task.UniqueTaskList;
import utask.model.task.UniqueTaskList.DuplicateTaskException;

/**
 * Wraps all data at the utask level
 * Duplicates are not allowed (by .equals comparison)
 */
public class UTask implements ReadOnlyUTask {

    private final UniqueTaskList tasks;
    private final UniqueTagList tags;

    /*
     * The 'unusual' code block below is an non-static initialization block, sometimes used to avoid duplication
     * between constructors. See https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     *
     * Note that non-static init blocks are not recommended to use. There are other ways to avoid duplication
     *   among constructors.
     */
    {
        tasks = new UniqueTaskList();
        tags = new UniqueTagList();
    }

    public UTask() {}

    /**
     * Creates a task using the Task and Tags in the {@code toBeCopied}
     */
    public UTask(ReadOnlyUTask toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    //@@author A0138493W
    /**
     * Sort task by comparator
     */
    public void sortByComparator(Comparator<ReadOnlyTask> comparator) {
        assert comparator != null;
        FXCollections.sort(tasks.getInternalList(), comparator);
    }

    //@@author

//// list overwrite operations

    public void setTasks(List<? extends ReadOnlyTask> tasks)
            throws UniqueTaskList.DuplicateTaskException {
        this.tasks.setTasks(tasks);
    }

    public void setTags(Collection<Tag> tags) throws UniqueTagList.DuplicateTagException {
        this.tags.setTags(tags);
    }

    public void resetData(ReadOnlyUTask newData) {
        assert newData != null;
        try {
            setTasks(newData.getTaskList());
        } catch (UniqueTaskList.DuplicateTaskException e) {
            assert false : "UTask should not have duplicate tasks";
        }
        try {
            setTags(newData.getTagList());
        } catch (UniqueTagList.DuplicateTagException e) {
            assert false : "UTask should not have duplicate tags";
        }
        syncMasterTagListWith(tasks);
    }

//// task-level operations

    /**
     * Adds a task to UTask.
     * Also checks the new task's tags and updates {@link #tags} with any new tags found,
     * and updates the Tag objects in the task to point to those in {@link #tags}.
     *
     * @throws UniqueTaskList.DuplicateTaskException if an equivalent task already exists.
     */
    public void addTask(Task p) throws UniqueTaskList.DuplicateTaskException {
        syncMasterTagListWith(p);
        tasks.add(p);
    }

    //@@author A0138423J
    /**
     * Updates the original task in the list {@code taskToEdit} with {@code editedReadOnlyTask}.
     * {@code UTask}'s tag list will be updated with the tags of {@code editedReadOnlyTask}.
     * @see #syncMasterTagListWith(Task)
     *
     * @throws DuplicateTaskException if updating the task's details causes the task to be equivalent to
     *      another existing task in the list.
     * @throws IndexOutOfBoundsException if {@code index} < 0 or >= the size of the list.
     */
    public void updateTask(ReadOnlyTask taskToEdit, ReadOnlyTask editedReadOnlyTask)
            throws UniqueTaskList.DuplicateTaskException {
        assert taskToEdit != null;
        assert editedReadOnlyTask != null;

        syncMasterTagListWith((Task) editedReadOnlyTask);
        tasks.updateTask(taskToEdit, editedReadOnlyTask);
    }

    public void updateTask(int taskToEdit, ReadOnlyTask editedReadOnlyTask)
            throws UniqueTaskList.DuplicateTaskException {
        assert taskToEdit >= 0;
        assert editedReadOnlyTask != null;

        syncMasterTagListWith((Task) editedReadOnlyTask);
        tasks.updateTask(taskToEdit, editedReadOnlyTask);
    }
    //@@author

    /**
     * Ensures that every tag in this task:
     *  - exists in the master list {@link #tags}
     *  - points to a Tag object in the master list
     */
    private void syncMasterTagListWith(Task tempTask) {
        final UniqueTagList taskTags = tempTask.getTags();
        tags.mergeFrom(taskTags);

        // Create map with values = tag object references in the master list
        // used for checking task tag references
        final Map<Tag, Tag> masterTagObjects = new HashMap<>();
        tags.forEach(tag -> masterTagObjects.put(tag, tag));

        // Rebuild the list of task tags to point to the relevant tags in the master tag list.
        final Set<Tag> correctTagReferences = new HashSet<>();
        taskTags.forEach(tag -> correctTagReferences.add(masterTagObjects.get(tag)));
        tempTask.setTags(new UniqueTagList(correctTagReferences));
    }

    /**
     * Ensures that every tag in these tasks:
     *  - exists in the master list {@link #tags}
     *  - points to a Tag object in the master list
     *  @see #syncMasterTagListWith(Task)
     */
    private void syncMasterTagListWith(UniqueTaskList tasks) {
        tasks.forEach(this::syncMasterTagListWith);
    }

    public boolean removeTask(ReadOnlyTask key) throws UniqueTaskList.TaskNotFoundException {
        if (tasks.remove(key)) {
            return true;
        } else {
            throw new UniqueTaskList.TaskNotFoundException();
        }
    }

//// tag-level operations
    // @@ author A0138423J
    public void addTag(Tag t) throws UniqueTagList.DuplicateTagException {
        tags.add(t);
        EventsCenter.getInstance().post(new UIShowTagColorDialogEvent(tags.getAsListTag()));
    }

    public void deleteTag(Tag t) {
        tags.delete(t);
        EventsCenter.getInstance().post(new UIShowTagColorDialogEvent(tags.getAsListTag()));
    }

    public void updateTag(Tag tagToReplace, Tag updatedTag) {
        tags.update(tagToReplace, updatedTag);
        EventsCenter.getInstance().post(new UIShowTagColorDialogEvent(tags.getAsListTag()));
    }

    // @@ author

//// util methods

    @Override
    public String toString() {
        return tasks.asObservableList().size() + " tasks, " + tags.asObservableList().size() +  " tags";
        // TODO: refine later
    }

    @Override
    public ObservableList<ReadOnlyTask> getTaskList() {
        return new UnmodifiableObservableList<>(tasks.asObservableList());
    }

    @Override
    public ObservableList<Tag> getTagList() {
        return new UnmodifiableObservableList<>(tags.asObservableList());
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof UTask // instanceof handles nulls
                && this.tasks.equals(((UTask) other).tasks)
                && this.tags.equalsOrderInsensitive(((UTask) other).tags));
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(tasks, tags);
    }
}
