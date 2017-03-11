package seedu.address.model.person;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.utask.model.task.Frequency;

public class FrequencyTest {

    @Test
    public void isValidAddress() {
        // invalid addresses
        assertFalse(Frequency.isValidFrequency("")); // empty string
        assertFalse(Frequency.isValidFrequency(" ")); // spaces only

        // valid addresses
        assertTrue(Frequency.isValidFrequency("Every Monday"));
    }
}
