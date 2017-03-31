// @@author A0139996A
package utask.staging.ui;

import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import utask.commons.core.LogsCenter;
import utask.commons.events.ui.NewResultAvailableEvent;
import utask.commons.events.ui.ShowHelpRequestEvent;
import utask.commons.util.FxViewUtil;
import utask.logic.Logic;
import utask.logic.commands.CommandResult;
import utask.logic.commands.exceptions.CommandException;
import utask.staging.ui.events.KeyboardEscapeKeyPressedEvent;
import utask.staging.ui.helper.SuggestionHelper;

public class UTCommandBox extends StagingUiPart<Region> {

    private static final String FXML = "UTCommandBox.fxml";
    private static final String ERROR_TEXTFIELD_STYLE_CLASS = "error-textfield";
    private static final String ERROR_SUGGESTION_STYLE_CLASS = "error-suggestion";

    private final Logger logger = LogsCenter.getLogger(UTCommandBox.class);
    private final Logic logic;

    @FXML
    private Pane rootPane;

    @FXML
    private TextField commandTextField;

    @FXML
    private Label lblSuggestion;

    private String lastValidCommandEntry = "";

    public UTCommandBox(Pane parent, Logic logic) {
        super(FXML);
        this.logic = logic;
        addEventHandlerToControls();
        addControlsToParent(parent);
        addCommandBoxBehaviour();
    }

    private void addCommandBoxBehaviour() {
        Platform.runLater(() -> {
            commandTextField.requestFocus();
        });
    }

    private void addControlsToParent(Pane parent) {
        FxViewUtil.applyAnchorBoundaryParameters(rootPane, 0.0, 0.0, 0.0, 0.0);
        parent.getChildren().add(rootPane);
    }

    private void addEventHandlerToControls() {
        commandTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent ke) {
                handleSpecialKeyCombination(ke);
            }
        });

        commandTextField.setOnKeyReleased(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent ke) {
                handleKeyPressed(ke);
            }
        });
    }

    /**
     * Show full suggested command format on recognized command pattern.
     */
    private void handleKeyPressed(KeyEvent ke) {
        String input = commandTextField.getText();

        if (!input.isEmpty()) {
            input = (input.toLowerCase().split(" "))[0];
            String suggestion = SuggestionHelper.getInputSuggestionOfPreamble(input);
            lblSuggestion.setText(suggestion);
        } else {
            lblSuggestion.setText("");
        }
    }

    private void handleSpecialKeyCombination(KeyEvent ke) {
        KeyCode keyPressed = ke.getCode();

        switch (keyPressed) {
        case ENTER :
            handleCommandInputChanged();
            break;
        case ESCAPE :
            raise(new KeyboardEscapeKeyPressedEvent());
            break;
        case F1 :
            raise(new ShowHelpRequestEvent());
            break;
        case UP :
            commandTextField.setText(lastValidCommandEntry);
            break;
        default:
            setStyleToIndicateCommandSuccess();
            break;
        }
    }

    //@@author
    private void handleCommandInputChanged() {
        try {
            CommandResult commandResult = logic.execute(commandTextField.getText());

            // process result of the command
            setStyleToIndicateCommandSuccess();
            lastValidCommandEntry = commandTextField.getText();
            commandTextField.setText("");
            lblSuggestion.setText("");
            logger.info("Result: " + commandResult.feedbackToUser);
            raise(new NewResultAvailableEvent(commandResult.feedbackToUser));

        } catch (CommandException e) {
            // handle command failure
            setStyleToIndicateCommandFailure();
            logger.info("Invalid command: " + commandTextField.getText());
            raise(new NewResultAvailableEvent(e.getMessage()));
        }
    }

    /**
     * Sets the command box style to indicate a successful command.
     */
    private void setStyleToIndicateCommandSuccess() {
        commandTextField.getStyleClass().remove(ERROR_TEXTFIELD_STYLE_CLASS);
        lblSuggestion.getStyleClass().remove(ERROR_SUGGESTION_STYLE_CLASS);
    }

    /**
     * Sets the command box style to indicate a failed command.
     */
    private void setStyleToIndicateCommandFailure() {
        commandTextField.getStyleClass().add(ERROR_TEXTFIELD_STYLE_CLASS);
        lblSuggestion.getStyleClass().add(ERROR_SUGGESTION_STYLE_CLASS);
    }
}
