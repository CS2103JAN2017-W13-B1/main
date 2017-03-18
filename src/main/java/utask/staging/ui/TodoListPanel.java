package utask.staging.ui;

import com.jfoenix.controls.JFXListView;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import seedu.address.commons.util.FxViewUtil;

public class TodoListPanel extends StagingUiPart<Region> {

    private static final String FXML = "TodoListPanel.fxml";

    @FXML
    private VBox rootPane;

    @FXML
    private JFXListView<Region> lstTasks;

    public TodoListPanel(Pane parent) {
        super(FXML);

        assert(parent != null);
        populate();

//        label.prefWidthProperty().bind(lstTasks.widthProperty());

        FxViewUtil.applyAnchorBoundaryParameters(rootPane, 0.0, 0.0, 0.0, 0.0);
        parent.getChildren().add(rootPane);
    }

    private void populate() {
        lstTasks.getItems().add(new TaskListCard().getRoot());
        lstTasks.getItems().add(new TaskListCard().getRoot());
        lstTasks.getItems().add(new TaskListCard().getRoot());
        lstTasks.getItems().add(new TaskListCard().getRoot());
        lstTasks.getItems().add(new TaskListCard().getRoot());
        lstTasks.getItems().add(new TaskListCard().getRoot());
    }
}
