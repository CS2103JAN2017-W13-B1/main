//@@author A0139996A
package utask.staging.ui.helper;

import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.collections.transformation.FilteredList;
import utask.commons.core.EventsCenter;
import utask.commons.core.LogsCenter;
import utask.commons.events.model.UTaskChangedEvent;
import utask.model.task.ReadOnlyTask;
import utask.staging.ui.UTFindTaskOverlay;

/*
 * UTListHelper uses facade and singleton pattern
 * It provides an interface to simplify the manage of multiple underlying FliterList.
 *
 * */
public class UTFilteredListHelper extends UTListHelper<FilteredList<ReadOnlyTask>, ReadOnlyTask> {
    private static UTFilteredListHelper instance = null;
    private boolean isFindOverlayShowing = false;
    private static final Logger logger = LogsCenter.getLogger(UTFilteredListHelper.class);

    private UTFilteredListHelper() {
        EventsCenter.getInstance().registerHandler(this);
    }

    public static UTFilteredListHelper getInstance() {
        if (instance == null) {
            instance = new UTFilteredListHelper();
        }

        return instance;
    }

    //Exposes function in a name that does not reveal actual implementation
    public void refresh() {
        updateOffsetMap();
    }

    //Exposes function in a name that does not reveal actual implementation
    public FilteredList<ReadOnlyTask> getUnderlyingListByIndex(int index) {
        if (isFindOverlayShowing) {
            //TODO: Use Multiton
            logger.info("FindOverlay is showing");
            return lists.get(lists.size() - 1);
        }
        return getActualListFromDisplayIndex(index);
    }

    /*
     * Normalise the given to index to actual numbering in ListView
     *
     * @param index is zero-based
     *
     * */
    public int getActualIndexFromDisplayIndex(int index) {
        assert index >= 0;

        if (isFindOverlayShowing) {
            return index; //WYSIWYG
        }

        FilteredList<ReadOnlyTask> lw = getActualListFromDisplayIndex(index);
        int actualInt = getActualIndexOfList(lw, index);

        return actualInt;
    }

    /**
     *  Returns total sizes of all ListView
     *
     *  Recalculation is necessary every time
     */
    public int getTotalSizeOfAllList() {

        assert(lists.size() > 0) :
            this.getClass() + " was used for the first time. Please add a ListView before calling this method";

        if (isFindOverlayShowing) {
            //TODO: Use Multiton
            return lists.get(lists.size() - 1).size();
        }

        int totalSize = 0;

        for (FilteredList<ReadOnlyTask> lv : lists) {
            totalSize += lv.size();
        }

        return totalSize;
    }

    public void setIfFindOverlayShowing(boolean isShowing) {
        isFindOverlayShowing = isShowing;
    }

    @Subscribe
    public void handleUTaskChangedEvent(UTaskChangedEvent e) {
        updateOffsetMap();
    }
}
