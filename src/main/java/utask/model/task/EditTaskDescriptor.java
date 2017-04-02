package utask.model.task;

import java.util.Optional;

import utask.commons.util.CollectionUtil;
import utask.model.tag.UniqueTagList;

public class EditTaskDescriptor {
    private Optional<Name> name = Optional.empty();
    private Optional<Deadline> deadLine = Optional.empty();
    private Optional<Timestamp> timeStamp = Optional.empty();
    private Optional<Frequency> frequency = Optional.empty();
    private Optional<UniqueTagList> tags = Optional.empty();
    private Optional<IsCompleted> isCompleted = Optional.empty();

    public EditTaskDescriptor() {
    }

    public EditTaskDescriptor(EditTaskDescriptor toCopy) {
        this.name = toCopy.getName();
        this.deadLine = toCopy.getDeadline();
        this.timeStamp = toCopy.getTimeStamp();
        this.frequency = toCopy.getFrequency();
        this.tags = toCopy.getTags();
        this.isCompleted = toCopy.getIsCompleted();
    }

    /**
     * Returns true if at least one field is edited.
     */
    public boolean isAnyFieldEdited() {
        return CollectionUtil.isAnyPresent(this.name, this.deadLine,
                this.timeStamp, this.frequency, this.tags,
                this.isCompleted);
    }

    public void setName(Optional<Name> name) {
        assert name != null;
        this.name = name;
    }

    public Optional<Name> getName() {
        return name;
    }

    public void setDeadline(Optional<Deadline> deadLine) {
        assert deadLine != null;
        this.deadLine = deadLine;
    }

    public Optional<Deadline> getDeadline() {
        return deadLine;
    }

    public void setTimeStamp(Optional<Timestamp> timeStamp) {
        assert timeStamp != null;
        this.timeStamp = timeStamp;
    }

    public Optional<Timestamp> getTimeStamp() {
        return timeStamp;
    }

    public void setFrequency(Optional<Frequency> frequency) {
        assert frequency != null;
        this.frequency = frequency;
    }

    public Optional<Frequency> getFrequency() {
        return frequency;
    }

    public void setTags(Optional<UniqueTagList> tags) {
        assert tags != null;
        this.tags = tags;
    }

    public Optional<UniqueTagList> getTags() {
        return tags;
    }

    public void setIsCompleted(Optional<IsCompleted> isCompleted) {
        assert isCompleted != null;
        this.isCompleted = isCompleted;
    }

    public Optional<IsCompleted> getIsCompleted() {
        return isCompleted;
    }

}
