package utask.logic.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import utask.commons.core.EventsCenter;
import utask.commons.core.Messages;
import utask.commons.events.ui.ShowTaskOfInterestEvent;
import utask.commons.util.CollectionUtil;
import utask.logic.commands.exceptions.CommandException;
import utask.logic.commands.inteface.ReversibleCommand;
import utask.model.tag.UniqueTagList;
import utask.model.task.Deadline;
import utask.model.task.DeadlineTask;
import utask.model.task.EventTask;
import utask.model.task.FloatingTask;
import utask.model.task.Frequency;
import utask.model.task.IsCompleted;
import utask.model.task.Name;
import utask.model.task.ReadOnlyTask;
import utask.model.task.Task;
import utask.model.task.Timestamp;
import utask.model.task.UniqueTaskList;
import utask.staging.ui.helper.UTFliterListHelper;

/**
 * Edits the details of an existing task in the uTask.
 */

//@@author A0138423J
public class UpdateCommand extends Command implements ReversibleCommand {

    public static final String COMMAND_WORD = "update";

    public static final String COMMAND_FORMAT = "INDEX (must be a positive integer) [/name NAME] [/by DEADLINE]"
            + " [/from START_TIME to END_TIME] [/repeat FREQUENCY] [/tag TAG...][/done YES|NO]...";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Edits the details of the task specified "
            + "by the index number used in the last task listing. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: " + COMMAND_FORMAT + "\n"
            + "Example: " + COMMAND_WORD + " 1 /name do homework";

    public static final String MESSAGE_EDIT_TASK_SUCCESS = "Edited task: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_TASK = "This task already exists in the uTask.";

    private final int filteredTaskListIndex;
    private final EditTaskDescriptor editTaskDescriptor;
    private final ArrayList<String> attributeToRemove;

    private ReadOnlyTask taskToEdit;
    private Task editedTask;

    // @@author A0138423J
    /**
     * @param filteredTaskListIndex
     *            the index of the task in the filtered task list to edit
     * @param editTaskDescriptor
     *            details to edit the task with
     * @param attributeToRemove
     *            list <int> of attributes to be removed
     *            0 : Deadline, 1 : Timestamp, 2 : Tag, 3 : Frequency
     */
    public UpdateCommand(int filteredTaskListIndex,
            EditTaskDescriptor editTaskDescriptor,
            ArrayList<String> attributeToRemove) {
        assert filteredTaskListIndex > 0;
        assert editTaskDescriptor != null;
        assert attributeToRemove != null;

        // converts filteredTaskListIndex from one-based to zero-based.
        this.filteredTaskListIndex = filteredTaskListIndex - 1;
        this.editTaskDescriptor = new EditTaskDescriptor(editTaskDescriptor);
        this.attributeToRemove = attributeToRemove;
        System.out.println(attributeToRemove.toString());
    }

