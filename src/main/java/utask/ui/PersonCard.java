package utask.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import utask.model.task.ReadOnlyTask;

public class PersonCard extends UiPart<Region> {

    private static final String FXML = "PersonListCard.fxml";

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label phone;
    @FXML
    private Label address;
    @FXML
    private Label email;
    @FXML
    private FlowPane tags;

    public PersonCard(ReadOnlyTask person, int displayedIndex) {
        super(FXML);
        name.setText(person.getName().fullName);
        id.setText(displayedIndex + ". ");
//        phone.setText(person.getDeadline().value);
        address.setText(person.getFrequency().value);
        email.setText(person.getTimestamp().value);
        initTags(person);
    }

    private void initTags(ReadOnlyTask person) {
        person.getTags().forEach(tag -> tags.getChildren().add(new Label(tag.getTagname().toString())));
    }
}
