package utask.commons.events.ui;

import utask.commons.events.BaseEvent;
import utask.model.task.ReadOnlyTask;

//@@author A0139996A
/*
 * This event is used to notify FindTaskOverlay to select last added or updated task
 * */
public class ShowTaskOfInterestInFindTaskOverlayEvent extends BaseEvent {

    public final ReadOnlyTask task;

    public ShowTaskOfInterestInFindTaskOverlayEvent(ReadOnlyTask task) {
        this.task = task;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
