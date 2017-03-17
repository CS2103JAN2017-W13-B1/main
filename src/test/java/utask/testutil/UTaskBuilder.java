package utask.testutil;

import utask.commons.exceptions.IllegalValueException;
import utask.model.UTask;
import utask.model.tag.Tag;
import utask.model.task.Task;
import utask.model.task.UniqueTaskList;

/**
 * A utility class to help with building UTask objects.
 * Example usage: <br>
 *     {@code UTask ab = new UTaskBuilder().withTask("John", "Doe").withTag("Friend").build();}
 */
public class UTaskBuilder {

    private UTask uTask;

    public UTaskBuilder(UTask uTask) {
        this.uTask = uTask;
    }

    public UTaskBuilder withTask(Task task) throws UniqueTaskList.DuplicateTaskException {
        uTask.addTask(task);
        return this;
    }

    public UTaskBuilder withTag(String tagName) throws IllegalValueException {
        uTask.addTag(new Tag(tagName));
        return this;
    }

    public UTask build() {
        return uTask;
    }
}
