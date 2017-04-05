package utask.commons.events.ui;

import java.util.List;

import utask.commons.events.BaseEvent;
import utask.model.tag.Tag;

/*
 * This event is used to notify UTMainWindow to show a tag color dialog
 * */
public class UIShowTagColorDialogEvent extends BaseEvent {

    public final List<Tag> tags;

    public UIShowTagColorDialogEvent(List<Tag> tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
