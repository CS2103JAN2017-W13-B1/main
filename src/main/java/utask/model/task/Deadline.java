package utask.model.task;

import java.text.ParseException;
import java.util.Date;

import javax.xml.bind.annotation.XmlElement;

import utask.commons.exceptions.IllegalValueException;
import utask.commons.util.DateUtil;
import utask.model.task.abs.AbsDeadline;

/**
 * Represents a Task's deadline in the UTask. Guarantees: immutable; is valid as
 * declared in {@link #isValidDeadline(String)}
 */
public class Deadline extends AbsDeadline {

    public static final String MESSAGE_DEADLINE_CONSTRAINTS = "Deadline for tasks should in format DDMMYY";
    public static final String DEADLINE_VALIDATION_REGEX = "^(0[1-9]|[1-2][0-9]|"
            + "31(?!(?:0[2469]|11))|30(?!02))(0[1-9]|1[0-2])([1-2]\\d{1})";
    public static final String DEADLINE_REMOVAL_VALIDATION_REGEX = "^[-]$";

    //Denotes that this will be the attribute we are interested to serialise in this class
    @XmlElement
    private final Date date;

    /**
     * Validates given deadline.
     *
     * @throws IllegalValueException
     *             if given deadline string is invalid.
     */
    public Deadline(String deadline) throws IllegalValueException {
        assert deadline != null && !deadline.isEmpty();
        String trimmedDeadline = deadline.trim();
        if (!isValidDeadline(trimmedDeadline)) {
            throw new IllegalValueException(MESSAGE_DEADLINE_CONSTRAINTS);
        }

        if (deadline.equals("-")) {
            date = null;
        } else {
            date = DateUtil.parseStringToDate(deadline).get();
        }
    }

    public Deadline(Date date) {
        this.date = date;
    }

    private Deadline() {
        date = null;
    }

    public static Deadline getEmptyDeadline() {
        return new Deadline();
    }

    public boolean isEmpty() {
        return (date == null);
    }

    // @@author A0138493W
    /**
     * Returns deadline as date.
     *
     * @return date
     * @throws ParseException
     */
    public Date getDate() {
        assert date != null;

//        DateFormat fmt = new SimpleDateFormat("ddMMyyyy");
//        StringBuilder dateString = new StringBuilder(value);
//        dateString.insert(4, "20");
//        Date date = fmt.parse(dateString.toString());
        return date;
    }

    // @@author

    /**
     * Returns true if a given string is a valid task deadline.
     */
    public static boolean isValidDeadline(String test) {
        return (test.matches(DEADLINE_VALIDATION_REGEX)
                || test.matches(DEADLINE_REMOVAL_VALIDATION_REGEX)
                || DateUtil.isWordAValidDate(test));
    }

    @Override
    public String toString() {
        if (date == null) {
            return "";
        }

        return date.toString();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Deadline // instanceof handles nulls
                        && this.toString().equals(((Deadline) other).toString()));
    }

    @Override
    public int hashCode() {
        if (date == null) {
            return "".hashCode();
        }
        return date.hashCode();
    }
}
