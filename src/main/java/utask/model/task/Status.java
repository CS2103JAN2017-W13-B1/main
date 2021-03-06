package utask.model.task;

import utask.commons.exceptions.IllegalValueException;

/**
 * Represents the boolean value of the Status attribute in the uTask.
 * Guarantees: immutable; is valid as declared in
 * {@link #isValidBoolean(String)}
 */
// @@author A0138423J
public class Status {
    public static final String MESSAGE_STATUS_CONSTRAINTS = "Task status should be complete/incomplete"
            + " case insensitive, and it should not be blank";
    public static final String STATUS_COMPLETE = "Complete";
    public static final String STATUS_INCOMPLETE = "Incomplete";

    /*
     * The first character of the status must not be a whitespace, otherwise " "
     * (a blank string) becomes a valid input.
     */
    public static final String STATUS_VALIDATION_REGEX = "^(COMPLETE|complete|Complete|INCOMPLETE|"
            + "incomplete|Incomplete)$";

    public final Boolean value;

    /**
     * Validates given status.
     *
     * @throws IllegalValueException
     *             if given name string is invalid.
     */
    public Status(String input) throws IllegalValueException {
        assert input != null;
        String trimmedName = input.trim();
        if (!isValidBoolean(trimmedName)) {
//            assert false : "Illegal Value!";
            throw new IllegalValueException(MESSAGE_STATUS_CONSTRAINTS);
        }
        this.value = checkValue(input);
    }

    private Boolean checkValue(String strToEvaluate)
            throws IllegalValueException {
        char value = strToEvaluate.toLowerCase().charAt(0);
        if ('c' == value) {
            return true;
        } else {
            return false;
        }
    }

    private Status() {
        this.value = false;
    }

    public static Status getEmptyStatus() {
        return new Status();
    }

    public boolean isEmpty() {
        return "".equals(value);
    }

    public boolean isStatusComplete() {
        return value;
    }

    /**
     * Returns true if a given string is a valid true/false or yes/no.
     */
    public static boolean isValidBoolean(String test) {
        return test.matches(STATUS_VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        if (value) {
            return STATUS_COMPLETE;
        } else {
            return STATUS_INCOMPLETE;
        }
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Status // instanceof handles nulls
                        && this.value.equals(((Status) other).value)); // state
                                                                       // check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
