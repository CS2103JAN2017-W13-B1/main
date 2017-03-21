package utask.staging.ui;

import java.util.logging.Logger;

import javafx.event.ActionEvent;
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
import utask.commons.util.FxViewUtil;
import utask.logic.Logic;
import utask.logic.commands.CommandResult;
import utask.logic.commands.exceptions.CommandException;
import utask.staging.ui.events.KeyboardEscapeKeyPressedEvent;
import utask.staging.ui.helper.SuggestionHelper;

public class UTCommandBox extends StagingUiPart<Region> {
    private final Logger logger = LogsCenter.getLogger(UTCommandBox.class);
    private static final String FXML = "UTCommandBox.fxml";
    public static final String ERROR_STYLE_CLASS = "error-textfield";

    private final Logic logic;

    @FXML
    private Pane rootPane;

    @FXML
    private TextField commandTextField;

    @FXML
    private Label lblSuggestion;

    private String lastValidCommand = "";

    public UTCommandBox(Pane parent, Logic logic) {
        super(FXML);
        this.logic = logic;
        addToParent(parent);
    }

    private void addToParent(Pane parent) {
        //TODO: Simpify with lambda expression
        commandTextField.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                handleCommandInputChanged();
            }
        });

        commandTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent ke) {
                handleKeyPressed(ke);
            }
        });

        FxViewUtil.applyAnchorBoundaryParameters(rootPane, 0.0, 0.0, 0.0, 0.0);
        parent.getChildren().add(rootPane);
//        SplitPane.setResizableWithParent(placeHolderPane, false);
//        placeHolderPane.getChildren().add(commandTextField);
//        FxViewUtil.applyAnchorBoundaryParameters(getRoot(), 0.0, 0.0, 0.0, 0.0);
//        FxViewUtil.applyAnchorBoundaryParameters(commandTextField, 0.0, 0.0, 0.0, 0.0);
    }

    private void handleCommandInputChanged() {
        try {
            CommandResult commandResult = logic.execute(commandTextField.getText());

            // process result of the command
            setStyleToIndicateCommandSuccess();
            lastValidCommand = commandTextField.getText();
            commandTextField.setText("");
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
     * Show full suggested command format on recognised command pattern.
     */
    private void handleKeyPressed(KeyEvent ke) {
        if (ke.getCode() == KeyCode.ESCAPE) {
            raise(new KeyboardEscapeKeyPressedEvent());
            return;
        }

        //TODO: Upgrade to binary tree
        String input = commandTextField.getText();

        if (!"".equals(input)) {
            input = (input.toLowerCase().split(" "))[0];
            String suggestion = SuggestionHelper.getInputSuggestionOfPreamble(input);
            lblSuggestion.setText(suggestion);
        }
    }

    /**
     * Sets the command box style to indicate a successful command.
     */
    private void setStyleToIndicateCommandSuccess() {
        commandTextField.getStyleClass().remove(ERROR_STYLE_CLASS);
    }

    /**
     * Sets the command box style to indicate a failed command.
     */
    private void setStyleToIndicateCommandFailure() {
        commandTextField.getStyleClass().add(ERROR_STYLE_CLASS);
    }
}
