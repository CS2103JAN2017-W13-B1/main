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
import utask.commons.events.ui.KeyboardEscapeKeyPressedEvent;
//@@author A0139996A
/*
 * Dialog enables displaying of information on MainWindow as popup dialog, which can be dismissed with esc key.
 * It uses Decorator pattern and leaves the actual UI implementation of its subclass.
 *
 * E.g. TagColorDialog extends it and add a FlowPane with tags as text labels
 *      AliasDialog extends it and add a VBox with alias as text labels
 * */
public abstract class Dialog {
    private static final String DIALOG_BUTTON_TEXT = "CLOSE [ESC]";
    private static final String DIALOG_BUTTON_CSS = "-fx-text-fill: -fx-utask-accentcolor";
    private static final Logger logger = LogsCenter.getLogger(Dialog.class);
    private final JFXDialog dialog;
    protected final JFXDialogLayout contentLayout;
    private boolean isShowing = false;

    public Dialog(StackPane parent) {
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

        JFXButton closeButton = new JFXButton(DIALOG_BUTTON_TEXT);
        closeButton.setStyle(DIALOG_BUTTON_CSS);
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
