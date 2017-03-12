package seedu.utask.model.task;

import java.util.Objects;

import seedu.address.commons.util.CollectionUtil;
import seedu.address.model.tag.UniqueTagList;

/**
 * Represents a Person in the address book.
 * Guarantees: details are present and not null, field values are validated.
 */
public abstract class Task implements ReadOnlyTask {

    private Name name;
    private Deadline deadline;
    private Timestamp timestamp;

    private Frequency frequency;
    private boolean isCompleted;
    private UniqueTagList tags;

    /**
     * Every field must be present and not null.
     */
    public Task(Name name, Deadline deadline, Timestamp timestamp, Frequency frequency, UniqueTagList tags) {
        assert !CollectionUtil.isAnyNull(name, deadline, timestamp, frequency, tags);
        this.name = name;
        this.deadline = deadline;
        this.timestamp = timestamp;
        this.frequency = frequency;
        this.tags = new UniqueTagList(tags); // protect internal tags from changes in the arg list
    }

    /**
     * Creates a copy of the given ReadOnlyPerson.
     */
    public Task(ReadOnlyTask source) {
        this(source.getName(), source.getDeadline(), source.getTimestamp(), source.getFrequency(), source.getTags());
    }

    public void setName(Name name) {
        assert name != null;
        this.name = name;
    }

    @Override
    public Name getName() {
        return name;
    }

    public void setDeadline(Deadline deadline) {
        assert deadline != null;
        this.deadline = deadline;
    }

    @Override
    public Deadline getDeadline() {
        return deadline;
    }

    public void setTimestamp(Timestamp timestamp) {
        assert timestamp != null;
        this.timestamp = timestamp;
    }

    @Override
    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setFrequency(Frequency frequency) {
        assert frequency != null;
        this.frequency = frequency;
    }

    @Override
    public Frequency getFrequency() {
        return frequency;
    }

    @Override
    public UniqueTagList getTags() {
        return new UniqueTagList(tags);
    }

    /**
     * Replaces this person's tags with the tags in the argument tag list.
     */
    public void setTags(UniqueTagList replacement) {
        tags.setTags(replacement);
    }

    /**
     * Updates this person with the details of {@code replacement}.
     */
    public void resetData(ReadOnlyTask replacement) {
        assert replacement != null;

        this.setName(replacement.getName());
        this.setDeadline(replacement.getDeadline());
        this.setTimestamp(replacement.getTimestamp());
        this.setFrequency(replacement.getFrequency());
        this.setTags(replacement.getTags());
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ReadOnlyTask // instanceof handles nulls
                && this.isSameStateAs((ReadOnlyTask) other));
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, deadline, timestamp, frequency, tags);
    }

    @Override
    public String toString() {
        return getAsText();
    }

}
