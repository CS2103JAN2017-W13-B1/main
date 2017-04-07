package utask.model.task;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import utask.commons.exceptions.IllegalValueException;

/**
 * Represents a Person's phone number in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidTimestamp(String)}
 */
public class Timestamp {

    public static final String MESSAGE_TIMESTAMP_CONSTRAINTS =
            "Timestamps for tasks should be in format from HHMM to HHMM where HHMM is in the range of 0000 to 2359";
    public static final Pattern TIMESTAMP_VALIDATION_REGEX = Pattern.compile("^(?<from>(?:0[0-9]|1[0-9]|2[0-3])"
            + "(?:[0-5][0-9]))(?:\\sto\\s)(?<to>(?:0[0-9]|1[0-9]|2[0-3])(?:[0-5][0-9]))");
    public static final String TIMESTAMP_REMOVAL_VALIDATION_REGEX = "^[-]$";

    public static final String MATCHER_GROUP_FROM = "from";
    public static final String MATCHER_GROUP_TO = "to";

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
        Matcher matcher = TIMESTAMP_VALIDATION_REGEX.matcher(test);

        if (matcher.matches()) {
            int from = Integer.parseInt(matcher.group(MATCHER_GROUP_FROM));
            int to = Integer.parseInt(matcher.group(MATCHER_GROUP_TO));

            if (from < to) {
                return  true;
            } else {
                return false;
            }
        }

        return test.matches(TIMESTAMP_REMOVAL_VALIDATION_REGEX);
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
