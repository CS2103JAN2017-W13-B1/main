package seedu.address.logic.commands;

import java.util.List;
import java.util.Optional;

import seedu.address.commons.core.Messages;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.tag.UniqueTagList;
import seedu.utask.model.task.Deadline;
import seedu.utask.model.task.DeadlineTask;
import seedu.utask.model.task.EventTask;
import seedu.utask.model.task.FloatingTask;
import seedu.utask.model.task.Frequency;
import seedu.utask.model.task.Name;
import seedu.utask.model.task.ReadOnlyTask;
import seedu.utask.model.task.Task;
import seedu.utask.model.task.Timestamp;
import seedu.utask.model.task.UniqueTaskList;

/**
 * Edits the details of an existing person in the address book.
 */
public class EditCommand extends Command {

    public static final String COMMAND_WORD = "update";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Edits the details of the task specified "
            + "by the index number used in the last task listing. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) [/name NAME] [/by DEADLINE] "
            + "[/from START_TIME to END_TIME] [/repeat FREQUENCY] [/tag TAG...][/done YES|NO]...\n"
            + "Example: " + COMMAND_WORD + " 1 /name do homework";

    public static final String MESSAGE_EDIT_PERSON_SUCCESS = "Edited task: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_PERSON = "This task already exists in the address book.";

    private final int filteredTaskListIndex;
    private final EditTaskDescriptor editTaskDescriptor;

    /**
     * @param filteredTaskListIndex
     *            the index of the person in the filtered person list to edit
     * @param editTaskDescriptor
     *            details to edit the person with
     */
    public EditCommand(int filteredPersonListIndex,
            EditTaskDescriptor editPersonDescriptor) {
        assert filteredPersonListIndex > 0;
        assert editPersonDescriptor != null;

        // converts filteredPersonListIndex from one-based to zero-based.
        this.filteredTaskListIndex = filteredPersonListIndex - 1;

        this.editTaskDescriptor = new EditTaskDescriptor(editPersonDescriptor);
    }

    @Override
    public CommandResult execute() throws CommandException {
        List<ReadOnlyTask> lastShownList = model.getFilteredTaskList();

        if (filteredTaskListIndex >= lastShownList.size()) {
            throw new CommandException(
                    Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        }

        // Retrieve task to be edited from save file
        ReadOnlyTask taskToEdit = lastShownList.get(filteredTaskListIndex);

        // create modified task from existing task
        Task editedTask = createEditedTask(taskToEdit, editTaskDescriptor);

        try {
            model.updateTask(filteredTaskListIndex, editedTask);
        } catch (UniqueTaskList.DuplicateTaskException dpe) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }
        model.updateFilteredListToShowAll();
        return new CommandResult(
                String.format(MESSAGE_EDIT_PERSON_SUCCESS, taskToEdit));
    }

    /**
     * Creates and returns a {@code Person} with the details of
     * {@code personToEdit} edited with {@code editPersonDescriptor}.
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

        // TODO
        Task placeholder = null;
        if (!updatedDeadline.isEmpty() && !updatedTimestamp.isEmpty()) {
            placeholder = new EventTask(updatedName, updatedDeadline,
                    updatedTimestamp, updatedFrequency, updatedTags);
        } else if (!updatedDeadline.isEmpty() && updatedTimestamp.isEmpty()) {
            placeholder = new DeadlineTask(updatedName, updatedDeadline,
                    updatedFrequency, updatedTags);
        } else if (updatedDeadline.isEmpty() && updatedTimestamp.isEmpty()) {
            placeholder = new FloatingTask(updatedName, updatedFrequency,
                    updatedTags);
        }
        return placeholder;
        // return new EventTask(updatedName, updatedDeadline, updatedTimestamp,
        // updatedFrequency, updatedTags);
    }

    /**
     * Stores the details to edit the person with. Each non-empty field value
     * will replace the corresponding field value of the person.
     */
    public static class EditTaskDescriptor {
        private Optional<Name> name = Optional.empty();
        private Optional<Deadline> deadLine = Optional.empty();
        private Optional<Timestamp> timeStamp = Optional.empty();
        private Optional<Frequency> frequency = Optional.empty();
        private Optional<UniqueTagList> tags = Optional.empty();

        public EditTaskDescriptor() {
        }

        public EditTaskDescriptor(EditTaskDescriptor toCopy) {
            this.name = toCopy.getName();
            this.deadLine = toCopy.getDeadline();
            this.timeStamp = toCopy.getTimeStamp();
            this.frequency = toCopy.getFrequency();
            this.tags = toCopy.getTags();
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyPresent(this.name, this.deadLine,
                    this.timeStamp, this.frequency, this.tags);
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
    }
}
