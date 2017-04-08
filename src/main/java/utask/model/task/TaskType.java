package utask.model.task;
//@@author A0138423J
public enum TaskType {
    EVENT("event"),
    DEADLINE("deadline"),
    FLOATING("floating"),
    UNKNOWN("?");

    String key;

    TaskType(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return key;
    }

}
