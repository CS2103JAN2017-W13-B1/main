package utask.model.tag;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import utask.commons.exceptions.IllegalValueException;

public class TagNameTest {

    @Test
    public void isValidName() {
        // invalid name
        assertFalse(TagName.isValidName("")); // empty string
        assertFalse(TagName.isValidName(" ")); // spaces only
        assertFalse(TagName.isValidName("^")); // only non-alphanumeric characters
        assertFalse(TagName.isValidName("Run*")); // contains non-alphanumeric characters

        // valid name
        assertTrue(TagName.isValidName("School")); // alphabets only
        assertTrue(TagName.isValidName("12345")); // numbers only
        assertTrue(TagName.isValidName("c0d3L33t")); // alphanumeric characters
        assertTrue(TagName.isValidName("Home")); // with capital letters
    }

    @Test
    public void validTagNameConstructor() throws IllegalValueException {
        TagName t = new TagName("Hello");
        TagName toCompare = new TagName(t);
        assertEquals(t, toCompare); //checking if both TagName tally
    }

    @Test(expected = AssertionError.class)
    public void invalidTagNameConstructor() throws IllegalValueException {
        TagName t = null;
        TagName toCompare = new TagName(t);
        assertEquals(t, toCompare); //checking if both TagName tally
    }

}
