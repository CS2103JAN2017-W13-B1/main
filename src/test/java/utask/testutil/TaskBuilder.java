package utask.testutil;

import utask.commons.exceptions.IllegalValueException;
import utask.model.tag.Tag;
import utask.model.tag.UniqueTagList;
import utask.model.task.Deadline;
import utask.model.task.Frequency;
import utask.model.task.Name;
import utask.model.task.Status;
import utask.model.task.Timestamp;

/**
 *
 */
public class TaskBuilder {

    private TestTask task;

    public TaskBuilder() {
        this.task = new TestTask();
    }

    /**
     * Initializes the TaskBuilder with the data of {@code taskToCopy}.
     */
    public TaskBuilder(TestTask taskToCopy) {
        this.task = new TestTask(taskToCopy);
    }

    public TaskBuilder withName(String name) throws IllegalValueException {
        this.task.setName(new Name(name));
        return this;
    }

    public TaskBuilder withTags(String ... tags) throws IllegalValueException {
        task.setTags(new UniqueTagList());
        for (String tag: tags) {
            task.getTags().add(new Tag(tag));
        }
        return this;
    }

    public TaskBuilder withFrequency(String frequency) throws IllegalValueException {
        this.task.setFrequency(new Frequency(frequency));
        return this;
    }

    public TaskBuilder withDeadline(String deadline) throws IllegalValueException {
        this.task.setDeadline(new Deadline(deadline));
        return this;
    }

    public TaskBuilder withTimestamp(String timestamp) throws IllegalValueException {
        this.task.setTimestamp(new Timestamp(timestamp));
        return this;
    }

    //author A0138423J
    public TaskBuilder withStatus(String status) throws IllegalValueException {
        this.task.setStatus(new Status(status));
        return this;
    }

    public TestTask build() {
        return this.task;
    }

}
