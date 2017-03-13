package seedu.utask.model.task;

import java.util.Objects;

import seedu.address.model.tag.UniqueTagList;

public class DeadlineTask extends Task {

    private Deadline deadline;
    private Timestamp fakeTimestamp;

    public DeadlineTask(Name name, Deadline deadline, Frequency frequency, UniqueTagList tags) {
        super(name, frequency, tags);

        this.deadline = deadline;

        fakeTimestamp();
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
        return fakeTimestamp;
    }

    private void fakeTimestamp() {
        try {
            Timestamp t = new Timestamp("0000 to 0000");
            fakeTimestamp = t;
        } catch (Exception e) {

        }
    }
}
