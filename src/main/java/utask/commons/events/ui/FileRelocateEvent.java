//@@author A0138493W

package utask.commons.events.ui;

import utask.commons.events.BaseEvent;

/**
 * Indicates the path when relocate data storage file
 */
public class FileRelocateEvent extends BaseEvent {

    private String path;

    public FileRelocateEvent(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

}
