package utask.staging.ui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialog.DialogTransition;
import com.jfoenix.controls.JFXDialogLayout;

import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import utask.commons.core.EventsCenter;

public class UTDialog {
    private JFXDialog dialog;
    private Label lblHeading;
    private Label lblContent;

    public UTDialog(StackPane parent) {
        initialize(parent);
        EventsCenter.getInstance().registerHandler(this);
    }

    private void initialize(StackPane parent) {
        JFXDialogLayout contentLayout = new JFXDialogLayout();
        lblHeading = new Label();
        lblContent = new Label();
        contentLayout.setHeading(lblHeading);
        contentLayout.setBody(lblContent);

        dialog = new JFXDialog(parent, contentLayout, DialogTransition.CENTER);

        JFXButton escButton = new JFXButton("CANCEL [ESC]");
        escButton.setStyle("-fx-text-fill: -fx-utask-accentcolor");
        escButton.setOnAction((e) -> {
            dialog.close();
        });

        JFXButton okButton = new JFXButton("CONTINUE [ENTER]");
        okButton.setStyle("-fx-text-fill: -fx-utask-accentcolor");
        contentLayout.setActions(escButton, okButton);
    }

    public void show(String headerText, String contentText) {
        lblHeading.setText(headerText);
        lblContent.setText(contentText);
        dialog.show();
    }


}
