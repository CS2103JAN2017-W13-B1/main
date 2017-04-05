package utask.ui.dialogs;

import com.google.common.eventbus.Subscribe;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialog.DialogTransition;
import com.jfoenix.controls.JFXDialogLayout;

import javafx.event.ActionEvent;
import javafx.scene.layout.StackPane;
import utask.commons.core.EventsCenter;
import utask.staging.ui.events.KeyboardEscapeKeyPressedEvent;

public abstract class UTDialog {
    protected final JFXDialog dialog;
    protected final JFXDialogLayout contentLayout;
    private boolean isShowing = false;

    public UTDialog(StackPane parent) {
        dialog = new JFXDialog();
        contentLayout = new JFXDialogLayout();
        initialize(parent);
        initialize();
        EventsCenter.getInstance().registerHandler(this);
    }

    private void initialize(StackPane parent) {
        dialog.setDialogContainer(parent);
        dialog.setContent(contentLayout);
        dialog.setTransitionType(DialogTransition.CENTER);

        JFXButton closeButton = new JFXButton("CLOSE [ESC]");
        closeButton.setStyle("-fx-text-fill: -fx-utask-accentcolor");
        closeButton.setOnAction(this::onClose);

        contentLayout.setActions(closeButton);
    }

    protected abstract void initialize();

    protected void show() {
        isShowing = true;
        dialog.show();
    }

    private void onClose(ActionEvent event) {
        isShowing = false;
        dialog.close();
    }

    @Subscribe
    private void handleKeyboardEscapeKeyPressedEvent(KeyboardEscapeKeyPressedEvent event) {
        if (isShowing) {
            onClose(null);
        }
    }
}
