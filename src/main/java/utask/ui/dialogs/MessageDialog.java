package utask.ui.dialogs;

import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class MessageDialog extends Dialog {

    private Label lblHeading;
    private Label lblContent;

    public MessageDialog(StackPane parent) {
        super(parent);
    }

    private void initialize(String headingText, String contentText) {
        lblHeading = new Label();
        lblContent = new Label();
        lblHeading.setText(headingText);
        lblContent.setText(contentText);
        contentLayout.setHeading(lblHeading);
        contentLayout.setBody(lblContent);
    }

    public void show(String headingText, String contentText) {
        initialize(headingText, contentText);
        super.show();
    }
}
