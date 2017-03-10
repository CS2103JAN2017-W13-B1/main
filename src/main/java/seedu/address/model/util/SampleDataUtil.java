package seedu.address.model.util;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.tag.UniqueTagList;
import seedu.utask.model.task.Deadline;
import seedu.utask.model.task.Frequency;
import seedu.utask.model.task.Name;
import seedu.utask.model.task.Task;
import seedu.utask.model.task.Timestamp;

import seedu.utask.model.task.UniqueTaskList.DuplicateTaskException;

public class SampleDataUtil {
    public static Task[] getSamplePersons() {
        try {
            return new Task[] {
                new Task(new Name("Alex Yeoh"), new Deadline("87438807"), new Timestamp("alexyeoh@gmail.com"),
                    new Frequency("Blk 30 Geylang Street 29, #06-40"),
                    new UniqueTagList("friends")),
                new Task(new Name("Bernice Yu"), new Deadline("99272758"), new Timestamp("berniceyu@gmail.com"),
                    new Frequency("Blk 30 Lorong 3 Serangoon Gardens, #07-18"),
                    new UniqueTagList("colleagues", "friends")),
                new Task(new Name("Charlotte Oliveiro"), new Deadline("93210283"), new Timestamp("charlotte@yahoo.com"),
                    new Frequency("Blk 11 Ang Mo Kio Street 74, #11-04"),
                    new UniqueTagList("neighbours")),
                new Task(new Name("David Li"), new Deadline("91031282"), new Timestamp("lidavid@google.com"),
                    new Frequency("Blk 436 Serangoon Gardens Street 26, #16-43"),
                    new UniqueTagList("family")),
                new Task(new Name("Irfan Ibrahim"), new Deadline("92492021"), new Timestamp("irfan@outlook.com"),
                    new Frequency("Blk 47 Tampines Street 20, #17-35"),
                    new UniqueTagList("classmates")),
                new Task(new Name("Roy Balakrishnan"), new Deadline("92624417"), new Timestamp("royb@gmail.com"),
                    new Frequency("Blk 45 Aljunied Street 85, #11-31"),
                    new UniqueTagList("colleagues"))
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
