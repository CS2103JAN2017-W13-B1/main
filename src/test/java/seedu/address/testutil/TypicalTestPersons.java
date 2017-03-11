package seedu.address.testutil;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.AddressBook;
import seedu.utask.model.task.Task;
import seedu.utask.model.task.UniqueTaskList;

/**
 *
 */
public class TypicalTestPersons {

    public TestTask a, b, c, d, e, f, g, h, i;

    public TypicalTestPersons() {
        try {
            a = new TaskBuilder().withName("Ascertain work has been complete")
                    .withFrequency("Every Friday").withTimestamp("from 0800 to 2000")
                    .withDeadline("011217")
                    .withTags("important").build();
            b = new TaskBuilder().withName("Busy with project").withFrequency("-")
                    .withTimestamp("from 0800 to 1230").withDeadline("010217")
                    .withTags("busy", "important").build();
            c = new TaskBuilder().withName("Cook Dinner Tonight").withDeadline("010317")
                    .withTimestamp("from 1500 to 1830").withFrequency("-").build();
            d = new TaskBuilder().withName("Dinner with Alice").withDeadline("010317")
                    .withTimestamp("from 1600 to 1800").withFrequency("-").build();
            e = new TaskBuilder().withName("Eat at Mcdonalds").withDeadline("010117")
                    .withTimestamp("from 1700 to 1800").withFrequency("-").build();
            f = new TaskBuilder().withName("Free").withDeadline("131217")
                    .withTimestamp("from 1800 to 2359").withFrequency("Every Year").build();
            g = new TaskBuilder().withName("Go out with Alice").withDeadline("120317")
                    .withTimestamp("from 1900 to 2000").withFrequency("-").build();

            // Manually added
            h = new TaskBuilder().withName("Help Alice with project").withDeadline("020417")
                    .withTimestamp("from 0900 to 1300").withFrequency("-").build();
            i = new TaskBuilder().withName("Inspect Goods").withDeadline("010417")
                    .withTimestamp("from 0900 to 1300").withFrequency("-").build();
        } catch (IllegalValueException e) {
            e.printStackTrace();
            assert false : "not possible";
        }
    }

    public static void loadAddressBookWithSampleData(AddressBook ab) {
        for (TestTask person : new TypicalTestPersons().getTypicalPersons()) {
            try {
                ab.addPerson(new Task(person));
            } catch (UniqueTaskList.DuplicateTaskException e) {
                assert false : "not possible";
            }
        }
    }

    public TestTask[] getTypicalPersons() {
        return new TestTask[]{a, b, c, d, e, f, g};
    }

    public AddressBook getTypicalAddressBook() {
        AddressBook ab = new AddressBook();
        loadAddressBookWithSampleData(ab);
        return ab;
    }
}
