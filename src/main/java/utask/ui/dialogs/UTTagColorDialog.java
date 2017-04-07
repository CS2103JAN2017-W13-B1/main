package utask.ui.dialogs;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import utask.model.tag.Tag;
import utask.staging.ui.helper.TagColorHelper;

public class UTTagColorDialog extends UTDialog {
    private static final String HAS_TAGS_HEADING = "Tags";
    private static final String NO_TAGS_MESSAGE = "Try adding some tags first!";
    private static final String NO_TAGS_HEADING = ":( NO TAGS";
    private static final String LABEL_CSS = "-fx-padding: 1 3 1 3; -fx-text-fill: WHITE; -fx-background-color: %s;";


    public UTTagColorDialog(StackPane parent) {
        super(parent);
    }

    private void initialize(List<Tag> tags) {
        List<Node> displayTags = new ArrayList<Node>();

        for (Tag tag : tags) {
            Label label = createLabel(tag.getTagname().toString(),
                    tag.getTagcolorindex().getTagColorIndexAsInt());
            Node displayTag = setLabelSizingForDialogDisplay(label);
            displayTags.add(displayTag);
        }

        if (displayTags.size() > 0) {
            FlowPane pane = new FlowPane();
            pane.getChildren().addAll(displayTags);
            contentLayout.setHeading(new Label(HAS_TAGS_HEADING));
            contentLayout.setBody(pane);
        } else {
            contentLayout.setHeading(new Label(NO_TAGS_HEADING));
            contentLayout.setBody(new Label(NO_TAGS_MESSAGE));
        }
    }

    private Node setLabelSizingForDialogDisplay(Label label) {
        label.setMinHeight(20);
        label.setMaxHeight(20);
        label.setPrefHeight(20);
        label.setMinWidth(75);
        label.setMaxWidth(75);
        label.setPrefWidth(75);

        VBox vBox = new VBox();
        vBox.setPadding(new Insets(2, 2, 2, 2));
        vBox.getChildren().add(label);
        return vBox;
    }

    public void show(List<Tag> tags) {
        initialize(tags);
        super.show();
    }


    //TODO: COPYPASTA FROM UTTASKLISTCARD REUSEABLE CODE!!!
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
}
