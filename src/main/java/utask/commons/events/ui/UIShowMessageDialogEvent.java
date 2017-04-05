package utask.commons.events.ui;

import utask.commons.events.BaseEvent;

/*
 * This event is used to notify UTMainWindow to show a message dialog
 * */
public class UIShowMessageDialogEvent extends BaseEvent {

    public final String headingText;
    public final String contentText;

    public UIShowMessageDialogEvent(String headingText, String contentText) {
        this.headingText = headingText;
        this.contentText = contentText;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
