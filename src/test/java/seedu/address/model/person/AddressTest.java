package seedu.address.model.person;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.utask.model.task.Frequency;

public class AddressTest {

    @Test
    public void isValidAddress() {
        // invalid addresses
        assertFalse(Frequency.isValidAddress("")); // empty string
        assertFalse(Frequency.isValidAddress(" ")); // spaces only

        // valid addresses
        assertTrue(Frequency.isValidAddress("Blk 456, Den Road, #01-355"));
        assertTrue(Frequency.isValidAddress("-")); // one character
        assertTrue(Frequency.isValidAddress("Leng Inc; 1234 Market St; San Francisco CA 2349879; USA")); // long address
    }
}
