package utask.model.task;

public abstract class AbsName {

    /**
     * Returns true if a given string is a valid task name.
     */
    public static boolean isValidName(String test) {
        return false;
    }

    @Override
    public abstract String toString();

    @Override
    public abstract boolean equals(Object other);

    @Override
    public abstract int hashCode();

}
