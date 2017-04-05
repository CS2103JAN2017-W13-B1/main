package utask.model;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.logging.Logger;

import javafx.collections.transformation.FilteredList;
import utask.commons.comparators.AscendingAlphabeticalComparator;
import utask.commons.comparators.DescendingAlphabeticalComparator;
import utask.commons.comparators.EarliestDeadlineComparator;
import utask.commons.comparators.LatestDeadlineComparator;
import utask.commons.comparators.TagsNameComparator;
import utask.commons.core.ComponentManager;
import utask.commons.core.LogsCenter;
import utask.commons.core.UnmodifiableObservableList;
import utask.commons.events.model.UTaskChangedEvent;
import utask.commons.util.CollectionUtil;
import utask.commons.util.StringUtil;
import utask.logic.commands.inteface.ReversibleCommand;
import utask.model.tag.Tag;
import utask.model.tag.UniqueTagList.DuplicateTagException;
import utask.model.task.ReadOnlyTask;
import utask.model.task.Task;
import utask.model.task.UniqueTaskList;
import utask.model.task.UniqueTaskList.TaskNotFoundException;
import utask.staging.ui.helper.UTFilteredListHelper;

/**
 * Represents the in-memory model of the UTask data.
 * All changes to any model should be synchronized.
 */
public class ModelManager extends ComponentManager implements Model {
    private String sortingConfig = null;
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final UTask uTask;
    private final UserPrefs userPrefs;
    private final AliasMap aliasMap;
    private final FilteredList<ReadOnlyTask> filteredFindTasks;

    private final FilteredList<ReadOnlyTask> dueTasks;
    private final FilteredList<ReadOnlyTask> todayTasks;
    private final FilteredList<ReadOnlyTask> tomorrowTasks;
    private final FilteredList<ReadOnlyTask> futureTasks;
    private final FilteredList<ReadOnlyTask> floatingTasks;

    private final Stack<ReversibleCommand> undoStack;
    private final Stack<ReversibleCommand> redoStack;
    /**
     * Initializes a ModelManager with the given UTask and userPrefs.
     */
    public ModelManager(ReadOnlyUTask uTask, UserPrefs userPrefs) {
        super();
        assert !CollectionUtil.isAnyNull(uTask, userPrefs);

        logger.fine("Initializing with UTask: " + uTask + " and user prefs " + userPrefs);

        this.uTask = new UTask(uTask);
        this.aliasMap = new AliasMap();
        this.userPrefs = userPrefs;
        undoStack = new Stack<ReversibleCommand>();
        redoStack = new Stack<ReversibleCommand>();
        filteredFindTasks = new FilteredList<>(this.uTask.getTaskList());

        Date todayDate = new Date();
        Date yesterdayDate = new Date();
        yesterdayDate.setDate(yesterdayDate.getDate() - 1);
        Date tomorrowDate = new Date();
        tomorrowDate.setDate(tomorrowDate.getDate() + 1);

        dueTasks = getTasksFliteredListByBeforeGivenDate(yesterdayDate);
        todayTasks = getTasksFliteredListByExactDate(todayDate);
        tomorrowTasks = getTasksFliteredListByExactDate(tomorrowDate);
        futureTasks = getTasksFliteredListByAfterGivenDate(tomorrowDate);
        floatingTasks = getFloatingTaskFliteredListByEmptyDeadlineAndTimestamp();

        UTFilteredListHelper.getInstance().addList(dueTasks, todayTasks, tomorrowTasks, futureTasks, floatingTasks);

//        UTFliterListHelper.getInstance().addList(dueTasks);
//        UTFliterListHelper.getInstance().addList(todayTasks);
//        UTFliterListHelper.getInstance().addList(tomorrowTasks);
//        UTFliterListHelper.getInstance().addList(futureTasks);
//        UTFliterListHelper.getInstance().addList(floatingTasks);

        UTFilteredListHelper.getInstance().addFindFilteredList(filteredFindTasks);
        sortingConfig = Model.SORT_ORDER_DEFAULT;
        sortFilteredTaskList(sortingConfig);
    }

