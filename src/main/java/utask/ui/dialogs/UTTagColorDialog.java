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
    private static final String NO_TAGS_HEADING = ":( No tags";
    private static final String LABEL_CSS = "-fx-padding: 1 3 1 3; -fx-text-fill: WHITE; -fx-background-color: %s;";


    public UTTagColorDialog(StackPane parent) {
        super(parent);
    }

    @Override
    protected void initialize() {
        List<Node> labels = new ArrayList<Node>();


        labels.add(setLabelSizingForDialogDisplay(createLabel("Important")));
        labels.add(setLabelSizingForDialogDisplay(createLabel("Urgent")));
        labels.add(setLabelSizingForDialogDisplay(createLabel("Todo")));
        labels.add(setLabelSizingForDialogDisplay(createLabel("Friends")));

        if (labels.size() > 0) {
            FlowPane pane = new FlowPane();
            pane.getChildren().addAll(labels);
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
        super.show();
    }

    //TODO: COPYPASTA FROM UTTASKLISTCARD REUSEABLE CODE!!!
    private Label createLabel(String name) {
        Label label = new Label(name);
        addStylingPropertiesToLabel(label);
        return label;
    }

    private void addStylingPropertiesToLabel(Label label) {
        label.setAlignment(Pos.CENTER);
        label.setTextAlignment(TextAlignment.CENTER);
        label.setTextOverrun(OverrunStyle.CLIP);
        label.setMinWidth(15.0);
        label.setStyle(String.format(LABEL_CSS, TagColorHelper.getARandomColor()));
        HBox.setMargin(label, new Insets(5, 5, 5, 0));
    }
}
