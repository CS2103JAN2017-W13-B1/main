package guitests;

import utask.model.UTask;
import utask.testutil.TestUtil;

public class SampleDataTest extends UTaskGuiTest {
    @Override
    protected UTask getInitialData() {
        // return null to force test app to load data from file only
        return null;
    }

    @Override
    protected String getDataFileLocation() {
        // return a non-existent file location to force test app to load sample data
        return TestUtil.getFilePathInSandboxFolder("SomeFileThatDoesNotExist1234567890.xml");
    }

//    @Test
//    public void addressBook_dataFileDoesNotExist_loadSampleData() throws Exception {
//        Task[] expectedList = (Task[]) SampleDataUtil.getSampleTasks().toArray();
//        assertTrue(personListPanel.isListMatching(expectedList));
//    }
}
