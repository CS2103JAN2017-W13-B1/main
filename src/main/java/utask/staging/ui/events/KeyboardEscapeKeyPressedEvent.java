package utask.staging.ui.events;

import utask.commons.events.BaseEvent;

public class KeyboardEscapeKeyPressedEvent extends BaseEvent {
    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
