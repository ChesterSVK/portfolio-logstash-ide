package cz.muni.ics.bro.exceptions;

import java.util.ResourceBundle;

/**
 * Messages for Bro module
 *
 * @author Jozef Cibík
 */

public class BroMessages {
    //Bundle
    private static final ResourceBundle broBundle = ResourceBundle.getBundle("bro_messages");
    //Warn
    public static final String WARN_CHANGING_DIR = broBundle.getString("WARN_CHANGING_DIR");
    //Error
    public static final String ERROR_NULL_CONFIG = broBundle.getString("ERROR_NULL_CONFIG");
    public static final String ERROR_INVALID_BRO_BIN = broBundle.getString("ERROR_INVALID_BRO_BIN");
    public static final String ERROR_FILE_NOT_EXIST = broBundle.getString("ERROR_FILE_NOT_EXIST");
    public static final String ERROR_INPUT_FILE_HIDDEN = broBundle.getString("ERROR_INPUT_FILE_HIDDEN");
    public static final String ERROR_INVALID_EXTENSION = broBundle.getString("ERROR_INVALID_EXTENSION");
    public static final String ERROR_INVALID_WORKING_DIR = broBundle.getString("ERROR_INVALID_WORKING_DIR");
    public static final String ERROR_CREATING_CONFIG = broBundle.getString("ERROR_CREATING_CONFIG");
    public static final String ERROR_EMPTY_CONFIG_COMMAND = broBundle.getString("ERROR_EMPTY_CONFIG_COMMAND");
    public static final String ERROR_DIR_INVALID = broBundle.getString("ERROR_DIR_INVALID");
    public static final String ERROR_DIR_NOT_EXISTS = broBundle.getString("ERROR_DIR_NOT_EXISTS");
    public static final String ERROR_INVALID_OUTPUT_DIR = broBundle.getString("ERROR_INVALID_OUTPUT_DIR");
    public static final String ERROR_NO_OUTPUT_DIR = broBundle.getString("ERROR_NO_OUTPUT_DIR");
}
