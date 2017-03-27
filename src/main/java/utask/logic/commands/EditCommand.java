package utask.logic.commands;

import java.util.List;
import java.util.Optional;

import utask.commons.core.Messages;
import utask.commons.util.CollectionUtil;
import utask.logic.commands.exceptions.CommandException;
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
import utask.staging.ui.UTListHelper;

/**
 * Edits the details of an existing task in the uTask.
 */
//@@author A0138423J
public class EditCommand extends Command {

    public static final String COMMAND_WORD = "update";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Edits the details of the task specified "
            + "by the index number used in the last task listing. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) [/name NAME] [/by DEADLINE] "
            + "[/from START_TIME to END_TIME] [/repeat FREQUENCY] [/tag TAG...][/done YES|NO]...\n"
            + "Example: " + COMMAND_WORD + " 1 /name do homework";

    public static final String MESSAGE_EDIT_TASK_SUCCESS = "Edited task: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_TASK = "This task already exists in the uTask.";

    private final int filteredTaskListIndex;
    private final EditTaskDescriptor editTaskDescriptor;

    /**
     * @param filteredTaskListIndex
     *            the index of the task in the filtered task list to edit
     * @param editTaskDescriptor
     *            details to edit the task with
     */
    public EditCommand(int filteredTaskListIndex,
            EditTaskDescriptor editTaskDescriptor) {
        assert filteredTaskListIndex > 0;
        assert editTaskDescriptor != null;

        // converts filteredTaskListIndex from one-based to zero-based.
        this.filteredTaskListIndex = filteredTaskListIndex - 1;

        this.editTaskDescriptor = new EditTaskDescriptor(editTaskDescriptor);
    }

    //@author A0139996A
    @Override
    public CommandResult execute() throws CommandException {
//        List<ReadOnlyTask> lastShownList = model.getFilteredTaskList();
//
//        if (filteredTaskListIndex >= lastShownList.size()) {
//            throw new CommandException(
//                    Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
//        }

        if (filteredTaskListIndex >= model.getTotalSizeOfLists()) {
            throw new CommandException(
                    Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        }

        // Retrieve task to be edited from save file
        //ReadOnlyTask taskToEdit = lastShownList.get(filteredTaskListIndex);

        List<ReadOnlyTask> lastShownList =
                UTListHelper.getInstance().getUnderlyingListOfListViewByIndex(filteredTaskListIndex);

        int actualInt = UTListHelper.getInstance().getActualIndexFromDisplayIndex(filteredTaskListIndex);

        System.out.println(model.getTotalSizeOfLists());
        System.out.println(filteredTaskListIndex);
        System.out.println(actualInt);
        System.out.println(lastShownList.size());

        ReadOnlyTask taskToEdit = lastShownList.get(actualInt);

        // create modified task from existing task
        Task editedTask = createEditedTask(taskToEdit, editTaskDescriptor);

        try {
            //model.updateTask(filteredTaskListIndex, editedTask);
            model.updateTask(taskToEdit, editedTask);
        } catch (UniqueTaskList.DuplicateTaskException dpe) {
            throw new CommandException(MESSAGE_DUPLICATE_TASK);
        }
        //model.updateFilteredListToShowAll();
        return new CommandResult(
                String.format(MESSAGE_EDIT_TASK_SUCCESS, editedTask));
    }

    //@@author A0138423J
    /**
     * Creates and returns a {@code Task} with the details of
     * {@code taskToEdit} edited with {@code editTaskDescriptor}.
     */
    private static Task createEditedTask(ReadOnlyTask taskToEdit,
            EditTaskDescriptor editTaskDescriptor) {
        assert taskToEdit != null;

        Name updatedName = editTaskDescriptor.getName()
                .orElseGet(taskToEdit::getName);
        Deadline updatedDeadline = editTaskDescriptor.getDeadline()
                .orElseGet(taskToEdit::getDeadline);
        Timestamp updatedTimestamp = editTaskDescriptor.getTimeStamp()
                .orElseGet(taskToEdit::getTimestamp);
        Frequency updatedFrequency = editTaskDescriptor.getFrequency()
                .orElseGet(taskToEdit::getFrequency);
        UniqueTagList updatedTags = editTaskDescriptor.getTags()
                .orElseGet(taskToEdit::getTags);
        IsCompleted updatedIsCompleted = editTaskDescriptor.getIsCompleted()
                .orElseGet(taskToEdit::getIsCompleted);

        // TODO
        Task placeholder = null;
        if (!updatedDeadline.isEmpty() && !updatedTimestamp.isEmpty()) {
            placeholder = new EventTask(updatedName, updatedDeadline,
                    updatedTimestamp, updatedFrequency, updatedTags, updatedIsCompleted);
        } else if (!updatedDeadline.isEmpty() && updatedTimestamp.isEmpty()) {
            placeholder = new DeadlineTask(updatedName, updatedDeadline,
                    updatedFrequency, updatedTags, updatedIsCompleted);
        } else if (updatedDeadline.isEmpty() && updatedTimestamp.isEmpty()) {
            placeholder = new FloatingTask(updatedName, updatedFrequency,
                    updatedTags, updatedIsCompleted);
        }
        return placeholder;
    }

    /**
     * Stores the details to edit the task with. Each non-empty field value
     * will replace the corresponding field value of the Task.
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
                    this.timeStamp, this.frequency, this.tags, this.isCompleted);
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
}
