package utask.logic.commands;

import java.util.Set;

import utask.commons.exceptions.IllegalValueException;
import utask.model.tag.UniqueTagList;
import utask.model.task.FloatingTask;
import utask.model.task.Name;

//@@author A0139996A
public class CreateFloatingTaskCommand extends CreateCommand {

    public CreateFloatingTaskCommand(String name, String frequency,
            Set<String> tags, String status) throws IllegalValueException {
        super(frequency, tags, status);

        this.toAdd = new FloatingTask(new Name(name), this.frequency,
                new UniqueTagList(tagSet), this.status);
    }
}
