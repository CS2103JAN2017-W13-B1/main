package utask.model.task;

import java.util.Objects;

import utask.model.tag.UniqueTagList;

public class DeadlineTask extends Task {

    private Deadline deadline;

    public DeadlineTask(Name name, Deadline deadline, Frequency frequency, UniqueTagList tags,
            Status status) {
        super(name, frequency, tags, status);
        this.deadline = deadline;
    }

    /**
     * Creates a copy of the given ReadOnlyTask.
     */
    public DeadlineTask(ReadOnlyTask source) {
        this(source.getName(), source.getDeadline(), source.getFrequency(), source.getTags(), source.getStatus());
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
        return Objects.hash(name, deadline, frequency, tags, status);
    }
}
