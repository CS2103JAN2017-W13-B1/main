package utask.model;

import java.text.ParseException;
import java.util.Date;
import java.util.Set;
import java.util.logging.Logger;

import javafx.collections.transformation.FilteredList;
import utask.commons.comparators.AToZComparator;
import utask.commons.comparators.EarliestFirstComparator;
import utask.commons.comparators.LatestFirstComparator;
import utask.commons.comparators.TagComparator;
import utask.commons.comparators.ZToAComparator;
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

    private final FilteredList<ReadOnlyTask> dueTasks;
    private final FilteredList<ReadOnlyTask> todayTasks;
    private final FilteredList<ReadOnlyTask> tomorrowTasks;
    private final FilteredList<ReadOnlyTask> futureTasks;
    private final FilteredList<ReadOnlyTask> floatingTasks;

    /**
     * Initializes a ModelManager with the given UTask and userPrefs.
     */
    public ModelManager(ReadOnlyUTask uTask, UserPrefs userPrefs) {
        super();
        assert !CollectionUtil.isAnyNull(uTask, userPrefs);

        logger.fine("Initializing with UTask: " + uTask + " and user prefs " + userPrefs);

        this.uTask = new UTask(uTask);
        filteredTasks = new FilteredList<>(this.uTask.getTaskList());

        Date todayDate = new Date();
        Date tomorrowDate = new Date();
        tomorrowDate.setDate(tomorrowDate.getDate() + 1);

        dueTasks = getTasksFliteredListByBeforeGivenDate(todayDate);
        todayTasks = getTasksFliteredListByExactDate(todayDate);
        tomorrowTasks = getTasksFliteredListByExactDate(tomorrowDate);
        futureTasks = getTasksFliteredListByAfterGivenDate(tomorrowDate);
        floatingTasks = getFloatingTaskFliteredListByEmptyDeadlineAndTimestamp();
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

    //@@author A0139996A
    private FilteredList<ReadOnlyTask> getTasksFliteredListByExactDate(Date date) {
        FilteredList<ReadOnlyTask> task = new FilteredList<>(this.uTask.getTaskList());
        updateFilteredList(task, new PredicateExpression(new ExactDateQualifier(date)));
        return task;
    }

    private FilteredList<ReadOnlyTask> getTasksFliteredListByBeforeGivenDate(Date date) {
        FilteredList<ReadOnlyTask> task = new FilteredList<>(this.uTask.getTaskList());
        updateFilteredList(task, new PredicateExpression(new DateBeforeQualifier(date)));
        return task;
    }

    private FilteredList<ReadOnlyTask> getTasksFliteredListByAfterGivenDate(Date date) {
        FilteredList<ReadOnlyTask> task = new FilteredList<>(this.uTask.getTaskList());
        updateFilteredList(task, new PredicateExpression(new DateAfterQualifier(date)));
        return task;
    }

    private FilteredList<ReadOnlyTask> getFloatingTaskFliteredListByEmptyDeadlineAndTimestamp() {
        FilteredList<ReadOnlyTask> task = new FilteredList<>(this.uTask.getTaskList());
        updateFilteredList(task, new PredicateExpression(new EmptyDeadlineAndTimestampQualifier()));
        return task;
    }
    //@@author

    //=========== Filtered Task List Accessors =============================================================

    @Override
    public UnmodifiableObservableList<ReadOnlyTask> getFilteredTaskList() {
        return new UnmodifiableObservableList<>(filteredTasks);
    }

    //@@author A0139996A
    @Override
    public UnmodifiableObservableList<ReadOnlyTask> getDueFilteredTaskList() {
        return new UnmodifiableObservableList<>(dueTasks);
    }

    @Override
    public UnmodifiableObservableList<ReadOnlyTask> getTodayFilteredTaskList() {
        return new UnmodifiableObservableList<>(todayTasks);
    }

    @Override
    public UnmodifiableObservableList<ReadOnlyTask> getTomorrowFilteredTaskList() {
        return new UnmodifiableObservableList<>(tomorrowTasks);
    }

    @Override
    public UnmodifiableObservableList<ReadOnlyTask> getFutureFilteredTaskList() {
        return new UnmodifiableObservableList<>(futureTasks);
    }

    @Override
    public UnmodifiableObservableList<ReadOnlyTask> getFloatingFilteredTaskList() {
        return new UnmodifiableObservableList<>(floatingTasks);
    }
    //@@author

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

    //@@author A0139996A
    private void updateFilteredList(FilteredList<ReadOnlyTask> list, Expression expression) {
        list.setPredicate(expression::satisfies);
    }

    //@@author A0138493W
    @Override
    public void sortFilteredTaskList(String sortingOrder) {
        assert sortingOrder != null;
        switch(sortingOrder) {
        case "": //default sorting order
            uTask.sortByComparator(new EarliestFirstComparator());
            break;

        case Model.SORT_ORDER_BY_EARLIEST_FIRST:
            uTask.sortByComparator(new EarliestFirstComparator());
            break;

        case Model.SORT_ORDER_BY_LATEST_FIRST:
            uTask.sortByComparator(new LatestFirstComparator());
            break;

        case Model.SORT_ORDER_BY_A_TO_Z:
            uTask.sortByComparator(new AToZComparator());
            break;

        case Model.SORT_ORDER_BY_Z_TO_A:
            uTask.sortByComparator(new ZToAComparator());
            break;

        case Model.SORT_ORDER_BY_TAG:
            uTask.sortByComparator(new TagComparator());
            break;

        default:
            logger.warning(Model.SORT_ORDER_ERROR + sortingOrder);
            break;
        }
        indicateUTaskChanged();
    }

    //@@author

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

    //@@author A0139996A
    private class ExactDateQualifier implements Qualifier {
        private Date date;

        ExactDateQualifier(Date date) {
            assert date != null;
            this.date = date;
        }

        @Override
        public boolean run(ReadOnlyTask task) {

            if (task.getDeadline().isEmpty()) {
                return false;
            }

            try {
                Date taskDate = task.getDeadline().getDate();
                return taskDate.getYear() == date.getYear()
                        && taskDate.getMonth() == date.getMonth()
                        && taskDate.getDay() == date.getDay();
            } catch (ParseException e) {
                assert false : "Date is in wrong format";
            }

            return false;
        }

        @Override
        public String toString() {
            return "date=" + date;
        }
    }

    private class DateBeforeQualifier implements Qualifier {
        private Date date;

        DateBeforeQualifier(Date date) {
            assert date != null;
            this.date = date;
        }

        @Override
        public boolean run(ReadOnlyTask task) {
            if (task.getDeadline().isEmpty()) {
                return false;
            }

            try {
                return task.getDeadline().getDate().before(date);
            } catch (ParseException e) {
                assert false : "Date is in wrong format";
            }
            return false;
        }

        @Override
        public String toString() {
            return "date=" + date.toString();
        }
    }

    private class DateAfterQualifier implements Qualifier {
        private Date date;

        DateAfterQualifier(Date date) {
            assert date != null;
            this.date = date;
        }

        @Override
        public boolean run(ReadOnlyTask task) {

            if (task.getDeadline().isEmpty()) {
                return false;
            }

            try {
                return task.getDeadline().getDate().after(date);
            } catch (ParseException e) {
                assert false : "Date is in wrong format";
            }

            return false;
        }

        @Override
        public String toString() {
            return "date=" + date.toString();
        }
    }

    private class EmptyDeadlineAndTimestampQualifier implements Qualifier {
        @Override
        public boolean run(ReadOnlyTask task) {
            if (task.getDeadline().isEmpty() && task.getTimestamp().isEmpty()) {
                return true;
            } else {
                return false;
            }
        }

        @Override
        public String toString() {
            return "deadline&timestamp=empty";
        }
    }
}
