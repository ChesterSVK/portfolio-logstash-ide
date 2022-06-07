package cz.muni.ics.services.exceptions;

import java.util.ResourceBundle;

/**
 * Messages for Services module
 *
 * @author Jozef Cibík
 */

public final class ServiceMessages {

    private static final ResourceBundle serviceBundle = ResourceBundle.getBundle("service_messages");

    public static final String ERROR_ILLEGAL_KEYS =         serviceBundle.getString("ERROR_ILLEGAL_KEYS");
    public static final String ERROR_DURING_FILE_PARSING =  serviceBundle.getString("ERROR_DURING_FILE_PARSING");
    public static final String ERROR_UNKNOWN_SERVICE =     serviceBundle.getString("ERROR_UNKNOWN_SERVICE");
    public static final String ERROR_NULL_IMPORT_TYPE =     serviceBundle.getString("ERROR_NULL_IMPORT_TYPE");
}
