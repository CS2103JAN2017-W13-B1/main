package utask.staging.ui;

import com.jfoenix.controls.JFXListView;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import seedu.address.commons.util.FxViewUtil;

public class TaskAnchorPane extends StagingUiPart<Region> {

    private static final String FXML = "TaskAnchorPane.fxml";

    @FXML
    private JFXListView<Label> lstTasks;

    private AnchorPane parent;

    /**
     * @param placeholder The AnchorPane where the BrowserPanel must be inserted
     */
    public TaskAnchorPane(AnchorPane placeholder) {
        super(FXML);

        parent = placeholder;

        FxViewUtil.applyAnchorBoundaryParameters(lstTasks, 0.0, 0.0, 0.0, 0.0);
        placeholder.getChildren().add(lstTasks);
        populate();
    }

    public void setOverlay() {
        //TODO: Detect if search is active then overlay
        parent.getChildren().add(lstTasks);
    }

    private void populate() {
        for (int i = 0; i < 4; i++) {
            lstTasks.getItems().add(new Label("Item " + i));
        }

        lstTasks.getSelectionModel().select(1);
        lstTasks.getFocusModel().focus(1);
    }

    public void freeResources() {
    }

}
