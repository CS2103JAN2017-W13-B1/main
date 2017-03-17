package utask.model;


import javafx.collections.ObservableList;
import utask.model.tag.Tag;
import utask.model.task.ReadOnlyTask;

/**
 * Unmodifiable view of a UTask
 */
public interface ReadOnlyUTask {

    /**
     * Returns an unmodifiable view of the task list.
     * This list will not contain any duplicate tasks.
     */
    ObservableList<ReadOnlyTask> getTaskList();

    /**
     * Returns an unmodifiable view of the tags list.
     * This list will not contain any duplicate tags.
     */
    ObservableList<Tag> getTagList();

}
