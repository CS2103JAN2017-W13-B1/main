package utask.ui.dialogs;

import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class UTMessageDialog extends UTDialog {

    private Label lblHeading;
    private Label lblContent;

    public UTMessageDialog(StackPane parent) {
        super(parent);
    }

    @Override
    protected void initialize() {
        lblHeading = new Label();
        lblContent = new Label();
        contentLayout.setHeading(lblHeading);
        contentLayout.setBody(lblContent);
    }

    public void show(String headingText, String contentText) {
        lblHeading.setText(headingText);
        lblContent.setText(contentText);
        super.show();
    }
}
