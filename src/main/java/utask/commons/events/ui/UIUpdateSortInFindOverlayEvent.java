//@@author A0139996A
package utask.commons.events.ui;

import utask.commons.events.BaseEvent;

/*
 * This event is used to notify UTListViewHelper
 *  to select last added or updated task
 * */
public class UIUpdateSortInFindOverlayEvent extends BaseEvent {

    public final String columnAlphabet;
    public final String orderBy;

    public UIUpdateSortInFindOverlayEvent(String columnAlphabet, String orderBy) {
        this.columnAlphabet = columnAlphabet;
        this.orderBy = orderBy;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
