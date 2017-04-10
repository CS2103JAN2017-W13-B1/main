package utask.commons.events.ui;

import javafx.scene.Node;
import utask.commons.events.BaseEvent;

/**
 * Indicates a request to jump to the list of task
 */
public class JumpToTaskListRequestEvent extends BaseEvent {

    public final Node targetNode;
    public final int index;

    public JumpToTaskListRequestEvent(Node targetNode, int index) {
        this.targetNode = targetNode;
        this.index = index;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

}
