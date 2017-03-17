package utask.model;

import java.util.Set;
import java.util.logging.Logger;

import javafx.collections.transformation.FilteredList;
import utask.commons.core.ComponentManager;
import utask.commons.core.LogsCenter;
import utask.commons.core.UnmodifiableObservableList;
import utask.commons.events.model.UTaskChangedEvent;
import utask.commons.util.CollectionUtil;
import utask.commons.util.StringUtil;
import utask.model.task.ReadOnlyTask;
import utask.model.task.Task;
import utask.model.task.UniqueTaskList;
import utask.model.task.UniqueTaskList.TaskNotFoundException;

/**
 * Represents the in-memory model of the address book data.
 * All changes to any model should be synchronized.
 */
public class ModelManager extends ComponentManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final UTask addressBook;
    private final FilteredList<ReadOnlyTask> filteredPersons;

    /**
     * Initializes a ModelManager with the given addressBook and userPrefs.
     */
    public ModelManager(ReadOnlyUTask addressBook, UserPrefs userPrefs) {
        super();
        assert !CollectionUtil.isAnyNull(addressBook, userPrefs);

        logger.fine("Initializing with address book: " + addressBook + " and user prefs " + userPrefs);

        this.addressBook = new UTask(addressBook);
        filteredPersons = new FilteredList<>(this.addressBook.getTaskList());
    }

    public ModelManager() {
        this(new UTask(), new UserPrefs());
    }

    @Override
    public void resetData(ReadOnlyUTask newData) {
        addressBook.resetData(newData);
        indicateAddressBookChanged();
    }

    @Override
    public ReadOnlyUTask getAddressBook() {
        return addressBook;
    }

    /** Raises an event to indicate the model has changed */
    private void indicateAddressBookChanged() {
        raise(new UTaskChangedEvent(addressBook));
    }

    @Override
    public synchronized void deletePerson(ReadOnlyTask target) throws TaskNotFoundException {
        addressBook.removeTask(target);
        indicateAddressBookChanged();
    }

    @Override
    public synchronized void addPerson(Task person) throws UniqueTaskList.DuplicateTaskException {
        addressBook.addTask(person);
        updateFilteredListToShowAll();
        indicateAddressBookChanged();
    }

    @Override
    public void updateTask(int filteredPersonListIndex, ReadOnlyTask editedPerson)
            throws UniqueTaskList.DuplicateTaskException {
        assert editedPerson != null;

        int addressBookIndex = filteredPersons.getSourceIndex(filteredPersonListIndex);
        addressBook.updateTask(addressBookIndex, editedPerson);
        indicateAddressBookChanged();
    }

    //=========== Filtered Person List Accessors =============================================================

    @Override
    public UnmodifiableObservableList<ReadOnlyTask> getFilteredTaskList() {
        return new UnmodifiableObservableList<>(filteredPersons);
    }

    @Override
    public void updateFilteredListToShowAll() {
        filteredPersons.setPredicate(null);
    }

    @Override
    public void updateFilteredPersonList(Set<String> keywords) {
        updateFilteredPersonList(new PredicateExpression(new NameQualifier(keywords)));
    }

    private void updateFilteredPersonList(Expression expression) {
        filteredPersons.setPredicate(expression::satisfies);
    }

    //========== Inner classes/interfaces used for filtering =================================================

    interface Expression {
        boolean satisfies(ReadOnlyTask person);
        String toString();
    }

    private class PredicateExpression implements Expression {

        private final Qualifier qualifier;

        PredicateExpression(Qualifier qualifier) {
            this.qualifier = qualifier;
        }

        @Override
        public boolean satisfies(ReadOnlyTask person) {
            return qualifier.run(person);
        }

        @Override
        public String toString() {
            return qualifier.toString();
        }
    }

    interface Qualifier {
        boolean run(ReadOnlyTask person);
        String toString();
    }

    private class NameQualifier implements Qualifier {
        private Set<String> nameKeyWords;

        NameQualifier(Set<String> nameKeyWords) {
            this.nameKeyWords = nameKeyWords;
        }

        @Override
        public boolean run(ReadOnlyTask person) {
            return nameKeyWords.stream()
                    .filter(keyword -> StringUtil.containsWordIgnoreCase(person.getName().fullName, keyword)
                            || StringUtil.containsWordIgnoreCase(person.getDeadline().value, keyword)
                            || StringUtil.containsWordIgnoreCase(person.getTimestamp().value, keyword)
                            || StringUtil.containsWordIgnoreCase(person.getFrequency().value, keyword)
                            || StringUtil.containsWordIgnoreCase(person.getTags().getAllTagNames(), keyword))
                    .findAny()
                    .isPresent();
        }

        @Override
        public String toString() {
            return "name=" + String.join(", ", nameKeyWords);
        }
    }

}
