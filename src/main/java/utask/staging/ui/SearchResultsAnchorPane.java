package utask.staging.ui;

import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.util.Callback;
import seedu.address.commons.util.FxViewUtil;

public class SearchResultsAnchorPane extends StagingUiPart<Region> {

    private static final String FXML = "SearchResultsAnchorPane.fxml";

    @FXML
    private JFXTreeTableView<ReadonlySearchTask> searchResults;

    @FXML
    private JFXTreeTableColumn<ReadonlySearchTask, String> nameColumn;

    @FXML
    private JFXTreeTableColumn<ReadonlySearchTask, String> dateColumn;


    @FXML
    private JFXTreeTableColumn<ReadonlySearchTask, String> tagColumn;



    /**
     * @param placeholder The AnchorPane where the BrowserPanel must be inserted
     */
    public SearchResultsAnchorPane(AnchorPane placeholder) {
        super(FXML);
        //placeholder.setOnKeyPressed(Event::consume); // To prevent triggering events for typing inside the
//        AnchorPane.setTopAnchor(jfxTreeTableViewSearchResults, 10.0);             // loaded Web page.

        nameColumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<ReadonlySearchTask, String>,
                ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<ReadonlySearchTask, String> params) {
                return params.getValue().getValue().name;
            }
        });

        dateColumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<ReadonlySearchTask,
                String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<ReadonlySearchTask, String> params) {
                return params.getValue().getValue().date;
            }
        });

        tagColumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<ReadonlySearchTask, String>,
                ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<ReadonlySearchTask, String> params) {
                return params.getValue().getValue().tags;
            }
        });

        populate();
        FxViewUtil.applyAnchorBoundaryParameters(searchResults, 0.0, 0.0, 0.0, 0.0);
        placeholder.getChildren().add(searchResults);

        fliter("dog");
    }

    private void populate() {
        try {
            ObservableList<ReadonlySearchTask> users = FXCollections.observableArrayList();
            users.add(new ReadonlySearchTask("Walk my dog", "131217", "impt"));
            users.add(new ReadonlySearchTask("Sales Department", "131217", "now"));
            users.add(new ReadonlySearchTask("Sales Department", "131217", "later"));

            // build tree
            TreeItem<ReadonlySearchTask> root =
                    new RecursiveTreeItem<ReadonlySearchTask>(users, RecursiveTreeObject::getChildren);
            searchResults.setRoot(root);
            searchResults.setShowRoot(false);
//            scene.getStylesheets().add(
            //SearchResultsAnchorPane.class.getResource("/resources/css/jfoenix-components.css").toExternalForm());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void fliter(String keywords) {
        searchResults.setPredicate(task ->
            task.getValue().name.get().contains(keywords) ||
            task.getValue().date.get().contains(keywords) ||
            task.getValue().tags.get().contains(keywords));
    }

    public void freeResources() {
    }

    class ReadonlySearchTask extends RecursiveTreeObject<ReadonlySearchTask> {

        StringProperty name;
        StringProperty date;
        StringProperty tags;

        public ReadonlySearchTask(String name, String due, String tags) {
            this.name = new SimpleStringProperty(name);
            this.date = new SimpleStringProperty(due);
            this.tags = new SimpleStringProperty(tags);
        }
    }
}
