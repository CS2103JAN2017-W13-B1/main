package utask.model.task;

import utask.commons.exceptions.IllegalValueException;

/**
 * Represents the boolean value of the isCompleted attribute in the uTask.
 * Guarantees: immutable; is valid as declared in
 * {@link #isValidBoolean(String)}
 */
// @@author A0138423J
public class IsCompleted {
    public static final String MESSAGE_ISCOMPLETED_CONSTRAINTS = "Task status should be true/false or "
            + "yes/no case insensitive, and it should not be blank";

    /*
     * The first character of the status must not be a whitespace, otherwise " "
     * (a blank string) becomes a valid input.
     */
    public static final String ISCOMPLETED_VALIDATION_REGEX = "^(YES|yes|Y|y|TRUE|true|T|t|NO|no|N|n|FALSE|false|F|f)$";
    //
    public final String value;

    /**
     * Validates given status.
     *
     * @throws IllegalValueException
     *             if given name string is invalid.
     */
    public IsCompleted(String input) throws IllegalValueException {
        assert input != null;
        String trimmedName = input.trim();
        if (!isValidBoolean(trimmedName)) {
            throw new IllegalValueException(MESSAGE_ISCOMPLETED_CONSTRAINTS);
        }
        this.value = checkValue(input).toString();
    }

    private Boolean checkValue(String strToEvaluate) throws IllegalValueException {
        char value = strToEvaluate.toLowerCase().charAt(0);
        switch (value) {
        case 't':
            return true;
        case 'y':
            return true;
        case 'f':
            return false;
        case 'n':
            return false;
        default:
            throw new IllegalValueException(MESSAGE_ISCOMPLETED_CONSTRAINTS);
        }
    }

    private IsCompleted() {
        this.value = "false";
    }

    public static IsCompleted getEmptyIsCompleted() {
        return new IsCompleted();
    }

    public boolean isEmpty() {
        return "".equals(value);
    }

    public boolean isCompleted() {
        if (value.isEmpty()) {
            return false;
        }

        boolean isComp = false;

        try {
            isComp = checkValue(value);
        } catch (IllegalValueException e) {
            assert false : "IsCompleted has already been validated. This should not happen";
        }

        return isComp;
    }

    /**
     * Returns true if a given string is a valid true/false or yes/no.
     */
    public static boolean isValidBoolean(String test) {
        return test.matches(ISCOMPLETED_VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof IsCompleted // instanceof handles nulls
                        && this.value
                                .equals(((IsCompleted) other).value)); // state
                                                                             // check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
