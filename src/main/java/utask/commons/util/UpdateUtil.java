package utask.commons.util;

import java.util.ArrayList;
import java.util.List;

import utask.commons.exceptions.IllegalValueException;
import utask.model.tag.UniqueTagList;
import utask.model.task.Attribute;
import utask.model.task.Deadline;
import utask.model.task.DeadlineTask;
import utask.model.task.EditTaskDescriptor;
import utask.model.task.EventTask;
import utask.model.task.FloatingTask;
import utask.model.task.Frequency;
import utask.model.task.Name;
import utask.model.task.ReadOnlyTask;
import utask.model.task.Status;
import utask.model.task.Task;
import utask.model.task.TaskType;
import utask.model.task.Timestamp;
import utask.ui.helper.FilteredListHelper;

// @@author A0138423J
public class UpdateUtil {
    public static final String TO_BE_REMOVED = "-";

    public static ReadOnlyTask fetchTaskToEdit(int index) {
        assert index >= 0;
        List<ReadOnlyTask> lastShownList = FilteredListHelper.getInstance()
                .getUnderlyingListByIndex(index);
        int actualInt = FilteredListHelper.getInstance()
                .getActualIndexFromDisplayIndex(index);

        return lastShownList.get(actualInt);
    }

    /**
     * Checks {@code updatedDeadline} and {@code updatedTimestamp} to see
     * whether both are empty or not. Subsequently, based on the statuses, this
     * method will determine the type of editedTask. Types are pre-set based on
     * Enum {@code TaskType}
     */
    public static TaskType typeOfEditedTask(Deadline updatedDeadline,
            Timestamp updatedTimestamp) {
        assert updatedDeadline != null;
        assert updatedTimestamp != null;

        Boolean isDeadlineEmpty = false;
        if (updatedDeadline.equals(Deadline.getEmptyDeadline())) {
            isDeadlineEmpty = true;
        }
        Boolean isTimestampEmpty = false;
        if (updatedTimestamp.equals(Timestamp.getEmptyTimestamp())) {
            isTimestampEmpty = true;
        }
        if (!isDeadlineEmpty && !isTimestampEmpty) {
            return TaskType.EVENT;
        } else if (!isDeadlineEmpty && isTimestampEmpty) {
            return TaskType.DEADLINE;
        } else if (isDeadlineEmpty && isTimestampEmpty) {
            return TaskType.FLOATING;
        }
        return TaskType.UNKNOWN;
    }

    /**
     * Creates and returns a {@code Task} with the details of {@code taskToEdit}
     * edited with {@code editTaskDescriptor}.
     */
    public static Task createEditedTask(ReadOnlyTask taskToEdit, String value)
            throws IllegalValueException {
        assert taskToEdit != null;
        assert value != null;

        Task placeholder = null;
        switch (typeOfEditedTask(taskToEdit.getDeadline(),
                taskToEdit.getTimestamp())) {
        case FLOATING:
            placeholder = new FloatingTask(taskToEdit.getName(),
                    taskToEdit.getFrequency(), taskToEdit.getTags(),
                    new Status(value.toString()));
            break;
        case DEADLINE:
            placeholder = new DeadlineTask(taskToEdit.getName(),
                    taskToEdit.getDeadline(), taskToEdit.getFrequency(),
                    taskToEdit.getTags(), new Status(value));
            break;
        case EVENT:
            placeholder = new EventTask(taskToEdit.getName(),
                    taskToEdit.getDeadline(), taskToEdit.getTimestamp(),
                    taskToEdit.getFrequency(), taskToEdit.getTags(),
                    new Status(value.toString()));
            break;
        default:
            assert false : "Should never come to this default";
        }
        return placeholder;
    }

    /**
     * Creates and returns a {@code Task} with the details of {@code taskToEdit}
     * edited with {@code editTaskDescriptor}.
     */
    public static Task createEditedTask(ReadOnlyTask taskToEdit,
            EditTaskDescriptor editTaskDescriptor,
            ArrayList<Attribute> attributeToRemove) {
        assert taskToEdit != null;
        assert editTaskDescriptor != null;
        assert attributeToRemove != null;

        Name updatedName = editTaskDescriptor.getName()
                .orElseGet(taskToEdit::getName);
        Status updatedIsCompleted = editTaskDescriptor.getStatus()
                .orElseGet(taskToEdit::getStatus);
        Deadline updatedDeadline = updateOrRemoveDeadline(taskToEdit,
                editTaskDescriptor, attributeToRemove);
        Timestamp updatedTimestamp = updateOrRemoveTimestamp(taskToEdit,
                editTaskDescriptor, attributeToRemove, updatedDeadline);
        Frequency updatedFrequency = updateOrRemoveFrequency(taskToEdit,
                editTaskDescriptor, attributeToRemove);
        UniqueTagList updatedTags = updateOrRemoveUniqueTagList(taskToEdit,
                editTaskDescriptor, attributeToRemove);

        Task placeholder = null;
        switch (typeOfEditedTask(updatedDeadline, updatedTimestamp)) {
        case FLOATING:
            placeholder = new FloatingTask(updatedName, updatedFrequency,
                    updatedTags, updatedIsCompleted);
            break;
        case DEADLINE:
            placeholder = new DeadlineTask(updatedName, updatedDeadline,
                    updatedFrequency, updatedTags, updatedIsCompleted);
            break;
        case EVENT:
            placeholder = new EventTask(updatedName, updatedDeadline,
                    updatedTimestamp, updatedFrequency, updatedTags,
                    updatedIsCompleted);
            break;
        default:
            assert false : "Should never come to this default";
        }
        return placeholder;
    }

