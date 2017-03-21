//@@author A0139996A
package utask.staging.ui;

import java.util.ArrayList;
import java.util.HashMap;

import javafx.application.Platform;
import javafx.scene.control.ListView;
import utask.model.task.ReadOnlyTask;

/*
 * UTListViewHelper coordinates multiple listview to ensure their index numbers are in running sequence
 *
 * */
public class UTListViewHelper {
    private static UTListViewHelper instance = null;
    private final ArrayList<ListView<ReadOnlyTask>> listviews;
    private final HashMap<ListView<ReadOnlyTask>, Integer> offsetMap;


    private UTListViewHelper() {
        listviews = new ArrayList<ListView<ReadOnlyTask>>();
        offsetMap = new HashMap<ListView<ReadOnlyTask>, Integer>();
    }

    public static UTListViewHelper getInstance() {
        if (instance == null) {
            instance = new UTListViewHelper();
        }

        return instance;
    }

    public void add(ListView<ReadOnlyTask> lw) {
        listviews.add(lw);
        lw.setCellFactory(l -> new TaskListViewCell(0));
    }

    public int getTotalSize() {
        int  totalSize = 0;

        for (ListView<ReadOnlyTask> lv : listviews) {

            totalSize += lv.getItems().size();
        }

        return totalSize;
    }

    public void updateListView() {
        if (listviews.size() > 1) {

            int totalSize = 0;

            addToOffsetMap(listviews.get(0), 1);

            for (int j = 1; j < listviews.size(); j++) {
                ListView<ReadOnlyTask> prevListView = listviews.get(j - 1);
                ListView<ReadOnlyTask> currListView = listviews.get(j);

                totalSize += prevListView.getItems().size();

                final int value = totalSize; //Required by Java compiler

                addToOffsetMap(currListView, value);
                currListView.setCellFactory(l -> new TaskListViewCell(value));
            }

            for (ListView<ReadOnlyTask> lv : listviews) {
                lv.refresh();
            }
        }
    }

    private void addToOffsetMap(ListView<ReadOnlyTask> lv, int offset) {
        offsetMap.put(lv, offset);
    }

    public void scrollTo(int index) {
        assert (index >= 0);

        ListView<ReadOnlyTask> listView = getActualListViewFromDisplayIndex(index);
        scrollTo(listView, index);
    }

    private void scrollTo(ListView<ReadOnlyTask> listView, int index) {
        Platform.runLater(() -> {

            int actualIndex = getActualIndex(listView, index);

            for (ListView<ReadOnlyTask> lv : listviews) {
                lv.getSelectionModel().clearSelection();
            }

            listView.scrollTo(actualIndex);
            listView.getSelectionModel().select(actualIndex);
        });
    }

    /*
     * Normalise the given to index to listview numbering
     *
     * @param index is zero-based
     *
     * */
    private int getActualIndex(ListView<ReadOnlyTask> listView, int index) {
        int offset = offsetMap.get(listView);

        if (index < offset) { //No need to normalise
            return index;
        }

        return index - offset;
    }

    /*
     * Searches for the listview given by the index
     *
     * @param index is zero-based
     *
     * */
    private ListView<ReadOnlyTask> getActualListViewFromDisplayIndex(int index) {
        int  totalSize = 0;

        for (ListView<ReadOnlyTask> lv : listviews) {

            totalSize += lv.getItems().size();

            if (index < totalSize) {
                return lv;
            }
        }

        return null;
    }
}

