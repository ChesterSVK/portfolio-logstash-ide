package cz.muni.ics.json.exception;

import java.util.ResourceBundle;

/**
 * Messages for Json module
 *
 * @author Jozef Cibík
 */

public class JsonMessages {

    private static final ResourceBundle jsonBundle = ResourceBundle.getBundle("json");

    public static final String ERROR_JSON_IO = jsonBundle.getString("ERROR_JSON_IO");
    public static final String ERROR_JSON_PARSING = jsonBundle.getString("ERROR_JSON_PARSING");
    public static final String ERROR_JSON_FILE_HIDDEN = jsonBundle.getString("ERROR_JSON_FILE_HIDDEN");
    public static final String ERROR_JSON_NOT_EXISTS = jsonBundle.getString("ERROR_JSON_NOT_EXISTS");
    public static final String ERROR_OBJECT_TO_MAP_NULL = jsonBundle.getString("ERROR_OBJECT_TO_MAP_NULL");
    public static final String ERROR_OUTPUT_DIR_NOT_DIR = jsonBundle.getString("ERROR_OUTPUT_DIR_NOT_DIR");
    public static final String ERROR_JSON_INVALID_FORMAT = jsonBundle.getString("ERROR_JSON_INVALID_FORMAT");
    public static final String ERROR_OUTPUT_DIR_NOT_EXISTS = jsonBundle.getString("ERROR_OUTPUT_DIR_NOT_EXISTS");
}
