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
    String getUTaskFilePath();

    void setFilePath(String path);

    /**
     * Returns UTask data as a {@link ReadOnlyUTask}.
     *   Returns {@code Optional.empty()} if storage file is not found.
     * @throws DataConversionException if the data in storage is not in the expected format.
     * @throws IOException if there was any problem when reading from the storage.
     */
    Optional<ReadOnlyUTask> readUTask() throws DataConversionException, IOException;

    /**
     * @see #getUTaskFilePath()
     */
    Optional<ReadOnlyUTask> readUTask(String filePath) throws DataConversionException, IOException;

    /**
     * Saves the given {@link ReadOnlyUTask} to the storage.
     * @param uTask cannot be null.
     * @throws IOException if there was any problem writing to the file.
     */
    void saveUTask(ReadOnlyUTask uTask) throws IOException;

    /**
     * @see #saveUTask(ReadOnlyUTask)
     */
    void saveUTask(ReadOnlyUTask uTask, String filePath) throws IOException;

    /*
     * Move the storage data file to new path
     */
    void moveSaveFile(String prePath, String newPath);

}
