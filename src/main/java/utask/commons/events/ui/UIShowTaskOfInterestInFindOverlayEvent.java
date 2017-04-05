package utask.commons.events.ui;

import utask.commons.events.BaseEvent;
import utask.model.task.ReadOnlyTask;

/*
 * This event is used to notify UTFindOverlay
 *  to select last added or updated task
 * */
public class UIShowTaskOfInterestInFindOverlayEvent extends BaseEvent {

    public final ReadOnlyTask task;

    public UIShowTaskOfInterestInFindOverlayEvent(ReadOnlyTask task) {
        this.task = task;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
