package seedu.address.model.util;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.tag.UniqueTagList;
import seedu.utask.model.task.Deadline;
import seedu.utask.model.task.EventTask;
import seedu.utask.model.task.Frequency;
import seedu.utask.model.task.Name;
import seedu.utask.model.task.Task;
import seedu.utask.model.task.Timestamp;
import seedu.utask.model.task.UniqueTaskList.DuplicateTaskException;

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

    public static ReadOnlyAddressBook getSampleAddressBook() {
        try {
            AddressBook sampleAB = new AddressBook();
            for (Task samplePerson : getSamplePersons()) {
                sampleAB.addPerson(samplePerson);
            }
            return sampleAB;
        } catch (DuplicateTaskException e) {
            throw new AssertionError("sample data cannot contain duplicate persons", e);
        }
    }
}
