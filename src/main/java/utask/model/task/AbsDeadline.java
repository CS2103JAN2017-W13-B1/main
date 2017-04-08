package utask.model.task;

import java.text.ParseException;
import java.util.Date;

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

    @Override
    public abstract String toString();

    @Override
    public abstract boolean equals(Object other);

    @Override
    public abstract int hashCode();

}
