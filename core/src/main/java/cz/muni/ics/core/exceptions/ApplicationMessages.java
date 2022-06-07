package cz.muni.ics.core.exceptions;

import java.util.ResourceBundle;


/**
 * Messages for Core module
 *
 * @author Jozef Cibík
 */
public class ApplicationMessages {
    private static final ResourceBundle appBundle = ResourceBundle.getBundle("core_messages");
    public static final String ERROR_EXECUTOR_DATA_NULL = appBundle.getString("ERROR_EXECUTOR_DATA_NULL");
    public static final String INFO_APP_STARTED = appBundle.getString("INFO_APP_STARTED");
}
