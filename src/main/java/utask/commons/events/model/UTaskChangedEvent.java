package utask.commons.events.model;

import utask.commons.events.BaseEvent;
import utask.model.ReadOnlyUTask;

/** Indicates the UTask in the model has changed*/
public class UTaskChangedEvent extends BaseEvent {

    public final ReadOnlyUTask data;

    public UTaskChangedEvent(ReadOnlyUTask data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "number of tasks " + data.getTaskList().size() + ", number of tags " + data.getTagList().size();
    }
}
