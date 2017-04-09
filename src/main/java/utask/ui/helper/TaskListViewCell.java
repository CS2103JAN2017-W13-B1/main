package utask.ui.helper;

import com.jfoenix.controls.JFXListCell;

import utask.model.task.ReadOnlyTask;
import utask.ui.TaskListCard;

//@@author A0139996A
/*
 * TaskListViewCell renders TaskListCard in ListViews.
 * Note it extends JFXListCell, which ensure correct material styling are applied.
 * */
public class TaskListViewCell extends JFXListCell<ReadOnlyTask> {

    private int offset;

    public TaskListViewCell(int offset) {
        updateOffset(offset);
    }

    private void updateOffset(int offset) {
        this.offset = offset + 1;
    }

    @Override
    public void updateItem(ReadOnlyTask task, boolean empty) {
        super.updateItem(task, empty);

        if (empty || task == null) {
            setGraphic(null);
            setText(null);
        } else {
            setGraphic(new TaskListCard(task, getIndex() + offset).getRoot());
        }
    }
}
