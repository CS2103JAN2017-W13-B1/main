package utask.model.tag;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import utask.commons.exceptions.IllegalValueException;

public class TagColorIndexTest {

    @Test
    public void isValidName() {
        // invalid name
        assertFalse(TagColorIndex.isValidColorIndex(" ")); // spaces only
        assertFalse(TagColorIndex.isValidColorIndex("^")); // only non-alphanumeric characters
        assertFalse(TagColorIndex.isValidColorIndex("Run*")); // contains non-alphanumeric characters
        assertFalse(TagColorIndex.isValidColorIndex("Home")); // invalid color
        assertFalse(TagColorIndex.isValidColorIndex("9")); // invalid color index

        // valid name
        assertTrue(TagColorIndex.isValidColorIndex("")); // random coloring
        assertTrue(TagColorIndex.isValidColorIndex("0")); // valid color index
        assertTrue(TagColorIndex.isValidColorIndex("blue")); // lower case valid color
        assertTrue(TagColorIndex.isValidColorIndex("GREEN")); // upper case valid color
        assertTrue(TagColorIndex.isValidColorIndex("Yellow")); // Mixed case valid color
    }

    @Test
    public void validTagColorIndexConstructor() throws IllegalValueException {
        TagColorIndex t = new TagColorIndex("");
        TagColorIndex toCompare = new TagColorIndex(t);
        assertEquals(t, toCompare); //checking if both TagColorIndex tally

        t = new TagColorIndex("6");
        toCompare = new TagColorIndex(t);
        assertEquals(t, toCompare); //checking if both TagColorIndex tally

        t = new TagColorIndex("Cyan");
        toCompare = new TagColorIndex(t);
        assertEquals(t, toCompare); //checking if both TagColorIndex tally
    }

    @Test(expected = IllegalValueException.class)
    public void invalidTagColorIndexConstructor() throws IllegalValueException {
        TagColorIndex t = new TagColorIndex("$%^&*("); // constructor with random symbols
        TagColorIndex toCompare = new TagColorIndex(t);
        assertEquals(t, toCompare); //checking if both TagColorIndex tally
    }

}
