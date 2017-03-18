package utask.staging.ui;

import com.jfoenix.controls.JFXListView;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import seedu.address.commons.util.FxViewUtil;

public class TaskListPanel extends StagingUiPart<Region> {

    private static final String FXML = "TaskAnchorPane.fxml";

    @FXML
    private JFXListView<Region> list;

    @FXML
    private Label dueLabel;
    @FXML
    private JFXListView<Region> dueList;
    @FXML
    private JFXListView<Region> subList;
    @FXML
    private ScrollPane rootPane;
    //
    @FXML
    private VBox container;
    // @FXML
    // private Pane rootPane;

    private Pane parent;

    /**
     * @param placeholder
     *            The AnchorPane where the BrowserPanel must be inserted
     */
    public TaskListPanel(Pane placeholder) {
        super(FXML);

        assert (placeholder != null);

        parent = placeholder;
        populate();

        //
        // dueLabel.setVisible(false);
        // dueList.setVisible(false);
        //// dueList.setOpacity(0);
        // dueLabel.setManaged(false);
        // dueList.setManaged(false);
    }

    // public void setOverlay() {
    // //TODO: Detect if search is active then overlay
    // parent.getChildren().clear();
    // parent.getChildren().add(rootPane);
    // }

    private void populate() {
        // Platform.runLater(() -> {

        double height = 150.0;
        JFXListView<Region> due = createListControlAndAddToParent("Due", container);
        due.getItems().add(new TaskListCard().getRoot());
        due.setMinHeight(height);

        JFXListView<Region> today = createListControlAndAddToParent("Today", container);
        today.getItems().add(new TaskListCard().getRoot());
        today.getItems().add(new TaskListCard().getRoot());
        today.getItems().add(new TaskListCard().getRoot());
        today.getItems().add(new TaskListCard().getRoot());
        today.setMinHeight(height * 4);
        //
        // subList.getItems().add(new TaskListCard().getRoot());
        //
//        list.minHeightProperty().bind(Bindings.size(list.getItems()).multiply(height));
//        list.setMinHeight(list.getItems().size() * height );
//        System.out.println(list.getItems().size() * height );
         
        // JFXScrollPane.smoothScrolling((ScrollPane)
        // rootPane.getChildren().get(0));
        // tomorrowList.getItems().add(new TaskListCard().getRoot());
        // tomorrowList.getItems().add(new TaskListCard().getRoot());
        // tomorrowList.getItems().add(new TaskListCard().getRoot());
        // tomorrowList.getItems().add(new TaskListCard().getRoot());
        // tomorrowList.getItems().add(new TaskListCard().getRoot());
        // tomorrowList.getItems().add(new TaskListCard().getRoot());
        // tomorrowList.setMinHeight(1000);

        // list.setExpanded(true);
        //
        // due.getItems().add(new TaskListCard().getRoot());

        // lstTasks.getSelectionModel().select(-1);
        // lstTasks.getFocusModel().focus(-1);
        //
        // Node group = subList.getGroupnode();
        //
        // System.out.println(group == null);

        FxViewUtil.applyAnchorBoundaryParameters(rootPane, 0.0, 0.0, 0.0, 0.0);
        parent.getChildren().add(rootPane);
        //
        // });

    }

    public void freeResources() {
    }

    private JFXListView<Region> createListControlAndAddToParent(String name, Pane parent) {
        Label label = new Label(name);
        label.getStyleClass().add("list-label");
        JFXListView<Region> list = new JFXListView<Region>();
        list.getStyleClass().add("custom-jfx-list-view1");
        
        parent.getChildren().add(label);
        parent.getChildren().add(list);
        
        return list;
    }

    private void showHeader(JFXListView<Region> subList) {
        Node node = subList.getGroupnode();

        assert node != null;

        Event.fireEvent(node, new MouseEvent(MouseEvent.MOUSE_CLICKED, 0, 0, 0, 0, MouseButton.PRIMARY, 1, true, true,
                true, true, true, true, true, true, true, true, null));

    }
}

