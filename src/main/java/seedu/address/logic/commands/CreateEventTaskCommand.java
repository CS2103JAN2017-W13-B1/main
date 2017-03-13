package seedu.address.logic.commands;

import java.util.Set;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.tag.UniqueTagList;
import seedu.utask.model.task.Deadline;
import seedu.utask.model.task.EventTask;
import seedu.utask.model.task.Name;
import seedu.utask.model.task.Timestamp;

public class CreateEventTaskCommand extends CreateCommand {

    public CreateEventTaskCommand(String name, String deadline, String timestamp, String frequency, Set<String> tags)
            throws IllegalValueException {
        super(frequency, tags);

        this.toAdd = new EventTask(
                new Name(name),
                new Deadline(deadline),
                new Timestamp(timestamp),
                super.frequency,
                new UniqueTagList(tagSet));
    }

}
