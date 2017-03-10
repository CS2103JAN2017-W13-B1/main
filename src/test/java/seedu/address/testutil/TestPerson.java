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
public class TestPerson implements ReadOnlyTask {

    private Name name;
    private Frequency address;
    private Timestamp email;
    private Deadline phone;
    private UniqueTagList tags;

    public TestPerson() {
        tags = new UniqueTagList();
    }

    /**
     * Creates a copy of {@code personToCopy}.
     */
    public TestPerson(TestPerson personToCopy) {
        this.name = personToCopy.getName();
        this.phone = personToCopy.getDeadline();
        this.email = personToCopy.getTimestamp();
        this.address = personToCopy.getFrequency();
        this.tags = personToCopy.getTags();
    }

    public void setName(Name name) {
        this.name = name;
    }

    public void setAddress(Frequency address) {
        this.address = address;
    }

    public void setEmail(Timestamp email) {
        this.email = email;
    }

    public void setPhone(Deadline phone) {
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
        sb.append("add " + this.getName().fullName + " ");
        sb.append("a/" + this.getFrequency().value + " ");
        sb.append("p/" + this.getDeadline().value + " ");
        sb.append("e/" + this.getTimestamp().value + " ");
        this.getTags().asObservableList().stream().forEach(s -> sb.append("t/" + s.tagName + " "));
        return sb.toString();
    }
}
