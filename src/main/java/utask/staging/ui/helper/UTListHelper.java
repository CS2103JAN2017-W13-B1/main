package utask.staging.ui.helper;

import java.util.ArrayList;
import java.util.HashMap;

import javafx.collections.transformation.FilteredList;
import javafx.scene.control.ListView;
import utask.commons.core.EventsCenter;

/*
 * UTListHelper groups similar method used by UTListHelper and UTListViewHelper
 *
 * Due to how different the actual type T is in the concrete classes, casting is unavoidable.
 * Although downcasting is a code smell, it is still used to avoid duplication of similar functions.
 *
 * */
public abstract class UTListHelper<T> {

    protected final ArrayList<T> lists;
    protected final HashMap<T, Integer> offsetMap;

    protected UTListHelper() {
        lists = new ArrayList<T>();
        offsetMap = new HashMap<T, Integer>();
        EventsCenter.getInstance().registerHandler(this);
    }

    public void addList(T list) {
        assert list != null;
        lists.add(list);
    }

    //TODO: It is not type safe use vargs with generics
    public void addList(T... list) {
        assert list != null;
        //assert list[0].get(0) instanceof ReadOnlyTask;

        for (int i = 0; i < list.length; i++) {
            addList(list[i]);
        }
    }

    protected void addToOffsetMap(T list, int offset) {
        offsetMap.put(list, offset);
    }

    /*
     * Normalise the given to index to actual numbering in list
     *
     * @param index is zero-based
     *
     * */
    protected int getActualIndexOfList(T list, int index) {
        int offset = offsetMap.get(list);
        return index - offset;
    }

    protected void updateOffsetMap() {
        addToOffsetMap(lists.get(0), 0); //First list starts counting from 1

        if (lists.size() > 1) { //There's no point to refresh one list, otherwise
            int totalSize = 0;

            //Traverse and update next listview index based on previous size
            for (int i = 1; i < lists.size(); i++) {
                T prevListView = lists.get(i - 1);
                T currListView = lists.get(i);

                int index = 0;
                //= ((FilteredList) prevListView).size();

                //Bad casting see reason above
                if (FilteredList.class.isInstance(prevListView)) {
                    index = ((FilteredList) prevListView).size();
                } else if (ListView.class.isInstance(prevListView)) {
                    index = ((ListView) prevListView).getItems().size();
                } else {
                    assert false : "N";
                }

                totalSize += index;
                addToOffsetMap(currListView, totalSize); //Temporary cache this value for faster calculation
            }
        }
    }

    /*
     * Searches for a listview given by the index
     *
     * @param index is zero-based
     *
     * */
    public T getActualListFromDisplayIndex(int index) {
        assert index >= 0;
        int totalSize = 0;

        for (T l : lists) {
            //Bad casting see reason above
            if (FilteredList.class.isInstance(l)) {
                totalSize += ((FilteredList) l).size();
            } else if (ListView.class.isInstance(l)) {
                totalSize += ((ListView) l).getItems().size();
            } else {
                assert false : "N";
            }

            if (index < totalSize) {
                return l;
            }
        }

        assert false : "This line is suppose to be unreachable";
        return null;
    }
}
