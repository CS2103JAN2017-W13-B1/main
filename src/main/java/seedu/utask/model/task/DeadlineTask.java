package seedu.utask.model.task;

import java.util.Objects;

import seedu.address.model.tag.UniqueTagList;

public class DeadlineTask extends Task {

    private Deadline deadline;

    public DeadlineTask(Name name, Deadline deadline, Frequency frequency, UniqueTagList tags) {
        super(name, frequency, tags);

        this.deadline = deadline;
    }

    /**
     * Creates a copy of the given ReadOnlyPerson.
     */
    public DeadlineTask(ReadOnlyTask source) {
        this(source.getName(), source.getDeadline(), source.getFrequency(), source.getTags());
    }

    public void setDeadline(Deadline deadline) {
        assert deadline != null;
        this.deadline = deadline;
    }

    @Override
    public Deadline getDeadline() {
        return deadline;
    }

    public void resetData(ReadOnlyTask replacement) {
        super.resetData(replacement);
        this.setDeadline(replacement.getDeadline());
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, deadline, frequency, tags);
    }

    @Override
    public Timestamp getTimestamp() {
        // TODO Auto-generated method stub
        return null;
    }
}
