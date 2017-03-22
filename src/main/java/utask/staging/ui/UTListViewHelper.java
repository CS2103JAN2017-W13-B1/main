//@@author A0139996A
package utask.staging.ui;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.common.eventbus.Subscribe;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import utask.commons.core.EventsCenter;
import utask.model.task.ReadOnlyTask;
import utask.staging.ui.events.TaskListPanelSelectionChangedEvent;
import utask.staging.ui.helper.TaskListViewCell;

/*
 * UTListViewHelper coordinates multiple listview to ensure their index numbers are in running sequence
 *
 * */
public class UTListViewHelper {
    private static UTListViewHelper instance = null;
    private final ArrayList<ListView<ReadOnlyTask>> listViews;
    private final HashMap<ListView<ReadOnlyTask>, Integer> offsetMap;

    private UTListViewHelper() {
        listViews = new ArrayList<ListView<ReadOnlyTask>>();
        offsetMap = new HashMap<ListView<ReadOnlyTask>, Integer>();
        EventsCenter.getInstance().registerHandler(this);
    }

    public static UTListViewHelper getInstance() {
        if (instance == null) {
            instance = new UTListViewHelper();
        }

        return instance;
    }

    public void addListView(ListView<ReadOnlyTask> lv) {
        listViews.add(lv);
        addDefaultCellFactory(lv);
    }

    private void addDefaultCellFactory(ListView<ReadOnlyTask> lv) {
        lv.setCellFactory(l -> new TaskListViewCell(0));
    }

    public void updateListViews() {
        Platform.runLater(() -> {
            addToOffsetMap(listViews.get(0), 1);

            if (listViews.size() > 1) { //There's no point to refresh one list
                int totalSize = 0;

                //Traverse and update following listview index based on previous size
                for (int j = 1; j < listViews.size(); j++) {
                    ListView<ReadOnlyTask> prevListView = listViews.get(j - 1);
                    ListView<ReadOnlyTask> currListView = listViews.get(j);

                    totalSize += prevListView.getItems().size();

                    final int value = totalSize; //Required by Java compiler

                    addToOffsetMap(currListView, value);
                    currListView.setCellFactory(l -> new TaskListViewCell(value));
                }

//            for (ListView<ReadOnlyTask> lv : listViews) {
//                lv.refresh();
//            }
            }
        });
    }

    public int getTotalSizeOfAllListViews() {
        int  totalSize = 0;

        for (ListView<ReadOnlyTask> lv : listViews) {
            totalSize += lv.getItems().size();
        }

        return totalSize;
    }

    private void addToOffsetMap(ListView<ReadOnlyTask> lv, int offset) {
        offsetMap.put(lv, offset);
    }

    /*
     * Normalise the given to index to listview numbering
     *
     * @param index is zero-based
     *
     * */
    private int getActualIndexOfListView(ListView<ReadOnlyTask> listView, int index) {
        int offset = offsetMap.get(listView);

        if (index < offset) { //No need to normalise
            return index;
        }

        return index - offset;
    }

    public int getActualIndexFromDisplayIndex(int displayIndex) {
        ListView<ReadOnlyTask> lw = getActualListViewFromDisplayIndex(displayIndex);

        int actualInt = getActualIndexOfListView(lw, displayIndex);

        return actualInt;
    }

    /*
     * Searches for a listview given by the index
     *
     * @param index is zero-based
     *
     * */
    public ListView<ReadOnlyTask> getActualListViewFromDisplayIndex(int index) {
        int  totalSize = 0;

        for (ListView<ReadOnlyTask> lv : listViews) {

            totalSize += lv.getItems().size();

            if (index < totalSize) {
                return lv;
            }
        }

        return null;
    }

    public ObservableList<ReadOnlyTask> getFilteredTaskList(int index) {
        assert (index >= 0);

        ListView<ReadOnlyTask> listView = getActualListViewFromDisplayIndex(index);

        assert(listView != null);

//        final UnmodifiableObservableList<ReadOnlyTask> finalList =
//                (UnmodifiableObservableList<ReadOnlyTask>) listView.getItems();

        return listView.getItems();
    }

    private void clearSelectionOfAllListViews() {
        for (ListView<ReadOnlyTask> lv : listViews) {
            lv.getSelectionModel().clearSelection();
        }
    }

    public void scrollTo(int index) {
        assert (index >= 0);

        ListView<ReadOnlyTask> listView = getActualListViewFromDisplayIndex(index);

        assert(listView != null);

        scrollTo(listView, index);
    }

    private void scrollTo(ListView<ReadOnlyTask> listView, int index) {
        Platform.runLater(() -> {
            int actualIndex = getActualIndexOfListView(listView, index);

            clearSelectionOfAllListViews();

            listView.scrollTo(actualIndex);
            listView.getSelectionModel().select(actualIndex);
        });
    }

    @Subscribe
    public void handleTaskListPanelSelectionChangedEvent(TaskListPanelSelectionChangedEvent e) {
        clearSelectionOfAllListViews();
        ListView<ReadOnlyTask> sender = e.getSender();
        sender.getSelectionModel().select(e.getNewSelection());
    }
}
