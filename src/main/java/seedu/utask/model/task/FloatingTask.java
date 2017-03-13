package seedu.utask.model.task;

import seedu.address.model.tag.UniqueTagList;

public class FloatingTask extends Task {

    public FloatingTask(Name name, Frequency frequency, UniqueTagList tags) {
        super(name, frequency, tags);
    }

    /**
     * Creates a copy of the given ReadOnlyPerson.
     */
    public FloatingTask(ReadOnlyTask source) {
        this(source.getName(), source.getFrequency(), source.getTags());
    }

    @Override
    public Deadline getDeadline() {
        return Deadline.getEmptyDeadline();
    }

    @Override
    public Timestamp getTimestamp() {
        return Timestamp.getEmptyTimestamp();
    }
}
