//@@author A0139996A
package utask.ui.helper;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import utask.ui.helper.TagColorHelper.ColorType;


public class TagColorHelperTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void get_RandomColor() {
        String color = TagColorHelper.getARandomColor();
        assert(color != "");
    }

    @Test
    public void get_ColorName_FromIndex() {
        String black = TagColorHelper.getColorNameFromIndex(0);
        black = black.toUpperCase();
        assert black.equals(ColorType.BLACK.name());
    }

    @Test
    public void get_SupportedColors() {
        String colors = TagColorHelper.getListOfSupportedColor();
        assert (colors != "");
    }

    @Test
    public void get_Color_ByIndexAtFirstItem() {
        int validSize = 0;
        TagColorHelper.getColorValueFromIndex(validSize);
    }

    @Test
    public void get_Color_ByIndexAtBounds() {
        int validSize = TagColorHelper.getSize() - 1;
        TagColorHelper.getColorValueFromIndex(validSize);
    }

    @Test
    public void get_ValidColor_ByIndex() {
        String expectedColorValue = ColorType.BLACK.toString();
        String blackColorValue = TagColorHelper.getColorValueFromIndex(0);

        assert(expectedColorValue == blackColorValue);
    }

    @Test
    public void get_ValidColor_WithKnownName() {
        String expectedColorValue = ColorType.BLACK.toString();
        String blackColorValue = TagColorHelper.getColorValueFromName("BLACK");

        assert(expectedColorValue == blackColorValue);
    }

    @Test
    public void get_ValidColor_WithKnownNameInLowerCase() {
        String expectedColorValue = ColorType.BLACK.toString();
        String blackColorValue = TagColorHelper.getColorValueFromName("black");

        assert(expectedColorValue == blackColorValue);
    }

    @Test
    public void get_InvalidColor_ByNegativeIndex() {
        thrown.expect(AssertionError.class);
        TagColorHelper.getColorValueFromIndex(-1);
    }

    @Test
    public void get_InvalidColor_ByOutOfRangeIndex() {
        int invalidSize = TagColorHelper.getSize();

        thrown.expect(AssertionError.class);
        TagColorHelper.getColorValueFromIndex(invalidSize);
    }

    @Test
    public void get_InvalidColor_WithUnknownName() {
        thrown.expect(IllegalArgumentException.class);
        TagColorHelper.getColorValueFromName("GOLD");
    }
}
