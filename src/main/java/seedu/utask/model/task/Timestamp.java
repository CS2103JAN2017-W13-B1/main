package seedu.utask.model.task;


import seedu.address.commons.exceptions.IllegalValueException;

/**
 * Represents a Person's phone number in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidTimestamp(String)}
 */
public class Timestamp {

    public static final String MESSAGE_TIMESTAMP_CONSTRAINTS =
            "Timestamps for tasks should be in format from HHMM to HHMM where HHMM is in the range of 0000 to 2359";
    public static final String TIMESTAMP_VALIDATION_REGEX = "^((?:0[0-9]|1[0-9]|2[0-3])(?:[0-5][0-9]))\\sto\\s"
            + "((?:0[0-9]|1[0-9]|2[0-3])(?:[0-5][0-9]))";

    public final String value;

    /**
     * Validates given timestamps.
     *
     * @throws IllegalValueException if given timestamps string is invalid.
     */
    public Timestamp(String timestamp) throws IllegalValueException {
        assert timestamp != null;
        String trimmedTimestamp = timestamp.trim();
        if (!isValidTimestamp(trimmedTimestamp)) {
            throw new IllegalValueException(MESSAGE_TIMESTAMP_CONSTRAINTS);
        }
        this.value = trimmedTimestamp;
    }

    private Timestamp() {
        this.value = "";
    }

    public static Timestamp getEmptyTimestamp() {
        return new Timestamp();
    }

    public boolean isEmpty() {
        return "".equals(value);
    }

    /**
     * Returns if a given string is a valid timestamps.
     */
    public static boolean isValidTimestamp(String test) {
        return test.matches(TIMESTAMP_VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Timestamp // instanceof handles nulls
                && this.value.equals(((Timestamp) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
