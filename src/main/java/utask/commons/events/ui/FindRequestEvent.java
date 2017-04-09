package utask.commons.events.ui;

import utask.commons.events.BaseEvent;

//@@author A0139996A
//An event to notify FindTaskOverlay to show its UI
public class FindRequestEvent extends BaseEvent {
    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
