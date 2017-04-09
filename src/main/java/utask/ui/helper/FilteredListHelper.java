//@@author A0139996A
package utask.ui.helper;

import java.util.HashMap;
import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.collections.transformation.FilteredList;
import utask.commons.core.EventsCenter;
import utask.commons.core.LogsCenter;
import utask.commons.events.model.UTaskChangedEvent;
import utask.model.task.ReadOnlyTask;

/*
 * FilteredListHelper uses facade and singleton pattern
 * It provides an interface to simplify the manage of multiple underlying FliterList.
 *
 * */
public class FilteredListHelper extends ListHelper<FilteredList<ReadOnlyTask>, ReadOnlyTask> {
    private static final Logger logger = LogsCenter.getLogger(FilteredListHelper.class);
    private static final String NAMED_LIST_FIND_FILTERED_LIST = "FindFilteredList";

    private static FilteredListHelper instance = null;
    private boolean isFindOverlayShowing = false;
    private final HashMap<String, FilteredList<ReadOnlyTask>> namedList;

    private FilteredListHelper() {
        namedList = new HashMap<String, FilteredList<ReadOnlyTask>>();
        EventsCenter.getInstance().registerHandler(this);
    }

    public static FilteredListHelper getInstance() {
        if (instance == null) {
            instance = new FilteredListHelper();
        }

        return instance;
    }

    /*
     * Currently unused publicly, so use private access modifier
     * Uses Multiton pattern idea to retrieve named instance
     *
     * Named instance will not be chain processed with other lists
     * */
    private void addNamedList(String name, FilteredList<ReadOnlyTask> list) {
        assert name != "" && !name.isEmpty() : "Name provided for NamedList is invalid";
        assert list != null : "NamedList cannot be null";

        namedList.put(name, list);
    }

    public void addFindFilteredList(FilteredList<ReadOnlyTask> list) {
        addNamedList(NAMED_LIST_FIND_FILTERED_LIST, list);
    }

    private FilteredList<ReadOnlyTask> getFindFilteredList() {
        return namedList.get(NAMED_LIST_FIND_FILTERED_LIST);
    }

    //Exposes function in a name that does not reveal actual implementation
    public void refresh() {
        updateOffsetMap();
    }

    //Exposes function in a name that does not reveal actual implementation
    public FilteredList<ReadOnlyTask> getUnderlyingListByIndex(int index) {
        if (isFindOverlayShowing) {
            logger.info("FindOverlay is showing");
            return getFindFilteredList();
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
            logger.info("FindOverlay is showing");
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
            FilteredList<ReadOnlyTask> list = getFindFilteredList();
            return list.size();
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

    public boolean isFindOverlayShowing() {
        return isFindOverlayShowing;
    }

    @Subscribe
    public void handleUTaskChangedEvent(UTaskChangedEvent e) {
        updateOffsetMap();
    }
}
