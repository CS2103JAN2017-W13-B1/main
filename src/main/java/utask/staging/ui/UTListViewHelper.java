//@@author A0139996A
package utask.staging.ui;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.common.eventbus.Subscribe;

import javafx.application.Platform;
import javafx.scene.control.ListView;
import utask.commons.core.EventsCenter;
import utask.commons.events.model.UTaskChangedEvent;
import utask.commons.events.ui.JumpToTaskListRequestEvent;
import utask.commons.events.ui.ShowTaskOfInterestEvent;
import utask.model.task.ReadOnlyTask;
import utask.staging.ui.events.TaskListPanelSelectionChangedEvent;
import utask.staging.ui.helper.TaskListViewCell;

/*
 * UTListViewHelper uses facade and singleton pattern
 * It coordinates multiple listview to ensure their index numbers are in running sequence
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

    public void updateListViews() {
        Platform.runLater(() -> {
            addToOffsetMap(listViews.get(0), 0); //First list starts counting from 1

            if (listViews.size() > 1) { //There's no point to refresh one list, otherwise
                int totalSize = 0;

                //Traverse and update next listview index based on previous size
                for (int i = 1; i < listViews.size(); i++) {
                    ListView<ReadOnlyTask> prevListView = listViews.get(i - 1);
                    ListView<ReadOnlyTask> currListView = listViews.get(i);

                    totalSize += prevListView.getItems().size();

                    final int value = totalSize; //Required by Java compiler
                    currListView.setCellFactory(l -> new TaskListViewCell(value));

                    addToOffsetMap(currListView, value); //Temporary cache this value for faster calculation
                }
            }
        });
    }


    //TODO: Possible to use lazy rendering to prevent double rendering
    private void addDefaultCellFactory(ListView<ReadOnlyTask> lv) {
        lv.setCellFactory(l -> new TaskListViewCell(0));
    }

    private void addToOffsetMap(ListView<ReadOnlyTask> lv, int offset) {
        offsetMap.put(lv, offset);
    }

    /**
     *  Returns total sizes of all listview
     *
     *  Recalculation is necessary every time
     */
//    public int getTotalSizeOfAllListViews() {
//
//        assert(listViews.size() > 0) :
//            "UTListViewHelper was used for the first time. Please add ListViews before calling this method";
//
//        int  totalSize = 0;
//
//        for (ListView<ReadOnlyTask> lv : listViews) {
//            totalSize += lv.getItems().size();
//        }
//
//        return totalSize;
//    }

    /*
     * Normalise the given to index to actual numbering in listview
     *
     * @param index is zero-based
     *
     * */
//    public int getActualIndexFromDisplayIndex(int index) {
//        ListView<ReadOnlyTask> lw = getActualListViewFromDisplayIndex(index);
//
//        int actualInt = getActualIndexOfListView(lw, index);
//
//        return actualInt;
//    }

    /*
     * Normalise the given to index to actual numbering in listview
     *
     * @param index is zero-based
     *
     * */
    private int getActualIndexOfListView(ListView<ReadOnlyTask> listView, int index) {
        int offset = offsetMap.get(listView);

//        if (index < offset) { //No need to normalise
//            return index;
//        }

        return index - offset;
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

    private ListView<ReadOnlyTask> getActualListViewFromReadOnlyTask(ReadOnlyTask task) {

        for (ListView<ReadOnlyTask> lv : listViews) {

            if (lv.getItems().contains(task)) {
                return lv;
            }
        }

        return null;
    }

    public int getDisplayedIndexFromReadOnlyTask(ReadOnlyTask task) {

        ListView<ReadOnlyTask>  list = getActualListViewFromReadOnlyTask(task);

        int position = list.getItems().indexOf(task);
        int offset = offsetMap.get(list);

        return offset + position;
    }

//    public ObservableList<ReadOnlyTask> getUnderlyingListOfListViewByIndex(int index) {
//        assert (index >= 0);
//
//        ListView<ReadOnlyTask> listView = getActualListViewFromDisplayIndex(index);
//
//        assert(listView != null);
//
//        return listView.getItems();
//    }

    public void clearSelectionOfAllListViews() {
        for (ListView<ReadOnlyTask> lv : listViews) {
            lv.getSelectionModel().clearSelection();
        }
    }

    public void scrollTo(int index) {
        Platform.runLater(() -> {
        assert (index >= 0);
            ListView<ReadOnlyTask> listView = getActualListViewFromDisplayIndex(index);

            assert(listView != null);

            int actualIndex = getActualIndexOfListView(listView, index);

            EventsCenter.getInstance().post(new JumpToTaskListRequestEvent(listView, actualIndex));

            scrollTo(listView, actualIndex);
        });
    }

    private void scrollTo(ListView<ReadOnlyTask> listView, int actualIndex) {
        Platform.runLater(() -> {
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

    @Subscribe
    public void handleUTaskChangedEvent(UTaskChangedEvent e) {
        updateListViews();
    }

    @Subscribe
    public void handleShowTaskOfInterestEvent(ShowTaskOfInterestEvent e) {
        int displayIndex = getDisplayedIndexFromReadOnlyTask(e.task);
        scrollTo(displayIndex);
    }
}
