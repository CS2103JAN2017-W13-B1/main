package utask.model.task;
//@@author A0138423J
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class AttributeTest {
    @Test
    public void is_Valid_AttributeName() {
        assertEquals("DEADLINE", Attribute.DEADLINE.name());
        assertEquals("TIMESTAMP", Attribute.TIMESTAMP.name());
        assertEquals("STATUS", Attribute.STATUS.name());
        assertEquals("FREQUENCY", Attribute.FREQUENCY.name());
        assertEquals("TAG", Attribute.TAG.name());
        assertEquals("UNKNOWN", Attribute.UNKNOWN.name());
    }

    @Test
    public void is_Valid_AttributeString() {
        assertEquals("deadline", Attribute.DEADLINE.toString());
        assertEquals("timestamp", Attribute.TIMESTAMP.toString());
        assertEquals("status", Attribute.STATUS.toString());
        assertEquals("frequency", Attribute.FREQUENCY.toString());
        assertEquals("tag", Attribute.TAG.toString());
        assertEquals("?", Attribute.UNKNOWN.toString());
    }

}
