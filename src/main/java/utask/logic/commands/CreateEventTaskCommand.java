package utask.logic.commands;

import java.util.Set;

import utask.commons.exceptions.IllegalValueException;
import utask.model.tag.UniqueTagList;
import utask.model.task.Deadline;
import utask.model.task.EventTask;
import utask.model.task.Name;
import utask.model.task.Timestamp;

//@@author A0139996A
public class CreateEventTaskCommand extends CreateCommand {

    public CreateEventTaskCommand(String name, String deadline,
            String timestamp, String frequency, Set<String> tags,
            String status) throws IllegalValueException {
        super(frequency, tags, status);

        this.toAdd = new EventTask(new Name(name), new Deadline(deadline),
                new Timestamp(deadline, timestamp), this.frequency,
                new UniqueTagList(tagSet), this.status);
    }
}
