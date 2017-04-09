package utask.model.tag;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Optional;

import org.junit.Test;

import utask.commons.exceptions.IllegalValueException;
//@@author A0138423J
public class EditTagDescriptorTest {

    @Test
    public void valid_EditTagDescriptor_Constructor() throws IllegalValueException {
        EditTagDescriptor t = new EditTagDescriptor();
        Optional<TagName> tempName = Optional.of(new TagName("IMPT"));
        Optional<TagColorIndex> tempColor = Optional.of(new TagColorIndex("yellow"));
        t.setTagName(tempName);
        t.setTagColor(tempColor);
        EditTagDescriptor toCompare = new EditTagDescriptor(t);
        assertTrue(toCompare.isAnyFieldEdited());
        assertEquals(t.getTagName(), toCompare.getTagName()); // check matching tagName
        assertEquals(t.getTagColor(), toCompare.getTagColor()); // check matching TagColor
    }
}
