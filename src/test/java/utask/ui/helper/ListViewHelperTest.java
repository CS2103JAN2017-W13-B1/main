
package utask.ui.helper;

import static org.junit.Assert.assertNotNull;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import utask.model.task.ReadOnlyTask;

//@@author A0139996A
public class ListViewHelperTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void execute_getInstance_notNull() {
        assertNotNull(ListViewHelper.getInstance());
    }

    @Test
    public void add_nullList_notAllowed() {
        UTListView<ReadOnlyTask> lv = null;
        thrown.expect(AssertionError.class);
        ListViewHelper.getInstance().addList(lv);
    }

//      THE BELOW TESTS CANNOT BE UNIT TESTED. THEY REQUIRE JAVAFX UI
//    @Test
//    public void add_validListView_success() {
//        UTListView<ReadOnlyTask> lv = new UTListView<ReadOnlyTask>();
//        UTListViewHelper.getInstance().addList(lv);
//    }
//
//    @Test
//    public void execute_updateOnEmptyList_notAllowed() {
//        UTListViewHelper.getInstance().updateListViews();
//    }
}
