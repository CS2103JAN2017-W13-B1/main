package utask.model.task;
//@@author A0138423J
public enum Attribute {
    DEADLINE("deadline"),
    TIMESTAMP("timestamp"),
    STATUS("status"),
    FREQUENCY("frequency"),
    TAG("tag"),
    UNKNOWN("?");

    String key;

    Attribute(String key) {
        this.key = key;
    }

    Attribute() {
    }

    @Override
    public String toString() {
        return key;
    }

}
