package utask.staging.ui;

import com.jfoenix.controls.JFXCheckBox;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.text.TextAlignment;
import utask.model.tag.Tag;
import utask.model.tag.UniqueTagList;
import utask.model.task.ReadOnlyTask;

public class TaskListCard extends StagingUiPart<Region> {

    private static final String FXML = "TodoListCard.fxml";
    private static final String LABEL_CSS = "-fx-padding: 1px; -fx-text-fill: WHITE; -fx-background-color: %s;";

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
        assert(task != null && displayedIndex > 0);

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
            Label label = createTagLabel(tag.tagName);
            HBox.setMargin(label, new Insets(5, 5, 5, 5));
            tagPane.getChildren().add(label);
        }
    }

    private Label createTagLabel(String name) {
        Label label = new Label(name);
        label.setAlignment(Pos.CENTER);;
        label.setTextAlignment(TextAlignment.CENTER);
        label.setTextOverrun(OverrunStyle.CLIP);
        label.setMinWidth(15.0);
        label.setStyle(String.format(LABEL_CSS, TaskColorHelper.getARandomColor()));
        return label;
    }

    private void preventRenderingOfHiddenElement() {
        //dueList.setManaged(false);
    }
}
