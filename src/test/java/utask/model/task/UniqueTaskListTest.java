package utask.model.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utask.model.tag.Tag;
import utask.model.tag.TagColorIndex;
import utask.model.tag.TagName;
import utask.model.tag.UniqueTagList;
import utask.model.task.UniqueTaskList.TaskNotFoundException;
import utask.model.util.SampleDataUtil;

//@@author A0138423J
public class UniqueTaskListTest {

    @Test
    public void validEventUniqueTaskListConstructorAndHashCode() throws Exception {
        // UniqueTaskList created from its constructor
        UniqueTaskList listOfTask = new UniqueTaskList();
        Task taskToReplace = SampleDataUtil.generateEventTaskWithSeed(1);
        listOfTask.add(taskToReplace);

        // Task created from raw codes
        ObservableList<Task> rawlist = FXCollections
                .observableArrayList();
        rawlist.add(taskToReplace);

        // compare hashcode of both lists
        assertEquals(listOfTask.hashCode(), rawlist.hashCode());
    }

    @Test
    public void validEventUniqueTaskListUpdateTag() throws Exception {
        // UniqueTaskList created from its constructor
        UniqueTaskList listOfTask = new UniqueTaskList();
        Task taskToReplace = SampleDataUtil.generateEventTaskWithSeed(1);
        listOfTask.add(taskToReplace);
        Tag tagToReplace = new Tag(
                new TagName("tag" + Math.abs(1)),
                new TagColorIndex("2"));
        Tag tagIncoming = new Tag(
                new TagName("tag" + Math.abs(10)),
                new TagColorIndex("8"));
        listOfTask.updateTaskWithUpdatedTag(tagToReplace, tagIncoming);
        taskToReplace.setTags(
                new UniqueTagList(new Tag(
                        new TagName("tag" + Math.abs(10)),
                        new TagColorIndex("8")),
                        new Tag(new TagName("tag" + Math.abs(2)),
                        new TagColorIndex("8"))));
        assertTrue(listOfTask.contains(taskToReplace));
    }

    @Test(expected = TaskNotFoundException.class)
    public void validEventUniqueTaskListRemoveFail() throws Exception {
        // UniqueTaskList created from its constructor
        UniqueTaskList listOfTask = new UniqueTaskList();
        Task taskToReplace = SampleDataUtil.generateEventTaskWithSeed(1);
        listOfTask.remove(taskToReplace);
        assertFalse(listOfTask.contains(taskToReplace));
    }

}
