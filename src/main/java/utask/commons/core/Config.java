package utask.commons.core;

import java.util.Objects;
import java.util.logging.Level;

/**
 * Config values used by the app
 */
public class Config {

    public static final String DEFAULT_CONFIG_FILE = "config.json";

    // Config values customizable through config file
    private String appTitle = "uTask";
    private Level logLevel = Level.INFO;
    private String userPrefsFilePath = "preferences.json";
    private String uTaskFilePath = getUTaskFilePath();
    private String uTaskName = "MyUTask";


    public String getAppTitle() {
        return appTitle;
    }

    public void setAppTitle(String appTitle) {
        this.appTitle = appTitle;
    }

    public Level getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(Level logLevel) {
        this.logLevel = logLevel;
    }

    public String getUserPrefsFilePath() {
        return userPrefsFilePath;
    }

    public void setUserPrefsFilePath(String userPrefsFilePath) {
        this.userPrefsFilePath = userPrefsFilePath;
    }

    public String getUTaskFilePath() {
        if(uTaskFilePath == null) {
            return "data/utask.xml";
        }
        return uTaskFilePath;
    }

    public void setUTaskFilePath(String uTaskFilePath) {
        this.uTaskFilePath = uTaskFilePath;
    }

    public String getUTaskName() {
        return uTaskName;
    }

    public void setUTaskName(String uTaskName) {
        this.uTaskName = uTaskName;
    }


    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Config)) { //this handles null as well.
            return false;
        }

        Config o = (Config) other;

        return Objects.equals(appTitle, o.appTitle)
                && Objects.equals(logLevel, o.logLevel)
                && Objects.equals(userPrefsFilePath, o.userPrefsFilePath)
                && Objects.equals(uTaskFilePath, o.uTaskFilePath)
                && Objects.equals(uTaskName, o.uTaskName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(appTitle, logLevel, userPrefsFilePath, uTaskFilePath, uTaskName);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("App title : " + appTitle);
        sb.append("\nCurrent log level : " + logLevel);
        sb.append("\nPreference file Location : " + userPrefsFilePath);
        sb.append("\nLocal data file location : " + uTaskFilePath);
        sb.append("\nUTask name : " + uTaskName);
        return sb.toString();
    }

}
