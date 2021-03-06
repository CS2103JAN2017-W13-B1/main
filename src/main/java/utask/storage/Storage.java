package utask.storage;

import java.io.IOException;
import java.util.Optional;

import utask.commons.events.model.UTaskChangedEvent;
import utask.commons.events.storage.DataSavingExceptionEvent;
import utask.commons.exceptions.DataConversionException;
import utask.model.ReadOnlyUTask;
import utask.model.UserPrefs;

/**
 * API of the Storage component
 */
public interface Storage extends UTaskStorage, UserPrefsStorage {

    @Override
    Optional<UserPrefs> readUserPrefs() throws DataConversionException, IOException;

    @Override
    void saveUserPrefs(UserPrefs userPrefs) throws IOException;

    @Override
    String getUTaskFilePath();

    @Override
    Optional<ReadOnlyUTask> readUTask() throws DataConversionException, IOException;

    @Override
    void saveUTask(ReadOnlyUTask uTask) throws IOException;

    /**
     * Saves the current version of the UTask to the hard disk.
     *   Creates the data file if it is missing.
     * Raises {@link DataSavingExceptionEvent} if there was an error during saving.
     */
    void handleUTaskChangedEvent(UTaskChangedEvent abce);
}
