package utask.logic.commands;

import java.util.Set;

import utask.commons.exceptions.IllegalValueException;
import utask.model.tag.UniqueTagList;
import utask.model.task.Deadline;
import utask.model.task.DeadlineTask;
import utask.model.task.Name;

public class CreateDeadlineTaskCommand extends CreateCommand {

    public CreateDeadlineTaskCommand(String name, String deadline, String frequency, Set<String> tags)
            throws IllegalValueException {
        super(frequency, tags);

        this.toAdd = new DeadlineTask(
                new Name(name),
                new Deadline(deadline),
                this.frequency,
                new UniqueTagList(tagSet));
    }

}
