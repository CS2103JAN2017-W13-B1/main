package utask.model.task;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import utask.model.task.Frequency;

public class FrequencyTest {

    @Test
    public void isValidFrequency() {
        // invalid addresses
        assertFalse(Frequency.isValidFrequency("")); // empty string
        assertFalse(Frequency.isValidFrequency(" ")); // spaces only

        // valid addresses
        assertTrue(Frequency.isValidFrequency("Every Monday"));
    }
}