    //@@author A0139996A
    @Override
    public CommandResult execute() throws CommandException {
        if (filteredTaskListIndex >= model.getTotalSizeOfLists()) {
            throw new CommandException(
                    Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        }

        List<ReadOnlyTask> lastShownList = UTFliterListHelper.getInstance()
                .getUnderlyingListOfListViewByIndex(filteredTaskListIndex);

        int actualInt = UTFliterListHelper.getInstance()
                .getActualIndexFromDisplayIndex(filteredTaskListIndex);

        taskToEdit = lastShownList.get(actualInt);

        // create modified task from existing task
        editedTask = createEditedTask(taskToEdit, editTaskDescriptor,
                attributeToRemove);

        try {
            model.updateTask(taskToEdit, editedTask);
            model.addUndoCommand(this);

            EventsCenter.getInstance().post(new ShowTaskOfInterestEvent(editedTask));
        } catch (UniqueTaskList.DuplicateTaskException dpe) {
            throw new CommandException(MESSAGE_DUPLICATE_TASK);
        }
        return new CommandResult(
                String.format(MESSAGE_EDIT_TASK_SUCCESS, editedTask));
    }

    //@@author A0138423J
    /**
     * Creates and returns a {@code Task} with the details of {@code taskToEdit}
     * edited with {@code editTaskDescriptor}.
     */
    private static Task createEditedTask(ReadOnlyTask taskToEdit,
            EditTaskDescriptor editTaskDescriptor,
            ArrayList<String> attributeToRemove) {
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
        case (0):
            return new FloatingTask(updatedName, updatedFrequency, updatedTags,
                    updatedIsCompleted);
        case (1):
            return new DeadlineTask(updatedName, updatedDeadline,
                    updatedFrequency, updatedTags, updatedIsCompleted);
        case (2):
            return new EventTask(updatedName, updatedDeadline, updatedTimestamp,
                    updatedFrequency, updatedTags, updatedIsCompleted);
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
            ArrayList<String> attributeToRemove) {
        Deadline updatedDeadline = editTaskDescriptor.getDeadline()
                .orElseGet(taskToEdit::getDeadline);
        if (attributeToRemove.contains("deadline")) {
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
            ArrayList<String> attributeToRemove) {
        Timestamp updatedTimestamp = editTaskDescriptor.getTimeStamp()
                .orElseGet(taskToEdit::getTimestamp);
        if (attributeToRemove.contains("timestamp")) {
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
            ArrayList<String> attributeToRemove) {
        Frequency updatedFrequency = editTaskDescriptor.getFrequency()
                .orElseGet(taskToEdit::getFrequency);
        if (attributeToRemove.contains("frequency")) {
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
            ArrayList<String> attributeToRemove) {
        UniqueTagList updatedTags = editTaskDescriptor.getTags()
                .orElseGet(taskToEdit::getTags);
        if (attributeToRemove.contains("tag")) {
            updatedTags = new UniqueTagList();
        }
        return updatedTags;
    }

    // @@author A0138423J
    /**
     * Checks {@code updatedDeadline} and {@code updatedTimestamp} to see whether
     * both are empty or not. Subsequently, based on the statuses, this method will
     * determine the type of editedTask. Types are pre-set based on following set:
     * 2 : Event Task, 1: Deadline Task, 0 : Floating Task
     */
    private static int typeOfEditedTask(Deadline updatedDeadline,
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
            return 2;
        } else if (!isDeadlineEmpty && isTimestampEmpty) {
            return 1;
        } else if (isDeadlineEmpty && isTimestampEmpty) {
            return 0;
        }
        return -1;
    }

    // @@author A0138423J
    /**
     * Stores the details to edit the task with. Each non-empty field value will
     * replace the corresponding field value of the Task.
     */
    public static class EditTaskDescriptor {
        private Optional<Name> name = Optional.empty();
        private Optional<Deadline> deadLine = Optional.empty();
        private Optional<Timestamp> timeStamp = Optional.empty();
        private Optional<Frequency> frequency = Optional.empty();
        private Optional<UniqueTagList> tags = Optional.empty();
        private Optional<IsCompleted> isCompleted = Optional.empty();

        public EditTaskDescriptor() {
        }

        public EditTaskDescriptor(EditTaskDescriptor toCopy) {
            this.name = toCopy.getName();
            this.deadLine = toCopy.getDeadline();
            this.timeStamp = toCopy.getTimeStamp();
            this.frequency = toCopy.getFrequency();
            this.tags = toCopy.getTags();
            this.isCompleted = toCopy.getIsCompleted();
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyPresent(this.name, this.deadLine,
                    this.timeStamp, this.frequency, this.tags,
                    this.isCompleted);
        }

        public void setName(Optional<Name> name) {
            assert name != null;
            this.name = name;
        }

        public Optional<Name> getName() {
            return name;
        }

        public void setDeadline(Optional<Deadline> deadLine) {
            assert deadLine != null;
            this.deadLine = deadLine;
        }

        public Optional<Deadline> getDeadline() {
            return deadLine;
        }

        public void setTimeStamp(Optional<Timestamp> timeStamp) {
            assert timeStamp != null;
            this.timeStamp = timeStamp;
        }

        public Optional<Timestamp> getTimeStamp() {
            return timeStamp;
        }

        public void setFrequency(Optional<Frequency> frequency) {
            assert frequency != null;
            this.frequency = frequency;
        }

        public Optional<Frequency> getFrequency() {
            return frequency;
        }

        public void setTags(Optional<UniqueTagList> tags) {
            assert tags != null;
            this.tags = tags;
        }

        public Optional<UniqueTagList> getTags() {
            return tags;
        }

        public void setIsCompleted(Optional<IsCompleted> isCompleted) {
            assert isCompleted != null;
            this.isCompleted = isCompleted;
        }

        public Optional<IsCompleted> getIsCompleted() {
            return isCompleted;
        }
    }

    @Override
    public void undo() throws Exception {
        model.updateTask(editedTask, taskToEdit);
    }

    @Override
    public void redo() throws Exception {
        model.updateTask(taskToEdit, editedTask);
    }
}