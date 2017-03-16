package utask.staging.ui;

import java.util.Comparator;

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
import javafx.scene.control.TreeTableColumn.SortType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.util.Callback;
import seedu.address.commons.util.FxViewUtil;

public class SearchResultsAnchorPane extends StagingUiPart<Region> {

    private static final String FXML = "SearchResultsAnchorPane.fxml";

    @FXML
    private JFXTreeTableView<ReadOnlyTask> searchResults;

    @FXML
    private JFXTreeTableColumn<ReadOnlyTask, String> nameColumn;

    @FXML
    private JFXTreeTableColumn<ReadOnlyTask, String> dateColumn;


    @FXML
    private JFXTreeTableColumn<ReadOnlyTask, String> tagColumn;


    private AnchorPane parent;

    private TreeItem<ReadOnlyTask> root;

    /**
     * @param placeholder The AnchorPane where the BrowserPanel must be inserted
     */
    public SearchResultsAnchorPane(AnchorPane placeholder) {
        super(FXML);
        this.parent = placeholder;
        //placeholder.setOnKeyPressed(Event::consume); // To prevent triggering events for typing inside the
//        AnchorPane.setTopAnchor(jfxTreeTableViewSearchResults, 10.0);             // loaded Web page.

        nameColumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<ReadOnlyTask, String>,
                ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<ReadOnlyTask, String> params) {
                return params.getValue().getValue().name;
            }
        });

        dateColumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<ReadOnlyTask,
                String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<ReadOnlyTask, String> params) {
                return params.getValue().getValue().date;
            }
        });

        tagColumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<ReadOnlyTask, String>,
                ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<ReadOnlyTask, String> params) {
                return params.getValue().getValue().tags;
            }
        });

        populate();
        FxViewUtil.applyAnchorBoundaryParameters(searchResults, 0.0, 0.0, 0.0, 0.0);
        //placeholder.getChildren().add(searchResults);

    }

    private void populate() {
        try {
            ObservableList<ReadOnlyTask> users = FXCollections.observableArrayList();
            users.add(new ReadOnlyTask("Walk my dog", "13022017", "impt"));
            users.add(new ReadOnlyTask("Swimming", "01022017", "now"));
            users.add(new ReadOnlyTask("Dinner with Alice", "05042017", "later"));

            // build tree
            root = new RecursiveTreeItem<ReadOnlyTask>(users, RecursiveTreeObject::getChildren);
            searchResults.setRoot(root);
            searchResults.setShowRoot(false);
//            scene.getStylesheets().add(
            //SearchResultsAnchorPane.class.getResource("/resources/css/jfoenix-components.css").toExternalForm());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isSearchActive = false;

    public boolean handleEscape() {
        if (isSearchActive) {
            isSearchActive = false;
            return true;
        }

        return false;
    }

    public void overlay() {
        if (!isSearchActive) {
            parent.getChildren().clear();
            parent.getChildren().add(searchResults);
            isSearchActive = true;
        }
//        resetFilter();
    }

    public boolean isSearchActive() {
        return isSearchActive;
    }

    public ReadOnlyTask selectIndex(int i) {
        searchResults.getSelectionModel().select(i);
        searchResults.getFocusModel().focus(i);

        TreeItem<ReadOnlyTask> item = searchResults.getTreeItem(i);
        return item.getValue();
    }

    public void fliter(String keywords) {
        searchResults.setPredicate(task ->
            task.getValue().name.get().toLowerCase().contains(keywords) ||
            task.getValue().date.get().toLowerCase().contains(keywords) ||
            task.getValue().tags.get().toLowerCase().contains(keywords));
    }

//    private void resetFilter() {
//        searchResults.setPredicate(task -> true);
//    }

    public void freeResources() {
    }

    public void sort(int column) {
//        root.getChildren().sort(new AToZComparator());


        SortType st = null;
        TreeTableColumn sortcolumn = null;

        System.out.println(searchResults.getSortOrder().size());

        if (searchResults.getSortOrder().size() > 0) {
            sortcolumn = searchResults.getSortOrder().get(column);
            st = sortcolumn.getSortType();
        }

        if (sortcolumn != null) {

            System.out.println(sortcolumn.getText());
            searchResults.getSortOrder().add(sortcolumn);
            sortcolumn.setSortType(st);
            sortcolumn.setSortable(true); // This performs a sort
        }
    }

    class ReadOnlyTask extends RecursiveTreeObject<ReadOnlyTask> {

        StringProperty name;
        StringProperty date;
        StringProperty tags;

        Integer ttt;

        public ReadOnlyTask(String name, String date, String tags) {
            this.name = new SimpleStringProperty(name);
            this.date = new SimpleStringProperty(date);
            this.tags = new SimpleStringProperty(tags);

            ttt = Integer.parseInt(date);
        }
    }

    class AToZComparator implements Comparator<TreeItem<ReadOnlyTask>> {

        @Override
        public int compare(TreeItem<ReadOnlyTask> a, TreeItem<ReadOnlyTask> b) {
            return a.getValue().ttt.compareTo(b.getValue().ttt);
        }
    }
}
