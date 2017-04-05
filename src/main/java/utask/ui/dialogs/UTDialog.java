package utask.ui.dialogs;

import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialog.DialogTransition;
import com.jfoenix.controls.JFXDialogLayout;

import javafx.scene.layout.StackPane;
import utask.commons.core.EventsCenter;
import utask.commons.core.LogsCenter;
import utask.staging.ui.UTMainWindow;
import utask.staging.ui.events.KeyboardEscapeKeyPressedEvent;

public abstract class UTDialog {
    private static final Logger logger = LogsCenter.getLogger(UTMainWindow.class);
    private final JFXDialog dialog;
    protected final JFXDialogLayout contentLayout;
    private boolean isShowing = false;

    public UTDialog(StackPane parent) {
        assert parent != null : "Parent cannot be null";

        dialog = new JFXDialog();
        contentLayout = new JFXDialogLayout();
        initialize(parent);
        EventsCenter.getInstance().registerHandler(this);
    }

    private void initialize(StackPane parent) {
        dialog.setDialogContainer(parent);
        dialog.setContent(contentLayout);
        dialog.setTransitionType(DialogTransition.CENTER);

        JFXButton closeButton = new JFXButton("CLOSE [ESC]");
        closeButton.setStyle("-fx-text-fill: -fx-utask-accentcolor");
        closeButton.setOnAction((event) -> {
            onClose();
        });

        contentLayout.setActions(closeButton);
    }

    protected void show() {
        logger.fine("Showing dialog window for detailed view of alias, tag colors or message.");
        isShowing = true;
        dialog.show();
    }

    private void onClose() {
        isShowing = false;
        dialog.close();
    }

    @Subscribe
    private void handleKeyboardEscapeKeyPressedEvent(KeyboardEscapeKeyPressedEvent event) {
        if (isShowing) {
            logger.info(LogsCenter.getEventHandlingLogMessage(event));
            onClose();
        }
    }
}
