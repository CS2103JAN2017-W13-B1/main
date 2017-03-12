package seedu.address.testutil;

import seedu.address.model.tag.UniqueTagList;
import seedu.utask.model.task.Deadline;
import seedu.utask.model.task.Frequency;
import seedu.utask.model.task.Name;
import seedu.utask.model.task.ReadOnlyTask;
import seedu.utask.model.task.Timestamp;

/**
 * A mutable person object. For testing only.
 */
public class TestTask implements ReadOnlyTask {

    private Name name;
    private Frequency frequency;
    private Timestamp timestamp;
    private Deadline deadline;
    private UniqueTagList tags;

    public TestTask() {
        tags = new UniqueTagList();
    }

    /**
     * Creates a copy of {@code personToCopy}.
     */
    public TestTask(TestTask personToCopy) {
        this.name = personToCopy.getName();
        this.deadline = personToCopy.getDeadline();
        this.timestamp = personToCopy.getTimestamp();
        this.frequency = personToCopy.getFrequency();
        this.tags = personToCopy.getTags();
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
        this.getTags().asObservableList().stream().forEach(s -> sb.append("/tag " + s.tagName + " "));
        return sb.toString();
    }
}
