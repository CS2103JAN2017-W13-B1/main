package utask.model.task;

import utask.model.tag.UniqueTagList;

/**
 * A read-only immutable interface for a Task in the uTask. Implementations
 * should guarantee: details are present and not null, field values are
 * validated.
 */
public interface ReadOnlyTask {

    Name getName();
    Frequency getFrequency();
    Status getStatus();

    /*
     * Deadline and Timestamp are optional based on type of task
     *
     *  EventTask has both Deadline and Timestamp
     *  DeadlineTask has only deadline
     */
    default Deadline getDeadline() {
        return Deadline.getEmptyDeadline();
    }

    default Timestamp getTimestamp() {
        return Timestamp.getEmptyTimestamp();
    }

    /**
     * The returned TagList is a deep copy of the internal TagList, changes on
     * the returned list will not affect the task's internal tags.
     */
    UniqueTagList getTags();

    /**
     * Returns true if both have the same state. (interfaces cannot override
     * .equals)
     */
    default boolean isSameStateAs(ReadOnlyTask other) {
        return other == this // short circuit if same object
                || (other != null // this is first to avoid NPE below
                        && other.getName().equals(this.getName()) // state
                                                                  // checks here
                                                                  // onwards
                        && other.getDeadline().equals(this.getDeadline())
                        && other.getTimestamp().equals(this.getTimestamp())
                        && other.getFrequency().equals(this.getFrequency())
                        && other.getStatus().equals(this.getStatus()));
    }

    /**
     * Formats the task as text, showing all task details.
     */
    default String getAsText() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getName()).append(" Deadline: ").append(getDeadline())
                .append(" Timestamp: ").append(getTimestamp())
                .append(" Frequency: ").append(getFrequency())
                .append(" Status: ").append(getStatus().toString())
                .append(" Tags: ");
        getTags().forEach(builder::append);
        return builder.toString();
    }

}
