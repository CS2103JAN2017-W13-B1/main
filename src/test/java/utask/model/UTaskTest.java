package utask.model;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utask.model.tag.Tag;
import utask.model.task.EventTask;
import utask.model.task.ReadOnlyTask;
import utask.model.task.Task;
import utask.testutil.TypicalTask;

public class UTaskTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private final UTask uTask = new UTask();

    @Test
    public void constructor() {
        assertEquals(Collections.emptyList(), uTask.getTaskList());
        assertEquals(Collections.emptyList(), uTask.getTagList());
    }

    @Test
    public void resetData_null_throwsAssertionError() {
        thrown.expect(AssertionError.class);
        uTask.resetData(null);
    }

    @Test
    public void resetData_withValidReadOnlyAddressBook_replacesData() {
        UTask newData = new TypicalTask().getTypicalAddressBook();
        uTask.resetData(newData);
        assertEquals(newData, uTask);
    }

    @Test
    public void resetData_withDuplicatePersons_throwsAssertionError() {
        TypicalTask td = new TypicalTask();
        // Repeat td.alice twice
        List<Task> newPersons = Arrays.asList(new EventTask(td.a), new EventTask(td.a));
        List<Tag> newTags = td.a.getTags().asObservableList();
        AddressBookStub newData = new AddressBookStub(newPersons, newTags);

        thrown.expect(AssertionError.class);
        uTask.resetData(newData);
    }

    @Test
    public void resetData_withDuplicateTags_throwsAssertionError() {
        UTask typicalAddressBook = new TypicalTask().getTypicalAddressBook();
        List<ReadOnlyTask> newPersons = typicalAddressBook.getTaskList();
        List<Tag> newTags = new ArrayList<>(typicalAddressBook.getTagList());
        // Repeat the first tag twice
        newTags.add(newTags.get(0));
        AddressBookStub newData = new AddressBookStub(newPersons, newTags);

        thrown.expect(AssertionError.class);
        uTask.resetData(newData);
    }

    /**
     * A stub ReadOnlyAddressBook whose persons and tags lists can violate interface constraints.
     */
    private static class AddressBookStub implements ReadOnlyUTask {
        private final ObservableList<ReadOnlyTask> persons = FXCollections.observableArrayList();
        private final ObservableList<Tag> tags = FXCollections.observableArrayList();

        AddressBookStub(Collection<? extends ReadOnlyTask> persons, Collection<? extends Tag> tags) {
            this.persons.setAll(persons);
            this.tags.setAll(tags);
        }

        @Override
        public ObservableList<ReadOnlyTask> getTaskList() {
            return persons;
        }

        @Override
        public ObservableList<Tag> getTagList() {
            return tags;
        }
    }

}
