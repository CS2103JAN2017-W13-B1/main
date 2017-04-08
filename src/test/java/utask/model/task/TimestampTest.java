package utask.model.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import utask.commons.exceptions.IllegalValueException;
//@@author A0138423J
public class TimestampTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void isValidTimestamp() {
        // blank timestamp
        assertFalse(Timestamp.isValidTimestamp("")); // empty string
        assertFalse(Timestamp.isValidTimestamp(" ")); //spaces treated as empty string

        // missing parts
        assertFalse(Timestamp.isValidTimestamp("1530 to ")); // missing to data portion
        assertFalse(Timestamp.isValidTimestamp("1530 top 2300")); //misspelled to
        // invalid parts
        assertFalse(Timestamp.isValidTimestamp("9999 to 9999")); // invalid hhmm
        assertFalse(Timestamp.isValidTimestamp("0000 to 9999"));
        assertFalse(Timestamp.isValidTimestamp("0000 to 2930"));
        assertFalse(Timestamp.isValidTimestamp("000 to 2359"));

        assertFalse(Timestamp.isValidTimestamp("1300 to 0100")); //Start is later than end

        // valid timestamp
        assertTrue(Timestamp.isValidTimestamp("0000 to 1200"));
        assertTrue(Timestamp.isValidTimestamp("0000 to 2359"));
        assertTrue(Timestamp.isValidTimestamp("1600 to 2359"));
    }

    @Test
    public void testConstructor() {
        Timestamp d;
        try {
            d = new Timestamp("1200 to 1500");
            String valueOfD = d.toString();
            assertTrue(Timestamp.isValidTimestamp(valueOfD));

            d = new Timestamp("-");
            thrown.expect(AssertionError.class);
            assertEquals(null, d.getFrom());
            assertEquals(null, d.getTo());

            d = new Timestamp("#$%^&*");
        } catch (IllegalValueException e) {
            e.printStackTrace();
        }
    }
}
