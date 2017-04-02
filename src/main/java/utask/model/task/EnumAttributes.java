package utask.model.task;
//@@author A0138423J
public class EnumAttributes {
    Attributes attribute;

    public EnumAttributes(Attributes attribute) {
        this.attribute = attribute;
    }

    public String whichAttribute() {
        switch (attribute) {
        case DEADLINE:
            return "deadline";
        case TIMESTAMP:
            return "timestamp";
        case ISCOMPLETED:
            return "iscompleted";
        case FREQUENCY:
            return "frequency";
        case TAG:
            return "tag";
        default:
            return "unknown";
        }

    }
}
