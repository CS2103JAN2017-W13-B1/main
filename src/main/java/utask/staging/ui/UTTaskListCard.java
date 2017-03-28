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
import utask.staging.ui.helper.TagColorHelper;

public class UTTaskListCard extends StagingUiPart<Region> {

    public static final double CARD_HEIGHT = 150.0;
    private static final String FXML = "UTTaskListCard.fxml";
    private static final String LABEL_CSS = "-fx-padding: 1px; -fx-text-fill: WHITE; -fx-background-color: %s;";

    //UI Element use Leszynski naming convention. Prefix is the element type
    @FXML
    private Label lblId;

    @FXML
    private JFXCheckBox chkDone;

    @FXML
    private Label lblName;

    @FXML
    private HBox hbTagContainer;

    @FXML
    private Label lblDate;

    public UTTaskListCard(ReadOnlyTask task, int displayedIndex) {
        super(FXML);
        assert(task != null && displayedIndex > 0);

        setTaskInfoToControls(task, displayedIndex);
    }

    private void setTaskInfoToControls(ReadOnlyTask task, int displayedIndex) {
        lblName.setText(task.getName().fullName);
        lblId.setText(displayedIndex + " ");
        String friendlyDate = buildFriendlyDateToDisplay(task);
        lblDate.setText(friendlyDate);
        initTags(task);
    }

    //TODO: Core model object should have friendly name parser
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
            Label label = createLabel(tag.tagName);
            hbTagContainer.getChildren().add(label);
        }
    }

    private Label createLabel(String name) {
        Label label = new Label(name);
        addStylingPropertiesToLabel(label);
        return label;
    }

    private void addStylingPropertiesToLabel(Label label) {
        label.setAlignment(Pos.CENTER);;
        label.setTextAlignment(TextAlignment.CENTER);
        label.setTextOverrun(OverrunStyle.CLIP);
        label.setMinWidth(15.0);
        label.setStyle(String.format(LABEL_CSS, TagColorHelper.getARandomColor()));
        HBox.setMargin(label, new Insets(5, 5, 5, 0));
    }
}
