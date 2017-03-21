package utask.model.task;

import utask.commons.exceptions.IllegalValueException;

/**
 * Represents the boolean value of the isCompleted attribute in the uTask.
 * Guarantees: immutable; is valid as declared in
 * {@link #isValidBoolean(String)}
 */
//@@author A0138423J
public class IsCompleted {
    public static final String MESSAGE_NAME_CONSTRAINTS = "Task status should be true/false or "
            + "yes/no case insensitive, and it should not be blank";

    /*
     * The first character of the status must not be a whitespace, otherwise " "
     * (a blank string) becomes a valid input.
     */
    public static final String ISCOMPLETED_VALIDATION_REGEX = "^(YES|yes|Y|y|TRUE|true|T|t|NO|no|N|n|FALSE|false|F|f)$";
    //
    public final Boolean isCompleted;

    /**
     * Validates given name.
     *
     * @throws IllegalValueException
     *             if given name string is invalid.
     */
    public IsCompleted(String input) throws IllegalValueException {
        assert input != null;
        String trimmedName = input.trim();
        if (!isValidBoolean(trimmedName)) {
            throw new IllegalValueException(MESSAGE_NAME_CONSTRAINTS);
        }
        char value = input.toLowerCase().charAt(0);
        Boolean valToBool = false;
        switch (value) {
        case 't':
            valToBool = true;
            break;
        case 'y':
            valToBool = true;
            break;
        case 'f':
            valToBool = false;
            break;
        case 'n':
            valToBool = false;
            break;
        }
        this.isCompleted = valToBool;
    }

    /**
     * Returns true if a given string is a valid true/false or yes/no.
     */
    public static boolean isValidBoolean(String test) {
        return test.matches(ISCOMPLETED_VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return isCompleted.toString();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof IsCompleted // instanceof handles nulls
                        && this.isCompleted
                                .equals(((IsCompleted) other).isCompleted)); // state
                                                                             // check
    }

    @Override
    public int hashCode() {
        return isCompleted.hashCode();
    }
}
