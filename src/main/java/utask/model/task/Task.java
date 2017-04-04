package utask.model.task;

import java.util.Objects;

import utask.commons.util.CollectionUtil;
import utask.model.tag.UniqueTagList;

/**
 * Represents a Task in the address book.
 * Guarantees: details are present and not null, field values are validated.
 */
public abstract class Task implements ReadOnlyTask {

    protected Name name;
    protected Frequency frequency;
    protected Status status;
    protected UniqueTagList tags;

    /**
     * Every field must be present and not null.
     */
    public Task(Name name, Frequency frequency, UniqueTagList tags, Status status) {
        assert !CollectionUtil.isAnyNull(name, frequency, tags);
        this.name = name;
        this.frequency = frequency;
        this.status = status;
        this.tags = new UniqueTagList(tags); // protect internal tags from changes in the arg list
    }

    public void setName(Name name) {
        assert name != null;
        this.name = name;
    }

    @Override
    public Name getName() {
        return name;
    }

    public void setFrequency(Frequency frequency) {
        assert frequency != null;
        this.frequency = frequency;
    }

    @Override
    public Frequency getFrequency() {
        return frequency;
    }

    //@@author A0138423J
    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public Status getStatus() {
        return status;
    }

    @Override
    public UniqueTagList getTags() {
        return new UniqueTagList(tags);
    }

    /**
     * Replaces this task's tags with the tags in the argument tag list.
     */
    public void setTags(UniqueTagList replacement) {
        tags.setTags(replacement);
    }

    /**
     * Updates this task with the details of {@code replacement}.
     */
    public void resetData(ReadOnlyTask replacement) {
        assert replacement != null;

        this.setName(replacement.getName());
        this.setFrequency(replacement.getFrequency());
        this.setStatus(replacement.getStatus());
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
        return Objects.hash(name, frequency, tags, status);
    }

    @Override
    public String toString() {
        return getAsText();
    }
}
