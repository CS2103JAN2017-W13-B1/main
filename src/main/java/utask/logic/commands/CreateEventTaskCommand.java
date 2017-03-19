package utask.logic.commands;

import java.util.Set;

import utask.commons.exceptions.IllegalValueException;
import utask.model.tag.UniqueTagList;
import utask.model.task.Deadline;
import utask.model.task.EventTask;
import utask.model.task.Name;
import utask.model.task.Timestamp;

public class CreateEventTaskCommand extends CreateCommand {

    public CreateEventTaskCommand(String name, String deadline, String timestamp, String frequency, Set<String> tags)
            throws IllegalValueException {
        super(frequency, tags);

        this.toAdd = new EventTask(
                new Name(name),
                new Deadline(deadline),
                new Timestamp(timestamp),
                this.frequency,
                new UniqueTagList(tagSet));
    }

}
