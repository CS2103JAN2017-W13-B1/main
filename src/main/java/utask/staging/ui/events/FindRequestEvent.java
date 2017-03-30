//@@author A0139996A
package utask.staging.ui.events;

import utask.commons.events.BaseEvent;

public class FindRequestEvent extends BaseEvent {

    public FindRequestEvent() {
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
