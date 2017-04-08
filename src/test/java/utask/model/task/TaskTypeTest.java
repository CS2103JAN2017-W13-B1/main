package utask.model.task;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
//@@author A0138423J
public class TaskTypeTest {
    @Test
    public void isValidTaskTypeName() {
        assertEquals("EVENT", TaskType.EVENT.name());
        assertEquals("DEADLINE", TaskType.DEADLINE.name());
        assertEquals("FLOATING", TaskType.FLOATING.name());
        assertEquals("UNKNOWN", TaskType.UNKNOWN.name());
    }

    @Test
    public void isValidTaskTypeString() {
        assertEquals("event", TaskType.EVENT.toString());
        assertEquals("deadline", TaskType.DEADLINE.toString());
        assertEquals("floating", TaskType.FLOATING.toString());
        assertEquals("?", TaskType.UNKNOWN.toString());
    }
}
