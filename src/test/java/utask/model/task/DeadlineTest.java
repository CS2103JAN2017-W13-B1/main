package utask.model.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import utask.commons.exceptions.IllegalValueException;
//@@author A0138423J
public class DeadlineTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

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

    @Test
    public void valid_Deadline_Constructor() throws IllegalValueException {
        Date date = new Date();
        Deadline d = new Deadline(date); // Date constructor
        assertEquals(d.getDate(), date); //checking if both dates tally
    }

    @Test
    public void valid_EmptyDeadline_Constructor() throws IllegalValueException {
        Deadline d = Deadline.getEmptyDeadline(); // empty constructor
        assertEquals(d.hashCode(), "".hashCode()); //checking hashCode when Deadline is Null
    }

    @Test
    public void remove_Deadline_Constructor() throws IllegalValueException {
        String value = "-";
        Deadline d = new Deadline(value); // constructor with dash only
        assertEquals(d.toString(), ""); //checking toString() value when Deadline is Null
    }

    @Test(expected = IllegalValueException.class)
    public void invalid_Deadline_Constructor() throws IllegalValueException {
        Deadline d = new Deadline("$%^&*("); // constructor with random symbols
    }
}
