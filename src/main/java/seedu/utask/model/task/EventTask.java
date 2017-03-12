package seedu.utask.model.task;

import java.util.Objects;

import seedu.address.model.tag.UniqueTagList;

public class EventTask extends Task {

    private Deadline deadline;
    private Timestamp timestamp;

    public EventTask(Name name, Deadline deadline, Timestamp timestamp, Frequency frequency, UniqueTagList tags) {
        super(name, frequency, tags);
        this.deadline = deadline;
        this.timestamp = timestamp;
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

    public void resetData(ReadOnlyTask replacement) {
        super.resetData(replacement);

        this.setDeadline(replacement.getDeadline());
        this.setTimestamp(replacement.getTimestamp());
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, deadline, timestamp, frequency, tags);
    }

}
