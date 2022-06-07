package cz.muni.ics.services.exceptions;

import java.util.List;

/**
 * Import exception class for Services module
 *
 * @author Jozef Cibík
 */

public class ImportException extends ServiceException{
    public ImportException(String message) {
        super(message);
    }
    public ImportException(String message, Throwable cause) {
        super(message, cause);
    }
}
