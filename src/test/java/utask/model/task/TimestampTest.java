package utask.model.task;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import utask.model.task.Timestamp;

public class TimestampTest {

    @Test
    public void isValidTimestamp() {
        // blank timestamp
        assertFalse(Timestamp.isValidTimestamp("")); // empty string
        assertFalse(Timestamp.isValidTimestamp(" ")); // spaces only

        // missing parts
        assertFalse(Timestamp.isValidTimestamp("1530 to ")); // hhmm in to
                                                                  // portion
        assertFalse(Timestamp.isValidTimestamp("from 1530 top 2300")); // misspelled
                                                                        // to
        // invalid parts
        assertFalse(Timestamp.isValidTimestamp("9999 to 9999")); // invalid hhmm
        assertFalse(Timestamp.isValidTimestamp("0000 to 9999"));
        assertFalse(Timestamp.isValidTimestamp("0000 to 2930"));
        assertFalse(Timestamp.isValidTimestamp("000 to 2359"));

        // valid timestamp
        assertTrue(Timestamp.isValidTimestamp("0000 to 1200"));
        assertTrue(Timestamp.isValidTimestamp("0000 to 2359"));
        assertTrue(Timestamp.isValidTimestamp("1600 to 2359"));
    }
}
