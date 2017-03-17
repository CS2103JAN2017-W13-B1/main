package utask.logic.commands;

import java.util.Set;

import utask.commons.exceptions.IllegalValueException;
import utask.model.tag.UniqueTagList;
import utask.model.task.FloatingTask;
import utask.model.task.Name;

public class CreateFloatingTaskCommand extends CreateCommand {

    public CreateFloatingTaskCommand(String name, String frequency, Set<String> tags)
            throws IllegalValueException {
        super(frequency, tags);

        //TODO: Code smells
        this.toAdd = new FloatingTask(
                new Name(name),
                this.frequency,
                new UniqueTagList(tagSet));
    }

}