    public ModelManager() {
        this(new UTask(), new UserPrefs());
    }

    //@@author A0139996A
    @Override
    public void addUndoCommand(ReversibleCommand undoCommand) {
        undoStack.push(undoCommand);
    }

    @Override
    public ReversibleCommand getUndoCommand() {
        return undoStack.pop();
    }

    @Override
    public int getUndoCommandCount() {
        return undoStack.size();
    }

    @Override
    public void addRedoCommand(ReversibleCommand redoCommand) {
        redoStack.push(redoCommand);
    }

    @Override
    public ReversibleCommand getRedoCommand() {
        return redoStack.pop();
    }

    @Override
    public int getRedoCommandCount() {
        return redoStack.size();
    }
    //@@author

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
        UTFilteredListHelper.getInstance().refresh();
        updateFilteredListToShowAll();
        indicateUTaskChanged();
    }

    @Override
    public synchronized void addTask(Task task) throws UniqueTaskList.DuplicateTaskException {
        uTask.addTask(task);

        //TODO: DUPLICATES
        UTFilteredListHelper.getInstance().refresh();
        updateFilteredListToShowAll();
        sortFilteredTaskList(sortingConfig);
    }

    @Override
    public synchronized void updateTask(int filteredTaskListIndex, ReadOnlyTask editedTask)
            throws UniqueTaskList.DuplicateTaskException {
        assert editedTask != null;

        int uTaskIndex = filteredFindTasks.getSourceIndex(filteredTaskListIndex);
        uTask.updateTask(uTaskIndex, editedTask);

        //TODO: DUPLICATES
        UTFilteredListHelper.getInstance().refresh();
        updateFilteredListToShowAll();
        sortFilteredTaskList(sortingConfig);
    }

    //@@author A0138423J
    @Override
    public synchronized void updateTask(ReadOnlyTask taskToEdit, ReadOnlyTask editedTask)
            throws UniqueTaskList.DuplicateTaskException {
        assert taskToEdit != null;
        assert editedTask != null;

        uTask.updateTask(taskToEdit, editedTask);

        //TODO: DUPLICATES
        UTFilteredListHelper.getInstance().refresh();
        updateFilteredListToShowAll();
        sortFilteredTaskList(sortingConfig);
    }

    @Override
    public void addTag(Tag tag) throws DuplicateTagException {
        assert tag != null;
        uTask.addTag(tag);
//        indicateUTaskChanged();
        UTFilteredListHelper.getInstance().refresh();
        sortFilteredTaskList(sortingConfig);
    }
    //@@author A0138423J

    //@@author A0139996A
    /** Gets total size of tasks in underlying lists of listviews*/
    @Override
    public int getTotalSizeOfLists() {
        return UTFilteredListHelper.getInstance().getTotalSizeOfAllList();
    }

    @Override
    public void setIfFindOverlayShowing(boolean isShowing) {
        UTFilteredListHelper.getInstance().setIfFindOverlayShowing(isShowing);
    }

    @Override
    public boolean isFindOverlayShowing() {
        return UTFilteredListHelper.getInstance().isFindOverlayShowing();
    }


    //TODO: refractor
    @Override
    public List<ReadOnlyTask> getUnderlyingListByIndex(int displayIndex) {
        return UTFilteredListHelper.getInstance().getUnderlyingListByIndex(displayIndex);
    }

    @Override
    public int getActualIndexFromDisplayIndex(int displayIndex) {
        return UTFilteredListHelper.getInstance().getActualIndexFromDisplayIndex(displayIndex);
    }
    //@author

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
        return new UnmodifiableObservableList<>(filteredFindTasks);
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
        filteredFindTasks.setPredicate(null);
    }

    @Override
    public void updateFilteredTaskList(Set<String> keywords) {
        updateFilteredTaskList(new PredicateExpression(new FindQualifier(keywords)));
    }

    private void updateFilteredTaskList(Expression expression) {
        filteredFindTasks.setPredicate(expression::satisfies);
    }

    //TODO: Mark for deletion
    //@@author A0139996A
    public void updateFilteredTaskListByKeywords(String keywords) {
        filteredFindTasks.setPredicate(task -> {
            // If filter text is empty, display all persons.
            if (keywords == null || keywords.isEmpty()) {
                return true;
            }

            //Set filter text as lower case, so to be case insensitive
            String lowerCaseFilter = keywords.toLowerCase();

            //TODO: Build comprehensive search
            return task.getName().fullName.toLowerCase().contains(lowerCaseFilter) ||
                   task.getTags().getAllTagNames().toLowerCase().contains(lowerCaseFilter);
        });
    }

    private void updateFilteredList(FilteredList<ReadOnlyTask> list, Expression expression) {
        list.setPredicate(expression::satisfies);
    }

    //@@author A0138493W
    @Override
    public void sortFilteredTaskList(String sortingOrder) {
        assert sortingOrder != null;
        switch(sortingOrder) {
        case "": //default sorting order
            uTask.sortByComparator(new EarliestDeadlineComparator());
            setSortingConfig(sortingOrder);
            break;

        case Model.SORT_ORDER_BY_EARLIEST_FIRST:
            uTask.sortByComparator(new EarliestDeadlineComparator());
            setSortingConfig(sortingOrder);
            break;

        case Model.SORT_ORDER_BY_LATEST_FIRST:
            uTask.sortByComparator(new LatestDeadlineComparator());
            setSortingConfig(sortingOrder);
            break;

        case Model.SORT_ORDER_BY_A_TO_Z:
            uTask.sortByComparator(new AscendingAlphabeticalComparator());
            setSortingConfig(sortingOrder);
            break;

        case Model.SORT_ORDER_BY_Z_TO_A:
            uTask.sortByComparator(new DescendingAlphabeticalComparator());
            setSortingConfig(sortingOrder);
            break;

        case Model.SORT_ORDER_BY_TAG:
            uTask.sortByComparator(new TagsNameComparator());
            setSortingConfig(sortingOrder);
            break;

        default:
            logger.warning(Model.SORT_ORDER_ERROR + sortingOrder);
            break;
        }
        indicateUTaskChanged();
    }

    /*
     * This method is used to set user sorting configuration
     */
    private void setSortingConfig(String userConfig) {
        assert userConfig != null;
        logger.fine("The sorting order is set as " + userConfig);
        this.sortingConfig = userConfig;
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

    //@@author A0138493W
    private class FindQualifier implements Qualifier {
        private Set<String> nameKeyWords;

        FindQualifier(Set<String> nameKeyWords) {
            this.nameKeyWords = nameKeyWords;
        }

        @Override
        public boolean run(ReadOnlyTask task) {
            return nameKeyWords.stream()
                    .filter(keyword -> StringUtil.containsWordIgnoreCase(task.getName().fullName, keyword)
                            || StringUtil.containsWordIgnoreCase(task.getDeadline().value, keyword)
                            || StringUtil.containsWordIgnoreCase(task.getTimestamp().value, keyword)
                            || StringUtil.containsWordIgnoreCase(task.getFrequency().value, keyword)
                            || StringUtil.containsWordIgnoreCase(task.getStatus().toString(), keyword)
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
                        && taskDate.getDate() == date.getDate()
                        && !task.getStatus().isStatusComplete();
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
                return task.getDeadline().getDate().before(date)
                        && !task.getStatus().isStatusComplete();
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
                return task.getDeadline().getDate().after(date)
                        && !task.getStatus().isStatusComplete();
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
            if (task.getDeadline().isEmpty() && task.getTimestamp().isEmpty() && !task.getStatus().isStatusComplete()) {
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

    @Override
    public UserPrefs getUserPrefs() {
        return userPrefs;
    }
}
