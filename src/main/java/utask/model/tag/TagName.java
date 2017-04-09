package utask.model.tag;

import utask.commons.core.Messages;
import utask.commons.exceptions.IllegalValueException;
//@@author A0138423J
public class TagName {

    public static final String TAGNAME_VALIDATION_REGEX = "^[a-zA-Z0-9]+$";

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
            throw new IllegalValueException(Messages.MESSAGE_TAG_CONSTRAINTS);
        }
        this.tagName = trimmedName;
    }

    public TagName(TagName name) throws IllegalValueException {
        assert name != null;
        if (!isValidName(name.toString())) {
            throw new IllegalValueException(Messages.MESSAGE_TAG_CONSTRAINTS);
        }
        this.tagName = name.toString();
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
