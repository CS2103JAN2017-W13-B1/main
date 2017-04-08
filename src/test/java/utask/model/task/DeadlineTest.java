package utask.model.task;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DeadlineTest {

    @Test
    public void isValidDeadline() {
        // invalid Deadline
        assertFalse(Deadline.isValidDeadline("")); // empty string
        assertFalse(Deadline.isValidDeadline(" ")); // spaces only
        assertFalse(Deadline.isValidDeadline("jan")); // non-numeric
        assertFalse(Deadline.isValidDeadline("13jan17")); // alphabets within digits
        assertFalse(Deadline.isValidDeadline("13 01 17")); // spaces within digits
        assertFalse(Deadline.isValidDeadline("310417")); // no 31st day in April
        // valid phone numbers
        assertTrue(Deadline.isValidDeadline("300406"));  // support backdated
        assertTrue(Deadline.isValidDeadline("130117"));
        assertTrue(Deadline.isValidDeadline("090820"));
    }
}
