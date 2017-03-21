//@@author A0139996A
package utask.staging.ui;

import java.util.ArrayList;

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


    private UTListViewHelper() {
        listviews = new ArrayList<ListView<ReadOnlyTask>>();
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

    public ListView<ReadOnlyTask> getActualListViewFromDisplayIndex(int displayIndex) {
        assert (displayIndex > 0);

        int  totalSize = 0;

        for (ListView<ReadOnlyTask> lv : listviews) {

            totalSize += lv.getItems().size();

            if (displayIndex <= totalSize) {
                return lv;
            }
        }

        return null;
    }

    public void updateListView() {
        if (listviews.size() > 1) {

            int totalSize = 0;

            for (int j = 1; j < listviews.size(); j++) {
                ListView<ReadOnlyTask> prevListView = listviews.get(j - 1);
                ListView<ReadOnlyTask> currListView = listviews.get(j);

                totalSize += prevListView.getItems().size();

                final int value = totalSize; //Required by Java compiler

                currListView.setCellFactory(l -> new TaskListViewCell(value));
            }

            for (ListView<ReadOnlyTask> lv : listviews) {
                lv.refresh();
            }
        }
    }

    public void scrollTo(ListView<ReadOnlyTask> listView, int index) {
        Platform.runLater(() -> {
            listView.scrollTo(index);
            listView.getSelectionModel().clearAndSelect(index);
        });
    }
}

