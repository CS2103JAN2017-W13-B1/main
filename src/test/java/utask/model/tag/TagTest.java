package utask.model.tag;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import utask.commons.exceptions.IllegalValueException;
//@@author A0138423J
public class TagTest {

    @Test
    public void valid_Tag_Constructor() throws IllegalValueException {
        Tag t = new Tag(new TagName("Hello"), new TagColorIndex("black"));
        Tag toCompare = new Tag(t);
        assertEquals(t, toCompare); //checking if both Tag tally
    }

    @Test(expected = AssertionError.class)
    public void invalid_Tag_Constructor() throws IllegalValueException {
        Tag t = null;
        Tag toCompare = new Tag(t);
        assertEquals(t, toCompare); //checking if both Tag tally
    }

}
