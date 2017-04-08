package utask.model.task;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class AttributeTest {
    @Test
    public void isValidAttributeName() {
        assertEquals("DEADLINE", Attribute.DEADLINE.name());
        assertEquals("TIMESTAMP", Attribute.TIMESTAMP.name());
        assertEquals("STATUS", Attribute.STATUS.name());
        assertEquals("FREQUENCY", Attribute.FREQUENCY.name());
        assertEquals("TAG", Attribute.TAG.name());
        assertEquals("UNKNOWN", Attribute.UNKNOWN.name());
    }

    @Test
    public void isValidAttributeString() {
        assertEquals("deadline", Attribute.DEADLINE.toString());
        assertEquals("timestamp", Attribute.TIMESTAMP.toString());
        assertEquals("status", Attribute.STATUS.toString());
        assertEquals("frequency", Attribute.FREQUENCY.toString());
        assertEquals("tag", Attribute.TAG.toString());
        assertEquals("?", Attribute.UNKNOWN.toString());
    }

}
