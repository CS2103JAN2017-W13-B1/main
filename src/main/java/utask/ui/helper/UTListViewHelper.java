//@@author A0139996A
package utask.ui.helper;

import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.application.Platform;
import javafx.scene.control.ListView;
import utask.commons.core.EventsCenter;
import utask.commons.core.LogsCenter;
import utask.commons.events.model.UTaskChangedEvent;
import utask.commons.events.ui.JumpToTaskListRequestEvent;
import utask.commons.events.ui.TaskListPanelSelectionChangedEvent;
import utask.commons.events.ui.UIShowTaskOfInterestInMainWindowEvent;
import utask.model.task.ReadOnlyTask;

/*
 * UTListViewHelper uses facade and singleton pattern
 * It coordinates multiple ListViews to ensure their index numbers are in running sequence
 * and also provides utility functions for retrieving and scrolling.
 *
 * */
public class UTListViewHelper extends UTListHelper<UTListView<ReadOnlyTask>, ReadOnlyTask> {
    private static UTListViewHelper instance = null;
    private final Logger logger = LogsCenter.getLogger(UTListViewHelper.class);

    private UTListViewHelper() {
        EventsCenter.getInstance().registerHandler(this);
    }

    public static UTListViewHelper getInstance() {
        if (instance == null) {
            instance = new UTListViewHelper();
        }

        return instance;
    }

    public void addList(UTListView<ReadOnlyTask> lv) {
        super.addList(lv);
        addDefaultCellFactory(lv);
        setEventHandlerForSelectionChangeEvent(lv);
    }

    //TODO: Possible to use lazy rendering to prevent double rendering
    private void addDefaultCellFactory(ListView<ReadOnlyTask> lv) {
        final int startIndex = 0;
        lv.setCellFactory(l -> new TaskListViewCell(startIndex));
    }

    //TODO: This function may look like updateOffsetMap() in parent but it has different feature
    //      Requires effort to correctly refractor this
    public void updateListViews() {
        Platform.runLater(() -> {
            addToOffsetMap(lists.get(0), 0); //First list starts counting from 1

            if (lists.size() > 1) { //There's no point to refresh one list, otherwise
                int totalSize = 0;

                //Traverse and update next listview index based on previous size
                for (int i = 1; i < lists.size(); i++) {
                    UTListView<ReadOnlyTask> prevListView = lists.get(i - 1);
                    UTListView<ReadOnlyTask> currListView = lists.get(i);

                    totalSize += prevListView.getItems().size();

                    final int value = totalSize; //Required by Java compiler
                    currListView.setCellFactory(l -> new TaskListViewCell(value));

                    addToOffsetMap(currListView, value); //Temporary cache this value for faster calculation
                }
            }
        });
    }

    private ListView<ReadOnlyTask> getActualListViewFromReadOnlyTask(ReadOnlyTask task) {
        for (ListView<ReadOnlyTask> lv : lists) {
            if (lv.getItems().contains(task)) {
                return lv;
            }
        }

        assert false : "Incorrect usage. ReadOnlyTask does not exists in ListView";
        return null;
    }

    /*
     * Gets display index of a ReadOnlyTask
     * */
    public int getDisplayedIndexFromReadOnlyTask(ReadOnlyTask task) {
        //Ensures the correctness when updating
        //i.e. Update that move a task from today list down to future list
        //Therefore, the lists after today list may have outdated offset of size + 1
        updateOffsetMap();

        ListView<ReadOnlyTask> list = getActualListViewFromReadOnlyTask(task);

        int position = list.getItems().indexOf(task); //Current position of task in list in zero-based indexing
        int offset = offsetMap.get(list);

        return offset + position;
    }

    private void clearSelectionOfAllListViews() {
        for (ListView<ReadOnlyTask> lv : lists) {
            lv.getSelectionModel().clearSelection();
        }
    }

    //TODO: Cleanup abit confusing?
    public void scrollTo(int index) {
        Platform.runLater(() -> {

        assert (index >= 0);
            UTListView<ReadOnlyTask> listView = getActualListFromDisplayIndex(index);

            assert(listView != null);

            int actualIndex = getActualIndexOfList(listView, index);

            EventsCenter.getInstance().post(new JumpToTaskListRequestEvent(listView, actualIndex));
            scrollTo(listView, actualIndex);
        });
    }

    private void scrollTo(ListView<ReadOnlyTask> listView, int actualIndex) {
        listView.scrollTo(actualIndex);
        listView.getSelectionModel().select(actualIndex);
    }

    private void setEventHandlerForSelectionChangeEvent(ListView<ReadOnlyTask> listView) {
        listView.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    //oldValue null and newValue !=null limits event propagation to only actual selection
                    //This prevents TaskListPaneSelectionChangedEvent from triggering during delete, update and done,
                    //which can go into a loop
                    if (oldValue == null && newValue != null) {
                        logger.fine("Selection in listview panel changed to : '" + newValue + "'");
                        EventsCenter.getInstance().post(new TaskListPanelSelectionChangedEvent(listView, newValue));
                    }
                });
    }

    @Subscribe
    public void handleTaskListPanelSelectionChangedEvent(TaskListPanelSelectionChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        clearSelectionOfAllListViews();
        ListView<ReadOnlyTask> sender = event.getSender();
        sender.getSelectionModel().select(event.getNewSelection());
    }

    @Subscribe
    public void handleUTaskChangedEvent(UTaskChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        updateListViews();
    }

    @Subscribe
    public void handleUIShowTaskOfInterestInMainWindowEvent(UIShowTaskOfInterestInMainWindowEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        int displayIndex = getDisplayedIndexFromReadOnlyTask(event.task);
        scrollTo(displayIndex);
    }

//    @Subscribe
//    public void handleClearListSelectionEvent(ClearListSelectionEvent event) {
//        logger.info(LogsCenter.getEventHandlingLogMessage(event));
//        clearSelectionOfAllListViews();
//    }
}
