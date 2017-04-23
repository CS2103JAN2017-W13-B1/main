package utask.model.task.abs;

import java.text.ParseException;
import java.util.Date;

import utask.model.task.Deadline;

//@@author A0138423J
public abstract class AbsDeadline {

    public static Deadline getEmptyDeadline() {
        return null;
    }

    public static boolean isValidDeadline(String test) {
        return false;
    }

    public abstract boolean isEmpty();

    public abstract Date getDate() throws ParseException;

    public abstract String toString();

    public abstract boolean equals(Object other);

    public abstract int hashCode();

}
