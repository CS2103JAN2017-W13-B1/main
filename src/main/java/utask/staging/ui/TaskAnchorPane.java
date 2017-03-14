package utask.staging.ui;

import com.jfoenix.controls.JFXListView;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

public class TaskAnchorPane extends StagingUiPart<Region> {

    private static final String FXML = "TaskAnchorPane.fxml";

    @FXML
    private JFXListView<Label> lstTasks;

    @FXML
    private HBox rootPane;

    private AnchorPane parent;

    /**
     * @param placeholder The AnchorPane where the BrowserPanel must be inserted
     */
    public TaskAnchorPane(AnchorPane placeholder) {
        super(FXML);

        parent = placeholder;

//        FxViewUtil.applyAnchorBoundaryParameters(rootFlowPane, 0.0, 0.0, 0.0, 0.0);
        placeholder.getChildren().add(rootPane);
        populate();
    }

    public void setOverlay() {
        //TODO: Detect if search is active then overlay
        parent.getChildren().clear();
        parent.getChildren().add(rootPane);
    }

    private void populate() {
        lstTasks.getItems().add(new Label("Walk my dog\n13 Feb 2017\nimpt"));
        lstTasks.getItems().add(new Label("Swimming\n01 Mar 2017\nnow"));
        lstTasks.getItems().add(new Label("Dinner with Alice\n05 Apr 2017\nlater"));
        lstTasks.getSelectionModel().select(1);
        lstTasks.getFocusModel().focus(1);
    }

    public void freeResources() {
    }

}
