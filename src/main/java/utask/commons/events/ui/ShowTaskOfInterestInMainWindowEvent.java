package utask.commons.events.ui;

import utask.commons.events.BaseEvent;
import utask.model.task.ReadOnlyTask;

/*
 * This event is used to notify UTListViewHelper
 *  to select last added or updated task
 * */
public class ShowTaskOfInterestInMainWindowEvent extends BaseEvent {

    public final ReadOnlyTask task;

    public ShowTaskOfInterestInMainWindowEvent(ReadOnlyTask task) {
        this.task = task;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
