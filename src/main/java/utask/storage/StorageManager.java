package utask.storage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import utask.commons.core.ComponentManager;
import utask.commons.core.LogsCenter;
import utask.commons.events.model.UTaskChangedEvent;
import utask.commons.events.storage.DataSavingExceptionEvent;
import utask.commons.exceptions.DataConversionException;
import utask.commons.util.StringUtil;
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

    // @@author A0138493W
    /*
     * This method is used to move the data file from old path to new path which is entered by relocate command
     * Exception will be thrown if cannot move the file
     */
    @Override
    public void moveSaveFile(String prePath, String newPath) {
        assert prePath != null;
        assert newPath != null;
        try {
            File oldFile = new File(prePath);
            if (oldFile.exists() && !oldFile.isDirectory()) {
                File newFile = new File(newPath);
                newFile.getParentFile().mkdirs();
                Files.move(oldFile.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                logger.fine("The file has been moved to new location: " + newPath);
            }
        } catch (IOException e) {
            logger.info("Failed to move the data file: " + StringUtil.getDetails(e));
        }
    }

    // @@author
    @Override
    public void setFilePath(String path) {
        uTaskStorage.setFilePath(path);
    }

}
