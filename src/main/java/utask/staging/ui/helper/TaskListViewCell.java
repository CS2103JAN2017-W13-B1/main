package utask.staging.ui.helper;

import com.jfoenix.controls.JFXListCell;

import utask.model.task.ReadOnlyTask;
import utask.staging.ui.UTTaskListCard;

public class TaskListViewCell extends JFXListCell<ReadOnlyTask> {

    private int offset;

    public TaskListViewCell(int offset) {
        updateOffset(offset);
    }

    public void updateOffset(int offset) {
        this.offset = offset + 1;
    }

    @Override
    public void updateItem(ReadOnlyTask task, boolean empty) {
        super.updateItem(task, empty);

        if (empty || task == null) {
            setGraphic(null);
            setText(null);
        } else {
            setGraphic(new UTTaskListCard(task, getIndex() + offset).getRoot());
        }
    }
}
