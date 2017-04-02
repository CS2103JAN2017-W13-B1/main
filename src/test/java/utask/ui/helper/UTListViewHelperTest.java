//@@author A0139996A
package utask.ui.helper;

import static org.junit.Assert.assertNotNull;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import utask.model.task.ReadOnlyTask;
import utask.staging.ui.helper.UTListView;
import utask.staging.ui.helper.UTListViewHelper;

public class UTListViewHelperTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void execute_getInstance_notNull() {
        assertNotNull(UTListViewHelper.getInstance());
    }

    @Test
    public void add_nullList_notAllowed() {
        UTListView<ReadOnlyTask> lv = null;
        thrown.expect(AssertionError.class);
        UTListViewHelper.getInstance().addList(lv);
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
