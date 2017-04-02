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
import utask.model.task.IsCompleted;
import utask.model.task.Name;
import utask.model.task.ReadOnlyTask;
import utask.model.task.Task;
import utask.model.task.TaskType;
import utask.model.task.Timestamp;
import utask.staging.ui.helper.UTFilteredListHelper;

// @@author A0138423J
public class UpdateUtil {

    public static final String TO_BE_REMOVED = "-";
    // @@author A0138423J
    public static ReadOnlyTask fetchTaskToEdit(int index) {
        List<ReadOnlyTask> lastShownList = UTFilteredListHelper.getInstance()
                .getUnderlyingListByIndex(index);
        int actualInt = UTFilteredListHelper.getInstance()
                .getActualIndexFromDisplayIndex(index);

        return lastShownList.get(actualInt);
    }

    // @@author A0138423J
    /**
     * Checks {@code updatedDeadline} and {@code updatedTimestamp} to see whether
     * both are empty or not. Subsequently, based on the statuses, this method will
     * determine the type of editedTask. Types are pre-set based on following set:
     * 2 : Event Task, 1: Deadline Task, 0 : Floating Task
     */
    public static TaskType typeOfEditedTask(Deadline updatedDeadline,
            Timestamp updatedTimestamp) {
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

    // @@author A0138423J
    /**
     * Creates and returns a {@code Task} with the details of {@code taskToEdit}
     * edited with {@code editTaskDescriptor}.
     */
    public static Task createEditedTask(ReadOnlyTask taskToEdit, Boolean value)
            throws IllegalValueException {
        assert taskToEdit != null;
        assert value != null;

        Task placeholder = null;
        switch (typeOfEditedTask(taskToEdit.getDeadline(),
                taskToEdit.getTimestamp())) {
        case FLOATING:
            placeholder = new FloatingTask(taskToEdit.getName(),
                    taskToEdit.getFrequency(), taskToEdit.getTags(),
                    new IsCompleted(value.toString()));
            break;
        case DEADLINE:
            placeholder = new DeadlineTask(taskToEdit.getName(),
                    taskToEdit.getDeadline(), taskToEdit.getFrequency(),
                    taskToEdit.getTags(), new IsCompleted(value.toString()));
            break;
        case EVENT:
            placeholder = new EventTask(taskToEdit.getName(),
                    taskToEdit.getDeadline(), taskToEdit.getTimestamp(),
                    taskToEdit.getFrequency(), taskToEdit.getTags(),
                    new IsCompleted(value.toString()));
            break;
        default:
            System.out.println("Error checking edited task type!");
        }

        return placeholder;
    }

    //@@author A0138423J
    /**
     * Creates and returns a {@code Task} with the details of {@code taskToEdit}
     * edited with {@code editTaskDescriptor}.
     */
    public static Task createEditedTask(ReadOnlyTask taskToEdit,
            EditTaskDescriptor editTaskDescriptor,
            ArrayList<Attribute> attributeToRemove) {
        assert taskToEdit != null;

        Name updatedName = editTaskDescriptor.getName()
                .orElseGet(taskToEdit::getName);
        Deadline updatedDeadline = updateOrRemoveDeadline(taskToEdit,
                editTaskDescriptor, attributeToRemove);
        Timestamp updatedTimestamp = updateOrRemoveTimestamp(taskToEdit,
                editTaskDescriptor, attributeToRemove);
        Frequency updatedFrequency = updateOrRemoveFrequency(taskToEdit,
                editTaskDescriptor, attributeToRemove);
        UniqueTagList updatedTags = updateOrRemoveUniqueTagList(taskToEdit,
                editTaskDescriptor, attributeToRemove);
        IsCompleted updatedIsCompleted = editTaskDescriptor.getIsCompleted()
                .orElseGet(taskToEdit::getIsCompleted);

        Task placeholder = null;
        switch (typeOfEditedTask(updatedDeadline, updatedTimestamp)) {
        case FLOATING:
            placeholder = new FloatingTask(updatedName, updatedFrequency, updatedTags,
                    updatedIsCompleted);
            break;
        case DEADLINE:
            placeholder =  new DeadlineTask(updatedName, updatedDeadline,
                    updatedFrequency, updatedTags, updatedIsCompleted);
            break;
        case EVENT:
            placeholder = new EventTask(updatedName, updatedDeadline, updatedTimestamp,
                    updatedFrequency, updatedTags, updatedIsCompleted);
            break;
        default:
            System.out.println("Error checking edited task type!");
        }
        return placeholder;
    }

    // @@author A0138423J
    /**
     * Creates and returns a {@code Deadline} with the details of {@code taskToEdit}
     * edited with {@code editTaskDescriptor}. Subsequently, checks to see if there
     * is any need to remove Deadline field based on {@code attributeToRemove}
     */
    private static Deadline updateOrRemoveDeadline(ReadOnlyTask taskToEdit,
            EditTaskDescriptor editTaskDescriptor,
            ArrayList<Attribute> attributeToRemove) {
        Deadline updatedDeadline = editTaskDescriptor.getDeadline()
                .orElseGet(taskToEdit::getDeadline);
        if (attributeToRemove.contains(Attribute.DEADLINE)) {
            updatedDeadline = Deadline.getEmptyDeadline();
        }
        return updatedDeadline;
    }

    // @@author A0138423J
    /**
     * Creates and returns a {@code Timestamp} with the details of {@code taskToEdit}
     * edited with {@code editTaskDescriptor}. Subsequently, checks to see if there
     * is any need to remove Timestamp field based on {@code attributeToRemove}
     */
    private static Timestamp updateOrRemoveTimestamp(ReadOnlyTask taskToEdit,
            EditTaskDescriptor editTaskDescriptor,
            ArrayList<Attribute> attributeToRemove) {
        Timestamp updatedTimestamp = editTaskDescriptor.getTimeStamp()
                .orElseGet(taskToEdit::getTimestamp);
        if (attributeToRemove.contains(Attribute.TIMESTAMP)) {
            updatedTimestamp = Timestamp.getEmptyTimestamp();
        }
        return updatedTimestamp;
    }

    // @@author A0138423J
    /**
     * Creates and returns a {@code UniqueTagList} with the details of {@code taskToEdit}
     * edited with {@code editTaskDescriptor}. Subsequently, checks to see if there
     * is any need to remove UniqueTagList field based on {@code attributeToRemove}
     */
    private static Frequency updateOrRemoveFrequency(ReadOnlyTask taskToEdit,
            EditTaskDescriptor editTaskDescriptor,
            ArrayList<Attribute> attributeToRemove) {
        Frequency updatedFrequency = editTaskDescriptor.getFrequency()
                .orElseGet(taskToEdit::getFrequency);
        if (attributeToRemove.contains(Attribute.FREQUENCY)) {
            updatedFrequency = Frequency.getEmptyFrequency();
        }
        return updatedFrequency;
    }

    // @@author A0138423J
    /**
     * Creates and returns a {@code UniqueTagList} with the details of {@code taskToEdit}
     * edited with {@code editTaskDescriptor}. Subsequently, checks to see if there
     * is any need to remove UniqueTagList field based on {@code attributeToRemove}
     */
    private static UniqueTagList updateOrRemoveUniqueTagList(ReadOnlyTask taskToEdit,
            EditTaskDescriptor editTaskDescriptor,
            ArrayList<Attribute> attributeToRemove) {
        UniqueTagList updatedTags = editTaskDescriptor.getTags()
                .orElseGet(taskToEdit::getTags);
        if (attributeToRemove.contains(Attribute.TAG)) {
            updatedTags = new UniqueTagList();
        }
        return updatedTags;
    }
}
