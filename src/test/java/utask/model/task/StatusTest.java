package utask.model.task;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

//@@author A0138423J
public class StatusTest {

    @Test
    public void isValidTimestamp() {
        // blank Status
        assertFalse(Status.isValidBoolean("")); // empty string
        assertFalse(Status.isValidBoolean(" ")); // spaces only

        // missing parts
        assertFalse(Status.isValidBoolean("TRU")); // Upper case with incomplete
                                                   // spelling
        assertFalse(Status.isValidBoolean("complet")); // Lower case with
                                                       // incomplete spelling

        // invalid parts
        assertFalse(Status.isValidBoolean("0")); // integer
        assertFalse(Status.isValidBoolean("1.0")); // double
        assertFalse(Status.isValidBoolean("@$#%")); // symbols
        assertFalse(Status.isValidBoolean("accomplished")); // strings

        // valid timestamp
        assertTrue(Status.isValidBoolean("TRUE"));
        assertTrue(Status.isValidBoolean("false"));
        assertTrue(Status.isValidBoolean("YES"));
        assertTrue(Status.isValidBoolean("no"));
        assertTrue(Status.isValidBoolean("Complete"));
        assertTrue(Status.isValidBoolean("Incomplete"));
    }

}