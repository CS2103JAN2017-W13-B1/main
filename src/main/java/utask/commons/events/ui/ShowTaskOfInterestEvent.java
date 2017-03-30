package utask.commons.events.ui;

import utask.commons.events.BaseEvent;
import utask.model.task.ReadOnlyTask;

public class ShowTaskOfInterestEvent extends BaseEvent {

    public final ReadOnlyTask task;

    public ShowTaskOfInterestEvent(ReadOnlyTask task) {
        this.task = task;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
