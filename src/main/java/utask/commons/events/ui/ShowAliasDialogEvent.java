package utask.commons.events.ui;

import java.util.Map;

import utask.commons.events.BaseEvent;

//@@author A0139996A
/*
 * This event is used to notify UTMainWindow to show a alias dialog
 * */
public class ShowAliasDialogEvent extends BaseEvent {

    public final Map<String, String> map;

    public ShowAliasDialogEvent(Map<String, String> map) {
        this.map = map;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
