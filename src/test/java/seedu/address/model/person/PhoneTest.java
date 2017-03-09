package seedu.address.model.person;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.utask.model.task.Deadline;

public class PhoneTest {

    @Test
    public void isValidPhone() {
        // invalid phone numbers
        assertFalse(Deadline.isValidPhone("")); // empty string
        assertFalse(Deadline.isValidPhone(" ")); // spaces only
        assertFalse(Deadline.isValidPhone("phone")); // non-numeric
        assertFalse(Deadline.isValidPhone("9011p041")); // alphabets within digits
        assertFalse(Deadline.isValidPhone("9312 1534")); // spaces within digits

        // valid phone numbers
        assertTrue(Deadline.isValidPhone("93121534"));
        assertTrue(Deadline.isValidPhone("4")); // short phone numbers
        assertTrue(Deadline.isValidPhone("124293842033123")); // long phone numbers
    }
}
