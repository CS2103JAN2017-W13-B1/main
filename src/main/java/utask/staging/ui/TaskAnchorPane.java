package utask.staging.ui;

import com.jfoenix.controls.JFXListView;

import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import seedu.address.commons.util.FxViewUtil;

public class TaskAnchorPane extends StagingUiPart<Region> {

    private static final String FXML = "TaskAnchorPane.fxml";

    @FXML
    private JFXListView<Region> lstTasks;

    @FXML
    private Pane rootPane;

    private Pane parent;

    /**
     * @param placeholder The AnchorPane where the BrowserPanel must be inserted
     */
    public TaskAnchorPane(Pane placeholder) {
        super(FXML);
        
        assert(placeholder != null);
        
        parent = placeholder;
        populate();

        FxViewUtil.applyAnchorBoundaryParameters(rootPane, 0.0, 0.0, 0.0, 0.0);
        placeholder.getChildren().add(rootPane);
    }

    public void setOverlay() {
        //TODO: Detect if search is active then overlay
        parent.getChildren().clear();
        parent.getChildren().add(rootPane);
    }

    private void populate() {
        lstTasks.getItems().add(new TaskListCard().getRoot());
        lstTasks.getItems().add(new TaskListCard().getRoot());
//        lstTasks.getSelectionModel().select(-1);
//        lstTasks.getFocusModel().focus(-1);
    }

    public void freeResources() {
    }
}
