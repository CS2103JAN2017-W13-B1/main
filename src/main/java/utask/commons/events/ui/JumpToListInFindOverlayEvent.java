package utask.commons.events.ui;

import utask.commons.events.BaseEvent;

/**
 * Indicates a request to jump to the list of tasks
 */
public class JumpToListInFindOverlayEvent extends BaseEvent {

    public final int targetIndex;

    public JumpToListInFindOverlayEvent(int targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
