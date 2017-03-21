package utask.testutil;

import utask.model.tag.UniqueTagList;
import utask.model.task.Deadline;
import utask.model.task.Frequency;
import utask.model.task.IsCompleted;
import utask.model.task.Name;
import utask.model.task.ReadOnlyTask;
import utask.model.task.Timestamp;

/**
 * A mutable task object. For testing only.
 */
public class TestTask implements ReadOnlyTask {

    private Name name;
    private Frequency frequency;
    private Timestamp timestamp;
    private Deadline deadline;
    private UniqueTagList tags;
    private IsCompleted iscompleted;

    public TestTask() {
        tags = new UniqueTagList();
    }

    /**
     * Creates a copy of {@code taskToCopy}.
     */
    public TestTask(TestTask taskToCopy) {
        this.name = taskToCopy.getName();
        this.deadline = taskToCopy.getDeadline();
        this.timestamp = taskToCopy.getTimestamp();
        this.frequency = taskToCopy.getFrequency();
        this.tags = taskToCopy.getTags();
        this.iscompleted = taskToCopy.getIsCompleted();
    }

    public void setName(Name name) {
        this.name = name;
    }

    public void setFrequency(Frequency address) {
        this.frequency = address;
    }

    public void setTimestamp(Timestamp email) {
        this.timestamp = email;
    }

    public void setDeadline(Deadline phone) {
        this.deadline = phone;
    }

    public void setTags(UniqueTagList tags) {
        this.tags = tags;
    }

    public void setIsCompleted(IsCompleted iscompleted) {
        this.iscompleted = iscompleted;
    }

    @Override
    public Name getName() {
        return name;
    }

    @Override
    public Deadline getDeadline() {
        return deadline;
    }

    @Override
    public Timestamp getTimestamp() {
        return timestamp;
    }

    @Override
    public Frequency getFrequency() {
        return frequency;
    }

    @Override
    public IsCompleted getIsCompleted() {
        return iscompleted;
    }

    @Override
    public UniqueTagList getTags() {
        return tags;
    }

    @Override
    public String toString() {
        return getAsText();
    }

    public String getAddCommand() {
        StringBuilder sb = new StringBuilder();
        sb.append("create " + this.getName().fullName + " ");
        sb.append("/by " + this.getDeadline().value + " ");
        sb.append("/from " + this.getTimestamp().value + " ");
        sb.append("/repeat " + this.getFrequency().value + " ");
        sb.append("/done " + this.getIsCompleted().value + " ");
        this.getTags().asObservableList().stream().forEach(s -> sb.append("/tag " + s.tagName + " "));
        return sb.toString();
    }
}
