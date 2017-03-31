//@@author A0139996A
package utask.staging.ui.helper;

import com.google.common.eventbus.Subscribe;

import javafx.collections.transformation.FilteredList;
import utask.commons.core.EventsCenter;
import utask.commons.events.model.UTaskChangedEvent;
import utask.model.task.ReadOnlyTask;

/*
 * UTListHelper uses facade and singleton pattern
 * It provides an interface to simplify the manage of multiple underlying filterlist.
 * */
public class UTFliterListHelper extends UTListHelper<FilteredList<ReadOnlyTask>, ReadOnlyTask> {
    private static UTFliterListHelper instance = null;

    private UTFliterListHelper() {
        EventsCenter.getInstance().registerHandler(this);
    }

    public static UTFliterListHelper getInstance() {
        if (instance == null) {
            instance = new UTFliterListHelper();
        }

        return instance;
    }

    //Exposes function in a name that does not reveal actual implementation
    public void refresh() {
        updateOffsetMap();
    }

    //Exposes function in a name that does not reveal actual implementation
    public FilteredList<ReadOnlyTask> getUnderlyingListOfListViewByIndex(int index) {
        return getActualListFromDisplayIndex(index);
    }

    /*
     * Normalise the given to index to actual numbering in ListView
     *
     * @param index is zero-based
     *
     * */
    public int getActualIndexFromDisplayIndex(int index) {
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

        int totalSize = 0;

        for (FilteredList<ReadOnlyTask> lv : lists) {
            totalSize += lv.size();
        }

        return totalSize;
    }

    @Subscribe
    public void handleUTaskChangedEvent(UTaskChangedEvent e) {
        updateOffsetMap();
    }
}
