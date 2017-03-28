//@@author A0139996A
package utask.staging.ui;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.common.eventbus.Subscribe;

import javafx.collections.transformation.FilteredList;
import utask.commons.core.EventsCenter;
import utask.commons.events.model.UTaskChangedEvent;
import utask.model.task.ReadOnlyTask;

/*
 * UTListHelper uses facade and singleton pattern
 * It provides an interface to simplify the manage of multiple underlying filterlist.
 *
 * */
public class UTListHelper {
    private static UTListHelper instance = null;
    private final ArrayList<FilteredList<ReadOnlyTask>> lists;
    private final HashMap<FilteredList<ReadOnlyTask>, Integer> offsetMap;

    private UTListHelper() {
        lists = new ArrayList<FilteredList<ReadOnlyTask>>();
        offsetMap = new HashMap<FilteredList<ReadOnlyTask>, Integer>();
        EventsCenter.getInstance().registerHandler(this);
    }

    public static UTListHelper getInstance() {
        if (instance == null) {
            instance = new UTListHelper();
        }

        return instance;
    }

    public void addListView(FilteredList<ReadOnlyTask>... list) {
        assert list != null;
//        assert list[0].get(0) instanceof ReadOnlyTask;

        for (int i = 0; i < list.length; i++) {
            addListView(list[i]);
        }
    }

    public void addListView(FilteredList<ReadOnlyTask> list) {
        assert list != null;
        lists.add(list);
    }

    public FilteredList<ReadOnlyTask> getUnderlyingListOfListViewByIndex(int index) {
        assert (index >= 0);

        FilteredList<ReadOnlyTask> list = getActualListViewFromDisplayIndex(index);

        assert(list != null);

        return list;
    }

    /*
     * Normalise the given to index to actual numbering in listview
     *
     * @param index is zero-based
     *
     * */
    public int getActualIndexFromDisplayIndex(int index) {
        FilteredList<ReadOnlyTask> lw = getActualListViewFromDisplayIndex(index);

        int actualInt = getActualIndexOfListView(lw, index);

        return actualInt;
    }

    /*
     * Normalise the given to index to actual numbering in listview
     *
     * @param index is zero-based
     *
     * */
    private int getActualIndexOfListView(FilteredList<ReadOnlyTask> listView, int index) {
        int offset = offsetMap.get(listView);

        if (index < offset) { //No need to normalise
            return index;
        }

        return index - offset;
    }

    /*
     * Searches for a listview given by the index
     *
     * @param index is zero-based
     *
     * */
    public FilteredList<ReadOnlyTask> getActualListViewFromDisplayIndex(int index) {
        int  totalSize = 0;

        for (FilteredList<ReadOnlyTask> l : lists) {

            totalSize += l.size();

            if (index < totalSize) {
                return l;
            }
        }

        assert false : "This line is suppose to be unreachable";
        return null;
    }

    /**
     *  Returns total sizes of all listview
     *
     *  Recalculation is necessary every time
     */
    public int getTotalSizeOfAllList() {

        assert(lists.size() > 0) :
            this.getClass() + " was used for the first time. Please add ListViews before calling this method";

        int  totalSize = 0;

        for (FilteredList<ReadOnlyTask> lv : lists) {
            totalSize += lv.size();
        }

        return totalSize;
    }

    public void refresh() {
        addToOffsetMap(lists.get(0), 1); //First list starts counting from 1

        if (lists.size() > 1) { //There's no point to refresh one list, otherwise
            int totalSize = 0;

            //Traverse and update next listview index based on previous size
            for (int i = 1; i < lists.size(); i++) {
                FilteredList<ReadOnlyTask> prevListView = lists.get(i - 1);
                FilteredList<ReadOnlyTask> currListView = lists.get(i);

                totalSize += prevListView.size();
                addToOffsetMap(currListView, totalSize); //Temporary cache this value for faster calculation
            }
        }
    }

    private void addToOffsetMap(FilteredList<ReadOnlyTask> lv, int offset) {
        offsetMap.put(lv, offset);
    }

    @Subscribe
    public void handleUTaskChangedEvent(UTaskChangedEvent e) {
        refresh();
    }
}
