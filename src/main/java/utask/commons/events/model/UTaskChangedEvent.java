package utask.commons.events.model;

import utask.commons.events.BaseEvent;
import utask.model.ReadOnlyAddressBook;

/** Indicates the AddressBook in the model has changed*/
public class UTaskChangedEvent extends BaseEvent {

    public final ReadOnlyAddressBook data;

    public UTaskChangedEvent(ReadOnlyAddressBook data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "number of tasks " + data.getTaskList().size() + ", number of tags " + data.getTagList().size();
    }
}
