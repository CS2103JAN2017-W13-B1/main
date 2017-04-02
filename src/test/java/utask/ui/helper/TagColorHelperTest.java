package utask.ui.helper;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import utask.staging.ui.helper.TagColorHelper;
import utask.staging.ui.helper.TagColorHelper.ColorType;


public class TagColorHelperTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void get_SupportedColors() {
        String colors = TagColorHelper.getListOfSupportedColor();
        assert (colors != "");
    }

    @Test
    public void get_Color_ByIndexAtFirstItem() {
        int validSize = 0;

        String colorValue = TagColorHelper.getColorValueFromIndex(validSize);
    }

    @Test
    public void get_Color_ByIndexAtBounds() {
        int validSize = TagColorHelper.getSize() - 1;

        String colorValue = TagColorHelper.getColorValueFromIndex(validSize);
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
        String colorValue = TagColorHelper.getColorValueFromIndex(-1);
    }

    @Test
    public void get_InvalidColor_ByOutOfRangeIndex() {
        int invalidSize = TagColorHelper.getSize();

        thrown.expect(AssertionError.class);
        String colorValue = TagColorHelper.getColorValueFromIndex(invalidSize);
    }

    @Test
    public void get_InvalidColor_WithUnknownName() {
        thrown.expect(IllegalArgumentException.class);
        String redColorValue = TagColorHelper.getColorValueFromName("GOLD");
    }
}
