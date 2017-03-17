package utask.testutil;

import utask.commons.exceptions.IllegalValueException;
import utask.model.AddressBook;
import utask.model.tag.Tag;
import utask.model.task.Task;
import utask.model.task.UniqueTaskList;

/**
 * A utility class to help with building Addressbook objects.
 * Example usage: <br>
 *     {@code AddressBook ab = new AddressBookBuilder().withPerson("John", "Doe").withTag("Friend").build();}
 */
public class UTaskBuilder {

    private AddressBook addressBook;

    public UTaskBuilder(AddressBook addressBook) {
        this.addressBook = addressBook;
    }

    public UTaskBuilder withPerson(Task person) throws UniqueTaskList.DuplicateTaskException {
        addressBook.addPerson(person);
        return this;
    }

    public UTaskBuilder withTag(String tagName) throws IllegalValueException {
        addressBook.addTag(new Tag(tagName));
        return this;
    }

    public AddressBook build() {
        return addressBook;
    }
}
