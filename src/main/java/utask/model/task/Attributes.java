package utask.model.task;
//@@author A0138423J
public enum Attributes {
    DEADLINE, TIMESTAMP, ISCOMPLETED, FREQUENCY, TAG, UNKNOWN;

    String key;

    Attributes(String key) {
        this.key = key;
    }

    Attributes() {
    }

    Attributes getValue(String attribute) {
        switch (attribute) {
        case "deadline":
            return DEADLINE;
        case "timestamp":
            return TIMESTAMP;
        case "iscompleted":
            return ISCOMPLETED;
        case "frequency":
            return FREQUENCY;
        case "tag":
            return TAG;
        default:
            return UNKNOWN;
        }
    }

}
