package utask.staging.ui.helper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import utask.commons.core.EventsCenter;

/* UTListHelper groups similar method used by UTFilterListHelper and UTListViewHelper
 *
 * Due to how different the actual type T is in the concrete classes,
 * it necessary have a common abstraction to get item size.
 *
 * As ListView and FliterList have no common ascendant or interface.
 *
 * So, to prevent the need to cast generic types to get item size.
 * E.g.    ((FilteredList) T).size()
 *         ((ListView) T).getItems().size()
 *
 **/
public abstract class UTListHelper<T extends Collection<E>, E> {

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
     * Normalize the given to index to actual numbering in list
     *
     * @param index is zero-based
     * */
    protected int getActualIndexOfList(T list, int index) {
        int offset = offsetMap.get(list);
        return index - offset;
    }

    protected void updateOffsetMap() {
        addToOffsetMap(lists.get(0), 0); //First list starts counting from 0

        if (lists.size() > 1) { //There's no point to refresh one list, otherwise
            int totalSize = 0;

            //Traverse and update next OffsetMap based on previous size
            for (int i = 1; i < lists.size(); i++) {
                T prevListView = lists.get(i - 1);
                T currListView = lists.get(i);

                totalSize += prevListView.size(); //Uses collection interface as to access different underlying function
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
            totalSize += l.size(); //Uses collection interface as to access different underlying function

            if (index < totalSize) {
                return l;
            }
        }

        assert false : "This line is suppose to be unreachable. Display Index provided was " + index;
        return null;
    }
}
