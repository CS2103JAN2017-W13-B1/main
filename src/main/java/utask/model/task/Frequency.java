package utask.model.task;


import utask.commons.exceptions.IllegalValueException;

/**
 * Represents a Task's Frequency in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidFrequency(String)}
 */
public class Frequency {

    public static final String MESSAGE_FREQUENCY_CONSTRAINTS =
            "Task frequency should not be blank";

    /*
     * The first character of the address must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String FREQUENCY_VALIDATION_REGEX = "[^\\s].*";
    public static final String FREQUENCY_REMOVAL_VALIDATION_REGEX = "^[-]$";

    public final String value;

    /**
     * Validates given frequency.
     *
     * @throws IllegalValueException if given frequency string is invalid.
     */
    public Frequency(String frequency) throws IllegalValueException {
        assert frequency != null;
        if (!isValidFrequency(frequency)) {
            throw new IllegalValueException(MESSAGE_FREQUENCY_CONSTRAINTS);
        }
        this.value = frequency;
    }

    private Frequency() {
        this.value = "";
    }

    public static Frequency getEmptyFrequency() {
        return new Frequency();
    }

    public boolean isEmpty() {
        return "".equals(value);
    }

    /**
     * Returns true if a given string is a valid task email.
     */
    public static boolean isValidFrequency(String test) {
        return test.matches(FREQUENCY_VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Frequency // instanceof handles nulls
                && this.value.equals(((Frequency) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
