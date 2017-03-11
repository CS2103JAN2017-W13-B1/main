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
    private Frequency address;
    private Timestamp email;
    private Deadline phone;
    private UniqueTagList tags;

    public TestTask() {
        tags = new UniqueTagList();
    }

    /**
     * Creates a copy of {@code personToCopy}.
     */
    public TestTask(TestTask personToCopy) {
        this.name = personToCopy.getName();
        this.phone = personToCopy.getDeadline();
        this.email = personToCopy.getTimestamp();
        this.address = personToCopy.getFrequency();
        this.tags = personToCopy.getTags();
    }

    public void setName(Name name) {
        this.name = name;
    }

    public void setFrequency(Frequency address) {
        this.address = address;
    }

    public void setTimestamp(Timestamp email) {
        this.email = email;
    }

    public void setDeadline(Deadline phone) {
        this.phone = phone;
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
        return phone;
    }

    @Override
    public Timestamp getTimestamp() {
        return email;
    }

    @Override
    public Frequency getFrequency() {
        return address;
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
