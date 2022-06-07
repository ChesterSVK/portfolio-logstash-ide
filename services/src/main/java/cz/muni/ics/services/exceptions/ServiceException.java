package cz.muni.ics.services.exceptions;

import lombok.Getter;

import java.util.List;

/**
 * Exception class for Services module
 *
 * @author Jozef Cibík
 */

public class ServiceException extends Exception{

    private List<String> arguments;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Constructor

    public ServiceException(String message) { super(message); }
    public ServiceException(String message, Throwable cause) { super(message, cause); }
    public ServiceException(String message, List<String> arguments) { super(message); this.arguments = arguments; }
    public ServiceException(String message, List<String> arguments, Throwable cause) { super(message, cause); this.arguments = arguments; }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Override

    @Override
    public String toString() {
        String exceptionName = getClass().getName();
        Throwable cause = getCause();
        return cause == null ? exceptionName : createMessage(cause.getLocalizedMessage(), exceptionName);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    Private

    private String createMessage(String causeMessage, String exceptionName) {
        String res = exceptionName + ": " + super.getLocalizedMessage() +"\n" + causeMessage;
        return (arguments != null && !arguments.isEmpty()) ? (res + "; Args: " + arguments) : res;
    }
}
