package utask.model.tag;

import java.util.Random;

import utask.commons.exceptions.IllegalValueException;
import utask.staging.ui.helper.TagColorHelper.ColorType;

public class TagColorIndex {

    public static final String MESSAGE_TAG_CONSTRAINTS = "Tags names should be alphanumeric";
    public static final String TAGCOLORINDEX_VALIDATION_REGEX = "($|\\d)";

    public final Integer tagColorIndex;

    /**
     * Validates given name.
     *
     * @throws IllegalValueException
     *             if given name string is invalid.
     */
    public TagColorIndex(String value) throws IllegalValueException {
        assert value != null;
        String trimmedValue = value.trim();
        int colorIndex = -1;
        if (!isValidColorIndex(trimmedValue)) {
            throw new IllegalValueException(MESSAGE_TAG_CONSTRAINTS);
        }

        if ("".equals(trimmedValue)) {
            colorIndex = getRandomColorIndex();
        } else if (1 == trimmedValue.length()) {
            colorIndex = Integer.parseInt(value);
        } else {
            colorIndex = ColorType.valueOf(trimmedValue).ordinal();
        }
        this.tagColorIndex = colorIndex;
    }

    private int getRandomColorIndex() {
        Random random = new Random();
        return random.nextInt(ColorType.values().length);
    }

    /**
     * Returns true if a given string is a valid task name.
     */
    public static boolean isValidColorIndex(String test) {
        return test.matches(TAGCOLORINDEX_VALIDATION_REGEX);
    }

    public Integer getTagColorIndexAsInt() {
        return tagColorIndex;
    }

    @Override
    public String toString() {
        return tagColorIndex.toString();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof TagColorIndex // instanceof handles nulls
                        && this.tagColorIndex.equals(((TagColorIndex) other).tagColorIndex)); // state
                                                                           // check
    }

    @Override
    public int hashCode() {
        return tagColorIndex.hashCode();
    }

}
