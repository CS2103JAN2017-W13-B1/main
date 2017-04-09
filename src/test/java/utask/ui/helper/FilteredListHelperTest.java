package utask.ui.helper;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import utask.commons.exceptions.IllegalValueException;
import utask.model.tag.UniqueTagList;
import utask.model.task.FloatingTask;
import utask.model.task.Frequency;
import utask.model.task.Name;
import utask.model.task.ReadOnlyTask;
import utask.model.task.Status;

//@@author A0139996A
public class FilteredListHelperTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void clearState() {
        FilteredListHelper.getInstance().clear();
    }

    @Test
    public void add_nullList_notAllowed() {
        FilteredList<ReadOnlyTask> lv = null;
        thrown.expect(AssertionError.class);
        FilteredListHelper.getInstance().addList(lv);
    }

    @Test
    public void add_validListView_success() {
        FilteredList<ReadOnlyTask> lv = new FilteredList<ReadOnlyTask>(FXCollections.emptyObservableList());
        FilteredListHelper.getInstance().addList(lv);
    }

    @Test
    public void execute_getInstance_notNull() {
        assertNotNull(FilteredListHelper.getInstance());
    }

    @Test
    public void execute_refreshOnEmptyList_notAllowed() {
        thrown.expect(AssertionError.class);
        FilteredListHelper.getInstance().refresh();
    }

    @Test
    public void execute_refreshOnNonEmptyList_success() {
        FilteredList<ReadOnlyTask> lv = new FilteredList<ReadOnlyTask>(FXCollections.emptyObservableList());
        FilteredListHelper.getInstance().addList(lv);
        FilteredListHelper.getInstance().refresh();
    }

    @Test
    public void getList_withNegativeIndex_notAllowed() {
        thrown.expect(AssertionError.class);
        FilteredListHelper.getInstance().getUnderlyingListByIndex(-1);
    }

    @Test
    public void getList_withValidIndexButEmptyList_notAllowed() {
        thrown.expect(AssertionError.class);
        FilteredListHelper.getInstance().getUnderlyingListByIndex(0);
    }

    @Test
    public void getList_withValidIndexValidList_success() {
        ObservableList<ReadOnlyTask> list = FXCollections.observableArrayList();
        //TODO: Create common test data helper
        try {
            ReadOnlyTask task = new FloatingTask(new Name("Test"),
                    Frequency.getEmptyFrequency(), new UniqueTagList(), Status.getEmptyStatus());
            list.add(task);
        } catch (IllegalValueException e) {
        }

        FilteredList<ReadOnlyTask> lv = new FilteredList<ReadOnlyTask>(list);
        FilteredListHelper.getInstance().addList(lv);
        FilteredListHelper.getInstance().getUnderlyingListByIndex(0);
    }

    @Test
    public void getActualIndex_withInvalidIndex_notAllowed() {
        thrown.expect(AssertionError.class);
        FilteredListHelper.getInstance().getActualIndexFromDisplayIndex(-1);
    }

    @Test
    public void getActualIndex_withValidIndexButEmptyList_notAllowed() {
        FilteredList<ReadOnlyTask> lv = new FilteredList<ReadOnlyTask>(FXCollections.emptyObservableList());
        FilteredListHelper.getInstance().addList(lv);

        thrown.expect(AssertionError.class);
        FilteredListHelper.getInstance().getActualIndexFromDisplayIndex(0);
    }
}
