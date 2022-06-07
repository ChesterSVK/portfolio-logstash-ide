package cz.muni.ics.core.exceptions;

import lombok.Getter;

import java.util.List;

/**
 * Exception class for Core module
 *
 * @author Jozef Cibík
 */
public class ApplicationException extends Exception {
    public ApplicationException(String message) {
        super(message);
    }
    public ApplicationException(String message, Throwable cause) {
        super(message, cause);
    }
}
