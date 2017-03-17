package utask.storage;

import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import utask.commons.core.ComponentManager;
import utask.commons.core.LogsCenter;
import utask.commons.events.model.UTaskChangedEvent;
import utask.commons.events.storage.DataSavingExceptionEvent;
import utask.commons.exceptions.DataConversionException;
import utask.model.ReadOnlyUTask;
import utask.model.UserPrefs;

/**
 * Manages storage of UTask data in local storage.
 */
public class StorageManager extends ComponentManager implements Storage {

    private static final Logger logger = LogsCenter.getLogger(StorageManager.class);
    private UTaskStorage uTaskStorage;
    private UserPrefsStorage userPrefsStorage;


    public StorageManager(UTaskStorage uTaskStorage, UserPrefsStorage userPrefsStorage) {
        super();
        this.uTaskStorage = uTaskStorage;
        this.userPrefsStorage = userPrefsStorage;
    }

    public StorageManager(String uTaskFilePath, String userPrefsFilePath) {
        this(new XmlUTaskStorage(uTaskFilePath), new JsonUserPrefsStorage(userPrefsFilePath));
    }

    // ================ UserPrefs methods ==============================

    @Override
    public Optional<UserPrefs> readUserPrefs() throws DataConversionException, IOException {
        return userPrefsStorage.readUserPrefs();
    }

    @Override
    public void saveUserPrefs(UserPrefs userPrefs) throws IOException {
        userPrefsStorage.saveUserPrefs(userPrefs);
    }


    // ================ UTask methods ==============================

    @Override
    public String getUTaskFilePath() {
        return uTaskStorage.getUTaskFilePath();
    }

    @Override
    public Optional<ReadOnlyUTask> readUTask() throws DataConversionException, IOException {
        return readUTask(uTaskStorage.getUTaskFilePath());
    }

    @Override
    public Optional<ReadOnlyUTask> readUTask(String filePath) throws DataConversionException, IOException {
        logger.fine("Attempting to read data from file: " + filePath);
        return uTaskStorage.readUTask(filePath);
    }

    @Override
    public void saveUTask(ReadOnlyUTask uTask) throws IOException {
        saveUTask(uTask, uTaskStorage.getUTaskFilePath());
    }

    @Override
    public void saveUTask(ReadOnlyUTask uTask, String filePath) throws IOException {
        logger.fine("Attempting to write to data file: " + filePath);
        uTaskStorage.saveUTask(uTask, filePath);
    }


    @Override
    @Subscribe
    public void handleUTaskChangedEvent(UTaskChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event, "Local data changed, saving to file"));
        try {
            saveUTask(event.data);
        } catch (IOException e) {
            raise(new DataSavingExceptionEvent(e));
        }
    }

}
