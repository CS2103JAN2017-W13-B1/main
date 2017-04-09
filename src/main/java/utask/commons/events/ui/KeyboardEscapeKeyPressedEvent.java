package utask.commons.events.ui;

import utask.commons.events.BaseEvent;

//@@author A0139996A
/*
 * This event is used to notify all subscribe that escape key has been press,
 * which results in the dismissal of dialogs and overlay
 * */
public class KeyboardEscapeKeyPressedEvent extends BaseEvent {
    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
