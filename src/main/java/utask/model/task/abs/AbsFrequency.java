package utask.model.task.abs;

import utask.model.task.Frequency;

//@@author A0138423J
public abstract class AbsFrequency {

    public static Frequency getEmptyFrequency() {
        return null;
    }

    public static boolean isValidFrequency(String test) {
        return false;
    }

    public abstract boolean isEmpty();

    public abstract String toString();

    public abstract boolean equals(Object other);

    public abstract int hashCode();

}
