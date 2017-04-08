package utask.model.task;

import static org.junit.Assert.assertEquals;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import utask.model.util.SampleDataUtil;

//@@author A0138423J
public class FloatingTaskTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void validFloatingTaskConstructorAndValidUpdate() throws Exception {
        // test floating task creation
        Task taskToReplace = SampleDataUtil.generateFloatingTaskWithSeed(1);
        // test update of correct task attributes
        Task taskToOverwrite = SampleDataUtil.generateFloatingTaskWithSeed(2);
        taskToReplace.resetData(taskToOverwrite);
        assertEquals(taskToReplace, taskToOverwrite);
    }

    @Test
    public void validFloatingTaskConstructorAndCopyReadOnlyTask() throws Exception {
        // test update of correct task attributes
        ReadOnlyTask taskToOverwrite = SampleDataUtil.generateFloatingTaskWithSeed(2);
        // test floating task creation
        Task taskToReplace = new FloatingTask(taskToOverwrite);
        assertEquals(taskToReplace, taskToOverwrite);
    }

    @Test
    public void validFloatingTaskConstructorAndInvalidNameUpdate()
            throws Exception {
        // test floating task creation
        Task taskToReplace = SampleDataUtil.generateFloatingTaskWithSeed(1);

        // test update of null task name
        Name taskName = null;
        thrown.expect(AssertionError.class);
        taskToReplace.setName(taskName);
        assertEquals(taskToReplace.getName(), null);
    }

    @Test
    public void validFloatingTaskConstructorAndInvalidFrequencyUpdate()
            throws Exception {
        // test floating task creation
        Task taskToReplace = SampleDataUtil.generateFloatingTaskWithSeed(1);

        // test update of wrong task names
        Frequency taskFrequency = null;
        thrown.expect(AssertionError.class);
        taskToReplace.setFrequency(taskFrequency);
        assertEquals(taskToReplace.getFrequency(), null);
    }

}
