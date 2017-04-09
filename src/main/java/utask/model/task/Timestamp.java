package utask.model.task;


import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.annotation.XmlElement;

import utask.commons.exceptions.IllegalValueException;
import utask.commons.util.DateUtil;

/**
 * Represents a timestamp in a task
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

    private static final int TO_PARAM_IN_ARRAY = 1;
    private static final int FROM_PARAM_IN_ARRAY = 0;
    private static final String TIMESTAMP_DELIMITER = " to ";

//    public final String value;
    //Denotes that this will be the attribute we are interested to serialise in this class
    @XmlElement
    private final Date from;

    @XmlElement
    private final Date to;

    /**
     * Validates given timestamps.
     *
     * @throws IllegalValueException if given timestamps string is invalid.
     */
    public Timestamp(String deadline, String timestamp) throws IllegalValueException {
        assert deadline != null && !deadline.isEmpty();
        assert timestamp != null && !timestamp.isEmpty();

        String trimmedTimestamp = timestamp.trim();
        if (!isValidTimestamp(trimmedTimestamp)) {
            throw new IllegalValueException(MESSAGE_TIMESTAMP_CONSTRAINTS);
        }

        if (!timestamp.equals("-")) {
            String[] str = timestamp.split(TIMESTAMP_DELIMITER);
            String fromString = str[FROM_PARAM_IN_ARRAY];
            String toString = str[TO_PARAM_IN_ARRAY];

            Date date = DateUtil.parseStringToDate(deadline).get();
            assert date != null : "Certainly sure that date will be valid as tested by deadline";

            from = DateUtil.addHHMMStringToDate((Date) date.clone(), fromString);
            to = DateUtil.addHHMMStringToDate((Date) date.clone(), toString);
        } else {
            from = null;
            to = null;
        }
    }

    public Timestamp(String timestamp) throws IllegalValueException {
        assert timestamp != null && !timestamp.isEmpty();

        String trimmedTimestamp = timestamp.trim();
        if (!isValidTimestamp(trimmedTimestamp)) {
            throw new IllegalValueException(MESSAGE_TIMESTAMP_CONSTRAINTS);
        }

        if (timestamp.equals("-")) {
            from = null;
            to = null;
        } else {
            String[] str = timestamp.split(TIMESTAMP_DELIMITER);
            String fromString = str[FROM_PARAM_IN_ARRAY];
            String toString = str[TO_PARAM_IN_ARRAY];

            Date date = DateUtil.getEmptyDate();

            from = DateUtil.addHHMMStringToDate((Date) date.clone(), fromString);
            to = DateUtil.addHHMMStringToDate((Date) date.clone(), toString);
        }
    }

    private Timestamp() {
        from = null;
        to = null;
    }

    private Timestamp(Date from, Date to) {
        this.from = from;
        this.to = to;
    }

    public static Timestamp getEmptyTimestamp() {
        return new Timestamp();
    }

    public boolean isEmpty() {
        return from == null || to == null;
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
                return true;
            } else {
                return false;
            }
        }

        return test.matches(TIMESTAMP_REMOVAL_VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "";
        } else {
            String fromDateInString = DateUtil.getFormattedTime(from);
            String toDateInString = DateUtil.getFormattedTime(to);
            return String.format("%s%s%s", fromDateInString, TIMESTAMP_DELIMITER, toDateInString);
        }
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Timestamp // instanceof handles nulls
                && this.toString().equals(((Timestamp) other).toString())); // state check
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    public Date getFrom() {
        assert from != null;
        return from;
    }

    public boolean hasFrom() {
        return from != null;
    }

    public Date getTo() {
        assert to != null;
        return to;
    }

    public static Timestamp getUpdateTimestampWithDeadline(Timestamp timestamp, Deadline deadline) {
        Date dateComponent = deadline.getDate();
        Date timeComponentOfFrom = timestamp.getFrom();
        Date timeComponentOfTo = timestamp.getTo();

        Date updatedFromDate = DateUtil.getDateUsingTimeComponentAndDateComponent(timeComponentOfFrom, dateComponent);
        Date updatedToDate = DateUtil.getDateUsingTimeComponentAndDateComponent(timeComponentOfTo, dateComponent);

        return new Timestamp(updatedFromDate, updatedToDate);
    }
}
