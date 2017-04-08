package utask.model.task.stub;

import java.util.Optional;

import utask.model.tag.UniqueTagList;
import utask.model.task.Deadline;
import utask.model.task.Frequency;
import utask.model.task.Name;
import utask.model.task.Status;
import utask.model.task.Timestamp;
import utask.model.task.abs.AbsEditTaskDescriptor;

//@@author A0138423J
public class EditTaskDescriptorStub extends AbsEditTaskDescriptor {

    @Override
    public boolean isAnyFieldEdited() {
        return false;
    }

    @Override
    public void setName(Optional<Name> name) {
    }

    @Override
    public Optional<Name> getName() {
        return null;
    }

    @Override
    public void setDeadline(Optional<Deadline> deadLine) {
    }

    @Override
    public Optional<Deadline> getDeadline() {
        return null;
    }

    @Override
    public void setTimeStamp(Optional<Timestamp> timeStamp) {
    }

    @Override
    public Optional<Timestamp> getTimeStamp() {
        return null;
    }

    @Override
    public void setFrequency(Optional<Frequency> frequency) {
    }

    @Override
    public Optional<Frequency> getFrequency() {
        return null;
    }

    @Override
    public void setTags(Optional<UniqueTagList> tags) {
    }

    @Override
    public Optional<UniqueTagList> getTags() {
        return null;
    }

    @Override
    public void setIsCompleted(Optional<Status> status) {
    }

    @Override
    public Optional<Status> getStatus() {
        return null;
    }

}
