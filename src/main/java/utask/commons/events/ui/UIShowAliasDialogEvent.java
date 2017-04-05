package utask.commons.events.ui;

import java.util.Map;

import utask.commons.events.BaseEvent;

/*
 * This event is used to notify UTMainWindow to show a alias dialog
 * */
public class UIShowAliasDialogEvent extends BaseEvent {

    public final Map<String, String> map;

    public UIShowAliasDialogEvent(Map<String, String> map) {
        this.map = map;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
