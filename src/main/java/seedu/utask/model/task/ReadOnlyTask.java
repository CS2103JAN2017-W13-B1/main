package seedu.utask.model.task;

import seedu.address.model.tag.UniqueTagList;

/**
 * A read-only immutable interface for a Person in the addressbook.
 * Implementations should guarantee: details are present and not null, field values are validated.
 */
public interface ReadOnlyTask {

    Name getName();
    Deadline getDeadline();
    Timestamp getTimestamp();
    Frequency getFrequency();

    /**
     * The returned TagList is a deep copy of the internal TagList,
     * changes on the returned list will not affect the person's internal tags.
     */
    UniqueTagList getTags();

    /**
     * Returns true if both have the same state. (interfaces cannot override .equals)
     */
    default boolean isSameStateAs(ReadOnlyTask other) {
        return other == this // short circuit if same object
                || (other != null // this is first to avoid NPE below
                && other.getName().equals(this.getName()) // state checks here onwards
                && other.getDeadline().equals(this.getDeadline())
                && other.getTimestamp().equals(this.getTimestamp())
                && other.getFrequency().equals(this.getFrequency()));
    }

    /**
     * Formats the person as text, showing all contact details.
     */
    default String getAsText() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getName())
                .append(" Phone: ")
                .append(getDeadline())
                .append(" Email: ")
                .append(getTimestamp())
                .append(" Address: ")
                .append(getFrequency())
                .append(" Tags: ");
        getTags().forEach(builder::append);
        return builder.toString();
    }

}
