package utask.model.tag;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import utask.commons.exceptions.IllegalValueException;

public class TagTest {

    @Test
    public void validTagConstructor() throws IllegalValueException {
        Tag t = new Tag(new TagName("Hello"), new TagColorIndex("black"));
        Tag toCompare = new Tag(t);
        assertEquals(t, toCompare); //checking if both Tag tally
    }

    @Test(expected = AssertionError.class)
    public void invalidTagNameConstructor() throws IllegalValueException {
        Tag t = null;
        Tag toCompare = new Tag(t);
        assertEquals(t, toCompare); //checking if both Tag tally
    }

}
