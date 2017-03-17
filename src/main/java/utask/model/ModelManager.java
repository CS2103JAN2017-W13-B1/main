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
 * Represents the in-memory model of the UTask data.
 * All changes to any model should be synchronized.
 */
public class ModelManager extends ComponentManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final UTask uTask;
    private final FilteredList<ReadOnlyTask> filteredTasks;

    /**
     * Initializes a ModelManager with the given UTask and userPrefs.
     */
    public ModelManager(ReadOnlyUTask uTask, UserPrefs userPrefs) {
        super();
        assert !CollectionUtil.isAnyNull(uTask, userPrefs);

        logger.fine("Initializing with UTask: " + uTask + " and user prefs " + userPrefs);

        this.uTask = new UTask(uTask);
        filteredTasks = new FilteredList<>(this.uTask.getTaskList());
    }

    public ModelManager() {
        this(new UTask(), new UserPrefs());
    }

    @Override
    public void resetData(ReadOnlyUTask newData) {
        uTask.resetData(newData);
        indicateUTaskChanged();
    }

    @Override
    public ReadOnlyUTask getUTask() {
        return uTask;
    }

    /** Raises an event to indicate the model has changed */
    private void indicateUTaskChanged() {
        raise(new UTaskChangedEvent(uTask));
    }

    @Override
    public synchronized void deleteTask(ReadOnlyTask target) throws TaskNotFoundException {
        uTask.removeTask(target);
        indicateUTaskChanged();
    }

    @Override
    public synchronized void addTask(Task task) throws UniqueTaskList.DuplicateTaskException {
        uTask.addTask(task);
        updateFilteredListToShowAll();
        indicateUTaskChanged();
    }

    @Override
    public void updateTask(int filteredTaskListIndex, ReadOnlyTask editedTask)
            throws UniqueTaskList.DuplicateTaskException {
        assert editedTask != null;

        int uTaskIndex = filteredTasks.getSourceIndex(filteredTaskListIndex);
        uTask.updateTask(uTaskIndex, editedTask);
        indicateUTaskChanged();
    }

    //=========== Filtered Task List Accessors =============================================================

    @Override
    public UnmodifiableObservableList<ReadOnlyTask> getFilteredTaskList() {
        return new UnmodifiableObservableList<>(filteredTasks);
    }

    @Override
    public void updateFilteredListToShowAll() {
        filteredTasks.setPredicate(null);
    }

    @Override
    public void updateFilteredTaskList(Set<String> keywords) {
        updateFilteredTaskList(new PredicateExpression(new NameQualifier(keywords)));
    }

    private void updateFilteredTaskList(Expression expression) {
        filteredTasks.setPredicate(expression::satisfies);
    }

    //========== Inner classes/interfaces used for filtering =================================================

    interface Expression {
        boolean satisfies(ReadOnlyTask task);
        String toString();
    }

    private class PredicateExpression implements Expression {

        private final Qualifier qualifier;

        PredicateExpression(Qualifier qualifier) {
            this.qualifier = qualifier;
        }

        @Override
        public boolean satisfies(ReadOnlyTask task) {
            return qualifier.run(task);
        }

        @Override
        public String toString() {
            return qualifier.toString();
        }
    }

    interface Qualifier {
        boolean run(ReadOnlyTask task);
        String toString();
    }

    private class NameQualifier implements Qualifier {
        private Set<String> nameKeyWords;

        NameQualifier(Set<String> nameKeyWords) {
            this.nameKeyWords = nameKeyWords;
        }

        @Override
        public boolean run(ReadOnlyTask task) {
            return nameKeyWords.stream()
                    .filter(keyword -> StringUtil.containsWordIgnoreCase(task.getName().fullName, keyword)
                            || StringUtil.containsWordIgnoreCase(task.getDeadline().value, keyword)
                            || StringUtil.containsWordIgnoreCase(task.getTimestamp().value, keyword)
                            || StringUtil.containsWordIgnoreCase(task.getFrequency().value, keyword)
                            || StringUtil.containsWordIgnoreCase(task.getTags().getAllTagNames(), keyword))
                    .findAny()
                    .isPresent();
        }

        @Override
        public String toString() {
            return "name=" + String.join(", ", nameKeyWords);
        }
    }

}
