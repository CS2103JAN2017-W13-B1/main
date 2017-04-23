package utask.model.task;

import java.util.Optional;

import utask.commons.util.CollectionUtil;
import utask.model.tag.UniqueTagList;
import utask.model.task.abs.AbsEditTaskDescriptor;

//@@author A0138423J
public class EditTaskDescriptor extends AbsEditTaskDescriptor {
    public EditTaskDescriptor() {
    }

    public EditTaskDescriptor(EditTaskDescriptor toCopy) {
        name = toCopy.getName();
        deadLine = toCopy.getDeadline();
        timeStamp = toCopy.getTimeStamp();
        frequency = toCopy.getFrequency();
        tags = toCopy.getTags();
        status = toCopy.getStatus();
    }

    /**
     * Returns true if at least one field is edited.
     */
    public boolean isAnyFieldEdited() {
        return CollectionUtil.isAnyPresent(this.name, this.deadLine,
                this.timeStamp, this.frequency, this.tags,
                this.status);
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

    public void setIsCompleted(Optional<Status> status) {
        assert status != null;
        this.status = status;
    }

    public Optional<Status> getStatus() {
        return status;
    }

}
