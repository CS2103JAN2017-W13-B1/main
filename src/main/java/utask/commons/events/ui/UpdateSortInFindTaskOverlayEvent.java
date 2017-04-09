package utask.commons.events.ui;

import utask.commons.events.BaseEvent;

//@@author A0139996A
/*
 * This event is used to notify ListViewHelper to select last added or updated task
 * */
public class UpdateSortInFindTaskOverlayEvent extends BaseEvent {

    public final String columnAlphabet;
    public final String orderBy;

    public UpdateSortInFindTaskOverlayEvent(String columnAlphabet, String orderBy) {
        this.columnAlphabet = columnAlphabet;
        this.orderBy = orderBy;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
