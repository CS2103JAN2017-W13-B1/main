package utask.model.task;
//@@author A0138423J
public enum TaskType {
    EVENT, DEADLINE, FLOATING, UNKNOWN;

    String key;

    TaskType(String key) {
        this.key = key;
    }

    TaskType() {
    }

    TaskType getValue(String attribute) {
        switch (attribute) {
        case "event":
            return EVENT;
        case "deadline":
            return DEADLINE;
        case "floating":
            return FLOATING;
        default:
            return UNKNOWN;
        }
    }

}
