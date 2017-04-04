package utask.commons.util;

import java.io.IOException;
import java.util.Optional;

import utask.commons.core.Config;
import utask.commons.exceptions.DataConversionException;

/**
 * A class for accessing the Config File.
 */
public class ConfigUtil {

    public static Optional<Config> readConfig(String configFilePath) throws DataConversionException {
        return JsonUtil.readJsonFile(configFilePath, Config.class);
    }

    public static void saveConfig(Config config, String configFilePath) throws IOException {
        JsonUtil.saveJsonFile(config, configFilePath);
        Config.relocatedFile = configFilePath;
    }

    // @@author A0138493W
    public static String getConfigPath() throws IOException {
        if (Config.relocatedFile != "") {
            return Config.relocatedFile;
        } else {
            return Config.DEFAULT_CONFIG_FILE;
        }
    }

}
