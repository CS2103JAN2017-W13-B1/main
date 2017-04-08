package utask.model.task;

import static org.junit.Assert.assertEquals;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import utask.model.util.SampleDataUtil;

//@@author A0138423J
public class DeadlineTaskTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void validDeadlineTaskConstructorAndValidUpdate() throws Exception {
        // test Deadline task creation
        Task taskToReplace = SampleDataUtil.generateDeadlineTaskWithSeed(1);
        // test update of correct task attributes
        Task taskToOverwrite = SampleDataUtil.generateDeadlineTaskWithSeed(2);
        taskToReplace.resetData(taskToOverwrite);
        assertEquals(taskToReplace, taskToOverwrite);
    }

    @Test
    public void validDeadlineTaskConstructorAndCopyReadOnlyTask()
            throws Exception {
        // test update of correct task attributes
        ReadOnlyTask taskToOverwrite = SampleDataUtil
                .generateDeadlineTaskWithSeed(2);
        // test Deadline task creation
        Task taskToReplace = new DeadlineTask(taskToOverwrite);
        assertEquals(taskToReplace, taskToOverwrite);
    }

    @Test
    public void validDeadlineTaskConstructorAndInvalidNameUpdate()
            throws Exception {
        // test Deadline task creation
        Task taskToReplace = SampleDataUtil.generateDeadlineTaskWithSeed(1);

        // test update of null task name
        Name taskName = null;
        thrown.expect(AssertionError.class);
        taskToReplace.setName(taskName);
        assertEquals(taskToReplace.getName(), null);
    }

    @Test
    public void validDeadlineTaskConstructorAndInvalidFrequencyUpdate()
            throws Exception {
        // test Deadline task creation
        Task taskToReplace = SampleDataUtil.generateDeadlineTaskWithSeed(1);

        // test update of wrong task names
        Frequency taskFrequency = null;
        thrown.expect(AssertionError.class);
        taskToReplace.setFrequency(taskFrequency);
        assertEquals(taskToReplace.getFrequency(), null);
    }
}