    /**
     * Creates and returns a {@code Deadline} with the details of
     * {@code taskToEdit} edited with {@code editTaskDescriptor}. Subsequently,
     * checks to see if there is any need to remove Deadline field based on
     * {@code attributeToRemove}
     */
    private static Deadline updateOrRemoveDeadline(ReadOnlyTask taskToEdit,
            EditTaskDescriptor editTaskDescriptor,
            ArrayList<Attribute> attributeToRemove) {
        assert taskToEdit != null;
        assert editTaskDescriptor != null;
        assert attributeToRemove != null;

        Deadline updatedDeadline = editTaskDescriptor.getDeadline()
                .orElseGet(taskToEdit::getDeadline);
        if (attributeToRemove.contains(Attribute.DEADLINE)) {
            updatedDeadline = Deadline.getEmptyDeadline();
        }
        return updatedDeadline;
    }

    /**
     * Creates and returns a {@code Timestamp} with the details of
     * {@code taskToEdit} edited with {@code editTaskDescriptor}. Subsequently,
     * checks to see if there is any need to remove Timestamp field based on
     * {@code attributeToRemove}
     */
    private static Timestamp updateOrRemoveTimestamp(ReadOnlyTask taskToEdit,
            EditTaskDescriptor editTaskDescriptor,
            ArrayList<Attribute> attributeToRemove,
            Deadline updatedDeadline) {
        assert taskToEdit != null;
        assert editTaskDescriptor != null;
        assert attributeToRemove != null;

        Timestamp timestampToUpdate = editTaskDescriptor.getTimeStamp()
              .orElseGet(taskToEdit::getTimestamp);

        if (!timestampToUpdate.equals(taskToEdit.getTimestamp())
                && !attributeToRemove.contains(Attribute.TIMESTAMP)) {
            timestampToUpdate = Timestamp
                    .getUpdateTimestampWithDeadline(timestampToUpdate,
                            updatedDeadline);
        }

        if (attributeToRemove.contains(Attribute.TIMESTAMP)) {
            timestampToUpdate = Timestamp.getEmptyTimestamp();
        }
        return timestampToUpdate;
    }

    /**
     * Creates and returns a {@code UniqueTagList} with the details of
     * {@code taskToEdit} edited with {@code editTaskDescriptor}. Subsequently,
     * checks to see if there is any need to remove UniqueTagList field based on
     * {@code attributeToRemove}
     */
    private static Frequency updateOrRemoveFrequency(ReadOnlyTask taskToEdit,
            EditTaskDescriptor editTaskDescriptor,
            ArrayList<Attribute> attributeToRemove) {
        assert taskToEdit != null;
        assert editTaskDescriptor != null;
        assert attributeToRemove != null;

        Frequency updatedFrequency = editTaskDescriptor.getFrequency()
                .orElseGet(taskToEdit::getFrequency);
        if (attributeToRemove.contains(Attribute.FREQUENCY)) {
            updatedFrequency = Frequency.getEmptyFrequency();
        }
        return updatedFrequency;
    }

    /**
     * Creates and returns a {@code UniqueTagList} with the details of
     * {@code taskToEdit} edited with {@code editTaskDescriptor}. Subsequently,
     * checks to see if there is any need to remove UniqueTagList field based on
     * {@code attributeToRemove}
     */
    private static UniqueTagList updateOrRemoveUniqueTagList(
            ReadOnlyTask taskToEdit, EditTaskDescriptor editTaskDescriptor,
            ArrayList<Attribute> attributeToRemove) {
        assert taskToEdit != null;
        assert editTaskDescriptor != null;
        assert attributeToRemove != null;

        UniqueTagList updatedTags = editTaskDescriptor.getTags()
                .orElseGet(taskToEdit::getTags);
        if (attributeToRemove.contains(Attribute.TAG)) {
            updatedTags = new UniqueTagList();
        }
        return updatedTags;
    }
}
