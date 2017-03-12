package seedu.address.logic.commands;

import java.util.Set;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.tag.UniqueTagList;
import seedu.utask.model.task.Deadline;
import seedu.utask.model.task.DeadlineTask;
import seedu.utask.model.task.Frequency;
import seedu.utask.model.task.Name;

public class CreateDeadlineTaskCommand extends CreateCommand {

    public CreateDeadlineTaskCommand(String name, String deadline, String timestamp, String frequency, Set<String> tags)
            throws IllegalValueException {
        super(tags);

        this.toAdd = new DeadlineTask(
                new Name(name),
                new Deadline(deadline),
                new Frequency(frequency),
                new UniqueTagList(tagSet));
    }

}
