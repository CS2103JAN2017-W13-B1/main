package utask.model.tag;

import utask.commons.exceptions.IllegalValueException;
//@@author A0138423J
public class TagName {

    public static final String MESSAGE_TAG_CONSTRAINTS = "Tags names should be alphanumeric";
    public static final String TAGNAME_VALIDATION_REGEX = "^\\w+$";

    public final String tagName;

    /**
     * Validates given name.
     *
     * @throws IllegalValueException
     *             if given name string is invalid.
     */
    public TagName(String name) throws IllegalValueException {
        assert name != null;
        String trimmedName = name.trim();
        if (!isValidName(trimmedName)) {
            throw new IllegalValueException(MESSAGE_TAG_CONSTRAINTS);
        }
        this.tagName = trimmedName;
    }

    /**
     * Returns true if a given string is a valid task name.
     */
    public static boolean isValidName(String test) {
        return test.matches(TAGNAME_VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return tagName;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof TagName // instanceof handles nulls
                        && this.tagName.equals(((TagName) other).tagName)); // state
                                                                           // check
    }

    @Override
    public int hashCode() {
        return tagName.hashCode();
    }

}
