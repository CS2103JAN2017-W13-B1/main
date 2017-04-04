package utask.testutil;

import utask.commons.exceptions.IllegalValueException;
import utask.model.UTask;
import utask.model.tag.Tag;
import utask.model.tag.TagColorIndex;
import utask.model.tag.TagName;
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
        uTask.addTag(new Tag(new TagName(tagName), new TagColorIndex("2")));
        return this;
    }

    public UTask build() {
        return uTask;
    }
}
