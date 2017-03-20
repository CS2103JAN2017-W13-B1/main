package utask.staging.ui;

import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;
import com.jfoenix.controls.JFXTextArea;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import utask.commons.core.LogsCenter;
import utask.commons.events.ui.NewResultAvailableEvent;
import utask.commons.util.FxViewUtil;
import utask.ui.ResultDisplay;

/**
 * A ui for the status bar that is displayed at the header of the application.
 */
public class UTResultDisplay extends StagingUiPart<Region> {

    private static final Logger logger = LogsCenter.getLogger(ResultDisplay.class);
    private static final String FXML = "UTResultDisplay.fxml";

    private final StringProperty displayed = new SimpleStringProperty("");

    @FXML
    private AnchorPane rootPane;

    @FXML
    private JFXTextArea resultDisplay;

    public UTResultDisplay(AnchorPane parent) {
        super(FXML);
        resultDisplay.textProperty().bind(displayed);
        FxViewUtil.applyAnchorBoundaryParameters(rootPane, 0.0, 0.0, 0.0, 0.0);
        parent.getChildren().add(rootPane);
        registerAsAnEventHandler(this);
    }

    @Subscribe
    private void handleNewResultAvailableEvent(NewResultAvailableEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        displayed.setValue(event.message);
    }

}

