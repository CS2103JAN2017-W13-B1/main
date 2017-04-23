package utask.model.task.abs;

import java.util.Optional;

import utask.model.tag.UniqueTagList;
import utask.model.task.Deadline;
import utask.model.task.Frequency;
import utask.model.task.Name;
import utask.model.task.Status;
import utask.model.task.Timestamp;

//@@author A0138423J
public abstract class AbsEditTaskDescriptor {

    protected Optional<Name> name = Optional.empty();
    protected Optional<Deadline> deadLine = Optional.empty();
    protected Optional<Timestamp> timeStamp = Optional.empty();
    protected Optional<Frequency> frequency = Optional.empty();
    protected Optional<UniqueTagList> tags = Optional.empty();
    protected Optional<Status> status = Optional.empty();

    /**
     * Returns true if at least one field is edited.
     */
    public abstract boolean isAnyFieldEdited();

    public abstract void setName(Optional<Name> name);

    public abstract Optional<Name> getName();

    public abstract void setDeadline(Optional<Deadline> deadLine);

    public abstract Optional<Deadline> getDeadline();

    public abstract void setTimeStamp(Optional<Timestamp> timeStamp);

    public abstract Optional<Timestamp> getTimeStamp();

    public abstract void setFrequency(Optional<Frequency> frequency);

    public abstract Optional<Frequency> getFrequency();

    public abstract void setTags(Optional<UniqueTagList> tags);

    public abstract Optional<UniqueTagList> getTags();

    public abstract void setIsCompleted(Optional<Status> status);

    public abstract Optional<Status> getStatus();

}
