package utask.staging.ui;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ReadOnlySearchTask {
    private final StringProperty name;
    private final StringProperty deadline;

    public ReadOnlySearchTask(String name, String deadline) {
        this.name = new SimpleStringProperty(name);
        this.deadline = new SimpleStringProperty(deadline);
    }

    public String getFirstName() {
        return name.get();
    }

    public void setFirstName(String firstName) {
        this.name.set(firstName);
    }

    public StringProperty firstNameProperty() {
        return name;
    }

    public String getLastName() {
        return deadline.get();
    }

    public void setLastName(String lastName) {
        this.deadline.set(lastName);
    }

    public StringProperty lastNameProperty() {
        return deadline;
    }
}
