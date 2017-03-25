//@@author A0139996A
package utask.staging.ui.events;

import utask.commons.events.BaseEvent;

public class FindRequestEvent extends BaseEvent {

    public final String findKeywords;

    public FindRequestEvent(String findKeywords) {
        this.findKeywords = findKeywords;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
