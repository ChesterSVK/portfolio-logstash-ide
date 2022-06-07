package cz.muni.ics.watchdog.exceptions;

import java.util.ResourceBundle;

/**
 * Messages for Watchdog module
 *
 * @author Jozef Cibík
 */

public class WatchdogMessages {
    //Bundle
    private static final ResourceBundle appBundle = ResourceBundle.getBundle("watchdog_messages");
    //Debug
    public static final String DEBUG_FILTERING_INVALID_FOLDERS = appBundle.getString("DEBUG_FILTERING_INVALID_FOLDERS");
    //Info
    public static final String INFO_SCRIPT_ENDED = appBundle.getString("INFO_SCRIPT_ENDED");
    public static final String INFO_APP_STARTED = appBundle.getString("INFO_APP_STARTED");
    ///Warn
    public static final String WARN_SCRIPT_ENDED_WITH_DIFFERENT_VALUE = appBundle.getString("WARN_SCRIPT_ENDED_WITH_DIFFERENT_VALUE");
    //Error
    public static final String ERROR_INVALID_SCRIPT_PATH = appBundle.getString("ERROR_INVALID_SCRIPT_PATH");

    public static final String ERROR_INVALID_CONFIG_PATH = appBundle.getString("ERROR_INVALID_CONFIG_PATH");
    public static final String ERROR_FILE_DOES_NOT_EXIST = appBundle.getString("ERROR_FILE_DOES_NOT_EXIST");
    public static final String ERROR_NO_DIRS_TO_WATCH = appBundle.getString("ERROR_NO_DIRS_TO_WATCH");
    public static final String ERROR_SCRIPT_FAILED = appBundle.getString("ERROR_SCRIPT_FAILED");
    public static final String ERROR_CREATING_CONFIG = appBundle.getString("ERROR_CREATING_CONFIG");
}
