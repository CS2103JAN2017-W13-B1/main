package seedu.address.model.person;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.utask.model.task.Deadline;

public class DeadlineTest {

    @Test
    public void isValidDeadline() {
        // invalid phone numbers
        assertFalse(Deadline.isValidDeadline("")); // empty string
        assertFalse(Deadline.isValidDeadline(" ")); // spaces only
        assertFalse(Deadline.isValidDeadline("jan")); // non-numeric
        assertFalse(Deadline.isValidDeadline("13jan17")); // alphabets within digits
        assertFalse(Deadline.isValidDeadline("13 01 17")); // spaces within digits
        assertFalse(Deadline.isValidDeadline("310417")); // no 31st day in April
        assertFalse(Deadline.isValidDeadline("300406")); // backdated
        // valid phone numbers
        assertTrue(Deadline.isValidDeadline("130117"));
        assertTrue(Deadline.isValidDeadline("090820"));
    }
}
