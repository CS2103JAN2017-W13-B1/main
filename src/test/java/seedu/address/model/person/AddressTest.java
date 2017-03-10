package seedu.address.model.person;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.utask.model.task.Frequency;

public class AddressTest {

    @Test
    public void isValidAddress() {
        // invalid addresses
        assertFalse(Frequency.isValidFrequency("")); // empty string
        assertFalse(Frequency.isValidFrequency(" ")); // spaces only

        // valid addresses
        assertTrue(Frequency.isValidFrequency("Blk 456, Den Road, #01-355"));
        assertTrue(Frequency.isValidFrequency("-")); // one character
        assertTrue(Frequency.isValidFrequency("Leng Inc; 1234 Market St; San Francisco 2349879; USA")); // long address
    }
}
