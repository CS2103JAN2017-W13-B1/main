package utask.model.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import utask.commons.exceptions.IllegalValueException;

//@@author A0138423J
public class StatusTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void isValidStatus() {
        // blank Status
        assertFalse(Status.isValidBoolean("")); // empty string
        assertFalse(Status.isValidBoolean(" ")); // spaces only

        // missing parts
        assertFalse(Status.isValidBoolean("COMPLE")); // Upper case with
                                                      // incomplete
                                                      // spelling
        assertFalse(Status.isValidBoolean("incompl")); // Lower case with
                                                       // incomplete spelling

        // invalid parts
        assertFalse(Status.isValidBoolean("0")); // integer
        assertFalse(Status.isValidBoolean("1.0")); // double
        assertFalse(Status.isValidBoolean("@$#%")); // symbols
        assertFalse(Status.isValidBoolean("accomplished")); // strings

        // valid timestamp
        assertTrue(Status.isValidBoolean("Complete"));
        assertTrue(Status.isValidBoolean("Incomplete"));
    }

    @Test
    public void valid_Status_Constructor() throws IllegalValueException {
        String value = "Complete";
        Status s = new Status(value);
        assertEquals(s.toString(), value);
    }

    @Test(expected = IllegalValueException.class)
    public void invalid_Status_Constructor() throws IllegalValueException {
        String value = "-";
        Status s = new Status(value);
        assertEquals(s.toString(), "");
    }
}
