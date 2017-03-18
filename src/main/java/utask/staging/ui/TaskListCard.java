package utask.staging.ui;

import com.jfoenix.controls.JFXCheckBox;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.UniqueTagList;
import seedu.utask.model.task.ReadOnlyTask;

public class TaskListCard extends StagingUiPart<Region> {

    private static final String FXML = "TodoListCard.fxml";

    @FXML
    private Label id;

    @FXML
    private JFXCheckBox done;

    @FXML
    private Label name;

    @FXML
    private HBox tagPane;

    public TaskListCard(ReadOnlyTask task, int displayedIndex) {
        super(FXML);

        name.setText(task.getName().fullName);
        id.setText(displayedIndex + " ");
        // phone.setText(task.getDeadline().value);
        // address.setText(task.getFrequency().value);
        // email.setText(task.getTimestamp().value);
        initTags(task);
    }

    private void initTags(ReadOnlyTask task) {
        UniqueTagList tags = task.getTags();

        for (Tag tag : tags) {
            Label label = new Label(tag.tagName);
            label.setStyle("-fx-background-color: blue;");
            tagPane.getChildren().add(label);
        }
    }
}
