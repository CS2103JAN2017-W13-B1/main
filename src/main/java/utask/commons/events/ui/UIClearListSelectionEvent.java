package utask.commons.events.ui;

import utask.commons.events.BaseEvent;

/*
 * This event is used to notify UTListViewHelper
 *  to clear all ListViews selection
 * */
public class UIClearListSelectionEvent extends BaseEvent {
    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
