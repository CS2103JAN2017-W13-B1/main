package utask.ui.dialogs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class UTAliasDialog extends UTDialog {
    private static final String HAS_ALIAS_HEADING = "Alias";
    private static final String NO_ALIAS_MESSAGE = "Try adding some alias first!";
    private static final String NO_ALIAS_HEADING = ":( NO ALIAS";

    public UTAliasDialog(StackPane parent) {
        super(parent);
    }

    private void initialize(Map<String, String> alias) {
        List<Node> displayNodes = new ArrayList<Node>();

        Iterator<Entry<String, String>> it = alias.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> pair = (Map.Entry<String, String>) it.next();
            String aliasText = pair.getKey();
            String commandText = pair.getValue();
            displayNodes.add(setLabelSizingForDialogDisplay(createLabel(aliasText, commandText)));
            it.remove(); // avoids a ConcurrentModificationException
        }

        if (displayNodes.size() > 0) {
            VBox vbox = new VBox();
            vbox.getChildren().addAll(displayNodes);
            contentLayout.setHeading(new Label(HAS_ALIAS_HEADING));
            contentLayout.setBody(vbox);
        } else {
            contentLayout.setHeading(new Label(NO_ALIAS_HEADING));
            contentLayout.setBody(new Label(NO_ALIAS_MESSAGE));
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

    public void show(Map<String, String> alias) {
        initialize(alias);
        super.show();
    }

    //TODO: CAN REFRACTOR THIS
    private Label createLabel(String alias, String command) {
        String text = String.format("%s -> %s", alias, command);
        Label label = new Label(text);
        return label;
    }
}
