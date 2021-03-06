package utask.commons.events.ui;

import java.util.List;

import utask.commons.events.BaseEvent;
import utask.model.tag.Tag;

//@@author A0139996A
/*
 * This event is used to notify MainWindow to show a tag color dialog
 * */
public class ShowTagColorDialogEvent extends BaseEvent {

    public final List<Tag> tags;

    public ShowTagColorDialogEvent(List<Tag> tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
