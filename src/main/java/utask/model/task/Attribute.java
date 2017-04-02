package utask.model.task;
//@@author A0138423J
public enum Attribute {
    DEADLINE, TIMESTAMP, ISCOMPLETED, FREQUENCY, TAG, UNKNOWN;

    String key;

    Attribute(String key) {
        this.key = key;
    }

    Attribute() {
    }

    Attribute getValue(String attribute) {
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
