package utask.testutil;

import utask.commons.exceptions.IllegalValueException;
import utask.model.UTask;
import utask.model.tag.Tag;
import utask.model.task.Task;
import utask.model.task.UniqueTaskList;

/**
 * A utility class to help with building Addressbook objects.
 * Example usage: <br>
 *     {@code AddressBook ab = new AddressBookBuilder().withPerson("John", "Doe").withTag("Friend").build();}
 */
public class UTaskBuilder {

    private UTask addressBook;

    public UTaskBuilder(UTask addressBook) {
        this.addressBook = addressBook;
    }

    public UTaskBuilder withPerson(Task person) throws UniqueTaskList.DuplicateTaskException {
        addressBook.addTask(person);
        return this;
    }

    public UTaskBuilder withTag(String tagName) throws IllegalValueException {
        addressBook.addTag(new Tag(tagName));
        return this;
    }

    public UTask build() {
        return addressBook;
    }
}
