package utask.commons.core;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import utask.model.task.Attribute;

public class MessagesTest {
    @Test
    public void isValidMessageString() {
        assertEquals("DEADLINE", Attribute.DEADLINE.name());
        assertEquals("TIMESTAMP", Attribute.TIMESTAMP.name());
        assertEquals("STATUS", Attribute.STATUS.name());
        assertEquals("FREQUENCY", Attribute.FREQUENCY.name());
        assertEquals("TAG", Attribute.TAG.name());
        assertEquals("UNKNOWN", Attribute.UNKNOWN.name());
    }
}
