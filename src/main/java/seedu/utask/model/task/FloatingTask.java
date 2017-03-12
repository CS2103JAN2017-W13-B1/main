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
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Timestamp getTimestamp() {
        // TODO Auto-generated method stub
        return null;
    }
}
