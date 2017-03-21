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
import utask.staging.ui.helper.TaskColorHelper;

public class UTTaskListCard extends StagingUiPart<Region> {

    private static final String FXML = "UTTaskListCard.fxml";
    private static final String LABEL_CSS = "-fx-padding: 1px; -fx-text-fill: WHITE; -fx-background-color: %s;";

    @FXML
    private Label id;

    @FXML
    private JFXCheckBox done;

    @FXML
    private Label name;

    @FXML
    private HBox tagPane;

    @FXML
    private Label lblDate;

    public UTTaskListCard(ReadOnlyTask task, int displayedIndex) {
        super(FXML);
        assert(task != null && displayedIndex > 0);

        name.setText(task.getName().fullName);
        id.setText(displayedIndex + " ");
        String friendlyDate = buildFriendlyDateToDisplay(task);
        lblDate.setText(friendlyDate);
        initTags(task);
    }

    private String buildFriendlyDateToDisplay(ReadOnlyTask task) {
        StringBuilder sb = new StringBuilder();

        if (!task.getFrequency().isEmpty()) {
            sb.append(task.getFrequency().value + ", ");
        }

        if (!task.getDeadline().isEmpty()) {
            sb.append(task.getDeadline().value);
        }

        if (!task.getTimestamp().isEmpty()) {
            sb.append(", " + task.getTimestamp().value);
        }

        return sb.toString();
    }

    private void initTags(ReadOnlyTask task) {
        UniqueTagList tags = task.getTags();

        for (Tag tag : tags) {
            Label label = createTagLabel(tag.tagName);
            HBox.setMargin(label, new Insets(5, 5, 5, 0));
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