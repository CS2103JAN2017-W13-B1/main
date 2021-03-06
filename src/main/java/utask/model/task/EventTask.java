package utask.model.task;

import java.util.Objects;

import utask.model.tag.UniqueTagList;

public class EventTask extends Task {

    private Deadline deadline;
    private Timestamp timestamp;

    public EventTask(Name name, Deadline deadline, Timestamp timestamp,
            Frequency frequency, UniqueTagList tags, Status status) {
        super(name, frequency, tags, status);
        this.deadline = deadline;
        this.timestamp = timestamp;
    }

    /**
     * Creates a copy of the given ReadOnlyTask.
     */
    public EventTask(ReadOnlyTask source) {
        this(source.getName(), source.getDeadline(), source.getTimestamp(),
                source.getFrequency(), source.getTags(),
                source.getStatus());
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
        return Objects.hash(name, deadline, timestamp, frequency, tags, status);
    }

}
