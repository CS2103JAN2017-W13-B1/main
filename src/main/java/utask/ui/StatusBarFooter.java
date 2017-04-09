package utask.ui;

import java.util.Date;
import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import utask.commons.core.LogsCenter;
import utask.commons.events.model.UTaskChangedEvent;
import utask.commons.events.ui.FileRelocateEvent;
import utask.commons.util.FxViewUtil;

public class StatusBarFooter extends UiPart<Region> {
    private static final Logger logger = LogsCenter.getLogger(StatusBarFooter.class);
    private static final String FXML = "StatusBarFooter.fxml";

    @FXML
    private Label syncStatus;
    @FXML
    private Label saveLocationStatus;

    public StatusBarFooter(Pane placeHolder, String saveLocation) {
        super(FXML);
        addToPlaceholder(placeHolder);
        setSyncStatus("Not updated yet in this session");
        setSaveLocation("./" + saveLocation);
        registerAsAnEventHandler(this);
    }

    private void addToPlaceholder(Pane placeHolder) {
        FxViewUtil.applyAnchorBoundaryParameters(getRoot(), 0.0, 0.0, 0.0, 0.0);
        placeHolder.getChildren().add(getRoot());
    }

    private void setSaveLocation(String location) {
        this.saveLocationStatus.setText(location);
    }

    private void setSyncStatus(String status) {
        this.syncStatus.setText(status);
    }

    @Subscribe
    public void handleUTaskChangedEvent(UTaskChangedEvent event) {
        String lastUpdated = (new Date()).toString();
        logger.info(LogsCenter.getEventHandlingLogMessage(event, "Setting last updated status to " + lastUpdated));
        setSyncStatus("Last Updated: " + lastUpdated);
    }

    @Subscribe
    public void handlePathChangedEvent(FileRelocateEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        setSaveLocation(event.toString());
    }
}
