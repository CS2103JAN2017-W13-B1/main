package utask.ui.dialogs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

//@@author A0138493W
public class AliasDialog extends Dialog {
    private static final String HAS_ALIAS_HEADING = "Alias";
    private static final String NO_ALIAS_MESSAGE = "Try adding some alias first!";
    private static final String NO_ALIAS_HEADING = ":( NO ALIAS";

    public AliasDialog(StackPane parent) {
        super(parent);
    }

    private void initialize(Map<String, String> alias) {
        List<Node> displayNodes = new ArrayList<Node>();

        Iterator<Entry<String, String>> it = alias.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> pair = (Map.Entry<String, String>) it.next();
            String aliasText = pair.getKey();
            String commandText = pair.getValue();
            displayNodes.add(createLabel(aliasText, commandText));
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

    public void show(Map<String, String> alias) {
        initialize(alias);
        super.show();
    }

    //TODO: CAN REFRACTOR THIS
    private Label createLabel(String alias, String command) {
        String text = String.format("%s >> %s", alias, command);
        Label label = new Label(text);
        return label;
    }
}
