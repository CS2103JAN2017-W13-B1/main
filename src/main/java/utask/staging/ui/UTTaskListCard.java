package utask.staging.ui;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.ocpsoft.prettytime.PrettyTime;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.text.TextAlignment;
import utask.commons.core.EventsCenter;
import utask.model.tag.Tag;
import utask.model.tag.UniqueTagList;
import utask.model.task.ReadOnlyTask;
import utask.staging.ui.helper.TagColorHelper;

//@@author A0139996A
public class UTTaskListCard extends StagingUiPart<Region> {
    public static final double CARD_HEIGHT = 102.0;
    private static final String FXML = "UTTaskListCard.fxml";
    private static final String LABEL_CSS = "-fx-padding: 1 3 1 3; -fx-text-fill: WHITE; -fx-background-color: %s;";
    private static final String LABEL_STRIKETHROUGH_STYLE = "label-text-done";

    // UI Element use Leszynski naming convention. Prefix is the element type
    @FXML
    private Label lblId;

    @FXML
    private CheckBox chkDone;

    @FXML
    private Label lblName;

    @FXML
    private HBox hbTagContainer;

    @FXML
    private Label lblDate;

    // TODO: Upgrade to property for observable binding instead of manually
    // checking for UI events
    private final ReadOnlyTask task;

    public UTTaskListCard(ReadOnlyTask task, int displayedIndex) {
        super(FXML);
        assert (task != null && displayedIndex > 0);
        this.task = task;
        setTaskInfoToControls(task, displayedIndex);
        addStylingProperitesOnCompletion();
        EventsCenter.getInstance().registerHandler(this);
    }

    private void setTaskInfoToControls(ReadOnlyTask task, int displayedIndex) {
        lblName.setText(task.getName().fullName);
        lblId.setText(displayedIndex + " ");
        String friendlyDate = buildFriendlyDateToDisplay(task);
        lblDate.setText(friendlyDate);
        createTags(task);
    }

    private String buildFriendlyDateToDisplay(ReadOnlyTask task) {
        StringBuilder sb = new StringBuilder();

        if (!task.getFrequency().isEmpty()) {
            sb.append(task.getFrequency().value + ", ");
        }

        if (!task.getDeadline().isEmpty()) {
            sb.append(getPrettyDate(task));
        }

        if (!task.getTimestamp().isEmpty()) {
            sb.append(", " + task.getTimestamp().toString());
        }

        return sb.toString();
    }

    // @@author A0138493W
    private String getPrettyDate(ReadOnlyTask task) {
        assert task != null;
        Date deadline = null;
        deadline = task.getDeadline().getDate();
        DateFormat fmt = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
        if (fmt.format(deadline).equals(fmt.format(new Date()))) {
            return fmt.format(deadline) + ", today";
        }
        PrettyTime p = new PrettyTime();
        return fmt.format(deadline) + ", " + p.format(deadline);
    }
    // @@author

    // @@author A0138423J
    private void createTags(ReadOnlyTask task) {
        UniqueTagList tags = task.getTags();
        for (Tag tag : tags) {
            Label label = createLabel(tag.getTagname().toString(),
                    tag.getTagcolorindex().getTagColorIndexAsInt());
            hbTagContainer.getChildren().add(label);
        }
    }
    // @@author

    private Label createLabel(String name, int colorIndex) {
        Label label = new Label(name);
        addStylingPropertiesToLabel(label, colorIndex);
        return label;
    }

    private void addStylingPropertiesToLabel(Label label, int colorIndex) {
        label.setAlignment(Pos.CENTER);
        label.setTextAlignment(TextAlignment.CENTER);
        label.setTextOverrun(OverrunStyle.CLIP);
        label.setMinWidth(15.0);
        label.setStyle(String.format(LABEL_CSS, TagColorHelper.getColorValueFromIndex(colorIndex)));
        HBox.setMargin(label, new Insets(5, 5, 5, 0));
    }

    public void addStylingProperitesOnCompletion() {
        boolean isComplete = task.getStatus().isStatusComplete();
        chkDone.setSelected(isComplete);

        if (isComplete) {
            lblName.getStyleClass().add(LABEL_STRIKETHROUGH_STYLE);
        } else {
            lblName.getStyleClass().remove(LABEL_STRIKETHROUGH_STYLE);
        }
    }
}
