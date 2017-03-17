package utask.storage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;

import utask.commons.core.LogsCenter;
import utask.commons.exceptions.DataConversionException;
import utask.commons.util.FileUtil;
import utask.model.ReadOnlyUTask;

/**
 * A class to access AddressBook data stored as an xml file on the hard disk.
 */
public class XmlUTaskStorage implements UTaskStorage {

    private static final Logger logger = LogsCenter.getLogger(XmlUTaskStorage.class);

    private String filePath;

    public XmlUTaskStorage(String filePath) {
        this.filePath = filePath;
    }

    public String getAddressBookFilePath() {
        return filePath;
    }

    @Override
    public Optional<ReadOnlyUTask> readAddressBook() throws DataConversionException, IOException {
        return readAddressBook(filePath);
    }

    /**
     * Similar to {@link #readAddressBook()}
     * @param filePath location of the data. Cannot be null
     * @throws DataConversionException if the file is not in the correct format.
     */
    public Optional<ReadOnlyUTask> readAddressBook(String filePath) throws DataConversionException,
                                                                                 FileNotFoundException {
        assert filePath != null;

        File addressBookFile = new File(filePath);

        if (!addressBookFile.exists()) {
            logger.info("AddressBook file "  + addressBookFile + " not found");
            return Optional.empty();
        }

        ReadOnlyUTask addressBookOptional = XmlFileStorage.loadDataFromSaveFile(new File(filePath));

        return Optional.of(addressBookOptional);
    }

    @Override
    public void saveAddressBook(ReadOnlyUTask addressBook) throws IOException {
        saveAddressBook(addressBook, filePath);
    }

    /**
     * Similar to {@link #saveAddressBook(ReadOnlyUTask)}
     * @param filePath location of the data. Cannot be null
     */
    public void saveAddressBook(ReadOnlyUTask addressBook, String filePath) throws IOException {
        assert addressBook != null;
        assert filePath != null;

        File file = new File(filePath);
        FileUtil.createIfMissing(file);
        XmlFileStorage.saveDataToFile(file, new XmlSerializableUTask(addressBook));
    }

}
