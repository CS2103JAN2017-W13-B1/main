package utask.model.task;

import static org.junit.Assert.assertEquals;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import utask.model.util.SampleDataUtil;

//@@author A0138423J
public class EventTaskTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void valid_EventTaskConstructor_AndValidUpdate() throws Exception {
        // test Event task creation
        Task taskToReplace = SampleDataUtil.generateEventTaskWithSeed(1);
        // test update of correct task attributes
        Task taskToOverwrite = SampleDataUtil.generateEventTaskWithSeed(2);
        taskToReplace.resetData(taskToOverwrite);
        assertEquals(taskToReplace, taskToOverwrite);
    }

    @Test
    public void valid_EventTaskConstructor_AndCopyReadOnlyTask()
            throws Exception {
        // test update of correct task attributes
        ReadOnlyTask taskToOverwrite = SampleDataUtil
                .generateEventTaskWithSeed(2);
        // test Event task creation
        Task taskToReplace = new EventTask(taskToOverwrite);
        assertEquals(taskToReplace, taskToOverwrite);
    }

    @Test
    public void valid_EventTaskConstructor_AndInvalidNameUpdate()
            throws Exception {
        // test Event task creation
        Task taskToReplace = SampleDataUtil.generateEventTaskWithSeed(1);

        // test update of null task name
        Name taskName = null;
        thrown.expect(AssertionError.class);
        taskToReplace.setName(taskName);
        assertEquals(taskToReplace.getName(), null);
    }

    @Test
    public void valid_EventTaskConstructor_AndInvalidFrequencyUpdate()
            throws Exception {
        // test Event task creation
        Task taskToReplace = SampleDataUtil.generateEventTaskWithSeed(1);

        // test update of wrong task names
        Frequency taskFrequency = null;
        thrown.expect(AssertionError.class);
        taskToReplace.setFrequency(taskFrequency);
        assertEquals(taskToReplace.getFrequency(), null);
    }
}
