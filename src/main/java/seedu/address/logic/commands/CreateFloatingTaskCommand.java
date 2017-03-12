package seedu.address.logic.commands;

import java.util.Set;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.tag.UniqueTagList;
import seedu.utask.model.task.FloatingTask;
import seedu.utask.model.task.Frequency;
import seedu.utask.model.task.Name;

public class CreateFloatingTaskCommand extends CreateCommand {

    public CreateFloatingTaskCommand(String name, String frequency, Set<String> tags)
            throws IllegalValueException {
        super(frequency, tags);

        //TODO: Code smells
        this.toAdd = new FloatingTask(
                new Name(name),
                super.frequency,
                new UniqueTagList(tagSet));
    }

}
