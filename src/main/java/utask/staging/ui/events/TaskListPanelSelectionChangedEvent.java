//@@author A0139996A
package utask.staging.ui.events;

import javafx.scene.control.ListView;
import utask.commons.events.BaseEvent;
import utask.model.task.ReadOnlyTask;

/**
 * Represents a selection change in the Task List Panel
 */
public class TaskListPanelSelectionChangedEvent extends BaseEvent {

    private final ReadOnlyTask newSelection;
    private final ListView<ReadOnlyTask> sender;

    public TaskListPanelSelectionChangedEvent(ListView<ReadOnlyTask> sender, ReadOnlyTask newSelection) {
        this.newSelection = newSelection;
        this.sender = sender;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

    public ListView<ReadOnlyTask> getSender() {
        return sender;
    }

    public ReadOnlyTask getNewSelection() {
        return newSelection;
    }
}
