package utask.model.util;

import utask.commons.exceptions.IllegalValueException;
import utask.model.ReadOnlyUTask;
import utask.model.UTask;
import utask.model.tag.UniqueTagList;
import utask.model.task.Deadline;
import utask.model.task.EventTask;
import utask.model.task.Frequency;
import utask.model.task.Name;
import utask.model.task.Task;
import utask.model.task.Timestamp;
import utask.model.task.UniqueTaskList.DuplicateTaskException;

public class SampleDataUtil {
    public static Task[] getSamplePersons() {
        try {
            return new Task[] {
                new EventTask(new Name("My first task"), new Deadline("010117"), new Timestamp("0900 to 1000"),
                new Frequency("-"),
                new UniqueTagList("important"))
            };
        } catch (IllegalValueException e) {
            throw new AssertionError("sample data cannot be invalid", e);
        }
    }

    public static ReadOnlyUTask getSampleAddressBook() {
        try {
            UTask sampleAB = new UTask();
            for (Task sampleTask : getSamplePersons()) {
                sampleAB.addTask(sampleTask);
            }
            return sampleAB;
        } catch (DuplicateTaskException e) {
            throw new AssertionError("sample data cannot contain duplicate persons", e);
        }
    }
}