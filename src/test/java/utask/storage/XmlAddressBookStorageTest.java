package utask.storage;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.IOException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import utask.commons.exceptions.DataConversionException;
import utask.commons.util.FileUtil;
import utask.model.ReadOnlyUTask;
import utask.model.UTask;
import utask.model.task.EventTask;
import utask.testutil.TypicalTask;

public class XmlAddressBookStorageTest {
    private static final String TEST_DATA_FOLDER = FileUtil.getPath("./src/test/data/XmlAddressBookStorageTest/");

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    @Test
    public void readAddressBook_nullFilePath_assertionFailure() throws Exception {
        thrown.expect(AssertionError.class);
        readAddressBook(null);
    }

    private java.util.Optional<ReadOnlyUTask> readAddressBook(String filePath) throws Exception {
        return new XmlUTaskStorage(filePath).readUTask(addToTestDataPathIfNotNull(filePath));
    }

    private String addToTestDataPathIfNotNull(String prefsFileInTestDataFolder) {
        return prefsFileInTestDataFolder != null
                ? TEST_DATA_FOLDER + prefsFileInTestDataFolder
                : null;
    }

    @Test
    public void read_missingFile_emptyResult() throws Exception {
        assertFalse(readAddressBook("NonExistentFile.xml").isPresent());
    }

    @Test
    public void read_notXmlFormat_exceptionThrown() throws Exception {

        thrown.expect(DataConversionException.class);
        readAddressBook("NotXmlFormatAddressBook.xml");

        /* IMPORTANT: Any code below an exception-throwing line (like the one above) will be ignored.
         * That means you should not have more than one exception test in one method
         */
    }

    @Test
    public void readAndSaveAddressBook_allInOrder_success() throws Exception {
        String filePath = testFolder.getRoot().getPath() + "TempAddressBook.xml";
        TypicalTask td = new TypicalTask();
        UTask original = td.getTypicalAddressBook();
        XmlUTaskStorage xmlAddressBookStorage = new XmlUTaskStorage(filePath);

        //Save in new file and read back
        xmlAddressBookStorage.saveUTask(original, filePath);
        ReadOnlyUTask readBack = xmlAddressBookStorage.readUTask(filePath).get();
        assertEquals(original, new UTask(readBack));

        //Modify data, overwrite exiting file, and read back
        original.addTask(new EventTask(td.h));
        original.removeTask(new EventTask(td.a));
        xmlAddressBookStorage.saveUTask(original, filePath);
        readBack = xmlAddressBookStorage.readUTask(filePath).get();
        assertEquals(original, new UTask(readBack));

        //Save and read without specifying file path
        original.addTask(new EventTask(td.i));
        xmlAddressBookStorage.saveUTask(original); //file path not specified
        readBack = xmlAddressBookStorage.readUTask().get(); //file path not specified
        assertEquals(original, new UTask(readBack));

    }

    @Test
    public void saveAddressBook_nullAddressBook_assertionFailure() throws IOException {
        thrown.expect(AssertionError.class);
        saveAddressBook(null, "SomeFile.xml");
    }

    private void saveAddressBook(ReadOnlyUTask readOnlyUTask, String filePath) throws IOException {
        new XmlUTaskStorage(filePath).saveUTask(readOnlyUTask, addToTestDataPathIfNotNull(filePath));
    }

    @Test
    public void saveAddressBook_nullFilePath_assertionFailure() throws IOException {
        thrown.expect(AssertionError.class);
        saveAddressBook(new UTask(), null);
    }


}
