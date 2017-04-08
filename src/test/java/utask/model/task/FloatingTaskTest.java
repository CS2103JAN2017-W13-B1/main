package utask.model.task;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import utask.commons.exceptions.IllegalValueException;
import utask.model.tag.Tag;
import utask.model.tag.TagColorIndex;
import utask.model.tag.TagName;
import utask.model.tag.UniqueTagList;

public class FloatingTaskTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void validFloatingTaskConstructorAndValidUpdate() throws IllegalValueException {
        // test floating task creation
        Name taskName = new Name("To Do Task");
        Frequency taskFrequency = new Frequency("Every Day");
        UniqueTagList taskTags = new UniqueTagList(new Tag(new TagName("School"), new TagColorIndex("red")));
        Status taskStatus = new Status("Incomplete");
        FloatingTask taskToReplace = new FloatingTask(taskName, taskFrequency,
                taskTags, taskStatus); // valid constructor

        //test update of correct task attributes
        taskName = new Name("Must Do Task");
        taskFrequency = new Frequency("Every Minute of our lives");
        taskTags = new UniqueTagList(new Tag(new TagName("NUS"), new TagColorIndex("orange")));
        taskStatus = new Status("Complete");
        FloatingTask taskToOverwrite = new FloatingTask(taskName, taskFrequency,
                taskTags, taskStatus); // valid constructor
        taskToReplace.resetData(taskToOverwrite);
    }

    @Test
    public void validFloatingTaskConstructorAndInvalidNameUpdate() throws IllegalValueException {
        // test floating task creation
        Name taskName = new Name("To Do Task");
        Frequency taskFrequency = new Frequency("Every Day");
        UniqueTagList taskTags = new UniqueTagList(new Tag(new TagName("School"), new TagColorIndex("red")));
        Status taskStatus = new Status("Incomplete");
        FloatingTask taskToReplace = new FloatingTask(taskName, taskFrequency,
                taskTags, taskStatus); // valid constructor

        //test update of correct task attributes
        taskName = null;
        thrown.expect(AssertionError.class);
        taskToReplace.setName(taskName);
    }

    @Test
    public void validFloatingTaskConstructorAndInvalidFrequencyUpdate() throws IllegalValueException {
        // test floating task creation
        Name taskName = new Name("To Do Task");
        Frequency taskFrequency = new Frequency("Every Day");
        UniqueTagList taskTags = new UniqueTagList(new Tag(new TagName("School"), new TagColorIndex("red")));
        Status taskStatus = new Status("Incomplete");
        FloatingTask taskToReplace = new FloatingTask(taskName, taskFrequency,
                taskTags, taskStatus); // valid constructor

        //test update of correct task attributes
        taskFrequency = null;
        thrown.expect(AssertionError.class);
        taskToReplace.setFrequency(taskFrequency);
    }

}
