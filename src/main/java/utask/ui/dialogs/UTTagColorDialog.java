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
import utask.staging.ui.helper.TagColorHelper;

public class UTTagColorDialog extends UTDialog {
    private static final String LABEL_CSS = "-fx-padding: 1 3 1 3; -fx-text-fill: WHITE; -fx-background-color: %s;";

    public UTTagColorDialog(StackPane parent) {
        super(parent);
    }

    @Override
    protected void setContent() {
        List<Node> labels = new ArrayList<Node>();

        labels.add(setLabelSizeForDialogDisplay(createLabel("Important")));
        labels.add(setLabelSizeForDialogDisplay(createLabel("Urgent")));
        labels.add(setLabelSizeForDialogDisplay(createLabel("Todo")));
        labels.add(setLabelSizeForDialogDisplay(createLabel("Friends")));

        labels.add(setLabelSizeForDialogDisplay(createLabel("Important")));
        labels.add(setLabelSizeForDialogDisplay(createLabel("Urgent")));
        labels.add(setLabelSizeForDialogDisplay(createLabel("Todo")));
        labels.add(setLabelSizeForDialogDisplay(createLabel("Friends")));

        labels.add(setLabelSizeForDialogDisplay(createLabel("Important")));
        labels.add(setLabelSizeForDialogDisplay(createLabel("Urgent")));
        labels.add(setLabelSizeForDialogDisplay(createLabel("Todo")));
        labels.add(setLabelSizeForDialogDisplay(createLabel("Friends")));

        labels.add(setLabelSizeForDialogDisplay(createLabel("Important")));
        labels.add(setLabelSizeForDialogDisplay(createLabel("Urgent")));
        labels.add(setLabelSizeForDialogDisplay(createLabel("Todo")));
        labels.add(setLabelSizeForDialogDisplay(createLabel("Friends")));
        labels.add(setLabelSizeForDialogDisplay(createLabel("Important")));
        labels.add(setLabelSizeForDialogDisplay(createLabel("Urgent")));
        labels.add(setLabelSizeForDialogDisplay(createLabel("Todo")));
        labels.add(setLabelSizeForDialogDisplay(createLabel("Friends")));

        labels.add(setLabelSizeForDialogDisplay(createLabel("Important")));
        labels.add(setLabelSizeForDialogDisplay(createLabel("Urgent")));
        labels.add(setLabelSizeForDialogDisplay(createLabel("Todo")));
        labels.add(setLabelSizeForDialogDisplay(createLabel("Friends")));
        labels.add(setLabelSizeForDialogDisplay(createLabel("Important")));
        labels.add(setLabelSizeForDialogDisplay(createLabel("Urgent")));
        labels.add(setLabelSizeForDialogDisplay(createLabel("Todo")));
        labels.add(setLabelSizeForDialogDisplay(createLabel("Friends")));

        labels.add(setLabelSizeForDialogDisplay(createLabel("Important")));
        labels.add(setLabelSizeForDialogDisplay(createLabel("Urgent")));
        labels.add(setLabelSizeForDialogDisplay(createLabel("Todo")));
        labels.add(setLabelSizeForDialogDisplay(createLabel("Friends")));

        FlowPane pane = new FlowPane();
//        pane.setMaxWidth(300);
        pane.getChildren().addAll(labels);

        contentLayout.setBody(pane);
    }

    private Node setLabelSizeForDialogDisplay(Label label) {
        label.setMinHeight(20);
        label.setMaxHeight(20);
        label.setPrefHeight(20);

        label.setMinWidth(75);
        label.setMaxWidth(75);
        label.setPrefWidth(75);

        VBox vb = new VBox();
        vb.setPadding(new Insets(2, 2, 2, 2));
//        vb.setSpacing(10);
        vb.getChildren().add(label);
        return vb;
    }

    public void show(String headingText, String contentText) {
        super.show();
    }

    //TODO: COPYPASTA FROM UTTASKLISTCAR REUSEABLE CODE!!!
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
