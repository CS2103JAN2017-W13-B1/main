package utask.storage;

import java.io.IOException;
import java.util.Optional;

import utask.commons.exceptions.DataConversionException;
import utask.model.ReadOnlyUTask;

/**
 * Represents a storage for {@link utask.model.UTask}.
 */
public interface UTaskStorage {

    /**
     * Returns the file path of the data file.
     */
    String getAddressBookFilePath();

    /**
     * Returns AddressBook data as a {@link ReadOnlyUTask}.
     *   Returns {@code Optional.empty()} if storage file is not found.
     * @throws DataConversionException if the data in storage is not in the expected format.
     * @throws IOException if there was any problem when reading from the storage.
     */
    Optional<ReadOnlyUTask> readAddressBook() throws DataConversionException, IOException;

    /**
     * @see #getAddressBookFilePath()
     */
    Optional<ReadOnlyUTask> readAddressBook(String filePath) throws DataConversionException, IOException;

    /**
     * Saves the given {@link ReadOnlyUTask} to the storage.
     * @param addressBook cannot be null.
     * @throws IOException if there was any problem writing to the file.
     */
    void saveAddressBook(ReadOnlyUTask addressBook) throws IOException;

    /**
     * @see #saveAddressBook(ReadOnlyUTask)
     */
    void saveAddressBook(ReadOnlyUTask addressBook, String filePath) throws IOException;

}
