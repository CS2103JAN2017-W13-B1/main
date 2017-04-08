//@@author A0139996A
package staging.guitests.handle;

import java.util.List;
import java.util.stream.Collectors;

import guitests.GuiRobot;
import javafx.scene.Node;
import javafx.scene.control.Labeled;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import utask.model.tag.UniqueTagList;
import utask.model.task.ReadOnlyTask;

/**
 * Provides a handle to a task card in the task list panel and the todo list panel.
 */
public class TaskCardHandle extends GuiHandle {
    private static final String INDEX_FIELD_ID = "#lblId";
    private static final String NAME_FIELD_ID = "#lblName";
    private static final String DATE_FIELD_ID = "#lblDate";
    private static final String TAGS_FIELD_ID = "#hbTagContainer";
    private static final String DONE_FIELD_ID = "#chkDone";

    private Node node;

    public TaskCardHandle(GuiRobot guiRobot, Stage primaryStage, Node node) {
        super(guiRobot, primaryStage, null);
        this.node = node;
    }

    protected String getTextFromLabel(String fieldId) {
        return getTextFromLabel(fieldId, node);
    }

    public String getIndex() {
        return getTextFromLabel(INDEX_FIELD_ID);
    }

    public String getTaskName() {
        return getTextFromLabel(NAME_FIELD_ID);
    }


    public String getDate() {
        return getTextFromLabel(DATE_FIELD_ID);
    }

    public String getEmail() {
        return getTextFromLabel(DATE_FIELD_ID);
    }

    public List<String> getTags() {
        return getTags(getTagsContainer());
    }

    private List<String> getTags(Region tagsContainer) {
        return tagsContainer
                .getChildrenUnmodifiable()
                .stream()
                .map(node -> ((Labeled) node).getText())
                .collect(Collectors.toList());
    }

    private List<String> getTags(UniqueTagList tags) {
        return tags
                .asObservableList()
                .stream()
                .map(tag -> tag.getTagname().toString())
                .collect(Collectors.toList());
    }

    private Region getTagsContainer() {
        return guiRobot.from(node).lookup(TAGS_FIELD_ID).query();
    }

    public boolean isSameCard(ReadOnlyTask task) {
        return getTaskName().equals(task.getName().fullName)
                && getEmail().equals(task.getTimestamp())
                && getIndex().equals(task.getFrequency().value)
                && getTags().equals(getTags(task.getTags()));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TaskCardHandle) {
            TaskCardHandle handle = (TaskCardHandle) obj;
            return getTaskName().equals(handle.getTaskName())
                    && getEmail().equals(handle.getEmail())
                    && getIndex().equals(handle.getIndex())
                    && getTags().equals(handle.getTags());
        }
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return getTaskName() + " " + getIndex();
    }
}
